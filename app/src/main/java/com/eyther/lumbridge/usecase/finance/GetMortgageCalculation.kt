package com.eyther.lumbridge.usecase.finance

import com.eyther.lumbridge.domain.repository.mortgage.MortgageRepository
import com.eyther.lumbridge.mapper.finance.toUi
import com.eyther.lumbridge.mapper.user.toDomain
import com.eyther.lumbridge.model.finance.MortgageUi
import com.eyther.lumbridge.model.user.UserMortgageUi
import com.eyther.lumbridge.usecase.user.profile.GetLocaleOrDefault
import javax.inject.Inject

class GetMortgageCalculation @Inject constructor(
    private val mortgageRepository: MortgageRepository,
    private val getLocaleOrDefault: GetLocaleOrDefault
) {
    suspend operator fun invoke(userMortgageUi: UserMortgageUi) : MortgageUi {
        val locale = getLocaleOrDefault()

        return mortgageRepository.calculate(
            userMortgageDomain = userMortgageUi.toDomain(),
            locale = locale
        ).toUi()
    }
}
