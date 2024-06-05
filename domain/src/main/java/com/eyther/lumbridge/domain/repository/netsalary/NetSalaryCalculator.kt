package com.eyther.lumbridge.domain.repository.netsalary

import com.eyther.lumbridge.domain.model.finance.NetSalary
import com.eyther.lumbridge.domain.model.user.UserFinancialsDomain

interface NetSalaryCalculator {
    /**
     * Calculates the net salary and saves the deductions made.
     *
     * @param userFinancialsDomain the user financials to calculate the net salary from
     *
     * @return the net salary and the deductions made
     */
    fun calculate(userFinancialsDomain: UserFinancialsDomain): NetSalary

    /**
     * Calculates the annual salary based on the monthly salary.
     */
    fun calculateAnnualSalary(monthlySalary: Float): Float

    /**
     * Calculates the monthly salary based on the annual salary.
     */
    fun calculateMonthlySalary(annualSalary: Float): Float
}
