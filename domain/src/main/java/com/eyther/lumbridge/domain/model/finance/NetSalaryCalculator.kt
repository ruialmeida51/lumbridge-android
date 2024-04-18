package com.eyther.lumbridge.domain.model.finance

interface NetSalaryCalculator {
    fun calculate(grossSalary: Float): Float
}