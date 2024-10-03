package com.eyther.lumbridge.data.model.news.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

const val RSS_FEED_TABLE_NAME = "rss_feed"

@Entity(
    tableName = RSS_FEED_TABLE_NAME
)
data class RssFeedEntity(
    @PrimaryKey(autoGenerate = true) val rssId: Long = 0,
    val name: String,
    val url: String
)
