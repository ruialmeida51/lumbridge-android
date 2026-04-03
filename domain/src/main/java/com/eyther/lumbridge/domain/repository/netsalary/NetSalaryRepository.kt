package com.eyther.lumbridge.domain.repository.netsalary

import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.domain.model.netsalary.NetSalary
import com.eyther.lumbridge.domain.model.user.UserFinancialsDomain

interface NetSalaryRepository {
    suspend fun calculate(userFinancialsDomain: UserFinancialsDomain, locale: SupportedLocales): NetSalary
    suspend fun getAnnualSalary(monthlySalary: Float, locale: SupportedLocales): Float
    suspend fun getMonthlySalary(annualSalary: Float, locale: SupportedLocales): Float
}
