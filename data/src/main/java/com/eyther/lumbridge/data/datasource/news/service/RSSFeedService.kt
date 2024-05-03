package com.eyther.lumbridge.data.datasource.news.service

import retrofit2.Response
import retrofit2.http.GET

interface RSSFeedService {
    @GET("rss?format=mrss&level=vertical&name=my-europe")
    suspend fun getEconomyRSSFeed(): Response<String>
}
