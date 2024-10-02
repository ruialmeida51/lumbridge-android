package com.eyther.lumbridge.domain.repository.news

import android.text.Html
import com.eyther.lumbridge.data.datasource.news.local.RssFeedLocalDataSource
import com.eyther.lumbridge.data.datasource.news.remote.NewsFeedRemoteDataSource
import com.eyther.lumbridge.domain.mapper.feed.toCached
import com.eyther.lumbridge.domain.mapper.feed.toDomain
import com.eyther.lumbridge.domain.model.news.Feed
import com.eyther.lumbridge.domain.model.news.FeedItem
import com.eyther.lumbridge.domain.model.news.RssFeed
import com.prof18.rssparser.RssParserBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NewsFeedRepository @Inject constructor(
    private val newsFeedRemoteDataSource: NewsFeedRemoteDataSource,
    private val rssFeedLocalDataSource: RssFeedLocalDataSource
) {
    fun getAvailableFeedsFlow(): Flow<List<RssFeed>> {
        return rssFeedLocalDataSource
            .rssFeedFlow
            .mapNotNull { it?.toDomain() }
    }

    suspend fun getAvailableFeeds(): List<RssFeed> {
        return rssFeedLocalDataSource.rssFeedFlow.firstOrNull()
            .orEmpty()
            .toDomain()
    }

    suspend fun saveRssFeed(rssFeed: RssFeed) {
        val currentFeeds = rssFeedLocalDataSource.rssFeedFlow.firstOrNull().orEmpty()
        val cachedFeedToAdd = rssFeed.toCached()

        val sanitisedListOfFeeds = currentFeeds.map { it.copy(name = it.name.replace("\\s".toRegex(), "")) }
        val sanitisedFeedToAdd = cachedFeedToAdd.copy(name = cachedFeedToAdd.name.replace("\\s".toRegex(), ""))

        // Check if the feed is already in the list. If it is, update it.
        val newFeeds = if (sanitisedListOfFeeds.any { it.name.equals(sanitisedFeedToAdd.name, true) }) {
            sanitisedListOfFeeds.filter { it.name != sanitisedFeedToAdd.name } + sanitisedFeedToAdd
        } else {
            sanitisedListOfFeeds + sanitisedFeedToAdd
        }

        rssFeedLocalDataSource.saveRssFeed(newFeeds)
    }

    suspend fun removeRssFeed(rssFeed: RssFeed) {
        val currentFeeds = rssFeedLocalDataSource.rssFeedFlow.firstOrNull().orEmpty()
        val newFeeds = currentFeeds.filter { it.name != rssFeed.toCached().name }

        rssFeedLocalDataSource.saveRssFeed(newFeeds)
    }

    suspend fun getNewsFeed(rssFeed: RssFeed): Feed = withContext(Dispatchers.IO) {
        val result = newsFeedRemoteDataSource.getRssFeed(rssFeed.url).orEmpty()

        val parsedRss = RssParserBuilder(charset = Charsets.UTF_8)
            .build()
            .parse(result)

        val feedItems = parsedRss.items.map {
            FeedItem(
                title = it.title.orEmpty().sanitize(),
                description = it.description.orEmpty().sanitize(),
                link = it.link.orEmpty().sanitize(),
                image = it.image.orEmpty().sanitize(),
                pubDate = it.pubDate.orEmpty().sanitize()
            )
        }

        return@withContext Feed(feedItems)
    }

    /**
     * Some RSS feeds have HTML tags in their content. This method sanitizes the content to avoid
     * any issues with the app. It uses [Html.fromHtml] to remove the tags.
     */
    private fun String.sanitize(): String {
        return Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY).toString().trim()
    }
}
