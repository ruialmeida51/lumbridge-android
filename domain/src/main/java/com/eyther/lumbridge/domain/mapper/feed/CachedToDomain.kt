package com.eyther.lumbridge.domain.mapper.feed

import com.eyther.lumbridge.data.model.news.local.RssFeedCached
import com.eyther.lumbridge.domain.model.news.RssFeed

fun RssFeedCached.toDomain() = RssFeed(
    id = id,
    name = name,
    url = url
)

fun List<RssFeedCached>.toDomain() = map { it.toDomain() }
