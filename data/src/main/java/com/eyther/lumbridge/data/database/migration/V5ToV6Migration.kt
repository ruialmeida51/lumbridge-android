package com.eyther.lumbridge.data.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.eyther.lumbridge.data.model.expenses.entity.EXPENSES_TABLE_NAME
import com.eyther.lumbridge.data.model.snapshotsalary.entity.SNAPSHOT_NET_SALARY_TABLE_NAME
import javax.inject.Inject

class V5ToV6Migration @Inject constructor() : Migration(5, 6) {
    companion object {
        const val EXPENSES_DETAILED_TABLE_NAME = "expenses_detailed"
        const val EXPENSES_CATEGORY_TABLE_NAME = "expenses_category"
        const val EXPENSES_MONTH_TABLE_NAME = "expenses_month"
    }

    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
        CREATE TABLE IF NOT EXISTS $SNAPSHOT_NET_SALARY_TABLE_NAME (
            snapshotId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
            amount REAL NOT NULL,
            year INTEGER NOT NULL,
            month INTEGER NOT NULL
        )
        """
        )

        db.execSQL(
            """
        CREATE TABLE IF NOT EXISTS $EXPENSES_TABLE_NAME (
            expenseId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
            categoryTypeOrdinal INTEGER NOT NULL,
            date TEXT NOT NULL,
            amount REAL NOT NULL,
            name TEXT NOT NULL
        )
        """
        )

        // 2. Migrate data from the old tables into the new structure
        // Query to join and combine data
        db.execSQL(
            """
        INSERT INTO $EXPENSES_TABLE_NAME (categoryTypeOrdinal, date, amount, name)
        SELECT
            category.categoryTypeOrdinal,
            printf('%04d-%02d-%02d', month.year, month.month, month.day) AS date,
            details.expenseAmount,
            details.expenseName
        FROM $EXPENSES_MONTH_TABLE_NAME month
        JOIN $EXPENSES_CATEGORY_TABLE_NAME category ON category.parentMonthId = month.monthId
        JOIN $EXPENSES_DETAILED_TABLE_NAME details ON details.parentCategoryId = category.categoryId
        """
        )

        // 3. Create the index on expenseId
        db.execSQL(
            """
        CREATE INDEX IF NOT EXISTS index_expenses_expenseId ON $EXPENSES_TABLE_NAME (expenseId)
        """
        )

        // Drop old tables
        db.execSQL("DROP TABLE IF EXISTS $EXPENSES_MONTH_TABLE_NAME ")
        db.execSQL("DROP TABLE IF EXISTS $EXPENSES_CATEGORY_TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $EXPENSES_DETAILED_TABLE_NAME")

    }
}
