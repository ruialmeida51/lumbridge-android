package com.eyther.lumbridge.domain.model.finance

import com.eyther.lumbridge.domain.model.finance.portugal.PortugalNetSalaryCalculator
import com.eyther.lumbridge.domain.model.locale.InternalLocale
import javax.inject.Inject

class NetSalaryProvider @Inject constructor(
    private val portugalNetSalaryCalculator: PortugalNetSalaryCalculator
) {

    fun calculate(grossSalary: Float, locale: InternalLocale) = when(locale) {
        InternalLocale.PORTUGAL -> portugalNetSalaryCalculator.calculate(grossSalary)
    }
}