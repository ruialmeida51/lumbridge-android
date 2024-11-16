package com.eyther.lumbridge.data.mappers.netsalary

import com.eyther.lumbridge.data.model.netsalary.files.portugal.PortugalFlatRateFormulaFromFile
import com.eyther.lumbridge.data.model.netsalary.files.portugal.PortugalFlatRateFromFile
import com.eyther.lumbridge.data.model.netsalary.files.portugal.PortugalIrsTableFromFile
import com.eyther.lumbridge.data.model.netsalary.files.portugal.PortugalTaxPercentageFromFile
import com.eyther.lumbridge.data.model.netsalary.local.portugal.PortugalFlatRateCached
import com.eyther.lumbridge.data.model.netsalary.local.portugal.PortugalFlatRateFormulaCached
import com.eyther.lumbridge.data.model.netsalary.local.portugal.PortugalIrsTableCached
import com.eyther.lumbridge.data.model.netsalary.local.portugal.PortugalTaxPercentageCached

fun PortugalIrsTableFromFile.toCached() = PortugalIrsTableCached(
    taxPercentage = taxPercentageFromFile.map { it.toCached() },
    flatRate = flatRateFromFile.map { it.toCached() }
)

fun PortugalTaxPercentageFromFile.toCached() = PortugalTaxPercentageCached(
    range = range,
    percentage = percentage
)

fun PortugalFlatRateFromFile.toCached() = PortugalFlatRateCached(
    range = range,
    rate = rate,
    formula = formula?.toCached(),
    perDependentAmount = perDependentAmount
)

fun PortugalFlatRateFormulaFromFile.toCached() = PortugalFlatRateFormulaCached(
    taxPercentage = taxPercentage,
    multiplier = multiplier,
    rate = rate
)
