package com.eyther.lumbridge.data.model.loan.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val LOAN_TABLE_NAME = "loan"

/**
 * Represents a loan. From this data, all loan calculations can be created:
 *
 * Given an interest rate and a loan type, we can use the proper formula to calculate the
 * monthly payment, the total amount paid, and the total interest paid and the months left.
 *
 * @property loanId The unique identifier for the loan.
 * @property name The name of the loan.
 * @property startDate The date the loan was taken out.
 * @property endDate The date the loan will be paid off.
 * @property currentAmount The amount of the loan.
 * @property fixedTaegInterestRate The fixed interest rate for the loan when using the TAEG formula.
 * @property variableEuribor The variable interest rate for the loan when using the EURIBOR formula.
 * @property variableSpread The variable spread for the loan when using the EURIBOR formula.
 * @property fixedTanInterestRate The fixed interest rate for the loan when using the TAN formula.
 * @property loanTypeOrdinal The ordinal of the loan type.
 * @property loanCategoryOrdinal The ordinal of the loan category.
 * @property shouldNotifyWhenPaid If true, the user should be notified when the loan is paid off.
 * @property shouldAutoAddToExpenses If true, the loan should be automatically added to the user's expenses.
 */
@Entity(
    tableName = LOAN_TABLE_NAME
)
data class LoanEntity(
    @PrimaryKey(autoGenerate = true) val loanId: Long = 0,
    val name: String,
    val startDate: String,
    val endDate: String,
    val currentAmount: Float,
    val initialAmount: Float,
    val fixedTaegInterestRate: Float?,
    val variableEuribor: Float?,
    val variableSpread: Float?,
    val fixedTanInterestRate: Float?,
    val loanTypeOrdinal: Int,
    val loanCategoryOrdinal: Int,
    @ColumnInfo(defaultValue = "0") val shouldNotifyWhenPaid: Boolean = false,
    @ColumnInfo(defaultValue = "0") val shouldAutoAddToExpenses: Boolean = false,
    val lastAutoPayDate: String? = null,
    val paymentDay: Int? = null
)
