package com.eyther.lumbridge.domain.model.news

/**
 * Represents an RSS feed that the app can consume.
 *
 * @property name The name of the RSS feed.
 * @property url The URL of the RSS feed.
 */
data class RssFeed(
    val name: String,
    val url: String
)
