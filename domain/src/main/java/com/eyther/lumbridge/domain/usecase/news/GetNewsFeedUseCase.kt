package com.eyther.lumbridge.domain.usecase.news

import com.eyther.lumbridge.domain.repository.news.NewsFeedRepository
import com.eyther.lumbridge.domain.mapper.feed.toDomain
import com.eyther.lumbridge.domain.mapper.feed.toUi
import com.eyther.lumbridge.domain.model.news.FeedItemUi
import com.eyther.lumbridge.domain.model.news.RssFeedUi
import javax.inject.Inject

class GetNewsFeedUseCase @Inject constructor(
    private val newsFeedRepository: NewsFeedRepository
) {
    /**
     * Attempts to fetch the RSS feed.
     */
    suspend operator fun invoke(rssFeed: RssFeedUi): List<FeedItemUi> {
        return newsFeedRepository.getNewsFeed(rssFeed.toDomain()).toUi()
    }
}
