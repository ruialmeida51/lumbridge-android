package com.eyther.lumbridge.mapper.news

import com.eyther.lumbridge.domain.model.news.RssFeed
import com.eyther.lumbridge.model.news.RssFeedUi

fun RssFeedUi.toDomain(): RssFeed {
    return when (label) {
        "Economy OECD" -> RssFeed.EconomyOECD
        "Finance OECD" -> RssFeed.FinanceOECD
        "Euronews" -> RssFeed.Euronews
        "The Portugal News" -> RssFeed.PortugalNews
        else -> throw IllegalArgumentException("Unknown RssFeed name: $label")
    }
}
