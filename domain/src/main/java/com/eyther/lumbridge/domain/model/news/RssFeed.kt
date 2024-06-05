package com.eyther.lumbridge.domain.model.news

sealed interface RssFeed {
    data object Euronews : RssFeed
    data object PortugalNews : RssFeed
    data object Publico : RssFeed
    data object RTP : RssFeed
}
