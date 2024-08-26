package com.eyther.lumbridge.data.database.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.eyther.lumbridge.data.datasource.expenses.dao.ExpensesDao
import com.eyther.lumbridge.data.model.expenses.entity.ExpensesCategoryEntity
import com.eyther.lumbridge.data.model.expenses.entity.ExpensesDetailedEntity
import com.eyther.lumbridge.data.model.expenses.entity.ExpensesMonthEntity

@Database(
    entities = [
        ExpensesMonthEntity::class,
        ExpensesCategoryEntity::class,
        ExpensesDetailedEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class LumbridgeRoomDatabase : RoomDatabase(){
    abstract fun expensesDao(): ExpensesDao
}
