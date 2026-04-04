package com.eyther.lumbridge.features.profile.overview.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.eyther.lumbridge.features.profile.overview.model.ProfileOverviewScreenViewState
import kotlinx.coroutines.flow.StateFlow

interface IProfileOverviewScreenViewModel {
    val viewState: StateFlow<ProfileOverviewScreenViewState>

    /**
     * Save the image to the device.
     *
     * @param image the image to save
     */
    fun saveImage(image: Bitmap)

    /**
     * Construct a bitmap from the given uri. This is used to display the image in the view, as
     * there are permission problems when trying to display the image directly from the uri aka
     * the system storage.
     *
     * @param uri the uri of the image
     * @param context the context
     *
     * @return the bitmap
     */
    suspend fun constructBitmapFromUri(uri: Uri, context: Context): Bitmap
}
