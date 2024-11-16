package com.eyther.lumbridge.model.finance

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NetSalaryUi(
    val monthlyGrossSalary: Float,
    val monthlyNetSalary: Float,
    val annualNetSalary: Float,
    val annualGrossSalary: Float,
    val monthlyFoodCard: Float,
    val dailyFoodCard: Float,
    val duodecimosTypeUi: DuodecimosTypeUi,
    val deductions: List<DeductionUi>,
    val moneyAllocations: List<MoneyAllocationUi>?
): Parcelable
