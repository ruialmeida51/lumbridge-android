package com.eyther.lumbridge.data.datasource.currencyexchange.interceptor

import com.eyther.lumbridge.data.model.http.HeaderTypes
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor to cache the currency exchange data for 1 hour, this is to reduce the number of requests to the same currency
 * to the currency exchange API.
 *
 * We're giving it a max-age of 1 hour, this means that the data will be cached for 1 hour before it's considered stale.
 */
class CurrencyExchangeCacheInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // Get the request from the chain.
        val request = chain.request()
            .newBuilder()
            .header(HeaderTypes.CacheControl.value, "public, max-age=" + 3600) // 1 hour
            .build()

        // Add the modified request to the chain.
        return chain.proceed(request)
    }
}
