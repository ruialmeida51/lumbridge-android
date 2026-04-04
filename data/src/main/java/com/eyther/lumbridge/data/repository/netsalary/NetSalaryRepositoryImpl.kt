package com.eyther.lumbridge.data.repository.netsalary

import com.eyther.lumbridge.data.repository.netsalary.portugal.PortugalNetSalaryCalculator
import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.domain.model.netsalary.NetSalary
import com.eyther.lumbridge.domain.model.user.UserFinancialsDomain
import com.eyther.lumbridge.domain.repository.netsalary.NetSalaryRepository
import com.eyther.lumbridge.shared.di.model.Schedulers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NetSalaryRepositoryImpl @Inject constructor(
    private val portugalNetSalaryCalculator: PortugalNetSalaryCalculator,
    private val schedulers: Schedulers
) : NetSalaryRepository {

    override suspend fun calculate(
        userFinancialsDomain: UserFinancialsDomain,
        locale: SupportedLocales
    ): NetSalary = withContext(schedulers.io) {
        withContext(schedulers.cpu) {
            when (locale) {
                SupportedLocales.PORTUGAL -> portugalNetSalaryCalculator.calculate(
                    userFinancialsDomain = userFinancialsDomain
                )
            }
        }
    }

    override suspend fun getAnnualSalary(
        monthlySalary: Float,
        locale: SupportedLocales
    ): Float = withContext(schedulers.cpu) {
        return@withContext when (locale) {
            SupportedLocales.PORTUGAL -> portugalNetSalaryCalculator.calculateAnnualSalary(monthlySalary)
        }
    }

    override suspend fun getMonthlySalary(
        annualSalary: Float,
        locale: SupportedLocales
    ): Float = withContext(schedulers.cpu) {
        return@withContext when (locale) {
            SupportedLocales.PORTUGAL -> portugalNetSalaryCalculator.calculateMonthlySalary(annualSalary)
        }
    }
}
