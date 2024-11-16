package com.eyther.lumbridge.domain.model.netsalary.allocation

/**
 * Represents the type of money allocation. This is used to calculate the amount of money
 * that should be allocated to a specific category.
 */
sealed interface MoneyAllocationType {
    data object Savings : MoneyAllocationType
    data object Necessities : MoneyAllocationType
    data object Luxuries : MoneyAllocationType
}
