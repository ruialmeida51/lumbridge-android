package com.eyther.lumbridge.domain.repository.news

import com.eyther.lumbridge.domain.model.news.Feed
import com.eyther.lumbridge.domain.model.news.RssFeed
import kotlinx.coroutines.flow.Flow

interface NewsFeedRepository {
    fun getAvailableFeedsFlow(): Flow<List<RssFeed>>
    suspend fun saveRssFeed(rssFeed: RssFeed)
    suspend fun removeRssFeed(rssFeedId: Long)
    suspend fun getNewsFeed(rssFeed: RssFeed): Feed
}
