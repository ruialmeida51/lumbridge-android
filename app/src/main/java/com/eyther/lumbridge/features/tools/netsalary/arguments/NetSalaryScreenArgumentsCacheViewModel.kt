package com.eyther.lumbridge.features.tools.netsalary.arguments

import androidx.lifecycle.ViewModel
import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.model.finance.NetSalaryUi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NetSalaryScreenArgumentsCacheViewModel @Inject constructor() :
    ViewModel(),
    INetSalaryScreenArgumentsCacheViewModel {

    override lateinit var netSalaryUi: NetSalaryUi
    override lateinit var locale: SupportedLocales

    override fun cacheArguments(
        netSalaryUi: NetSalaryUi,
        locale: SupportedLocales
    ) {
        this.netSalaryUi = netSalaryUi
        this.locale = locale
    }
}
