package com.eyther.lumbridge.domain.usecase.news

import com.eyther.lumbridge.domain.repository.news.NewsFeedRepository
import com.eyther.lumbridge.domain.mapper.feed.toDomain
import com.eyther.lumbridge.domain.model.news.RssFeedUi
import javax.inject.Inject

class SaveRssFeedUseCase @Inject constructor(
    private val newsFeedRepository: NewsFeedRepository
) {
    suspend operator fun invoke(rssFeedUi: RssFeedUi) {
        newsFeedRepository.saveRssFeed(rssFeedUi.toDomain())
    }
}
