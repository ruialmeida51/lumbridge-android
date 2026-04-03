package com.eyther.lumbridge.ui.common.composables.model.card

sealed interface PeekContentCardType {
    data object PlainText : PeekContentCardType
    data object Checkbox : PeekContentCardType
}
