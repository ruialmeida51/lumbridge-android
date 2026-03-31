package com.eyther.lumbridge.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.eyther.lumbridge.data.repository.currencyexchange.CurrencyExchangeRepositoryImpl
import com.eyther.lumbridge.domain.repository.currencyexchange.CurrencyExchangeRepository
import com.eyther.lumbridge.data.repository.expenses.ExpensesRepositoryImpl
import com.eyther.lumbridge.domain.repository.expenses.ExpensesRepository
import com.eyther.lumbridge.data.repository.loan.LoanRepositoryImpl
import com.eyther.lumbridge.domain.repository.loan.LoanRepository
import com.eyther.lumbridge.data.repository.netsalary.NetSalaryRepositoryImpl
import com.eyther.lumbridge.domain.repository.netsalary.NetSalaryRepository
import com.eyther.lumbridge.data.repository.news.NewsFeedRepositoryImpl
import com.eyther.lumbridge.domain.repository.news.NewsFeedRepository
import com.eyther.lumbridge.data.repository.notes.NotesRepositoryImpl
import com.eyther.lumbridge.domain.repository.notes.NotesRepository
import com.eyther.lumbridge.data.repository.preferences.PreferencesRepositoryImpl
import com.eyther.lumbridge.domain.repository.preferences.PreferencesRepository
import com.eyther.lumbridge.data.repository.recurringpayments.RecurringPaymentsRepositoryImpl
import com.eyther.lumbridge.domain.repository.recurringpayments.RecurringPaymentsRepository
import com.eyther.lumbridge.data.repository.reminders.RemindersRepositoryImpl
import com.eyther.lumbridge.domain.repository.reminders.RemindersRepository
import com.eyther.lumbridge.data.repository.shopping.ShoppingRepositoryImpl
import com.eyther.lumbridge.domain.repository.shopping.ShoppingRepository
import com.eyther.lumbridge.data.repository.snapshotsalary.SnapshotSalaryRepositoryImpl
import com.eyther.lumbridge.domain.repository.snapshotsalary.SnapshotSalaryRepository
import com.eyther.lumbridge.data.repository.user.UserRepositoryImpl
import com.eyther.lumbridge.domain.repository.user.UserRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindCurrencyExchangeRepository(
        currencyExchangeRepositoryImpl: CurrencyExchangeRepositoryImpl
    ): CurrencyExchangeRepository

    @Binds
    abstract fun bindExpensesRepository(
        expensesRepositoryImpl: ExpensesRepositoryImpl
    ): ExpensesRepository

    @Binds
    abstract fun bindLoanRepository(
        loanRepositoryImpl: LoanRepositoryImpl
    ): LoanRepository

    @Binds
    abstract fun bindNetSalaryRepository(
        netSalaryRepositoryImpl: NetSalaryRepositoryImpl
    ): NetSalaryRepository

    @Binds
    abstract fun bindNewsFeedRepository(
        newsFeedRepositoryImpl: NewsFeedRepositoryImpl
    ): NewsFeedRepository

    @Binds
    abstract fun bindNotesRepository(
        notesRepositoryImpl: NotesRepositoryImpl
    ): NotesRepository

    @Binds
    abstract fun bindPreferencesRepository(
        preferencesRepositoryImpl: PreferencesRepositoryImpl
    ): PreferencesRepository

    @Binds
    abstract fun bindRecurringPaymentsRepository(
        recurringPaymentsRepositoryImpl: RecurringPaymentsRepositoryImpl
    ): RecurringPaymentsRepository

    @Binds
    abstract fun bindRemindersRepository(
        remindersRepositoryImpl: RemindersRepositoryImpl
    ): RemindersRepository

    @Binds
    abstract fun bindShoppingRepository(
        shoppingRepositoryImpl: ShoppingRepositoryImpl
    ): ShoppingRepository

    @Binds
    abstract fun bindSnapshotSalaryRepository(
        snapshotSalaryRepositoryImpl: SnapshotSalaryRepositoryImpl
    ): SnapshotSalaryRepository

    @Binds
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository
}
