package com.eyther.lumbridge.domain.repository.finance

import com.eyther.lumbridge.domain.model.finance.NetSalary
import com.eyther.lumbridge.domain.repository.finance.portugal.PortugalNetSalaryCalculator
import com.eyther.lumbridge.domain.model.locale.SupportedLocales
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
     * @param annualGrossSalary the gross salary to calculate the net salary from
     * @param foodCardPerDiem the per diem for the food card
     * @param locale the locale to calculate the net salary from
     *
     * @return the net salary and the deductions made
     */
    suspend fun calculate(
        annualGrossSalary: Float,
        foodCardPerDiem: Float,
        locale: SupportedLocales
    ): NetSalary =
        withContext(Dispatchers.Default) {
            return@withContext when (locale) {
                SupportedLocales.PORTUGAL -> portugalNetSalaryCalculator.calculate(
                    annualGrossSalary = annualGrossSalary,
                    foodCardPerDiem = foodCardPerDiem
                )
            }
        }
}
