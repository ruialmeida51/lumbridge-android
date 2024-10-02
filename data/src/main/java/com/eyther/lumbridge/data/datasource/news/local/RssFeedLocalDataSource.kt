package com.eyther.lumbridge.data.datasource.news.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.eyther.lumbridge.data.di.LocalDataModule.RssFeedDataStore
import com.eyther.lumbridge.data.di.UtilModule.DefaultGson
import com.eyther.lumbridge.data.model.news.local.RssFeedCached
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.lang.reflect.Type
import javax.inject.Inject

class RssFeedLocalDataSource @Inject constructor(
    @RssFeedDataStore private val rssFeedDataStore: DataStore<Preferences>,
    @DefaultGson private val gson: Gson
) {
    private object PreferencesKeys {
        val feeds = stringPreferencesKey("feeds")
    }

    val rssFeedFlow: Flow<List<RssFeedCached>?> = rssFeedDataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val jsonType: Type = object : TypeToken<List<RssFeedCached>>() {}.type
            val feeds = preferences[PreferencesKeys.feeds] ?: return@map null

            gson.fromJson(feeds, jsonType)
        }

    suspend fun saveRssFeed(rssFeeds: List<RssFeedCached>) {
        val jsonType: Type = object : TypeToken<List<RssFeedCached>>() {}.type

        rssFeedDataStore.edit { preferences ->
            preferences[PreferencesKeys.feeds] = gson.toJson(rssFeeds, jsonType)
        }
    }
}
