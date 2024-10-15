package com.eyther.lumbridge.domain.repository.netsalary

import com.eyther.lumbridge.domain.di.model.Schedulers
import com.eyther.lumbridge.domain.model.finance.NetSalary
import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.domain.model.user.UserFinancialsDomain
import com.eyther.lumbridge.domain.repository.netsalary.portugal.PortugalNetSalaryCalculator
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NetSalaryRepository @Inject constructor(
    private val portugalNetSalaryCalculator: PortugalNetSalaryCalculator,
    private val schedulers: Schedulers
) {
    /**
     * Calculates the net salary and saves the deductions made. The calculation is done
     * based on the locale provided.
     *
     * @param userFinancialsDomain the user financials to calculate the net salary from
     * @param locale the locale to calculate the net salary from
     *
     * @return the net salary and the deductions made
     */
    suspend fun calculate(
        userFinancialsDomain: UserFinancialsDomain,
        locale: SupportedLocales
    ): NetSalary =
        withContext(schedulers.cpu) {
            return@withContext when (locale) {
                SupportedLocales.PORTUGAL -> portugalNetSalaryCalculator.calculate(
                    userFinancialsDomain = userFinancialsDomain
                )
            }
        }

    /**
     * Calculates the annual salary based on the monthly salary. The calculation is done
     * by the net salary calculator of each locale.
     *
     * @param monthlySalary the monthly salary to calculate the annual salary from
     * @param locale the locale to calculate the annual salary from
     *
     * @return the annual salary
     */
    suspend fun getAnnualSalary(
        monthlySalary: Float,
        locale: SupportedLocales
    ): Float = withContext(schedulers.cpu) {
        return@withContext when (locale) {
            SupportedLocales.PORTUGAL -> portugalNetSalaryCalculator.calculateAnnualSalary(
                monthlySalary
            )
        }
    }

    /**
     * Calculates the monthly salary based on the annual salary. The calculation is done
     * by the net salary calculator of each locale.
     *
     * @param annualSalary the annual salary to calculate the monthly salary from
     * @param locale the locale to calculate the monthly salary from
     *
     * @return the monthly salary
     */
    suspend fun getMonthlySalary(
        annualSalary: Float,
        locale: SupportedLocales
    ): Float = withContext(schedulers.cpu) {
        return@withContext when (locale) {
            SupportedLocales.PORTUGAL -> portugalNetSalaryCalculator.calculateMonthlySalary(
                annualSalary
            )
        }
    }
}

