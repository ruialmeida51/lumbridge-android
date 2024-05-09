package com.eyther.lumbridge.mapper.finance

import com.eyther.lumbridge.R
import com.eyther.lumbridge.domain.model.finance.Deduction
import com.eyther.lumbridge.domain.model.finance.DeductionType
import com.eyther.lumbridge.domain.model.finance.MoneyAllocation
import com.eyther.lumbridge.domain.model.finance.MoneyAllocationType
import com.eyther.lumbridge.domain.model.finance.MortgageCalculation
import com.eyther.lumbridge.domain.model.finance.MortgageType.FIXED
import com.eyther.lumbridge.domain.model.finance.MortgageType.VARIABLE
import com.eyther.lumbridge.domain.model.finance.NetSalary
import com.eyther.lumbridge.model.finance.DeductionUi
import com.eyther.lumbridge.model.finance.MoneyAllocationUi
import com.eyther.lumbridge.model.finance.MortgageUi
import com.eyther.lumbridge.model.finance.NetSalaryUi
import com.eyther.lumbridge.model.mortgage.MortgageTypeUi.Fixed
import com.eyther.lumbridge.model.mortgage.MortgageTypeUi.Variable

fun NetSalary.toUi(): NetSalaryUi {
    return NetSalaryUi(
        annualGrossSalary = annualGrossSalary,
        annualNetSalary = annualNetSalary,
        monthlyGrossSalary = monthlyGrossSalary,
        monthlyNetSalary = monthlyNetSalary,
        monthlyFoodCard = monthlyFoodCard,
        dailyFoodCard = dailyFoodCard,
        deductions = deductions.map { it.toUi() },
        moneyAllocations = moneyAllocation?.map { it.toUi() }
    )
}

fun Deduction.toUi(): DeductionUi {
    return when (this) {
        is Deduction.FlatDeduction -> DeductionUi(
            percentage = null,
            amount = value,
            label = getLabelForDeductionType(type)
        )

        is Deduction.PercentageDeduction -> DeductionUi(
            percentage = (percentage * 100).toString(),
            amount = value,
            label = getLabelForDeductionType(type)
        )
    }
}

private fun getLabelForDeductionType(type: DeductionType): Int {
    return when (type) {
        DeductionType.PortugalDeductionType.IRS -> R.string.irs
        DeductionType.PortugalDeductionType.SocialSecurity -> R.string.social_security
        DeductionType.PortugalDeductionType.Flat -> R.string.flat_tax
    }
}

fun MoneyAllocation.toUi(): MoneyAllocationUi {
    return MoneyAllocationUi(
        amount = amount,
        label = getLabelForMoneyAllocationType(type)
    )
}

private fun getLabelForMoneyAllocationType(type: MoneyAllocationType): Int {
    return when (type) {
        MoneyAllocationType.Luxuries -> R.string.luxuries
        MoneyAllocationType.Necessities -> R.string.necessities
        MoneyAllocationType.Savings -> R.string.savings
    }
}

fun MortgageCalculation.toUi() = MortgageUi(
    monthlyPayment = monthlyPayment,
    loanAmount = loanAmount,
    remainingAmount = remainingAmount,
    monthsLeft = monthsLeft,
    euribor = euribor,
    spread = spread,
    fixedInterestRate = fixedInterestRate,
    mortgageTypeUi = when (mortgageType) {
        FIXED -> Fixed
        VARIABLE -> Variable
    },
    totalPaid = totalPaid
)
