package com.eyther.lumbridge.usecase.news

import com.eyther.lumbridge.domain.model.news.RssFeed
import com.eyther.lumbridge.domain.repository.news.NewsFeedRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAvailableFeedsFlowUseCase @Inject constructor(
    private val newsFeedRepository: NewsFeedRepository
) {
    operator fun invoke(): Flow<List<RssFeed>> {
        return newsFeedRepository.getAvailableFeedsFlow()
    }
}
