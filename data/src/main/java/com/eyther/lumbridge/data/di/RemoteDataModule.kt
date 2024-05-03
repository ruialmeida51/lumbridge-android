package com.eyther.lumbridge.data.di

import com.eyther.lumbridge.data.datasource.news.service.RSSFeedService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteDataModule {

    @Provides
    @Named("base_url")
    fun provideBaseUrl(): String = "TBA"

    @Provides
    @Named("base_rss_feed_url")
    fun provideBaseRSSFeedUrl(): String = "https://euronews.com/"

    @Provides
    @Singleton
    @AppRetrofitClient
    fun provideRetrofit(
        @Named("base_url") baseUrl: String
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    @RSSFeedRetrofitClient
    fun provideRSSFeedRetrofitClient(
        @Named("base_rss_feed_url") baseRSSFeedUrl: String
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseRSSFeedUrl)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideRSSFeedService(
        @RSSFeedRetrofitClient retrofit: Retrofit
    ): RSSFeedService = retrofit.create(RSSFeedService::class.java)

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class RSSFeedRetrofitClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AppRetrofitClient

}