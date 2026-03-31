package com.eyther.lumbridge.domain.usecase.finance

import com.eyther.lumbridge.domain.repository.netsalary.NetSalaryRepository
import com.eyther.lumbridge.domain.mapper.finance.toUi
import com.eyther.lumbridge.domain.mapper.user.toDomain
import com.eyther.lumbridge.domain.model.finance.NetSalaryUi
import com.eyther.lumbridge.domain.model.user.UserFinancialsUi
import com.eyther.lumbridge.usecase.user.profile.GetLocaleOrDefault
import javax.inject.Inject

class GetNetSalaryUseCase @Inject constructor(
    private val netSalaryRepository: NetSalaryRepository,
    private val getLocaleOrDefault: GetLocaleOrDefault
) {
    suspend operator fun invoke(
        userFinancialsUi: UserFinancialsUi
    ): NetSalaryUi {
        val locale = getLocaleOrDefault()

        return netSalaryRepository.calculate(
            userFinancialsDomain = userFinancialsUi.toDomain(),
            locale = locale
        ).toUi()
    }
}
