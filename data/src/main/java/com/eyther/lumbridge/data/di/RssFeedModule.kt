package com.eyther.lumbridge.data.di

import com.eyther.lumbridge.data.datasource.news.service.EuronewsRssClient
import com.eyther.lumbridge.data.datasource.news.service.PortugalNewsRssClient
import com.eyther.lumbridge.data.datasource.news.service.PublicoRssClient
import com.eyther.lumbridge.data.datasource.news.service.RTPRssClient
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
    fun providePublicoNewsClient(): PublicoRssClient = Retrofit.Builder()
        .baseUrl("https://feeds.feedburner.com/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
        .create(PublicoRssClient::class.java)

    @Provides
    @Singleton
    fun provideRTPNewsClient(): RTPRssClient = Retrofit.Builder()
        .baseUrl("https://www.rtp.pt/noticias/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
        .create(RTPRssClient::class.java)
}