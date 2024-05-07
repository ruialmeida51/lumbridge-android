package com.eyther.lumbridge.usecase.news

import com.eyther.lumbridge.domain.repository.news.NewsFeedRepository
import com.eyther.lumbridge.mapper.news.toUi
import com.eyther.lumbridge.model.news.RssFeedUi
import javax.inject.Inject

class GetAvailableFeeds @Inject constructor(
    private val newsFeedRepository: NewsFeedRepository
) {
    operator fun invoke(): List<RssFeedUi> {
        return newsFeedRepository.getAvailableFeeds().map { it.toUi() }
    }
}
