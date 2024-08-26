package com.eyther.lumbridge.domain.model.expenses

import androidx.annotation.StringRes
import com.eyther.lumbridge.domain.R

sealed class ExpensesCategoryTypes(
    @StringRes val categoryRes: Int,
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
                else -> throw IllegalArgumentException("💥 Unknown ordinal: $ordinal")
            }
        }
    }

    data object Food : ExpensesCategoryTypes(R.string.food, 0)
    data object Transportation : ExpensesCategoryTypes(R.string.transportation, 1)
    data object HealthCare : ExpensesCategoryTypes(R.string.healthcare, 2)
    data object Entertainment : ExpensesCategoryTypes(R.string.entertainment, 3)
    data object Housing : ExpensesCategoryTypes(R.string.housing, 4)
    data object Education : ExpensesCategoryTypes(R.string.education, 5)
    data object Other : ExpensesCategoryTypes(R.string.other, 6)
}
