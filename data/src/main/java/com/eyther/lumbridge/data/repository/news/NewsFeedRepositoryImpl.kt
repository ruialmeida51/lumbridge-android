package com.eyther.lumbridge.data.repository.news

import android.text.Html
import com.eyther.lumbridge.data.datasource.news.local.RssFeedLocalDataSource
import com.eyther.lumbridge.data.datasource.news.remote.NewsFeedRemoteDataSource
import com.eyther.lumbridge.data.mapper.feed.toCached
import com.eyther.lumbridge.data.mapper.feed.toDomain
import com.eyther.lumbridge.domain.model.news.Feed
import com.eyther.lumbridge.domain.model.news.FeedItem
import com.eyther.lumbridge.domain.model.news.RssFeed
import com.eyther.lumbridge.domain.repository.news.NewsFeedRepository
import com.eyther.lumbridge.shared.di.model.Schedulers
import com.prof18.rssparser.RssParserBuilder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NewsFeedRepositoryImpl @Inject constructor(
    private val newsFeedRemoteDataSource: NewsFeedRemoteDataSource,
    private val rssFeedLocalDataSource: RssFeedLocalDataSource,
    private val schedulers: Schedulers
) : NewsFeedRepository {

    override fun getAvailableFeedsFlow(): Flow<List<RssFeed>> {
        return rssFeedLocalDataSource
            .rssFeedFlow
            .map { it.toDomain() }
    }

    override suspend fun saveRssFeed(rssFeed: RssFeed) = withContext(schedulers.io) {
        val sanitisedFeedToAdd = rssFeed
            .toCached()
            .copy(name = rssFeed.name.replace("\\s".toRegex(), ""))

        rssFeedLocalDataSource.saveRssFeed(sanitisedFeedToAdd)
    }

    override suspend fun removeRssFeed(rssFeedId: Long) = withContext(schedulers.io) {
        rssFeedLocalDataSource.deleteRssFeed(rssFeedId)
    }

    override suspend fun getNewsFeed(rssFeed: RssFeed): Feed = withContext(schedulers.io) {
        val result = newsFeedRemoteDataSource.getRssFeed(rssFeed.url).orEmpty()

        val parsedRss = RssParserBuilder(charset = Charsets.UTF_8)
            .build()
            .parse(result)

        val feedItems = parsedRss.items.map {
            FeedItem(
                title = it.title.orEmpty().sanitise(),
                description = it.description.orEmpty().sanitise(),
                link = it.link.orEmpty().sanitise(),
                image = it.image.orEmpty().sanitise(),
                pubDate = it.pubDate.orEmpty().sanitise()
            )
        }

        return@withContext Feed(feedItems)
    }

    private fun String.sanitise(): String {
        return Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY).toString().trim()
    }
}
