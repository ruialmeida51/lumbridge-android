package com.eyther.lumbridge.model.loan

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.eyther.lumbridge.R

sealed class LoanCategoryUi(
    @StringRes val label: Int,
    @DrawableRes val icon: Int,
    val ordinal: Int
) {
    companion object {
        fun entries() = listOf(House, Car, Personal, Other)

        fun fromOrdinal(ordinal: Int) = when (ordinal) {
            House.ordinal -> House
            Car.ordinal -> Car
            Personal.ordinal -> Personal
            Other.ordinal -> Other
            else -> throw IllegalArgumentException("💥 Unknown ordinal: $ordinal")
        }
    }
    data object House : LoanCategoryUi(R.string.house, R.drawable.ic_home, 0)
    data object Car : LoanCategoryUi(R.string.car, R.drawable.ic_car, 1)
    data object Personal : LoanCategoryUi(R.string.personal, R.drawable.ic_person, 2)
    data object Other : LoanCategoryUi(R.string.other, R.drawable.ic_scatter, 3)
}
