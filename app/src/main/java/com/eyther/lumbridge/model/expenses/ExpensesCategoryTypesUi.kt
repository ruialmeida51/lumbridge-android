package com.eyther.lumbridge.model.expenses

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.eyther.lumbridge.R
import com.eyther.lumbridge.ui.common.model.math.MathOperator

sealed class ExpensesCategoryTypesUi(
    @StringRes val categoryRes: Int,
    @DrawableRes val iconRes: Int,
    val operator: MathOperator = MathOperator.SUBTRACTION,
    val ordinal: Int,
    val orderOfAppearance: Int
) {
    companion object {
        /**
         * Returns a list of all [ExpensesCategoryTypesUi] in the order they should be displayed.
         *
         * [Surplus] is a special case, where it represents income rather than expenses, counting as
         * extra money and not counting towards the total expenses.
         */
        fun get() = listOf(
            Food,
            Transportation,
            HealthCare,
            Entertainment,
            Housing,
            Education,
            Pets,
            Sports,
            Vacations,
            Other
        ).sortedBy {
            it.orderOfAppearance
        }

        fun of(ordinal: Int): ExpensesCategoryTypesUi {
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
                Surplus.ordinal -> Surplus
                else -> throw IllegalArgumentException("💥 Unknown ordinal: $ordinal")
            }
        }
    }

    data object Food : ExpensesCategoryTypesUi(
        categoryRes = R.string.food,
        iconRes = R.drawable.ic_food,
        ordinal = 0,
        orderOfAppearance = 0
    )

    data object Transportation : ExpensesCategoryTypesUi(
        categoryRes = R.string.transportation,
        iconRes = R.drawable.ic_commute,
        ordinal = 1,
        orderOfAppearance = 1
    )

    data object HealthCare : ExpensesCategoryTypesUi(
        categoryRes = R.string.healthcare,
        iconRes = R.drawable.ic_health,
        ordinal = 2,
        orderOfAppearance = 2
    )

    data object Entertainment : ExpensesCategoryTypesUi(
        categoryRes = R.string.entertainment,
        iconRes = R.drawable.ic_entertainment,
        ordinal = 3,
        orderOfAppearance = 3
    )

    data object Housing : ExpensesCategoryTypesUi(
        categoryRes = R.string.housing,
        iconRes = R.drawable.ic_home,
        ordinal = 4,
        orderOfAppearance = 4
    )

    data object Education : ExpensesCategoryTypesUi(
        categoryRes = R.string.education,
        iconRes = R.drawable.ic_education,
        ordinal = 5,
        orderOfAppearance = 5
    )

    data object Other : ExpensesCategoryTypesUi(
        categoryRes = R.string.other,
        iconRes = R.drawable.ic_category,
        ordinal = 6,
        orderOfAppearance = 10
    )

    data object Pets : ExpensesCategoryTypesUi(
        categoryRes = R.string.pets,
        iconRes = R.drawable.ic_pets,
        ordinal = 7,
        orderOfAppearance = 6
    )

    data object Sports : ExpensesCategoryTypesUi(
        categoryRes = R.string.sports,
        iconRes = R.drawable.ic_sports,
        ordinal = 8,
        orderOfAppearance = 7
    )

    data object Vacations : ExpensesCategoryTypesUi(
        categoryRes = R.string.vacations,
        iconRes = R.drawable.ic_vacation,
        ordinal = 9,
        orderOfAppearance = 8
    )

    data object Surplus : ExpensesCategoryTypesUi(
        categoryRes = R.string.surplus,
        iconRes = R.drawable.ic_savings,
        operator = MathOperator.ADDITION,
        ordinal = 10,
        orderOfAppearance = Int.MAX_VALUE
    )
}
