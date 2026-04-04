# AGENTS.md: Target Architecture Contract (Monorepo Refactor)

## Overview

This document describes the intended, long-term Clean Architecture for the `lumbridge-android` project. It must guide all refactors, reviews, and future contributions until explicitly revised. No PR should violate this contract.

---

## Modules and Their Responsibilities

- **:app**
  - App entry point only: `LumbridgeApplication` (`@HiltAndroidApp`), `MainActivity`, `AndroidManifest.xml`
  - Platform code: notification receivers/senders, background workers, locale implementation
  - App icon/mipmap resources and build configuration (flavours, signing, Firebase distribution)
  - Must not contain UI feature screens, ViewModels, or UI models
  - Depends on `:presentation`, `:di`, `:domain`, and `:shared`

- **:presentation**
  - All UI feature screens and ViewModels
  - UI models (`model/` package) and domain→UI mappers (`mapper/` package)
  - Navigation (`ui/navigation/`), Compose theme, and reusable UI composables (`ui/`)
  - String and drawable resources (except app name/icon)
  - Platform UI extensions (`extensions/`)
  - Depends on `:domain` and `:shared`

- **:domain**
  - All business logic (use cases/interactors) — 62 use cases under `domain/usecase/`
  - Defines repository interfaces (not implementations) — interfaces under `domain/repository/`
  - Owns all domain models/entities
  - No reference to Android APIs or data implementation
  - Depends on `:shared` only

- **:data**
  - Implements the repository interfaces from `:domain`
  - Contains all data sources, network/service clients, and local DB access (Room, Retrofit, DataStore)
  - Owns data entities and mappers (data <-> domain)
  - May depend on 3rd-party libs (e.g., Room, Retrofit)
  - Depends on `:domain` and `:shared`

- **:di**
  - Dependency Injection modules (Hilt)
  - Wires everything together, binds all interfaces/implementations
  - Depends on `:domain`, `:data`, `:presentation`, and `:shared`

- **:shared**
  - Pure stateless utility code (coroutine dispatchers, schedulers model, etc.)
  - No app state or business/domain objects
  - No dependencies on any other project module

---

## Dependency Graph

```
:app --> :presentation --> :domain
      \-> :di ----------> :domain
                    \----> :data
:domain <--- :data
:shared (utility, referenced by any, no references to core/domain/data)
```

- `:app -> :presentation -> :domain`
- `:app -> :di -> (:domain, :data, :presentation)`
- `:app -> :domain` (for platform workers/notifications that inject domain use cases directly)
- `:data -> :domain`
- `:shared` is a leaf-only utility module (no dependencies on core, domain, or data)

---

## Allowed Dependencies

- `:presentation` depends on `:domain` and `:shared` (UI accesses domain through use cases)
- `:di` depends on `:domain`, `:data`, `:presentation`, and `:shared`
- `:app` depends on `:presentation`, `:di`, `:domain`, and `:shared`
- `:app` cannot depend on `:data` directly
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
| Mappers (domain <-> UI) | `:presentation` |
| ViewModels | `:presentation` |
| UI Feature Screens | `:presentation` |
| UI Models / DTOs | `:presentation` |
| Navigation | `:presentation` |
| Compose Theme | `:presentation` |
| String/Drawable Resources | `:presentation` |
| App Entry Point / Manifest | `:app` |
| Platform Code (workers, notifications) | `:app` |
| DI Configuration | `:di` |
| Pure Utils / Schedulers model | `:shared` |

---

## DI Files Inventory

All Hilt modules that exist (or will exist) in the project, and their target home in `:di`:

| File | Current Location | Target Module |
|---|---|---|
| `LocaleModule.kt` | `app/src/main/java/com/eyther/lumbridge/di/` | `:app` (intentional — binds `LocaleRepositoryImpl` which uses `AppCompatDelegate`) |
| `LocalDataModule.kt` | `data/src/main/java/com/eyther/lumbridge/data/di/` | `:di` |
| `RemoteDataModule.kt` | `data/src/main/java/com/eyther/lumbridge/data/di/` | `:di` |
| `RepositoryModule.kt` | `data/src/main/java/com/eyther/lumbridge/data/di/` | `:di` |
| `UtilModule.kt` | `data/src/main/java/com/eyther/lumbridge/data/di/` | `:di` |
| `SchedulersModule.kt` | `shared/src/main/java/com/eyther/lumbridge/shared/di/` | `:di` |

### Qualifier Annotations

The qualifier annotations used in DI modules are currently co-located with their respective modules. When DI modules are moved to `:di`, the qualifiers move with them. Note: `SchedulersModule` provides the `Schedulers` data class (defined in `shared/di/model/Schedulers.kt`) which stays in `:shared` and is injected throughout `:domain`, `:data`, and `:app`.

| Qualifier | Defined In | Used In |
|---|---|---|
| `@UserProfileDataSource`, `@UserFinancialsDataStore`, `@UserMortgageDataStore`, `@CurrencyRatesDataStore`, `@AppSettingsDataStore` | `LocalDataModule` | `:data` datasources |
| `@AppRetrofitClient`, `@CurrencyExchangeRetrofitClient` | `RemoteDataModule` | `:data` datasources |
| `@DefaultGson`, `@ComplexGson`, `@AndroidFileReader` | `UtilModule` | `:data` datasources |
| `@IoDispatcher`, `@CpuDispatcher`, `@MainDispatcher` | `SchedulersModule` | Only inside `SchedulersModule` itself |

---

## Models

- UI models/DTOs live in `:presentation` (`model/` package)
- Domain→UI mappers live in `:presentation` (`mapper/` package)
- Domain models have no Android/infra/data dependencies; used in business logic layer
- Data entities are only used in `:data`
- `Schedulers` data class lives in `shared/di/model/Schedulers.kt` (`:shared`) and is injected into repositories/use cases

---

## Current vs Target State (as of last update)

### Currently active modules (registered in `settings.gradle.kts`)
- `:app`, `:data`, `:di`, `:domain`, `:presentation`, `:shared`

### Current dependency graph (actual)
```
:app --> :presentation --> :domain, :shared
:app --> :di -----------> :domain, :data, :presentation, :shared
:app --> :domain, :shared (for platform workers/notifications)
:data --> :domain, :shared
:domain --> :shared
```

### Open gap
- None — `:presentation` module has been created and all UI code has been moved into it
- `LocaleModule` remains in `:app` intentionally: its sole binding (`LocaleRepositoryImpl`) uses `AppCompatDelegate` (an AppCompat/UI-layer dependency) which cannot live in `:data` or `:di` without introducing an inappropriate platform coupling
- `IntentExt.kt` remains in `:app/extensions/platform/` intentionally: it references `MainActivity` and is only used by `:app` notification receivers

---

## Guidelines / Tenets

- No Android imports in `:domain` (`:data` may use Android framework libraries such as Room for local DB access, but must not couple to UI or application lifecycle APIs)
- All code and new PRs must conform to the dependency graph above
- Test code for a module may depend only on the module it is testing and its dependencies

---

## Updating This Document

If the architecture changes, update this file and open a new issue referencing the change.
