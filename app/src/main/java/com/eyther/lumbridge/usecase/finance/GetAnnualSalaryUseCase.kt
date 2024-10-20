package com.eyther.lumbridge.usecase.finance

import com.eyther.lumbridge.domain.repository.netsalary.NetSalaryRepository
import com.eyther.lumbridge.usecase.user.profile.GetLocaleOrDefault
import javax.inject.Inject

class GetAnnualSalaryUseCase @Inject constructor(
    private val netSalaryRepository: NetSalaryRepository,
    private val getLocaleOrDefault: GetLocaleOrDefault
) {
    suspend operator fun invoke(monthlySalary: Float?): Float {
        val locale = getLocaleOrDefault()

        return netSalaryRepository.getAnnualSalary(
            monthlySalary = monthlySalary ?: 0f,
            locale = locale
        )
    }
}
