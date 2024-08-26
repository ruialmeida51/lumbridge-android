package com.eyther.lumbridge.domain.repository.news

import android.text.Html
import com.eyther.lumbridge.data.datasource.news.remote.NewsFeedRemoteDataSource
import com.eyther.lumbridge.domain.model.news.Feed
import com.eyther.lumbridge.domain.model.news.FeedItem
import com.eyther.lumbridge.domain.model.news.RssFeed
import com.eyther.lumbridge.domain.model.news.RssFeed.AndroidDevelopment
import com.eyther.lumbridge.domain.model.news.RssFeed.News
import com.prof18.rssparser.RssParserBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NewsFeedRepository @Inject constructor(
    private val newsFeedRemoteDataSource: NewsFeedRemoteDataSource
) {

    /**
     * The current available feeds. It doesn't necessarily mean we don't have more feeds available,
     * but these are the ones we are currently displaying.
     *
     * @return a list of available feeds
     */
    fun getAvailableFeeds() = listOf(
        News.Euronews,
        News.PortugalNews,
        News.JornalDeNegocios,
        News.RTP,
        News.Publico,
        AndroidDevelopment.Medium,
        AndroidDevelopment.JoeBirch,
        AndroidDevelopment.ProAndroidDev,
        AndroidDevelopment.AndroidCentral
    )

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
