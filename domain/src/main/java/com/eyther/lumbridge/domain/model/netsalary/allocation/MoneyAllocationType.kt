package com.eyther.lumbridge.domain.model.netsalary.allocation

/**
 * Represents the type of money allocation. This is used to calculate the amount of money
 * that should be allocated to a specific category.
 */
sealed class MoneyAllocationType(
    val ordinal: Int
) {
    companion object {
        fun of(ordinal: Int): MoneyAllocationType {
            return when (ordinal) {
                Necessities.ordinal -> Necessities
                Savings.ordinal -> Savings
                Luxuries.ordinal -> Luxuries
                else -> throw IllegalArgumentException("💥 Unknown ordinal: $ordinal")
            }
        }
    }

    data object Necessities : MoneyAllocationType(0)
    data object Savings : MoneyAllocationType(1)
    data object Luxuries : MoneyAllocationType(2)
}
