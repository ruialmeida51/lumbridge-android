package com.eyther.lumbridge.data.datasource.news.remote

import com.eyther.lumbridge.data.datasource.news.service.EuronewsRssClient
import com.eyther.lumbridge.data.datasource.news.service.PortugalNewsRssClient
import com.eyther.lumbridge.data.datasource.news.service.PublicoRssClient
import com.eyther.lumbridge.data.datasource.news.service.RTPRssClient
import javax.inject.Inject

class NewsFeedRemoteDataSource @Inject constructor(
    private val publicoRssClient: PublicoRssClient,
    private val RTPRssClient: RTPRssClient,
    private val euronewsRssClient: EuronewsRssClient,
    private val portugalNewsRssClient: PortugalNewsRssClient
) {
    suspend fun getPublicoRssFeed(): String? {
        return if (publicoRssClient.getRssFeed().isSuccessful) {
            publicoRssClient.getRssFeed().body()
        } else {
            null
        }
    }

    suspend fun getRTPRssFeed(): String? {
        return if (RTPRssClient.getRssFeed().isSuccessful) {
            RTPRssClient.getRssFeed().body()
        } else {
            null
        }
    }

    suspend fun getEuronewsRssFeed(): String? {
        return if (euronewsRssClient.getRssFeed().isSuccessful) {
            euronewsRssClient.getRssFeed().body()
        } else {
            null
        }
    }

    suspend fun getPortugalNewsRssFeed(): String? {
        return if (portugalNewsRssClient.getRssFeed().isSuccessful) {
            portugalNewsRssClient.getRssFeed().body()
        } else {
            null
        }
    }
}
