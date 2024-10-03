package com.eyther.lumbridge.usecase.news

import com.eyther.lumbridge.domain.repository.news.NewsFeedRepository
import javax.inject.Inject

class DeleteRssFeedUseCase @Inject constructor(
    private val newsFeedRepository: NewsFeedRepository
) {
    suspend operator fun invoke(rssFeedId: Long) {
        newsFeedRepository.removeRssFeed(rssFeedId)
    }
}
