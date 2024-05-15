package com.eyther.lumbridge.domain.repository.mortgage.portugal

import com.eyther.lumbridge.domain.model.finance.mortgage.MortgageAmortization
import com.eyther.lumbridge.domain.model.finance.mortgage.MortgageCalculation
import com.eyther.lumbridge.domain.model.user.UserMortgageDomain
import com.eyther.lumbridge.domain.repository.mortgage.MortgageCalculator
import com.eyther.lumbridge.domain.time.MONTHS_IN_YEAR
import javax.inject.Inject
import kotlin.math.pow

class PortugalMortgageCalculator @Inject constructor() : MortgageCalculator {

    companion object {
        private const val PERCENTAGE = 100.0
        private const val DEFAULT_AMORTIZATION = 10000.0f
    }

    override fun calculate(mortgageDomain: UserMortgageDomain): MortgageCalculation {
        val nextPayment = calculateNextPayment(
            interestRate = requireNotNull(mortgageDomain.interestRate),
            loanAmount = mortgageDomain.loanAmount,
            remainingMonths = mortgageDomain.monthsRemaining
        )

        val (nextPaymentCapital, nextPaymentInterest) = calculateMonthlyPaymentCapitalAndInterestPortions(
            totalMonthlyPayment = nextPayment,
            remainingLoanAmount = mortgageDomain.loanAmount.toDouble(),
            interestRate = requireNotNull(mortgageDomain.interestRate)
        )

        val amortizations = calculateAmortization(
            interestRate = requireNotNull(mortgageDomain.interestRate),
            loanAmount = mortgageDomain.loanAmount,
            remainingMonths = mortgageDomain.monthsRemaining
        )

        return MortgageCalculation(
            loanAmount = mortgageDomain.loanAmount,
            monthlyPayment = nextPayment.toFloat(),
            monthlyPaymentCapital = nextPaymentCapital.toFloat(),
            monthsLeft = mortgageDomain.monthsRemaining,
            euribor = mortgageDomain.euribor,
            spread = mortgageDomain.spread,
            fixedInterestRate = mortgageDomain.fixedInterestRate,
            mortgageType = mortgageDomain.mortgageType,
            monthlyPaymentInterest = nextPaymentInterest.toFloat(),
            amortizations = amortizations
        )
    }

    /**
     * Given a fixed interest rate, loan amount, and remaining months, calculates the next payment.
     * The formula used is the following:
     * P = r * L / (1 - (1 + r)^-n)
     * where:
     * P = monthly payment
     * r = monthly interest rate
     * L = loan amount
     * n = remaining months
     * The formula is derived from the PMT function in Excel.
     *
     * Remember that the interest rate is given in annual percentage (e.g., 3.92%).
     * To convert it to a decimal, we divide by 100 (3.92 / 100 = 0.0392).
     * Then, to obtain the monthly interest rate, we divide by 12 (0.0392 / 12).
     *
     * @param interestRate The interest rate as a percentage.
     * @param loanAmount The loan amount.
     * @param remainingMonths The remaining months.
     * @return The next payment.
     */
    private fun calculateNextPayment(
        interestRate: Float,
        loanAmount: Float,
        remainingMonths: Int
    ): Double {
        val monthlyInterestRate = calculateMonthlyInterestRate(
            annualInterestRate = interestRate.toDouble()
        )

        val denominator = calculateDenominator(
            remainingMonths = remainingMonths,
            monthlyInterestRate = monthlyInterestRate
        )

        return monthlyInterestRate * loanAmount.toDouble() / denominator
    }

    /**
     * Calculates the denominator of the formula used to calculate the next payment.
     * The denominator is the following:
     * 1 - (1 + r)^-n
     * @see calculateNextPayment
     */
    private fun calculateDenominator(remainingMonths: Int, monthlyInterestRate: Double): Double {
        return 1 - (1 + monthlyInterestRate).pow(-remainingMonths.toDouble())
    }

    /**
     * Calculates the monthly interest rate.
     *
     * The interest rate is given in annual percentage (e.g., 3.92%).
     * To convert it to a decimal, we divide by 100 (3.92 / 100 = 0.0392).
     * Then, to obtain the monthly interest rate, we divide by 12 (0.0392 / 12).
     *
     * @param annualInterestRate The annual interest rate as a percentage.
     * @return The monthly interest rate as a decimal.
     */
    private fun calculateMonthlyInterestRate(annualInterestRate: Double): Double {
        return annualInterestRate / PERCENTAGE / MONTHS_IN_YEAR
    }

    /**
     * Calculates the interest payment and capital payment for a given total monthly payment.
     *
     * The formula used for interest payment is the following:
     * I = P - C
     * where:
     * I = interest payment
     * P = total monthly payment
     * C = capital payment
     *
     * The formula used for capital payment is the following:
     * C = P - I
     * where:
     * C = capital payment
     * P = total monthly payment
     * I = interest payment
     *
     * @param totalMonthlyPayment The total monthly payment.
     * @param remainingLoanAmount The remaining loan amount.
     * @param interestRate The interest rate as a percentage.
     */
    private fun calculateMonthlyPaymentCapitalAndInterestPortions(
        totalMonthlyPayment: Double,
        remainingLoanAmount: Double,
        interestRate: Float
    ): Pair<Double, Double> {
        val monthlyInterestRate = calculateMonthlyInterestRate(interestRate.toDouble())
        val interestPayment = remainingLoanAmount * monthlyInterestRate
        val capitalPayment = totalMonthlyPayment - interestPayment

        return capitalPayment to interestPayment
    }

    /**
     * Calculates the amortizations for a given mortgage.
     * The amortizations are calculated based on the mortgage type.
     * @param interestRate The interest rate as a percentage.
     * @param loanAmount The loan amount.
     * @param remainingMonths The remaining months.
     * @return The amortizations.
     */
    private fun calculateAmortization(
        interestRate: Float,
        loanAmount: Float,
        remainingMonths: Int
    ): List<MortgageAmortization> {
        val amortizations = mutableListOf<MortgageAmortization>()
        var startingAmount = loanAmount
        var iterations = 0

        while (startingAmount >= DEFAULT_AMORTIZATION) {
            startingAmount -= DEFAULT_AMORTIZATION
            iterations += 1

            val nextPayment = calculateNextPayment(
                interestRate = interestRate,
                loanAmount = startingAmount,
                remainingMonths = remainingMonths
            )

            amortizations.add(
                MortgageAmortization(
                    amortization = DEFAULT_AMORTIZATION * iterations,
                    remainder = startingAmount,
                    nextPayment = nextPayment.toFloat()
                )
            )
        }

        return amortizations
    }
}
