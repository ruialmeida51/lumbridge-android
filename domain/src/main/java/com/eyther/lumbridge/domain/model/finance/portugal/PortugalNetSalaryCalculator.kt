package com.eyther.lumbridge.domain.model.finance.portugal

import com.eyther.lumbridge.domain.model.finance.NetSalaryCalculator
import javax.inject.Inject

class PortugalNetSalaryCalculator @Inject constructor(): NetSalaryCalculator {
    override fun calculate(grossSalary: Float): Float {
        val monthlySalary = grossSalary / 14f
        val irsDeduction = (monthlySalary * getIrsBracket(monthlySalary)) - getIrsFlatRate(monthlySalary)
        val ssDeduction = monthlySalary * getSocialSecurityRate()
        return monthlySalary - irsDeduction - ssDeduction
    }

    private fun getIrsBracket(monthlySalary: Float, dependants: Int = 0): Float {
        return when {
            monthlySalary <= 820.00 -> 0f
            monthlySalary > 820.00 && monthlySalary <= 935.00 -> 0.1325f
            monthlySalary > 935.00 && monthlySalary <= 1001.00 -> 0.18f
            monthlySalary > 1001.00 && monthlySalary <= 1123.00 -> 0.18f
            monthlySalary > 1123.00 && monthlySalary <= 1765.00 -> 0.26f
            monthlySalary > 1765.00 && monthlySalary <= 2057.00 -> 0.3275f
            monthlySalary > 2057.00 && monthlySalary <= 2664.00 -> 0.37f
            monthlySalary > 2664.00 && monthlySalary <= 3193.00 -> 0.3872f
            monthlySalary > 3193.00 && monthlySalary <= 4173.00 -> 0.4005f
            monthlySalary > 4173.00 && monthlySalary <= 5470.00 -> 0.4100f
            monthlySalary > 5470.00 && monthlySalary <= 6450.00 -> 0.4270f
            monthlySalary > 6450.00 && monthlySalary <= 20067.00 -> 0.4717f
            monthlySalary > 20067.00 -> 0.059f
            else -> error("💥 Could not match salary to tax bracket.")
        }
    }

    private fun getIrsFlatRate(monthlySalary: Float, dependants: Int = 0): Float {
        return when {
            monthlySalary <= 820.00 -> 0f
            monthlySalary > 820.00 && monthlySalary <= 935.00 -> 0.1325f * 2.6f * (1135.39f - monthlySalary)
            monthlySalary > 935.00 && monthlySalary <= 1001.00 -> 0.1325f * 1.4f * (1385.2f - monthlySalary)
            monthlySalary > 1001.00 && monthlySalary <= 1123.00 -> 96.82f
            monthlySalary > 1123.00 && monthlySalary <= 1765.00 -> 186.66f
            monthlySalary > 1765.00 && monthlySalary <= 2057.00 -> 305.80f
            monthlySalary > 2057.00 && monthlySalary <= 2664.00 -> 393.23f
            monthlySalary > 2664.00 && monthlySalary <= 3193.00 -> 439.05f
            monthlySalary > 3193.00 && monthlySalary <= 4173.00 -> 481.52f
            monthlySalary > 4173.00 && monthlySalary <= 5470.00 -> 521.17f
            monthlySalary > 5470.00 && monthlySalary <= 6450.00 -> 614.16f
            monthlySalary > 6450.00 && monthlySalary <= 20067.00 -> 761.31f
            monthlySalary > 20067.00 -> 1206.80f
            else -> error("💥 Could not match salary to tax bracket.")
        }
    }

    private fun getSocialSecurityRate(): Float = 0.11f
}
