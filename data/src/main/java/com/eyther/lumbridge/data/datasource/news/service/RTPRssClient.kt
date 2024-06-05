package com.eyther.lumbridge.data.datasource.news.service

import retrofit2.Response
import retrofit2.http.GET

interface RTPRssClient {
    @GET("rss")
    suspend fun getRssFeed(): Response<String>
}
