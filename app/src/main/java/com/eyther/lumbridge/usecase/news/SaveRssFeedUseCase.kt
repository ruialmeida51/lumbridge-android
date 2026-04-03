package com.eyther.lumbridge.usecase.news

import com.eyther.lumbridge.domain.model.news.RssFeed
import com.eyther.lumbridge.domain.repository.news.NewsFeedRepository
import javax.inject.Inject

class SaveRssFeedUseCase @Inject constructor(
    private val newsFeedRepository: NewsFeedRepository
) {
    suspend operator fun invoke(rssFeed: RssFeed) {
        newsFeedRepository.saveRssFeed(rssFeed)
    }
}
