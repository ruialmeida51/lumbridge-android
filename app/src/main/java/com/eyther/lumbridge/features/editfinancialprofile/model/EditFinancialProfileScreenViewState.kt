package com.eyther.lumbridge.features.editfinancialprofile.model

import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.model.user.UserFinancialsUi

sealed interface EditFinancialProfileScreenViewState {
    data object Loading : EditFinancialProfileScreenViewState

    sealed class Content(
        open val locale: SupportedLocales
    ): EditFinancialProfileScreenViewState {
        /**
         * There's no data to display, we need to ask the user to create a profile.
         */
        data class NoData(
            override val locale: SupportedLocales
        ): Content(locale)

        /**
         * We have saved user data that we can display.
         */
        data class Data(
            override val locale: SupportedLocales,
            val currentData: UserFinancialsUi
        ): Content(locale)

        /**
         * @return the user data if it exists, otherwise null.
         */
        fun getUserData() = when(this) {
            is NoData -> null
            is Data -> currentData
        }
    }
}
