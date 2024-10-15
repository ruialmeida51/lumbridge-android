package com.eyther.lumbridge.shared.di

import com.eyther.lumbridge.shared.di.model.Schedulers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
object SchedulersModule {

    @Provides
    fun provideSchedulers(
        @IoDispatcher io: CoroutineDispatcher,
        @MainDispatcher main: CoroutineDispatcher,
        @CpuDispatcher cpu: CoroutineDispatcher
    ): Schedulers {
        return Schedulers(
            io = io,
            main = main,
            cpu = cpu
        )
    }

    @Provides
    @IoDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @CpuDispatcher
    fun provideCpuDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @MainDispatcher
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Retention(AnnotationRetention.BINARY)
    @Qualifier
    annotation class IoDispatcher

    @Retention(AnnotationRetention.BINARY)
    @Qualifier
    annotation class CpuDispatcher

    @Retention(AnnotationRetention.BINARY)
    @Qualifier
    annotation class MainDispatcher
}
