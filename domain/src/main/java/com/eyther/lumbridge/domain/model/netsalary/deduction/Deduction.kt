package com.eyther.lumbridge.domain.model.netsalary.deduction

/**
 * This class represents a deduction that can be applied to a salary.
 * There are two types of deductions: percentage and flat.
 */
sealed class Deduction(
    open val type: DeductionType,
    open val value: Float
) {
    data class PercentageDeduction(
        override val type: DeductionType,
        override val value: Float,
        val percentage: Float
    ): Deduction(type, value)

    data class FlatDeduction(
        override val type: DeductionType,
        override val value: Float
    ): Deduction(type, value)
}
