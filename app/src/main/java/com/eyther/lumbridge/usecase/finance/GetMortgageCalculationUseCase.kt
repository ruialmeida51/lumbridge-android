package com.eyther.lumbridge.usecase.finance

import com.eyther.lumbridge.domain.repository.loan.LoanRepository
import com.eyther.lumbridge.usecase.user.profile.GetLocaleOrDefault
import javax.inject.Inject

class GetMortgageCalculationUseCase @Inject constructor(
    private val loanRepository: LoanRepository,
    private val getLocaleOrDefault: GetLocaleOrDefault
) {
    suspend operator fun invoke() {
//        val locale = getLocaleOrDefault()
//
//        return loanRepository.calculate(
//            userMortgageDomain = userMortgageUi.toDomain(),
//            locale = locale
//        ).toUi()
    }
}
