package com.eyther.lumbridge.domain.repository.finance

import com.eyther.lumbridge.domain.model.finance.NetSalary

interface NetSalaryCalculator {
    /**
     * Calculates the net salary and saves the deductions made.
     *
     * @param annualGrossSalary the annual gross salary to calculate the net salary from
     * @param foodCardPerDiem the per diem for the food card
     *
     * @return the net salary and the deductions made
     */
    fun calculate(annualGrossSalary: Float, foodCardPerDiem: Float): NetSalary
}
