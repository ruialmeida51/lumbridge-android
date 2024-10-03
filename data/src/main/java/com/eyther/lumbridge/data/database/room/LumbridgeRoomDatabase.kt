package com.eyther.lumbridge.data.database.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.eyther.lumbridge.data.datasource.expenses.dao.ExpensesDao
import com.eyther.lumbridge.data.datasource.news.dao.RssFeedDao
import com.eyther.lumbridge.data.model.expenses.entity.ExpensesCategoryEntity
import com.eyther.lumbridge.data.model.expenses.entity.ExpensesDetailedEntity
import com.eyther.lumbridge.data.model.expenses.entity.ExpensesMonthEntity
import com.eyther.lumbridge.data.model.news.entity.RssFeedEntity

@Database(
    entities = [
        ExpensesMonthEntity::class,
        ExpensesCategoryEntity::class,
        ExpensesDetailedEntity::class,
        RssFeedEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class LumbridgeRoomDatabase : RoomDatabase() {
    abstract fun expensesDao(): ExpensesDao
    abstract fun rssFeedDao(): RssFeedDao
}
