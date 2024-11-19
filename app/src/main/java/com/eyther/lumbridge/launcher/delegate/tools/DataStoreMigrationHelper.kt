package com.eyther.lumbridge.launcher.delegate.tools

import android.content.Context
import android.util.Log
import com.eyther.lumbridge.R
import com.eyther.lumbridge.domain.model.loan.LoanCategory
import com.eyther.lumbridge.domain.model.loan.LoanDomain
import com.eyther.lumbridge.domain.model.loan.LoanInterestRate
import com.eyther.lumbridge.domain.model.loan.LoanType
import com.eyther.lumbridge.domain.repository.loan.LoanRepository
import com.eyther.lumbridge.domain.repository.preferences.PreferencesRepository
import com.eyther.lumbridge.domain.repository.snapshotsalary.SnapshotSalaryRepository
import com.eyther.lumbridge.usecase.snapshotsalary.SaveSnapshotNetSalaryUseCase
import com.eyther.lumbridge.usecase.snapshotsalary.UpdateSnapshotSalaryWithAllocation
import com.eyther.lumbridge.usecase.user.financials.GetUserFinancials
import com.eyther.lumbridge.usecase.user.financials.SaveUserFinancials
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DataStoreMigrationHelper @Inject constructor(
    @ApplicationContext private val context: Context,
    private val loanRepository: LoanRepository,
    private val getUserFinancials: GetUserFinancials,
    private val saveUserFinancials: SaveUserFinancials,
    private val updateSnapshotSalaryWithAllocation: UpdateSnapshotSalaryWithAllocation,
    private val saveSnapshotNetSalaryUseCase: SaveSnapshotNetSalaryUseCase,
    private val appSettingsRepository: PreferencesRepository,
    private val snapshotSalaryRepository: SnapshotSalaryRepository
) {

    companion object {
        private const val TAG = "DataStoreMigrationHelper"
    }

    /**
     * Attempts to migrate the mortgage from the Data Store into the new Room database.
     *
     * If the operation fails, the migration will be cancelled and the mortgage will be deleted from
     * the Data Store. Meaning, it's a destructive operation.
     *
     * We only allow one mortgage at a time, and it was always a house mortgage. There was also no option to choose
     * TAEG for fixed mortgages, so we can safely assume that all fixed mortgages were TAN.
     */
    suspend fun tryMigrateMortgage() = runCatching {
        // If the migration has already been completed, we can skip this step
        if (appSettingsRepository.getCompletedMortgageMigration()) {
            Log.d(TAG, "⏩ Mortgage migration has already been completed")
            return@runCatching
        }

        val currentMortgage = loanRepository.getMortgageLoan()

        // If there is no mortgage, we can safely skip any migration as the user never had a mortgage
        // to begin with
        if (currentMortgage == null) {
            Log.d(TAG, "⏩ No mortgage found, skipping migration")
            appSettingsRepository.saveCompletedMortgageMigration()
            return@runCatching
        }

        val loanType = if (currentMortgage.euribor != null && currentMortgage.spread != null) {
            LoanType.EURIBOR_VARIABLE
        } else {
            LoanType.FIXED_TAN
        }

        val loanDomain = LoanDomain(
            name = context.getString(R.string.mortgage),
            startDate = currentMortgage.startDate,
            currentPaymentDate = currentMortgage.startDate,
            endDate = currentMortgage.endDate,
            initialAmount = currentMortgage.loanAmount,
            currentAmount = currentMortgage.loanAmount,
            loanInterestRate = LoanInterestRate.fromLoanType(
                loanType = loanType,
                variableEuribor = currentMortgage.euribor,
                variableSpread = currentMortgage.spread,
                fixedTanInterestRate = currentMortgage.fixedInterestRate,
                fixedTaegInterestRate = null
            ),
            loanCategory = LoanCategory.HOUSE,
            loanType = loanType,
            shouldNotifyWhenPaid = false,
            shouldAutoAddToExpenses = false,
            lastAutoPayDate = null
        )

        loanRepository.saveLoan(loanDomain)
        appSettingsRepository.saveCompletedMortgageMigration()

        Log.d(TAG, "✅ Mortgage migration completed successfully")
    }

    /**
     * The user financials salary percentage was stored in the Data Store as an INTEGER percentage, but this is not how percentages go.
     * The idea here is to convert those integers to a float from 0 to 1, so we can have a bit more flexibility.
     */
    suspend fun tryMigrateUserFinancialsSalaryPercentage() = runCatching {
        val currentUserFinancials = getUserFinancials()

        if (currentUserFinancials == null || appSettingsRepository.getCompletedSalaryPercentageMigration()) {
            Log.d(TAG, "⏩ No user financials found or salary percentage migration has already been completed, skipping migration")
            return@runCatching
        }

        // We need to convert the integer percentages to a float from 0 to 1
        saveUserFinancials(
            currentUserFinancials.copy(
                savingsPercentage = currentUserFinancials.savingsPercentage?.div(100),
                necessitiesPercentage = currentUserFinancials.necessitiesPercentage?.div(100),
                luxuriesPercentage = currentUserFinancials.luxuriesPercentage?.div(100)
            )
        )

        appSettingsRepository.saveCompletedSalaryPercentageMigration()
    }

    /**
     * Attempts to migrate the existing user financials net salary into the snap shot net salary database.
     * * If the operation fails, the migration will be cancelled.
     * * If the user financials are not found, the migration will be skipped.
     * * The first snapshot salary will be created with the current net salary and allocations.
     * * If the snapshot salary already exists, the migration will be skipped.
     */
    suspend fun tryMigrateFirstSnapshotSalaryAndAllocations() = runCatching {
        if (!appSettingsRepository.getCompletedSnapshotMigration()) {
            Log.d(TAG, "⏩ Snapshot migration has already been completed")
            return@runCatching
        }

        val userFinancials = getUserFinancials()

        if (userFinancials == null) {
            Log.d(TAG, "⏩ No user financials found, skipping migration. These will be created when the user fills this data.")
            return@runCatching
        }

        val snapshotSalaries = snapshotSalaryRepository.getAllSnapshotNetSalaries()

        when {
            snapshotSalaries.isEmpty() -> {
                Log.d(TAG, "⏩ User financials found, but no snapshot salary found, adding first snapshot salary as current salary.")

                saveSnapshotNetSalaryUseCase(userFinancials)

                Log.d(TAG, "✅ First snapshot migration completed successfully")
            }
            userFinancials.hasAllocations() && snapshotSalaries.any { it.moneyAllocations.isEmpty() } -> {
                Log.d(TAG, "⏩ User financials found, but snapshot salary found without allocations, adding allocations to snapshot salary.")

                snapshotSalaries
                    .filter { it.moneyAllocations.isEmpty() }
                    .forEach { snapshot -> updateSnapshotSalaryWithAllocation(userFinancials, snapshot.netSalary) }

                Log.d(TAG, "✅ Snapshot migration completed successfully")
            }
            else -> {
                Log.d(TAG, "⏩ User financials found, but snapshot already exists, skipping migration.")
            }
        }

        appSettingsRepository.saveCompletedSnapshotMigration()
    }
}
