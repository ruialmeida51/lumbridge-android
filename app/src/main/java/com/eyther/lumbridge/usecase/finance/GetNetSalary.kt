package com.eyther.lumbridge.usecase.finance

import com.eyther.lumbridge.domain.repository.finance.NetSalaryRepository
import com.eyther.lumbridge.mapper.finance.toUi
import com.eyther.lumbridge.model.finance.NetSalaryUi
import com.eyther.lumbridge.usecase.user.GetLocaleOrDefault
import javax.inject.Inject

class GetNetSalary @Inject constructor(
    private val netSalaryRepository: NetSalaryRepository,
    private val getLocaleOrDefault: GetLocaleOrDefault
) {
    suspend operator fun invoke(
        annualGrossSalary: Float,
        foodCardPerDiem: Float
    ): NetSalaryUi {
        val locale = getLocaleOrDefault()

        return netSalaryRepository.calculate(
            annualGrossSalary = annualGrossSalary,
            foodCardPerDiem = foodCardPerDiem,
            locale = locale
        ).toUi()
    }
}
