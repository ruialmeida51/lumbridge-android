package com.eyther.lumbridge.data.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.eyther.lumbridge.data.model.recurringpayments.entity.RECURRING_PAYMENTS_TABLE_NAME
import javax.inject.Inject

/**
 * Migration from version 7 to version 8. This migration was needed because we stored the [com.eyther.lumbridge.shared.time.model.Periodicity]
 * object before, and proguard obfuscated all the fields, so we couldn't read the data anymore. We decided to store the data in a less practical
 * way for the developer but more practical for the database. We do not do any data recovery here because the time that the feature was live
 * was very short, and we can afford to lose the data. Don't think this is a good practice, but in this case, it was the best solution.
 */
class V7ToV8Migration @Inject constructor() : Migration(7, 8) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Delete existing recurring payments table, if it exists

        db.execSQL(
            """
            DROP TABLE IF EXISTS $RECURRING_PAYMENTS_TABLE_NAME
            """
        )

        // Create the new recurring payments table, if it doesn't exist, with recurringPaymentId as the primary key
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS $RECURRING_PAYMENTS_TABLE_NAME (
                recurringPaymentId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                startDate TEXT NOT NULL,
                lastPaymentDate TEXT,
                label TEXT NOT NULL,
                amountToPay REAL NOT NULL,
                categoryTypeOrdinal INTEGER NOT NULL,
                shouldNotifyWhenPaid INTEGER NOT NULL,
                periodicityTypeOrdinal INTEGER,
                numOfDays INTEGER,
                numOfWeeks INTEGER,
                numOfMonths INTEGER,
                numOfYears INTEGER,
                dayOfWeekOrdinal INTEGER,
                dayOfMonth INTEGER,
                monthOfYearOrdinal INTEGER
            )
            """
        )
    }
}
