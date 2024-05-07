package com.eyther.lumbridge.domain.repository.news

import com.eyther.lumbridge.data.datasource.news.remote.NewsFeedRemoteDataSource
import com.eyther.lumbridge.domain.model.news.Feed
import com.eyther.lumbridge.domain.model.news.FeedItem
import com.eyther.lumbridge.domain.model.news.RssFeed
import com.prof18.rssparser.RssParserBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NewsFeedRepository @Inject constructor(
    private val newsFeedRemoteDataSource: NewsFeedRemoteDataSource
) {
    fun getAvailableFeeds() = listOf(
        RssFeed.Euronews,
        RssFeed.PortugalNews,
        RssFeed.EconomyOECD,
        RssFeed.FinanceOECD
    )

    suspend fun getNewsFeed(rssFeed: RssFeed): Feed = withContext(Dispatchers.IO) {
        val result = when(rssFeed) {
            RssFeed.EconomyOECD -> newsFeedRemoteDataSource.getEconomyOECDRssFeed()
            RssFeed.FinanceOECD -> newsFeedRemoteDataSource.getFinanceOECDRssFeed()
            RssFeed.Euronews -> newsFeedRemoteDataSource.getEuronewsRssFeed()
            RssFeed.PortugalNews -> newsFeedRemoteDataSource.getPortugalNewsRssFeed()
        }

        val parsedRss = RssParserBuilder(charset = Charsets.UTF_8)
            .build()
            .parse(result.orEmpty())

        val feedItems = parsedRss.items.map {
            FeedItem(
                title = it.title.orEmpty(),
                description = it.description.orEmpty(),
                link = it.link.orEmpty(),
                image = it.image.orEmpty(),
                pubDate = it.pubDate.orEmpty()
            )
        }

        return@withContext Feed(feedItems)
    }
}
