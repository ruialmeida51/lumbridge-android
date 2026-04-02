package com.eyther.lumbridge.data.di

import com.eyther.lumbridge.data.repository.currencyexchange.CurrencyExchangeRepositoryImpl
import com.eyther.lumbridge.data.repository.expenses.ExpensesRepositoryImpl
import com.eyther.lumbridge.data.repository.loan.LoanRepositoryImpl
import com.eyther.lumbridge.data.repository.netsalary.NetSalaryRepositoryImpl
import com.eyther.lumbridge.data.repository.news.NewsFeedRepositoryImpl
import com.eyther.lumbridge.data.repository.notes.NotesRepositoryImpl
import com.eyther.lumbridge.data.repository.preferences.PreferencesRepositoryImpl
import com.eyther.lumbridge.data.repository.recurringpayments.RecurringPaymentsRepositoryImpl
import com.eyther.lumbridge.data.repository.reminders.RemindersRepositoryImpl
import com.eyther.lumbridge.data.repository.shopping.ShoppingRepositoryImpl
import com.eyther.lumbridge.data.repository.snapshotsalary.SnapshotSalaryRepositoryImpl
import com.eyther.lumbridge.data.repository.user.UserRepositoryImpl
import com.eyther.lumbridge.domain.repository.currencyexchange.CurrencyExchangeRepository
import com.eyther.lumbridge.domain.repository.expenses.ExpensesRepository
import com.eyther.lumbridge.domain.repository.loan.LoanRepository
import com.eyther.lumbridge.domain.repository.netsalary.NetSalaryRepository
import com.eyther.lumbridge.domain.repository.news.NewsFeedRepository
import com.eyther.lumbridge.domain.repository.notes.NotesRepository
import com.eyther.lumbridge.domain.repository.preferences.PreferencesRepository
import com.eyther.lumbridge.domain.repository.recurringpayments.RecurringPaymentsRepository
import com.eyther.lumbridge.domain.repository.reminders.RemindersRepository
import com.eyther.lumbridge.domain.repository.shopping.ShoppingRepository
import com.eyther.lumbridge.domain.repository.snapshotsalary.SnapshotSalaryRepository
import com.eyther.lumbridge.domain.repository.user.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindExpensesRepository(impl: ExpensesRepositoryImpl): ExpensesRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindLoanRepository(impl: LoanRepositoryImpl): LoanRepository

    @Binds
    @Singleton
    abstract fun bindNetSalaryRepository(impl: NetSalaryRepositoryImpl): NetSalaryRepository

    @Binds
    @Singleton
    abstract fun bindNewsFeedRepository(impl: NewsFeedRepositoryImpl): NewsFeedRepository

    @Binds
    @Singleton
    abstract fun bindNotesRepository(impl: NotesRepositoryImpl): NotesRepository

    @Binds
    @Singleton
    abstract fun bindPreferencesRepository(impl: PreferencesRepositoryImpl): PreferencesRepository

    @Binds
    @Singleton
    abstract fun bindRecurringPaymentsRepository(impl: RecurringPaymentsRepositoryImpl): RecurringPaymentsRepository

    @Binds
    @Singleton
    abstract fun bindRemindersRepository(impl: RemindersRepositoryImpl): RemindersRepository

    @Binds
    @Singleton
    abstract fun bindShoppingRepository(impl: ShoppingRepositoryImpl): ShoppingRepository

    @Binds
    @Singleton
    abstract fun bindSnapshotSalaryRepository(impl: SnapshotSalaryRepositoryImpl): SnapshotSalaryRepository

    @Binds
    @Singleton
    abstract fun bindCurrencyExchangeRepository(impl: CurrencyExchangeRepositoryImpl): CurrencyExchangeRepository
}
