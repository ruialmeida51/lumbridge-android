package com.eyther.lumbridge.data.di

import javax.inject.Qualifier

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
