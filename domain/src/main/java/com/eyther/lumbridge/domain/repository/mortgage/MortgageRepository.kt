package com.eyther.lumbridge.domain.repository.mortgage

import com.eyther.lumbridge.domain.model.finance.MortgageCalculation
import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.domain.model.user.UserMortgageDomain
import com.eyther.lumbridge.domain.repository.mortgage.portugal.PortugalMortgageCalculator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MortgageRepository @Inject constructor(
    private val portugalMortgageCalculator: PortugalMortgageCalculator
) {
    /**
     * Calculates the mortgage.
     *
     * @param userMortgageDomain the user mortgage to calculate the mortgage from
     * @param locale the locale to calculate the mortgage from
     *
     * @return the mortgage calculation
     */
    suspend fun calculate(
        userMortgageDomain: UserMortgageDomain,
        locale: SupportedLocales
    ): MortgageCalculation =
        withContext(Dispatchers.Default) {
            return@withContext when (locale) {
                SupportedLocales.PORTUGAL -> portugalMortgageCalculator.calculate(
                    mortgageDomain = userMortgageDomain
                )
            }
        }
}
