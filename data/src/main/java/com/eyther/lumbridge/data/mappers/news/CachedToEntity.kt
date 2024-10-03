package com.eyther.lumbridge.data.mappers.news

import com.eyther.lumbridge.data.model.news.entity.RssFeedEntity
import com.eyther.lumbridge.data.model.news.local.RssFeedCached

fun RssFeedCached.toEntity() = RssFeedEntity(
    name = name,
    url = url
)
