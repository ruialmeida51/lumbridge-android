package com.eyther.lumbridge.usecase.news

import com.eyther.lumbridge.domain.repository.news.NewsFeedRepository
import com.eyther.lumbridge.mapper.news.toUi
import com.eyther.lumbridge.model.news.FeedItemUi
import javax.inject.Inject

class GetNewsFeed @Inject constructor(
    private val newsFeedRepository: NewsFeedRepository
) {
    /**
     * Attempts to fetch the RSS feed.
     */
    suspend operator fun invoke(): List<FeedItemUi> {
        return newsFeedRepository.getNewsFeed().toUi()
    }
}
