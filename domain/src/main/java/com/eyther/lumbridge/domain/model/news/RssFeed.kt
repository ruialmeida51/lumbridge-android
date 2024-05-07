package com.eyther.lumbridge.domain.model.news

sealed interface RssFeed {
    data object Euronews : RssFeed
    data object PortugalNews : RssFeed
    data object FinanceOECD : RssFeed
    data object EconomyOECD : RssFeed
}
