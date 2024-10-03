package com.eyther.lumbridge.data.datasource.news.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.eyther.lumbridge.data.model.news.entity.RSS_FEED_TABLE_NAME
import com.eyther.lumbridge.data.model.news.entity.RssFeedEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RssFeedDao {
    @Transaction
    @Query("SELECT * FROM $RSS_FEED_TABLE_NAME")
    fun getAllRssFeeds(): Flow<List<RssFeedEntity>?>

    @Transaction
    @Query("SELECT * FROM $RSS_FEED_TABLE_NAME WHERE rssId = :rssId")
    suspend fun getRssFeedById(rssId: Long): RssFeedEntity?

    @Transaction
    @Insert
    suspend fun insertRssFeed(rssFeedEntity: RssFeedEntity)

    @Transaction
    @Update(onConflict = REPLACE)
    suspend fun updateRssFeed(rssFeedEntity: RssFeedEntity)

    @Transaction
    @Query("DELETE FROM $RSS_FEED_TABLE_NAME WHERE rssId = :rssId")
    suspend fun deleteRssFeedById(rssId: Long)
}
