package com.eyther.lumbridge.domain.model.netsalary.allocation

import kotlin.math.ceil

/**
 * Represents a money allocation. A money allocation has a type and the amount allocated to
 * that type, given a net salary.
 *
 * @property type The type of money allocation.
 * @property percentage The percentage of the net salary allocated to the type.
 * @property amount The amount of money allocated to the type.
 */
data class MoneyAllocation(
    val type: MoneyAllocationType,
    val percentage: Float,
    val netSalary: Float
) {
    val amount: Float
        get() = ceil(netSalary * percentage)
}
