package com.eyther.lumbridge.domain.repository.mortgage.portugal

import com.eyther.lumbridge.domain.model.finance.MortgageCalculation
import com.eyther.lumbridge.domain.model.finance.MortgageType
import com.eyther.lumbridge.domain.model.user.UserMortgageDomain
import com.eyther.lumbridge.domain.repository.mortgage.MortgageCalculator
import javax.inject.Inject

class PortugalMortgageCalculator @Inject constructor() : MortgageCalculator {
    override fun calculate(mortgageDomain: UserMortgageDomain): MortgageCalculation {
        val interestRate = when (mortgageDomain.mortgageType) {
            MortgageType.FIXED -> {
                requireNotNull(mortgageDomain.fixedInterestRate)
            }
            MortgageType.VARIABLE -> {
                requireNotNull(mortgageDomain.euribor) + requireNotNull(mortgageDomain.spread)
            }
        }

        return MortgageCalculation(
            loanAmount = mortgageDomain.loanAmount,
            monthlyPayment = 0f,
            remainingAmount = 0f,
            monthsLeft = 0,
            euribor = 0f,
            spread = 0f,
            fixedInterestRate = 0f,
            mortgageType = mortgageDomain.mortgageType,
            totalPaid = 0f
        )
    }
}
