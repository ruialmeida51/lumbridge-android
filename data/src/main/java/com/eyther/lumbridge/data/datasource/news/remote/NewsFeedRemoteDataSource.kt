package com.eyther.lumbridge.data.datasource.news.remote

import com.eyther.lumbridge.data.datasource.news.service.RSSFeedService
import javax.inject.Inject

class NewsFeedRemoteDataSource @Inject constructor(
    private val rssFeedService: RSSFeedService
) {
    suspend fun getRSSFeed(): String? {
        val request = rssFeedService.getEconomyRSSFeed()

        return if (request.isSuccessful) {
            request.body()
        } else {
            null
        }
    }
}
