package com.eyther.lumbridge.domain.model.finance

/**
 * Represents a money allocation. A money allocation has a type and the amount allocated to
 * that type.
 *
 * @property type The type of money allocation.
 * @property amount The amount of money allocated to the type.
 */
data class MoneyAllocation(
    val type: MoneyAllocationType,
    val amount: Float
)
