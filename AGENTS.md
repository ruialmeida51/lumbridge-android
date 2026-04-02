# AGENTS.md: Target Architecture Contract (Monorepo Refactor)

## Overview

This document describes the intended, long-term Clean Architecture for the `lumbridge-android` project. It must guide all refactors, reviews, and future contributions until explicitly revised. No PR should violate this contract.

---

## Modules and Their Responsibilities

- **:app**
  - Android main app code (activities, fragments, screens, ViewModels, UI logic)
  - Contains only platform/UI code and DI entry point
  - Must not contain business logic, data access, or domain entities

- **:domain**
  - All business logic (use cases/interactors)
  - Defines repository interfaces (not implementations)
  - Owns all domain models/entities
  - No reference to Android APIs or data implementation

- **:data**
  - Implements the repository interfaces from `:domain`
  - Contains all data sources, network/service clients, and local DB access
  - Owns data entities and mappers (data <-> domain)
  - May depend on 3rd-party libs (e.g., Room, Retrofit)

- **:di** *(planned module — not yet created)*
  - Dependency Injection modules (Hilt/Koin)
  - Wires everything together, binds all interfaces/implementations
  - The only module aware of all other modules
  - Until `:di` is extracted, DI configuration lives in `:app` as an interim measure; all new DI code should be written with extraction in mind

- **:shared**
  - Pure stateless utility code
  - No app state or business/domain objects

---

## Dependency Graph

```
:app --> :di --> :domain
              \---> :data
:domain <--- :data
:shared (utility, referenced by any, no references to core/domain/data)
```

- `:app -> :di -> (:domain, :data)`
- `:data -> :domain`
- `:shared` is a leaf-only utility module (no dependencies on core, domain, or data)

---

## Allowed Dependencies

- Only `:di` depends on both `:domain` and `:data`
- `:app` depends on `:di`, cannot know about `:data` directly
- `:domain` is pure and only knows about itself (& `:shared`)
- `:data` can depend on `:domain` and `:shared`

---

## Where Code Lives

| Code Type | Module |
|---|---|
| Repository Interfaces | `:domain` |
| Repository Implementations | `:data` |
| Use Cases | `:domain` (orchestrate business logic only) |
| Mappers (data <-> domain) | `:data` |
| ViewModels | `:app` |
| DI Configuration | `:di` |
| Pure Utils | `:shared` |

---

## Models

- UI models/DTOs stay in `:app`
- Domain models have no Android/infra/data dependencies; used in business logic layer
- Data entities are only used in `:data`

---

## Guidelines / Tenets

- No Android imports in `:domain` (`:data` may use Android framework libraries such as Room for local DB access, but must not couple to UI or application lifecycle APIs)
- All code and new PRs must conform to the dependency graph above
- Test code for a module may depend only on the module it is testing and its dependencies

---

## Updating This Document

If the architecture changes, update this file and open a new issue referencing the change.
