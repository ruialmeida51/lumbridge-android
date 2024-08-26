package com.eyther.lumbridge.mapper.news

import com.eyther.lumbridge.domain.model.news.RssFeed
import com.eyther.lumbridge.model.news.RssFeedUi

fun RssFeedUi.toDomain(): RssFeed {
    return when (label) {
        RssFeed.News.Euronews.name -> RssFeed.News.Euronews
        RssFeed.News.PortugalNews.name -> RssFeed.News.PortugalNews
        RssFeed.News.JornalDeNegocios.name -> RssFeed.News.JornalDeNegocios
        RssFeed.News.RTP.name -> RssFeed.News.RTP
        RssFeed.News.Publico.name -> RssFeed.News.Publico
        RssFeed.AndroidDevelopment.Medium.name -> RssFeed.AndroidDevelopment.Medium
        RssFeed.AndroidDevelopment.JoeBirch.name -> RssFeed.AndroidDevelopment.JoeBirch
        RssFeed.AndroidDevelopment.ProAndroidDev.name -> RssFeed.AndroidDevelopment.ProAndroidDev
        RssFeed.AndroidDevelopment.AndroidCentral.name -> RssFeed.AndroidDevelopment.AndroidCentral
        else -> throw IllegalArgumentException("Unknown RssFeed name: $label")
    }
}
