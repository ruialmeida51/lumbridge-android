package com.eyther.lumbridge.model.notes

data class NoteUi(
    val id: Long = -1,
    val title: String,
    val text: String
) {
    /**
     * Get chunks of text to display in the preview.
     */
    fun getPreviewEntries() = text.chunked(40)
}
