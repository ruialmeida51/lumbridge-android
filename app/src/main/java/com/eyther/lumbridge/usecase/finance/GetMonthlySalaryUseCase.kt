package com.eyther.lumbridge.usecase.finance

import com.eyther.lumbridge.domain.repository.netsalary.NetSalaryRepository
import com.eyther.lumbridge.usecase.user.profile.GetLocaleOrDefault
import javax.inject.Inject

class GetMonthlySalaryUseCase @Inject constructor(
    private val netSalaryRepository: NetSalaryRepository,
    private val getLocaleOrDefault: GetLocaleOrDefault
) {
    suspend operator fun invoke(annualSalary: Float?): Float {
        val locale = getLocaleOrDefault()

        return netSalaryRepository.getMonthlySalary(
            annualSalary = annualSalary ?: 0f,
            locale = locale
        )
    }
}
