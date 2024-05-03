package com.eyther.lumbridge.domain.repository.news

import com.eyther.lumbridge.data.datasource.news.remote.NewsFeedRemoteDataSource
import com.eyther.lumbridge.domain.model.news.Feed
import com.eyther.lumbridge.domain.model.news.FeedItem
import com.prof18.rssparser.RssParserBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import javax.inject.Inject

class NewsFeedRepository @Inject constructor(
    private val newsFeedRemoteDataSource: NewsFeedRemoteDataSource
) {
    suspend fun getNewsFeed(): Feed = withContext(Dispatchers.IO) {
        val result = newsFeedRemoteDataSource.getRSSFeed()

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
