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
 * @property loanId The id of the loan. Defaults to 0 when creating a new one.
 * @property name The name of the loan, e.g. "Car Loan".
 * @property startDate The date when the loan was taken, when the contract was made.
 * @property currentPaymentDate The date when the current payment is due. This date is hidden from the user and it's just an internal information
 * to calculate the remaining months between the current date and the end date.
 * @property endDate The date when the loan is expected to be paid off.
 * @property currentAmount The current amount of the loan. This amount is updated every time a payment is made.
 * @property initialAmount The initial amount of the loan.
 * @property fixedTaegInterestRate The fixed interest rate of the loan, represented by a percentage.
 * @property variableEuribor The Euribor rate of the loan, represented by a percentage.
 * @property variableSpread The spread rate of the loan, represented by a percentage.
 * @property fixedTanInterestRate The fixed interest rate of the loan, represented by a percentage.
 * @property loanTypeOrdinal The ordinal of the loan type, can be fixed or variable.
 * @property loanCategoryOrdinal The ordinal of the loan category, e.g. "House", "Car", "Personal", "Other".
 * @property shouldNotifyWhenPaid True if the user wants to be notified when the loan is paid off, false otherwise.
 * @property shouldAutoAddToExpenses True if the user wants the loan to be automatically added to the expenses, false otherwise.
 * @property lastAutoPayDate The last date when the loan was automatically paid. This is used to calculate the next payment date.
 * @property paymentDay The day of the month when the payment is due, if the user enabled automatic payments.
 */
@Entity(
    tableName = LOAN_TABLE_NAME
)
data class LoanEntity(
    @PrimaryKey(autoGenerate = true) val loanId: Long = 0,
    val name: String,
    val startDate: String,
    val currentPaymentDate: String,
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
