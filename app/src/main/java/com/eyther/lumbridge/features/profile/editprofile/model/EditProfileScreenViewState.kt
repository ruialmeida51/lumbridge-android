package com.eyther.lumbridge.features.profile.editprofile.model

import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.model.user.UserProfileUi

sealed interface EditProfileScreenViewState {
    data object Loading : EditProfileScreenViewState

    sealed class Content(
        open val availableLocales: List<SupportedLocales>
    ): EditProfileScreenViewState {
        /**
         * There's no data to display, we need to ask the user to create a profile.
         */
        data class NoData(
            override val availableLocales: List<SupportedLocales>
        ): Content(availableLocales)

        /**
         * We have saved user data that we can display.
         */
        data class Data(
            override val availableLocales: List<SupportedLocales>,
            val currentData: UserProfileUi
        ): Content(availableLocales)

        /**
         * @return the user data if it exists, otherwise null.
         */
        fun getUserData() = when(this) {
            is NoData -> null
            is Data -> currentData
        }
    }
}
