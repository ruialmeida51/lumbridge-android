package com.eyther.lumbridge.data.database.room

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.eyther.lumbridge.data.database.converters.RoomConverters
import com.eyther.lumbridge.data.datasource.expenses.dao.ExpensesDao
import com.eyther.lumbridge.data.datasource.groceries.dao.GroceriesDao
import com.eyther.lumbridge.data.datasource.news.dao.RssFeedDao
import com.eyther.lumbridge.data.model.expenses.entity.ExpensesCategoryEntity
import com.eyther.lumbridge.data.model.expenses.entity.ExpensesDetailedEntity
import com.eyther.lumbridge.data.model.expenses.entity.ExpensesMonthEntity
import com.eyther.lumbridge.data.model.groceries.entity.GroceriesListEntity
import com.eyther.lumbridge.data.model.news.entity.RssFeedEntity

@Database(
    entities = [
        ExpensesMonthEntity::class,
        ExpensesCategoryEntity::class,
        ExpensesDetailedEntity::class,
        RssFeedEntity::class,
        GroceriesListEntity::class
    ],
    version = 4,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 2, to = 3)
    ]
)
@TypeConverters(RoomConverters::class)
abstract class LumbridgeRoomDatabase : RoomDatabase() {
    abstract fun expensesDao(): ExpensesDao
    abstract fun rssFeedDao(): RssFeedDao
    abstract fun groceriesDao(): GroceriesDao
}
