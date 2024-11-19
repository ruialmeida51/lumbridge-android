package com.eyther.lumbridge.domain.model.expenses

sealed class ExpensesCategoryTypes(
    val ordinal: Int
) {
    companion object {
        fun of(ordinal: Int): ExpensesCategoryTypes {
            return when (ordinal) {
                Food.ordinal -> Food
                Transportation.ordinal -> Transportation
                HealthCare.ordinal -> HealthCare
                Entertainment.ordinal -> Entertainment
                Housing.ordinal -> Housing
                Education.ordinal -> Education
                Other.ordinal -> Other
                Pets.ordinal -> Pets
                Sports.ordinal -> Sports
                Vacations.ordinal -> Vacations
                Investments.ordinal -> Investments
                Surplus.ordinal -> Surplus
                else -> throw IllegalArgumentException("💥 Unknown ordinal: $ordinal")
            }
        }
    }

    data object Food : ExpensesCategoryTypes(0)
    data object Transportation : ExpensesCategoryTypes(1)
    data object HealthCare : ExpensesCategoryTypes(2)
    data object Entertainment : ExpensesCategoryTypes(3)
    data object Housing : ExpensesCategoryTypes(4)
    data object Education : ExpensesCategoryTypes(5)
    data object Other : ExpensesCategoryTypes(6)
    data object Pets : ExpensesCategoryTypes(7)
    data object Sports : ExpensesCategoryTypes(8)
    data object Vacations : ExpensesCategoryTypes(9)
    data object Surplus : ExpensesCategoryTypes(10)
    data object Investments : ExpensesCategoryTypes(11)
}
