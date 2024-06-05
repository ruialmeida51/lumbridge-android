package com.eyther.lumbridge.data.datasource.news.service

import retrofit2.Response
import retrofit2.http.GET

interface PublicoRssClient {
    @GET("PublicoRSS")
    suspend fun getRssFeed(): Response<String>
}
