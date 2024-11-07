package com.eyther.lumbridge.features.tools.reminders.model.edit

import com.eyther.lumbridge.ui.common.model.text.TextResource

interface RemindersEditScreenViewEffects {
    data object CloseScreen : RemindersEditScreenViewEffects

    data class ShowError(
        val errorMessage: TextResource
    ) : RemindersEditScreenViewEffects
}
