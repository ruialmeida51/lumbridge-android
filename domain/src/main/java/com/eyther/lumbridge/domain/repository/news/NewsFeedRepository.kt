package com.eyther.lumbridge.domain.repository.news

import android.text.Html
import com.eyther.lumbridge.shared.di.model.Schedulers
import com.eyther.lumbridge.domain.mapper.feed.toCached
import com.eyther.lumbridge.domain.mapper.feed.toDomain
import com.eyther.lumbridge.domain.model.news.Feed
import com.eyther.lumbridge.domain.model.news.FeedItem
import com.eyther.lumbridge.domain.model.news.RssFeed
import com.prof18.rssparser.RssParserBuilder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface NewsFeedRepository {
    val sanitisedFeedToAdd
    val result
    val parsedRss
    val feedItems
    fun getAvailableFeedsFlow(): Flow<List<RssFeed>>
    suspend fun saveRssFeed(rssFeed: RssFeed)
    suspend fun removeRssFeed(rssFeedId: Long)
    suspend fun getNewsFeed(rssFeed: RssFeed): Feed
}
