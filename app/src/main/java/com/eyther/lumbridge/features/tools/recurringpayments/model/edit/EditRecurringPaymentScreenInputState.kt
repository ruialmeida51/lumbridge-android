package com.eyther.lumbridge.features.tools.recurringpayments.model.edit

import com.eyther.lumbridge.features.expenses.model.add.ExpensesAddSurplusOrExpenseChoice
import com.eyther.lumbridge.model.expenses.ExpensesCategoryTypesUi
import com.eyther.lumbridge.model.finance.MoneyAllocationTypeUi
import com.eyther.lumbridge.model.time.PeriodicityUi
import com.eyther.lumbridge.ui.common.composables.model.input.ChoiceTabState
import com.eyther.lumbridge.ui.common.composables.model.input.DateInputState
import com.eyther.lumbridge.ui.common.composables.model.input.TextInputState
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month

data class EditRecurringPaymentScreenInputState(
    val surplusOrExpenseChoice: ChoiceTabState = ChoiceTabState(
        tabsStringRes = ExpensesAddSurplusOrExpenseChoice.entries().map { it.label }
    ),
    val paymentName: TextInputState = TextInputState(),
    val paymentAmount: TextInputState = TextInputState(),
    val categoryType: ExpensesCategoryTypesUi = ExpensesCategoryTypesUi.Food,
    val allocationTypeUi: MoneyAllocationTypeUi = MoneyAllocationTypeUi.Necessities(),
    val paymentStartDate: DateInputState = DateInputState(date = LocalDate.now()),
    val periodicityUi: PeriodicityUi = PeriodicityUi.EveryXDays(LocalDate.now(), 1),
    val numOfDays: TextInputState = TextInputState(),
    val numOfWeeks: TextInputState = TextInputState(),
    val numOfMonths: TextInputState = TextInputState(),
    val numOfYears: TextInputState = TextInputState(),
    val dayOfWeek: DayOfWeek = DayOfWeek.MONDAY,
    val dayOfMonth: TextInputState = TextInputState(),
    val monthOfYear: Month = Month.JANUARY,
    val shouldNotifyWhenPaid: Boolean = false
) {
    val isSurplusSelected: Boolean
        get() = surplusOrExpenseChoice.selectedTab == ExpensesAddSurplusOrExpenseChoice.Surplus.ordinal
}
