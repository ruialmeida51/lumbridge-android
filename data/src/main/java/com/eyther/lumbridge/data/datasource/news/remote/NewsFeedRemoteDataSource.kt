package com.eyther.lumbridge.data.datasource.news.remote

import android.webkit.URLUtil
import com.eyther.lumbridge.data.extensions.okhttp.await
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject

class NewsFeedRemoteDataSource @Inject constructor() {
    /**
     * We don't use retrofit here because the URL is different for each RSS feed.
     * Thus, we need a way to build the URL dynamically. We use OkHttp for this.
     */
    private val okHttpClient = OkHttpClient.Builder()
        .build()

    /**
     * Request builder for the RSS feed. It is a GET request with the content type set to application/xml.
     *
     * @see Request.Builder
     */
    private val requestBuilder = Request.Builder()
        .addHeader("Content-Type", "application/xml")
        .method("GET", null)

    suspend fun getRssFeed(url: String): String? {
        if (!URLUtil.isHttpUrl(url) && !URLUtil.isHttpsUrl(url)) {
            return null
        }

        val request = requestBuilder.url(url).build()
        val response = okHttpClient.newCall(request).await()

        return if (response.isSuccessful) {
            response.body()?.string()
        } else {
            null
        }
    }
}
