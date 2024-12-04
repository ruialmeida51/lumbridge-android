package com.eyther.lumbridge.data.datasource.user.local

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Base64
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.eyther.lumbridge.data.datasource.user.local.UserProfileLocalDataSource.PreferencesKeys.COUNTRY_CODE
import com.eyther.lumbridge.data.datasource.user.local.UserProfileLocalDataSource.PreferencesKeys.EMAIL
import com.eyther.lumbridge.data.datasource.user.local.UserProfileLocalDataSource.PreferencesKeys.IMAGE_BITMAP
import com.eyther.lumbridge.data.datasource.user.local.UserProfileLocalDataSource.PreferencesKeys.NAME
import com.eyther.lumbridge.data.di.LocalDataModule.UserProfileDataSource
import com.eyther.lumbridge.data.model.user.local.UserProfileCached
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.ByteArrayOutputStream
import java.io.IOException
import javax.inject.Inject


class UserProfileLocalDataSource @Inject constructor(
    @UserProfileDataSource private val userProfileDataSource: DataStore<Preferences>
) {
    companion object {
        private const val TAG = "UserProfileLocalDataSource"
    }

    private object PreferencesKeys {
        val NAME = stringPreferencesKey("name")
        val EMAIL = stringPreferencesKey("email")
        val COUNTRY_CODE = stringPreferencesKey("country_code")
        val IMAGE_BITMAP = stringPreferencesKey("image_bitmap")
    }

    val userProfileFlow: Flow<UserProfileCached?> = userProfileDataSource.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val name = preferences[NAME] ?: return@map null
            val email = preferences[EMAIL] ?: return@map null
            val countryCode = preferences[COUNTRY_CODE] ?: return@map null
            val imageUri = convertStringToBitmap(preferences[IMAGE_BITMAP])

            UserProfileCached(
                name = name,
                email = email,
                countryCode = countryCode,
                imageBitmap = imageUri
            )
        }

    suspend fun saveUserData(userProfileCached: UserProfileCached) {
        userProfileDataSource.edit { preferences ->
            preferences[NAME] = userProfileCached.name
            preferences[EMAIL] = userProfileCached.email
            preferences[COUNTRY_CODE] = userProfileCached.countryCode

            userProfileCached.imageBitmap?.let { nonNullImageUri ->
                preferences[IMAGE_BITMAP] = convertBitmapToString(nonNullImageUri)
            }
        }
    }

    private fun convertBitmapToString(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            bitmap.compress(Bitmap.CompressFormat.WEBP_LOSSY, 100, byteArrayOutputStream)
        } else {
            bitmap.compress(Bitmap.CompressFormat.WEBP, 100, byteArrayOutputStream)
        }

        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)
    }

    private fun convertStringToBitmap(bitmapAsString: String?): Bitmap? {
        if (bitmapAsString.isNullOrEmpty()) return null

        return try {
            val encodeByte: ByteArray = Base64.decode(bitmapAsString, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
        } catch (e: Exception) {
            Log.e(TAG, "💥 Error decoding image", e)
            null
        }
    }
}
