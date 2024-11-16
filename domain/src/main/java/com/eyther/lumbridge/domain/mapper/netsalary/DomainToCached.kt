package com.eyther.lumbridge.domain.mapper.netsalary

import com.eyther.lumbridge.data.model.netsalary.local.portugal.PortugalIrsBracketTypeCached
import com.eyther.lumbridge.domain.model.netsalary.percountry.portugal.PortugalIrsTableType

fun PortugalIrsTableType.toCached(): PortugalIrsBracketTypeCached {
    return when(this) {
        PortugalIrsTableType.NoHandicap.FirstTable -> PortugalIrsBracketTypeCached.NotHandicapped.NotMarriedWithoutDependentsOrMarried
        PortugalIrsTableType.NoHandicap.SecondTable -> PortugalIrsBracketTypeCached.NotHandicapped.NotMarriedWithOneOrMoreDependents
        PortugalIrsTableType.NoHandicap.ThirdTable -> PortugalIrsBracketTypeCached.NotHandicapped.MarriedSingleHolder
        PortugalIrsTableType.Handicapped.FourthTable -> PortugalIrsBracketTypeCached.Handicapped.NotMarriedOrMarriedWithoutDependents
        PortugalIrsTableType.Handicapped.FifthTable -> PortugalIrsBracketTypeCached.Handicapped.NotMarriedWithOneOrMoreDependents
        PortugalIrsTableType.Handicapped.SixthTable -> PortugalIrsBracketTypeCached.Handicapped.MarriedJointHolderWithOneOrMoreDependents
        PortugalIrsTableType.Handicapped.SeventhTable -> PortugalIrsBracketTypeCached.Handicapped.MarriedSingleHolder
    }
}
