package com.eyther.lumbridge.domain.model.user

import com.eyther.lumbridge.domain.model.netsalary.deduction.DuodecimosType
import com.eyther.lumbridge.domain.model.netsalary.salarypaymenttype.SalaryInputType

data class UserFinancialsDomain(
    val annualGrossSalary: Float,
    val foodCardPerDiem: Float,
    val salaryInputType: SalaryInputType,
    val duodecimosType: DuodecimosType,
    val savingsPercentage: Int?,
    val necessitiesPercentage: Int?,
    val luxuriesPercentage: Int?,
    val numberOfDependants: Int?,
    val singleIncome: Boolean,
    val married: Boolean,
    val handicapped: Boolean
)
