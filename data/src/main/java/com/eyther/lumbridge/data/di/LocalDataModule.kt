package com.eyther.lumbridge.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.eyther.lumbridge.data.database.migration.V5ToV6Migration
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
        @ApplicationContext context: Context,
        v5ToV6Migration: V5ToV6Migration
    ): LumbridgeRoomDatabase {
        return Room.databaseBuilder(
            context,
            LumbridgeRoomDatabase::class.java,
            "lumbridge_room_database"
        )
            .addMigrations(v5ToV6Migration)
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
    fun provideShoppingDao(
        lumbridgeRoomDatabase: LumbridgeRoomDatabase
    ) = lumbridgeRoomDatabase.shoppingDao()

    @Provides
    @Singleton
    fun provideNotesDao(
        lumbridgeRoomDatabase: LumbridgeRoomDatabase
    ) = lumbridgeRoomDatabase.notesDao()

    @Provides
    @Singleton
    fun provideLoanDao(
        lumbridgeRoomDatabase: LumbridgeRoomDatabase
    ) = lumbridgeRoomDatabase.loanDao()

    @Provides
    @Singleton
    fun provideRecurringPaymentsDao(
        lumbridgeRoomDatabase: LumbridgeRoomDatabase
    ) = lumbridgeRoomDatabase.recurringPaymentsDao()

    @Provides
    @Singleton
    fun provideSnapshotSalaryDao(
        lumbridgeRoomDatabase: LumbridgeRoomDatabase
    ) = lumbridgeRoomDatabase.snapshotSalaryDao()

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
    @Deprecated("Use Room instead. This will be removed in the future, for now it is only maintained for migration purposes.")
    annotation class UserMortgageDataStore

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class CurrencyRatesDataStore

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AppSettingsDataStore
}
