package com.eyther.lumbridge.data.model.http

sealed class HeaderTypes(val value: String) {
    data object CacheControl : HeaderTypes("Cache-Control")
    data object ContentType : HeaderTypes("Content-Type")
}
