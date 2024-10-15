package com.eyther.lumbridge.features.profile.overview.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.eyther.lumbridge.shared.di.model.Schedulers
import com.eyther.lumbridge.features.profile.overview.model.ProfileOverviewScreenViewState
import com.eyther.lumbridge.model.user.UserProfileUi
import com.eyther.lumbridge.usecase.user.profile.GetLocaleOrDefault
import com.eyther.lumbridge.usecase.user.profile.GetUserProfileStream
import com.eyther.lumbridge.usecase.user.profile.SaveUserProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileOverviewScreenViewModel @Inject constructor(
    private val getUserProfileStream: GetUserProfileStream,
    private val saveUserProfile: SaveUserProfile,
    private val getLocaleOrDefault: GetLocaleOrDefault,
    private val schedulers: Schedulers
) : ViewModel(), IProfileOverviewScreenViewModel {

    override val viewState = MutableStateFlow<ProfileOverviewScreenViewState>(
        ProfileOverviewScreenViewState.Loading
    )

    init {
        observeChanges()
    }

    private fun observeChanges() {
        getUserProfileStream().onEach { user ->
            viewState.update {
                ProfileOverviewScreenViewState.Content(
                    username = user?.name,
                    email = user?.email,
                    locale = user?.locale,
                    imageBitmap = user?.imageBitmap
                )
            }
        }
            .launchIn(viewModelScope)
    }

    override fun saveImage(image: Bitmap) {
        viewModelScope.launch {
            val currentUser = getUserProfileStream().firstOrNull()

            if (currentUser != null) {
                saveUserProfile(currentUser.copy(imageBitmap = image))
            } else {
                saveUserProfile(UserProfileUi(imageBitmap = image, locale = getLocaleOrDefault()))
            }
        }
    }

    override suspend fun constructBitmapFromUri(uri: Uri, context: Context): Bitmap {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e(this::class.simpleName, "💥 Failed to load image", throwable)
        }

        return withContext(schedulers.cpu + coroutineExceptionHandler) {
            val loader = ImageLoader.Builder(context)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .crossfade(true)
                .build()

            val request = ImageRequest.Builder(context)
                .data(uri)
                .diskCachePolicy(CachePolicy.ENABLED)
                // ARGB_8888 is the standard. We can use other configs if we need to reduce
                // memory usage.
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                // Reduce memory usage when possible, when the image is not transparent.
                .allowRgb565(true)
                .build()

            return@withContext loader.execute(request).drawable?.toBitmap()
                ?: error("💥 Failed to load image")

        }
    }
}
