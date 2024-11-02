package com.eyther.lumbridge.features.tools.recurringpayments.editrecurringpayments.viewmodel.delegate

import com.eyther.lumbridge.R
import com.eyther.lumbridge.extensions.kotlin.getErrorOrNull
import com.eyther.lumbridge.features.expenses.model.add.ExpensesAddSurplusOrExpenseChoice.Surplus
import com.eyther.lumbridge.features.tools.recurringpayments.editrecurringpayments.model.EditRecurringPaymentScreenInputState
import com.eyther.lumbridge.model.expenses.ExpensesCategoryTypesUi
import com.eyther.lumbridge.model.recurringpayments.PeriodicityUi
import com.eyther.lumbridge.shared.time.extensions.toLocalDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.time.DayOfWeek
import java.time.Month
import javax.inject.Inject

class EditRecurringPaymentInputHandler @Inject constructor() : IEditRecurringPaymentInputHandler {
    override val inputState = MutableStateFlow(EditRecurringPaymentScreenInputState())

    private var cachedCategorySelection: ExpensesCategoryTypesUi? = null

    override fun onNotifyMeWhenPaidChanged(shouldNotifyWhenPaid: Boolean) {
        updateInput { state ->
            state.copy(
                shouldNotifyWhenPaid = shouldNotifyWhenPaid
            )
        }
    }

    override fun onPaymentNamedChanged(paymentName: String?) {
        updateInput { state ->
            val paymentNameText = paymentName ?: ""

            state.copy(
                paymentName = state.paymentName.copy(
                    text = paymentNameText,
                    error = paymentNameText.getErrorOrNull(
                        R.string.recurring_payment_edit_invalid_name
                    )
                )
            )
        }
    }

    override fun onPaymentAmountChanged(paymentAmount: Float?) {
        updateInput { state ->

            // If the expense amount is less than or equal to 0, it's invalid.
            val paymentAmountText = if ((paymentAmount ?: 0f) <= 0f) {
                null
            } else {
                paymentAmount?.toString()
            }

            state.copy(
                paymentAmount = state.paymentAmount.copy(
                    text = paymentAmountText,
                    error = paymentAmountText.getErrorOrNull(
                        R.string.recurring_payment_edit_invalid_amount
                    )
                )
            )
        }
    }

    override fun onPaymentStartDateChanged(paymentStartDate: Long?) {
        updateInput { state ->
            state.copy(
                paymentStartDate = state.paymentStartDate.copy(
                    date = paymentStartDate?.toLocalDate(),
                    error = paymentStartDate?.toString().getErrorOrNull(
                        R.string.edit_loan_profile_invalid_end_date
                    )
                )
            )
        }
    }

    override fun onPaymentTypeChanged(typeOrdinal: Int?) {
        updateInput { state ->
            state.copy(
                categoryType = ExpensesCategoryTypesUi.of(typeOrdinal ?: 0)
            )
        }
    }

    override fun onPaymentSurplusOrExpenseChanged(choiceOrdinal: Int) {
        updateInput { state ->
            state.copy(
                surplusOrExpenseChoice = state.surplusOrExpenseChoice.copy(
                    selectedTab = choiceOrdinal
                ),
                categoryType = if (isSurplus(choiceOrdinal)) {
                    // Cache the category selection if it's a surplus, as we need to restore it when switching back to expenses.
                    cachedCategorySelection = state.categoryType
                    // Force select the surplus category when switching to surplus.
                    ExpensesCategoryTypesUi.Surplus
                } else {
                    // Restore the cached category selection when switching back to expenses.
                    cachedCategorySelection ?: ExpensesCategoryTypesUi.Food
                }
            )
        }
    }

    override fun onPeriodicityTypeChanged(periodicityTypeOrdinal: Int?) {
        updateInput { state ->
            state.copy(
                periodicityUi = PeriodicityUi.defaultFromOrdinal(periodicityTypeOrdinal ?: 0)
            )
        }
    }

    override fun onNumOfDaysChanged(numOfDays: Int?) {
        updateInput { state ->
            val numOfDaysText = if (validateNumOfDays(numOfDays ?: 0)) {
                numOfDays?.toString()
            } else {
                null
            }

            state.copy(
                numOfDays = state.numOfDays.copy(
                    text = numOfDaysText,
                    error = numOfDaysText.getErrorOrNull(
                        R.string.recurring_payment_edit_frequency_invalid_number_of_days
                    )
                )
            )
        }
    }

    override fun onNumOfWeeksChanged(numOfWeeks: Int?) {
        updateInput { state ->
            val numOfWeeksText = if (validateNumOfWeeks(numOfWeeks ?: 0)) {
                numOfWeeks?.toString()
            } else {
                null
            }

            state.copy(
                numOfWeeks = state.numOfWeeks.copy(
                    text = numOfWeeksText,
                    error = numOfWeeksText.getErrorOrNull(
                        R.string.recurring_payment_edit_frequency_invalid_number_of_weeks
                    )
                )
            )
        }
    }

    override fun onNumOfMonthsChanged(numOfMonths: Int?) {
        updateInput { state ->
            val numOfMonthsText = if (validateNumOfMonths(numOfMonths ?: 0)) {
                numOfMonths?.toString()
            } else {
                null
            }

            state.copy(
                numOfMonths = state.numOfMonths.copy(
                    text = numOfMonthsText,
                    error = numOfMonthsText.getErrorOrNull(
                        R.string.recurring_payment_edit_frequency_invalid_number_of_months
                    )
                )
            )
        }
    }

    override fun onNumOfYearsChanged(numOfYears: Int?) {
        updateInput { state ->
            val numOfYearsText = if (validateNumOfYears(numOfYears ?: 0)) {
                numOfYears?.toString()
            } else {
                null
            }

            state.copy(
                numOfYears = state.numOfYears.copy(
                    text = numOfYearsText,
                    error = numOfYearsText.getErrorOrNull(
                        R.string.recurring_payment_edit_frequency_invalid_number_of_years
                    )
                )
            )
        }
    }

    override fun onDayOfWeekChanged(dayOfWeekOrdinal: Int?) {
        updateInput { state ->
            state.copy(
                dayOfWeek = DayOfWeek.of(dayOfWeekOrdinal ?: 1)
            )
        }
    }

    override fun onDayOfMonthChanged(dayOfMonth: Int?) {
        updateInput { state ->
            val dayOfMonthText = if (validateDayOfMonth(dayOfMonth ?: 0)) {
                dayOfMonth?.toString()
            } else {
                null
            }

            state.copy(
                dayOfMonth = state.dayOfMonth.copy(
                    text = dayOfMonthText,
                    error = dayOfMonthText.getErrorOrNull(
                        R.string.recurring_payment_edit_frequency_invalid_day_of_month
                    )
                )
            )
        }
    }

    override fun onMonthOfYearChanged(monthOfYearOrdinal: Int?) {
        updateInput { state ->
            state.copy(
                monthOfYear = Month.of(monthOfYearOrdinal ?: 1)
            )
        }
    }

    override fun validateInput(inputState: EditRecurringPaymentScreenInputState): Boolean {
        return inputState.paymentAmount.isValid() &&
            inputState.paymentName.isValid() &&
            inputState.paymentStartDate.isValid() &&
            validatePeriodicity()
    }

    override fun shouldEnableSaveButton(inputState: EditRecurringPaymentScreenInputState): Boolean {
        return validateInput(inputState)
    }

    override fun updateInput(update: (EditRecurringPaymentScreenInputState) -> EditRecurringPaymentScreenInputState) {
        inputState.update { currentState ->
            update(currentState)
        }
    }

    private fun isSurplus(selectedOrdinal: Int): Boolean {
        return selectedOrdinal == Surplus.ordinal
    }

    private fun validatePeriodicity(): Boolean {
        val periodicity = inputState.value.periodicityUi

        return when (periodicity) {
            is PeriodicityUi.EveryXDays -> validateNumOfDays(periodicity.numOfDays)
            is PeriodicityUi.EveryXWeeks -> validateNumOfWeeks(periodicity.numOfWeeks) && periodicity.dayOfWeek in DayOfWeek.MONDAY..DayOfWeek.SUNDAY
            is PeriodicityUi.EveryXMonths -> validateNumOfMonths(periodicity.numOfMonth) && validateDayOfMonth(periodicity.dayOfMonth)
            is PeriodicityUi.EveryXYears -> validateNumOfYears(periodicity.numOfYear) && periodicity.month in Month.JANUARY..Month.DECEMBER
            else -> false
        }
    }

    private fun validateNumOfDays(numOfDays: Int) = numOfDays > 0
    private fun validateNumOfWeeks(numOfWeeks: Int) = numOfWeeks > 0
    private fun validateNumOfMonths(numOfMonth: Int) = numOfMonth > 0
    private fun validateNumOfYears(numOfYears: Int) = numOfYears > 0
    private fun validateDayOfMonth(dayOfMonth: Int) = dayOfMonth > 0 && dayOfMonth in 1..31
}
