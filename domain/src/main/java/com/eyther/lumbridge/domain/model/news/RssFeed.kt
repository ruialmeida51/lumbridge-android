package com.eyther.lumbridge.domain.model.news

sealed interface RssFeed {
    data object Euronews : RssFeed
    data object PortugalOECD : RssFeed
    data object FinanceOECD : RssFeed
    data object EconomyOECD : RssFeed
}
