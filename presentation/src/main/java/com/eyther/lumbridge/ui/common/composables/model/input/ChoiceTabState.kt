package com.eyther.lumbridge.ui.common.composables.model.input

import androidx.annotation.StringRes

data class ChoiceTabState(
    val selectedTab: Int = 0,
    @StringRes val tabsStringRes: List<Int> = emptyList()
) {
    fun isSelected(tabIndex: Int) = selectedTab == tabIndex
}
