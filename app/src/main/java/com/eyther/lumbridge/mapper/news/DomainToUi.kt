package com.eyther.lumbridge.mapper.news

import com.eyther.lumbridge.domain.model.news.Feed
import com.eyther.lumbridge.domain.model.news.FeedItem
import com.eyther.lumbridge.domain.model.news.RssFeed
import com.eyther.lumbridge.model.news.FeedItemUi
import com.eyther.lumbridge.model.news.RssFeedUi

fun Feed.toUi(): List<FeedItemUi> {
    return items.map { it.toUi() }
}

fun FeedItem.toUi(): FeedItemUi {
    return FeedItemUi(
        title = title,
        description = description,
        link = link,
        image = image,
        pubDate = pubDate
    )
}

fun RssFeed.toUi(): RssFeedUi {
    return when (this) {
        RssFeed.EconomyOECD -> RssFeedUi("Economy OECD")
        RssFeed.FinanceOECD -> RssFeedUi("Finance OECD")
        RssFeed.Euronews -> RssFeedUi("Euronews")
        RssFeed.PortugalOECD -> RssFeedUi("Portugal OECD")
    }
}
