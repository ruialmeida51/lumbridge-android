package com.eyther.lumbridge.features.editfinancialprofile.model

import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.model.user.UserFinancialsUi

sealed interface EditFinancialProfileScreenViewState {
    data object Loading : EditFinancialProfileScreenViewState

    data class Content(
        val locale: SupportedLocales,
        val currentData: UserFinancialsUi? = null,
        val shouldEnableSaveButton: Boolean = false
    ): EditFinancialProfileScreenViewState

    fun asContent() = this as? Content
}
