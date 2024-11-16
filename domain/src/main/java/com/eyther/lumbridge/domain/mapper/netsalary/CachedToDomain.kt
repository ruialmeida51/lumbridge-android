package com.eyther.lumbridge.domain.mapper.netsalary

import com.eyther.lumbridge.data.model.netsalary.local.portugal.PortugalIrsBracketTypeCached
import com.eyther.lumbridge.domain.model.netsalary.percountry.portugal.PortugalIrsTableType

fun PortugalIrsBracketTypeCached.toCached(): PortugalIrsTableType {
    return when (this) {
        PortugalIrsBracketTypeCached.NotHandicapped.NotMarriedWithoutDependentsOrMarried -> PortugalIrsTableType.NoHandicap.FirstTable
        PortugalIrsBracketTypeCached.NotHandicapped.NotMarriedWithOneOrMoreDependents -> PortugalIrsTableType.NoHandicap.SecondTable
        PortugalIrsBracketTypeCached.NotHandicapped.MarriedSingleHolder -> PortugalIrsTableType.NoHandicap.ThirdTable
        PortugalIrsBracketTypeCached.Handicapped.NotMarriedOrMarriedWithoutDependents -> PortugalIrsTableType.Handicapped.FourthTable
        PortugalIrsBracketTypeCached.Handicapped.NotMarriedWithOneOrMoreDependents -> PortugalIrsTableType.Handicapped.FifthTable
        PortugalIrsBracketTypeCached.Handicapped.MarriedJointHolderWithOneOrMoreDependents -> PortugalIrsTableType.Handicapped.SixthTable
        PortugalIrsBracketTypeCached.Handicapped.MarriedSingleHolder -> PortugalIrsTableType.Handicapped.SeventhTable
    }
}

