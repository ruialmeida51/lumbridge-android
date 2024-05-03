package com.eyther.lumbridge.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDataModule {

    @Provides
    @Singleton
    @UserProfileDataSource
    fun provideUserProfileDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = {
                context.preferencesDataStoreFile("user_profile_data_store.db")
            }
        )
    }

    @Provides
    @Singleton
    @UserFinancialsDataStore
    fun provideUserFinancialsDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = {
                context.preferencesDataStoreFile("user_financials_data_store.db")
            }
        )
    }

    @Provides
    @Singleton
    @AppSettingsDataStore
    fun provideAppSettingsDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = {
                context.preferencesDataStoreFile("app_settings_data_store.db")
            }
        )
    }

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class UserProfileDataSource

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class UserFinancialsDataStore

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AppSettingsDataStore
}
