package com.eyther.lumbridge.domain.model.finance

/**
 * Represents the type of money allocation. This is used to calculate the amount of money
 * that should be allocated to a specific category.
 * @property name The label of the money allocation type.
 */
sealed class MoneyAllocationType(val name: String) {
    data object Savings : MoneyAllocationType("Savings")
    data object Necessities : MoneyAllocationType("Necessities")
    data object Luxuries : MoneyAllocationType("Luxuries")
}
