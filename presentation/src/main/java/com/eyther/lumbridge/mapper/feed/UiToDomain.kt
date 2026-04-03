package com.eyther.lumbridge.mapper.feed

import com.eyther.lumbridge.domain.model.news.RssFeed
import com.eyther.lumbridge.model.news.RssFeedUi

fun RssFeedUi.toDomain() = RssFeed(
    id = id,
    name = label,
    url = url
)
