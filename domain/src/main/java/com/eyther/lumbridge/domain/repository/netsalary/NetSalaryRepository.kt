package com.eyther.lumbridge.domain.repository.netsalary

import com.eyther.lumbridge.domain.model.finance.NetSalary
import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.domain.model.user.UserFinancialsDomain
import com.eyther.lumbridge.domain.repository.netsalary.portugal.PortugalNetSalaryCalculator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NetSalaryRepository @Inject constructor(
    private val portugalNetSalaryCalculator: PortugalNetSalaryCalculator
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
        withContext(Dispatchers.Default) {
            return@withContext when (locale) {
                SupportedLocales.PORTUGAL -> portugalNetSalaryCalculator.calculate(
                    userFinancialsDomain = userFinancialsDomain
                )
            }
        }
}
