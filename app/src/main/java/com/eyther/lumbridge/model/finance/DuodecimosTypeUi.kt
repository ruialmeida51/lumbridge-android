package com.eyther.lumbridge.model.finance

import android.os.Parcelable
import androidx.annotation.StringRes
import com.eyther.lumbridge.R
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class DuodecimosTypeUi(
    @StringRes val label: Int,
    val ordinal: Int
): Parcelable {
    companion object {
        fun entries() = listOf(
            TwelveMonths,
            ThirteenMonths,
            FourteenMonths
        )

        fun fromOrdinal(ordinal: Int) = when (ordinal) {
            TwelveMonths.ordinal -> TwelveMonths
            ThirteenMonths.ordinal -> ThirteenMonths
            FourteenMonths.ordinal -> FourteenMonths
            else -> throw IllegalArgumentException("💥 Unknown ordinal: $ordinal")
        }
    }

    @Parcelize
    data object TwelveMonths : DuodecimosTypeUi(
        label = R.string.edit_financial_profile_duocedimos_12_months,
        ordinal = 0
    )

    @Parcelize
    data object ThirteenMonths : DuodecimosTypeUi(
        label = R.string.edit_financial_profile_duodecimos_13_months,
        ordinal = 1
    )

    @Parcelize
    data object FourteenMonths : DuodecimosTypeUi(
        label = R.string.edit_financial_profile_duodecimos_14_months,
        ordinal = 2
    )
}
