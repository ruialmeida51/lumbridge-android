package com.eyther.lumbridge.usecase.news

import com.eyther.lumbridge.domain.repository.news.NewsFeedRepository
import com.eyther.lumbridge.mapper.feed.toDomain
import com.eyther.lumbridge.mapper.feed.toUi
import com.eyther.lumbridge.model.news.FeedItemUi
import com.eyther.lumbridge.model.news.RssFeedUi
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
