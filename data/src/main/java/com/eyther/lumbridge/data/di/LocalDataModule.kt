package com.eyther.lumbridge.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.eyther.lumbridge.data.database.room.LumbridgeRoomDatabase
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
    fun provideLumbridgeRoomDatabase(
        @ApplicationContext context: Context
    ): LumbridgeRoomDatabase {
        return Room.databaseBuilder(
            context,
            LumbridgeRoomDatabase::class.java,
            "lumbridge_room_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideExpensesDao(
        lumbridgeRoomDatabase: LumbridgeRoomDatabase
    ) = lumbridgeRoomDatabase.expensesDao()

    @Provides
    @Singleton
    fun provideRssFeedDao(
        lumbridgeRoomDatabase: LumbridgeRoomDatabase
    ) = lumbridgeRoomDatabase.rssFeedDao()

    @Provides
    @Singleton
    fun provideGroceriesDao(
        lumbridgeRoomDatabase: LumbridgeRoomDatabase
    ) = lumbridgeRoomDatabase.groceriesDao()

    @Provides
    @Singleton
    fun provideNotesDao(
        lumbridgeRoomDatabase: LumbridgeRoomDatabase
    ) = lumbridgeRoomDatabase.notesDao()

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
    @UserMortgageDataStore
    fun provideUserMortgageDataSource(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = {
                context.preferencesDataStoreFile("user_mortgage_data_store.db")
            }
        )
    }

    @Provides
    @Singleton
    @CurrencyRatesDataStore
    fun provideCurrencyRatesDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = {
                context.preferencesDataStoreFile("currency_rates_data_store.db")
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
    annotation class UserMortgageDataStore

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class CurrencyRatesDataStore

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AppSettingsDataStore
}
