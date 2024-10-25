package com.eyther.lumbridge.domain.repository.loan.portugal

import com.eyther.lumbridge.domain.model.loan.Loan
import com.eyther.lumbridge.domain.model.loan.LoanAmortization
import com.eyther.lumbridge.domain.model.loan.LoanCalculation
import com.eyther.lumbridge.domain.model.loan.LoanInterestRate
import com.eyther.lumbridge.domain.repository.loan.LoanCalculator
import com.eyther.lumbridge.domain.time.MONTHS_IN_YEAR
import javax.inject.Inject
import kotlin.math.pow

class PortugalLoanCalculator @Inject constructor() : LoanCalculator {

    companion object {
        private const val PERCENTAGE = 100.0f
        private const val DEFAULT_AMORTIZATION = 5000f
        private const val MAX_ITERATIONS = 20
    }

    /**
     * Tries to calculate all the meta data about the loan.
     *
     * A few important things to note:
     *
     * The formula used for each interest rate is the following:
     *
     * For EuriborVariable:
     *
     * * P = Monthly Payment
     * * L = Loan Amount
     * * r = Monthly Interest Rate (Euribor + Spread)
     * * n = Remaining Months
     *
     * For FixedTan:
     *
     * * P = Monthly Payment
     * * L = Loan Amount
     * * r = Annual Fixed Interest Rate (Tan divided by 12 months)
     * * n = Remaining Months
     *
     * For Taeg:
     *
     * * P = Monthly Payment
     * * L = Loan Amount
     * * r = Annual Taeg Interest Rate (specific formula to get the monthly interest rate: ((1 +TAEG /100) ^ (1 / 12) - 1)
     * * n = Remaining Months
     *
     * At the end, the formula used for all interest rates is the following:
     *
     * *  `P = L * r / (1 - (1 + r)^-n)`
     */
    override suspend fun calculate(loan: Loan): LoanCalculation {
        val nextPayment = calculateMonthlyPayment(
            interestRate = loan.loanInterestRate,
            loanAmount = loan.currentAmount,
            remainingMonths = loan.remainingMonths
        )

        val (nextPaymentCapital, nextPaymentInterest) = calculateMonthlyPaymentCapitalAndInterestPortions(
            totalMonthlyPayment = nextPayment,
            remainingLoanAmount = loan.currentAmount,
            interestRate = loan.loanInterestRate
        )

        val amortizations = calculateAmortization(
            interestRate = loan.loanInterestRate,
            loanAmount = loan.currentAmount,
            remainingMonths = loan.remainingMonths
        )

        return LoanCalculation(
            loanAmount = loan.currentAmount,
            monthlyPayment = nextPayment,
            monthlyPaymentCapital = nextPaymentCapital,
            remainingMonths = loan.remainingMonths,
            loanCategory = loan.loanCategory,
            loanType = loan.loanType,
            monthlyPaymentInterest = nextPaymentInterest,
            amortizations = amortizations
        )
    }

    /**
     * Given the formula explained in [calculate], this function does the `r` part.
     *
     * r = monthly interest rate, which the calculation depends on the loan type.
     *
     * For EuriborVariable:
     * * `r = (Euribor + Spread) / 100 / 12`
     *
     * For FixedTan:
     * * `r = Fixed Interest Rate / 100 / 12`
     *
     * For Taeg:
     * * `r = ((1 + TAEG / 100) ^ (1 / 12)) - 1`
     *
     * @param interestRate The interest rate as a percentage, depending on the loan type.
     *
     * @see calculate
     *
     * @return The monthly interest rate
     */
    private fun calculateMonthlyInterestRate(
        interestRate: LoanInterestRate
    ) = when (interestRate) {
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

    /**
     * Given the formula explained in [calculate], this function does the `1 - (1 + r)^-n` part, where:
     *
     * * r = monthly interest rate
     * * n = remaining months
     *
     * @param remainingMonths The remaining months.
     * @param monthlyInterestRate The monthly interest rate.
     *
     * @see calculate
     *
     * @return The denominator of the formula
     */
    private fun calculateDenominator(
        remainingMonths: Int,
        monthlyInterestRate: Float
    ) = 1 - (1 + monthlyInterestRate).pow(-remainingMonths)

    /**
     * Given the formula explained in [calculate], this function does the `P = L * r / (1 - (1 + r)^-n)` part.
     *
     * @param interestRate The interest rate as a percentage, depending on the loan type.
     * @param loanAmount The loan amount.
     * @param remainingMonths The remaining months to pay the loan.
     *
     * @see calculate
     */
    private fun calculateMonthlyPayment(
        interestRate: LoanInterestRate,
        loanAmount: Float,
        remainingMonths: Int
    ): Float {
        val monthlyInterestRate = calculateMonthlyInterestRate(interestRate)
        val denominator = calculateDenominator(remainingMonths, monthlyInterestRate)
        return loanAmount * monthlyInterestRate / denominator
    }

    /**
     * Calculates the interest payment and capital payment for a given total monthly payment.
     *
     * The formula used for interest payment is `I = P - C`, where:
     *
     * * I = interest payment
     * * P = total monthly payment
     * * C = capital payment
     *
     * The formula used for capital payment is `C = P - I`, where:
     *
     * * C = capital payment
     * * P = total monthly payment
     * * I = interest payment
     *
     * @param totalMonthlyPayment The total monthly payment, calculated by [calculateMonthlyPayment].
     * @param remainingLoanAmount The remaining loan amount.
     * @param interestRate The interest rate of the loan, depending on the loan type.
     */
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

    /**
     * Calculates the amortizations simulator for a given loan.
     *
     * The amortizations are calculated by subtracting 5% of the loan amount from the remaining amount until 20 iterations,
     * which equals 100% of the loan amount.
     *
     * The formula used for amortization is `A = 5% * R`, where:
     *
     * * A = Amortization
     * * R = Remaining amount
     *
     * @param interestRate The interest rate of the loan, depending on the loan type.
     * @param loanAmount The loan amount.
     * @param remainingMonths The remaining months to pay the loan.
     *
     */
    private fun calculateAmortization(
        interestRate: LoanInterestRate,
        loanAmount: Float,
        remainingMonths: Int
    ): List<LoanAmortization> {
        val amortizations = mutableListOf<LoanAmortization>()

        // Starting amount is the loan amount, we will subtract the default amortization from it
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
