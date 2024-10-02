package com.eyther.lumbridge.usecase.news

import com.eyther.lumbridge.domain.repository.news.NewsFeedRepository
import com.eyther.lumbridge.mapper.feed.toUi
import com.eyther.lumbridge.model.news.RssFeedUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAvailableFeedsFlowUseCase @Inject constructor(
    private val newsFeedRepository: NewsFeedRepository
) {
    operator fun invoke(): Flow<List<RssFeedUi>> {
        return newsFeedRepository.getAvailableFeedsFlow()
            .map { listOfFeeds ->
                listOfFeeds
                    .toUi()
                    .sortedBy { rssFeedUi ->  rssFeedUi.label }
            }
    }
}
