package com.eyther.lumbridge.data.mapper.feed

import com.eyther.lumbridge.data.model.news.local.RssFeedCached
import com.eyther.lumbridge.domain.model.news.RssFeed

fun RssFeed.toCached() = RssFeedCached(
    id = id,
    name = name,
    url = url
)
