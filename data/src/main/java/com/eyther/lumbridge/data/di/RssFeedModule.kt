package com.eyther.lumbridge.data.di

import com.eyther.lumbridge.data.datasource.news.service.EconomyOECDRssClient
import com.eyther.lumbridge.data.datasource.news.service.EuronewsRssClient
import com.eyther.lumbridge.data.datasource.news.service.FinanceOECDRssClient
import com.eyther.lumbridge.data.datasource.news.service.PortugalNewsRssClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RssFeedModule {

    @Provides
    @Singleton
    fun provideEuroNewsClient(): EuronewsRssClient = Retrofit.Builder()
        .baseUrl("https://euronews.com/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
        .create(EuronewsRssClient::class.java)

    @Provides
    @Singleton
    fun providePortugalNewsClient(): PortugalNewsRssClient = Retrofit.Builder()
        .baseUrl("https://www.theportugalnews.com/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
        .create(PortugalNewsRssClient::class.java)

    @Provides
    @Singleton
    fun provideEconomyOECDClient(): EconomyOECDRssClient = Retrofit.Builder()
        .baseUrl("https://www.oecd.org/economy/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
        .create(EconomyOECDRssClient::class.java)

    @Provides
    @Singleton
    fun provideFinanceECDClient(): FinanceOECDRssClient = Retrofit.Builder()
        .baseUrl("https://www.oecd.org/finance/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
        .create(FinanceOECDRssClient::class.java)
}