package com.eyther.lumbridge.data.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.eyther.lumbridge.data.model.loan.entity.LOAN_TABLE_NAME
import com.eyther.lumbridge.data.model.loan.entity.LoanEntity
import javax.inject.Inject

/**
 * With the introduction of Version 9, we added a column called 'currentPaymentDate' to the loans table. For this migration, we just need to
 * copy the value from the 'startDate' column to the 'currentPaymentDate' column.
 *
 * Quick and easy.
 *
 * @see LoanEntity
 */
class V8ToV9Migration @Inject constructor() : Migration(8, 9) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            ALTER TABLE $LOAN_TABLE_NAME ADD COLUMN currentPaymentDate TEXT NOT NULL DEFAULT ''
            """
        )

        db.execSQL(
            """
            UPDATE $LOAN_TABLE_NAME SET currentPaymentDate = startDate
            """
        )
    }
}
