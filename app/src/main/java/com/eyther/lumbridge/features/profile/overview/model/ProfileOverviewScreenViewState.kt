package com.eyther.lumbridge.features.profile.overview.model

import com.eyther.lumbridge.domain.model.locale.SupportedLocales

sealed interface ProfileOverviewScreenViewState {
    data object Loading: ProfileOverviewScreenViewState

    data class Content(
        val username: String?,
        val email: String?,
        val locale: SupportedLocales?
    ): ProfileOverviewScreenViewState
}
