package com.eyther.lumbridge.data.datasource.news.local

import com.eyther.lumbridge.data.datasource.news.dao.RssFeedDao
import com.eyther.lumbridge.data.mappers.news.toCached
import com.eyther.lumbridge.data.mappers.news.toEntity
import com.eyther.lumbridge.data.model.news.local.RssFeedCached
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class RssFeedLocalDataSource @Inject constructor(
    private val rssFeedDao: RssFeedDao
) {
    val rssFeedFlow: Flow<List<RssFeedCached>> = rssFeedDao.getAllRssFeeds()
        .mapNotNull { flowItem ->
            flowItem?.map { rssFeedEntity -> rssFeedEntity.toCached() }
        }

    suspend fun saveRssFeed(rssFeed: RssFeedCached) {
        if (rssFeed.id == -1L) {
            rssFeedDao.insertRssFeed(rssFeed.toEntity())
        } else {
            rssFeedDao.updateRssFeed(rssFeed.toEntity().copy(rssId = rssFeed.id))
        }
    }

    suspend fun deleteRssFeed(rssFeedId: Long) {
        rssFeedDao.deleteRssFeedById(rssFeedId)
    }
}
