package com.eyther.lumbridge.usecase.news

import com.eyther.lumbridge.domain.repository.news.NewsFeedRepository
import com.eyther.lumbridge.mapper.feed.toDomain
import com.eyther.lumbridge.model.news.RssFeedUi
import javax.inject.Inject

class SaveRssFeedUseCase @Inject constructor(
    private val newsFeedRepository: NewsFeedRepository
) {
    suspend operator fun invoke(rssFeedUi: RssFeedUi) {
        newsFeedRepository.saveRssFeed(rssFeedUi.toDomain())
    }
}
