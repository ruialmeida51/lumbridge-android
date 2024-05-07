package com.eyther.lumbridge.features.profile.settings.viewmodel

import com.eyther.lumbridge.features.profile.settings.model.ProfileAppSettingsScreenViewState
import kotlinx.coroutines.flow.StateFlow

interface IProfileAppSettingsScreenViewModel {
    val viewState: StateFlow<ProfileAppSettingsScreenViewState>
}
