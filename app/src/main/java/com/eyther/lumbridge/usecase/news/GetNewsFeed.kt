package com.eyther.lumbridge.usecase.news

import com.eyther.lumbridge.domain.repository.news.NewsFeedRepository
import com.eyther.lumbridge.mapper.news.toDomain
import com.eyther.lumbridge.mapper.news.toUi
import com.eyther.lumbridge.model.news.FeedItemUi
import com.eyther.lumbridge.model.news.RssFeedUi
import javax.inject.Inject

class GetNewsFeed @Inject constructor(
    private val newsFeedRepository: NewsFeedRepository
) {
    /**
     * Attempts to fetch the RSS feed.
     */
    suspend operator fun invoke(rssFeed: RssFeedUi): List<FeedItemUi> {
        return newsFeedRepository.getNewsFeed(rssFeed.toDomain()).toUi()
    }
}
