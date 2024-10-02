package com.eyther.lumbridge.usecase.news

import com.eyther.lumbridge.domain.repository.news.NewsFeedRepository
import com.eyther.lumbridge.mapper.feed.toUi
import javax.inject.Inject

class GetAvailableFeedsUseCase @Inject constructor(
    private val newsFeedRepository: NewsFeedRepository
) {
    suspend operator fun invoke() = newsFeedRepository.getAvailableFeeds()
        .map { it.toUi() }
        .sortedBy { it.label }
}
