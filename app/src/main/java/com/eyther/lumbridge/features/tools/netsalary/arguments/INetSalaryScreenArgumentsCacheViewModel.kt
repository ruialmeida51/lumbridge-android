package com.eyther.lumbridge.features.tools.netsalary.arguments

import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.model.finance.NetSalaryUi

/**
 * Android now recommends that we don't pass objects between composable screens, but instead use a
 * cache to store the object and retrieve it when needed via an ID.
 *
 * This is the cache for the NetSalaryUi object.
 *
 * I don't particularly like this approach, but it's what the Android team recommends and they made
 * sure to complicate the process of passing objects between screens.
 */
interface INetSalaryScreenArgumentsCacheViewModel {
    val netSalaryUi: NetSalaryUi
    val locale: SupportedLocales

    fun cacheArguments(
        netSalaryUi: NetSalaryUi,
        locale: SupportedLocales
    )
}
