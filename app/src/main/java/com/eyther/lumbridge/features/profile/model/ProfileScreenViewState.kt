package com.eyther.lumbridge.features.profile.model

sealed interface ProfileScreenViewState {
    data object Loading: ProfileScreenViewState

    data class Content(
        val username: String,
        val email: String,
        val isDarkModeEnabled: Boolean?
    ): ProfileScreenViewState

    fun asContent(): Content = this as Content
}
