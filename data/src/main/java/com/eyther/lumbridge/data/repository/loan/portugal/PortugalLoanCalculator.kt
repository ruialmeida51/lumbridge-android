package com.eyther.lumbridge.data.repository.loan.portugal

import com.eyther.lumbridge.domain.model.loan.LoanDomain
import com.eyther.lumbridge.domain.model.loan.LoanAmortization
import com.eyther.lumbridge.domain.model.loan.LoanCalculation
import com.eyther.lumbridge.domain.model.loan.LoanInterestRate
import com.eyther.lumbridge.domain.repository.loan.LoanCalculator
import com.eyther.lumbridge.shared.time.extensions.MONTHS_IN_YEAR
import javax.inject.Inject
import kotlin.math.pow

class PortugalLoanCalculator @Inject constructor() : LoanCalculator {

    companion object {
        private const val PERCENTAGE = 100.0f
        private const val DEFAULT_AMORTIZATION = 5000f
        private const val MAX_ITERATIONS = 20
    }

    override suspend fun calculate(loanDomain: LoanDomain): LoanCalculation {
        val nextPayment = calculateMonthlyPayment(
            interestRate = loanDomain.loanInterestRate,
            loanAmount = loanDomain.currentAmount,
            remainingMonths = loanDomain.remainingMonths
        )

        val (nextPaymentCapital, nextPaymentInterest) = calculateMonthlyPaymentCapitalAndInterestPortions(
            totalMonthlyPayment = nextPayment,
            remainingLoanAmount = loanDomain.currentAmount,
            interestRate = loanDomain.loanInterestRate
        )

        val amortizations = calculateAmortization(
            interestRate = loanDomain.loanInterestRate,
            loanAmount = loanDomain.currentAmount,
            remainingMonths = loanDomain.remainingMonths
        )

        return LoanCalculation(
            loanAmount = loanDomain.currentAmount,
            monthlyPayment = nextPayment,
            monthlyPaymentCapital = nextPaymentCapital,
            remainingMonths = loanDomain.remainingMonths,
            loanCategory = loanDomain.loanCategory,
            loanType = loanDomain.loanType,
            monthlyPaymentInterest = nextPaymentInterest,
            amortizations = amortizations
        )
    }

    private fun calculateMonthlyInterestRate(interestRate: LoanInterestRate) = when (interestRate) {
        is LoanInterestRate.EuriborVariable -> {
            (interestRate.euribor + interestRate.spread) / PERCENTAGE / MONTHS_IN_YEAR
        }
        is LoanInterestRate.FixedTan -> {
            interestRate.fixedInterestRate / PERCENTAGE / MONTHS_IN_YEAR
        }
        is LoanInterestRate.Taeg -> {
            ((1 + interestRate.taegInterestRate / PERCENTAGE).pow(1.0f / MONTHS_IN_YEAR)) - 1
        }
    }

    private fun calculateDenominator(remainingMonths: Int, monthlyInterestRate: Float) =
        1 - (1 + monthlyInterestRate).pow(-remainingMonths)

    private fun calculateMonthlyPayment(
        interestRate: LoanInterestRate,
        loanAmount: Float,
        remainingMonths: Int
    ): Float {
        val monthlyInterestRate = calculateMonthlyInterestRate(interestRate)
        val denominator = calculateDenominator(remainingMonths, monthlyInterestRate)
        return loanAmount * monthlyInterestRate / denominator
    }

    private fun calculateMonthlyPaymentCapitalAndInterestPortions(
        totalMonthlyPayment: Float,
        remainingLoanAmount: Float,
        interestRate: LoanInterestRate
    ): Pair<Float, Float> {
        val monthlyInterestRate = calculateMonthlyInterestRate(interestRate)
        val interestPayment = remainingLoanAmount * monthlyInterestRate
        val capitalPayment = totalMonthlyPayment - interestPayment
        return capitalPayment to interestPayment
    }

    private fun calculateAmortization(
        interestRate: LoanInterestRate,
        loanAmount: Float,
        remainingMonths: Int
    ): List<LoanAmortization> {
        val amortizations = mutableListOf<LoanAmortization>()
        var startingAmount = loanAmount
        var iterations = 0

        while (iterations < MAX_ITERATIONS && DEFAULT_AMORTIZATION < startingAmount) {
            startingAmount -= DEFAULT_AMORTIZATION
            iterations++

            val nextPayment = calculateMonthlyPayment(
                interestRate = interestRate,
                loanAmount = startingAmount,
                remainingMonths = remainingMonths
            )

            amortizations.add(
                LoanAmortization(
                    amortization = DEFAULT_AMORTIZATION * iterations,
                    remainder = startingAmount,
                    nextPayment = nextPayment
                )
            )
        }

        return amortizations
    }
}
