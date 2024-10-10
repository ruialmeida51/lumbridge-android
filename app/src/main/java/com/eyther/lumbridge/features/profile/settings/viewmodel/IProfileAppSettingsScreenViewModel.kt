package com.eyther.lumbridge.features.profile.settings.viewmodel

import com.eyther.lumbridge.features.profile.settings.model.ProfileAppSettingsScreenViewEffect
import com.eyther.lumbridge.features.profile.settings.model.ProfileAppSettingsScreenViewState
import com.eyther.lumbridge.features.profile.settings.viewmodel.delegate.IProfileAppSettingsScreenInputHandler
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface IProfileAppSettingsScreenViewModel: IProfileAppSettingsScreenInputHandler {
    val viewState: StateFlow<ProfileAppSettingsScreenViewState>
    val viewEffects: SharedFlow<ProfileAppSettingsScreenViewEffect>
}
