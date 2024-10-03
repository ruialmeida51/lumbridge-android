package com.eyther.lumbridge.data.mappers.news

import com.eyther.lumbridge.data.model.news.entity.RssFeedEntity
import com.eyther.lumbridge.data.model.news.local.RssFeedCached

fun RssFeedEntity.toCached() = RssFeedCached(
    id = rssId,
    name = name,
    url = url
)
