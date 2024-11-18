package com.eyther.lumbridge.model.finance

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.eyther.lumbridge.R
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class MoneyAllocationTypeUi(
    @StringRes val labelRes: Int,
    @DrawableRes val iconRes: Int,
    val ordinal: Int,
    open val allocated: Float
) : Parcelable {
    companion object {
        fun get() = listOf(
            Savings(),
            Luxuries(),
            Necessities()
        ).sortedBy { it.ordinal }

        fun toDefaultAllocationFromOrdinal(ordinal: Int): MoneyAllocationTypeUi {
            return when (ordinal) {
                Necessities().ordinal -> Necessities()
                Savings().ordinal -> Savings()
                Luxuries().ordinal -> Luxuries()
                else -> throw IllegalArgumentException("Unknown ordinal: $ordinal")
            }
        }
    }

    abstract fun updateAllocated(allocated: Float): MoneyAllocationTypeUi

    @Parcelize
    data class Necessities(
        override val allocated: Float = 0f
    ) : MoneyAllocationTypeUi(
        labelRes = R.string.necessities,
        iconRes = R.drawable.ic_allocation_necessities,
        ordinal = 0,
        allocated = allocated
    ) {
        override fun updateAllocated(allocated: Float): MoneyAllocationTypeUi {
            return copy(allocated = allocated)
        }
    }

    @Parcelize
    data class Savings(
        override val allocated: Float = 0f
    ) : MoneyAllocationTypeUi(
        labelRes = R.string.savings,
        iconRes = R.drawable.ic_allocation_savings,
        ordinal = 1,
        allocated = allocated
    ) {
        override fun updateAllocated(allocated: Float): MoneyAllocationTypeUi {
            return copy(allocated = allocated)
        }
    }

    @Parcelize
    data class Luxuries(
        override val allocated: Float = 0f
    ) : MoneyAllocationTypeUi(
        labelRes = R.string.luxuries,
        iconRes = R.drawable.ic_allocation_luxuries,
        ordinal = 2,
        allocated = allocated
    ) {
        override fun updateAllocated(allocated: Float): MoneyAllocationTypeUi {
            return copy(allocated = allocated)
        }
    }
}
