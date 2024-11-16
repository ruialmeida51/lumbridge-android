package com.eyther.lumbridge.data.model.netsalary.local.portugal

sealed interface PortugalIrsBracketTypeCached {

    sealed interface NotHandicapped : PortugalIrsBracketTypeCached {
        data object NotMarriedWithoutDependentsOrMarried : NotHandicapped
        data object NotMarriedWithOneOrMoreDependents : NotHandicapped
        data object MarriedSingleHolder : NotHandicapped
    }

    sealed interface Handicapped : PortugalIrsBracketTypeCached {
        data object NotMarriedOrMarriedWithoutDependents : Handicapped
        data object NotMarriedWithOneOrMoreDependents : Handicapped
        data object MarriedJointHolderWithOneOrMoreDependents : Handicapped
        data object MarriedSingleHolder : Handicapped
    }
}
