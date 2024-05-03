package com.eyther.lumbridge.mapper.news

import com.eyther.lumbridge.domain.model.news.Feed
import com.eyther.lumbridge.domain.model.news.FeedItem
import com.eyther.lumbridge.model.news.FeedItemUi

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
