package com.eyther.lumbridge.di

import com.eyther.lumbridge.domain.repository.locale.LocaleRepository
import com.eyther.lumbridge.platform.locale.LocaleRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocaleModule {

    @Binds
    @Singleton
    abstract fun bindLocaleRepository(impl: LocaleRepositoryImpl): LocaleRepository
}
