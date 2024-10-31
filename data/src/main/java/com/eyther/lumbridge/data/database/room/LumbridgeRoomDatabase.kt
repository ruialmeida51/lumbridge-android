package com.eyther.lumbridge.data.database.room

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.eyther.lumbridge.data.database.converters.RoomConverters
import com.eyther.lumbridge.data.database.migration.V3ToV4Migration
import com.eyther.lumbridge.data.datasource.expenses.dao.ExpensesDao
import com.eyther.lumbridge.data.datasource.loan.dao.LoanDao
import com.eyther.lumbridge.data.datasource.news.dao.RssFeedDao
import com.eyther.lumbridge.data.datasource.notes.dao.NotesDao
import com.eyther.lumbridge.data.datasource.recurringpayments.dao.RecurringPaymentsDao
import com.eyther.lumbridge.data.datasource.shopping.dao.ShoppingDao
import com.eyther.lumbridge.data.datasource.snapshotsalary.dao.SnapshotSalaryDao
import com.eyther.lumbridge.data.model.expenses.entity.ExpenseEntity
import com.eyther.lumbridge.data.model.loan.entity.LoanEntity
import com.eyther.lumbridge.data.model.news.entity.RssFeedEntity
import com.eyther.lumbridge.data.model.notes.entity.NoteEntity
import com.eyther.lumbridge.data.model.recurringpayments.entity.RecurringPaymentEntity
import com.eyther.lumbridge.data.model.shopping.entity.ShoppingListEntity
import com.eyther.lumbridge.data.model.snapshotsalary.entity.SnapshotNetSalaryEntity

@Database(
    entities = [
        ExpenseEntity::class,
        RssFeedEntity::class,
        ShoppingListEntity::class,
        NoteEntity::class,
        LoanEntity::class,
        SnapshotNetSalaryEntity::class,
        RecurringPaymentEntity::class
    ],
    version = 7,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4, spec = V3ToV4Migration::class),
        AutoMigration(from = 4, to = 5),
        AutoMigration(from = 6, to = 7)
    ]
)
@TypeConverters(RoomConverters::class)
abstract class LumbridgeRoomDatabase : RoomDatabase() {
    abstract fun expensesDao(): ExpensesDao
    abstract fun rssFeedDao(): RssFeedDao
    abstract fun shoppingDao(): ShoppingDao
    abstract fun notesDao(): NotesDao
    abstract fun loanDao(): LoanDao
    abstract fun snapshotSalaryDao(): SnapshotSalaryDao
    abstract fun recurringPaymentsDao(): RecurringPaymentsDao
}
