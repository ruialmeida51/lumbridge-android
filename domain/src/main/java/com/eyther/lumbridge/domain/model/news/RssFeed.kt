package com.eyther.lumbridge.domain.model.news

/**
 * Represents an RSS feed. It can have multiple subtypes, each representing a different kind of feed.
 * @property url The URL of the RSS feed.
 */
sealed class RssFeed(open val name: String, open val url: String) {

    /**
     * Represents a news RSS feed. These are feeds related to news, updates, and articles.
     * @property url The URL of the RSS feed.
     */
    sealed class News(
        override val name: String,
        override val url: String
    ) : RssFeed(name, url) {
        data object Euronews : News("EuroNews", "https://www.euronews.com/rss")
        data object PortugalNews : News("Portugal News", "https://www.theportugalnews.com/rss")
        data object JornalDeNegocios : News("JN", "https://www.jornaldenegocios.pt/rss")
        data object RTP : News("RTP", "https://www.rtp.pt/noticias/rss")
        data object Publico : News("Publico", "https://feeds.feedburner.com/PublicoRSS")
    }

    /**
     * Represents an Android development RSS feed. These are feeds related to Android development and the
     * android world in general. It also has the personal blogs of a few Android developers.
     *
     * @property url The URL of the RSS feed.
     */
    sealed class AndroidDevelopment(
        override val name: String,
        override val url: String
    ) : RssFeed(name, url) {
        data object Medium : AndroidDevelopment("Medium AD", "https://medium.com/feed/androiddevelopers")
        data object JoeBirch : AndroidDevelopment("Joe B.", "https://joebirch.co/feed/")
        data object ProAndroidDev : AndroidDevelopment("ProAndroidDev", "https://proandroiddev.com/feed")
        data object AndroidCentral : AndroidDevelopment("AndroidCentral", "https://www.androidcentral.com/feed")
    }
}
