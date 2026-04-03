package com.eyther.lumbridge.usecase.news

import com.eyther.lumbridge.domain.model.news.Feed
import com.eyther.lumbridge.domain.model.news.RssFeed
import com.eyther.lumbridge.domain.repository.news.NewsFeedRepository
import javax.inject.Inject

class GetNewsFeedUseCase @Inject constructor(
    private val newsFeedRepository: NewsFeedRepository
) {
    /**
     * Attempts to fetch the RSS feed.
     */
    suspend operator fun invoke(rssFeed: RssFeed): Feed {
        return newsFeedRepository.getNewsFeed(rssFeed)
    }
}
