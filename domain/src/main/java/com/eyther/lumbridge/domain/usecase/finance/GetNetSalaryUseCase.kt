package com.eyther.lumbridge.domain.usecase.finance

import com.eyther.lumbridge.domain.model.netsalary.NetSalary
import com.eyther.lumbridge.domain.model.user.UserFinancialsDomain
import com.eyther.lumbridge.domain.repository.netsalary.NetSalaryRepository
import com.eyther.lumbridge.domain.usecase.user.profile.GetLocaleOrDefault
import javax.inject.Inject

class GetNetSalaryUseCase @Inject constructor(
    private val netSalaryRepository: NetSalaryRepository,
    private val getLocaleOrDefault: GetLocaleOrDefault
) {
    suspend operator fun invoke(
        userFinancialsDomain: UserFinancialsDomain
    ): NetSalary {
        val locale = getLocaleOrDefault()

        return netSalaryRepository.calculate(
            userFinancialsDomain = userFinancialsDomain,
            locale = locale
        )
    }
}
