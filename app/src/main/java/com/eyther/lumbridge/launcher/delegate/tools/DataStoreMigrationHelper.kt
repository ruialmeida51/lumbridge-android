package com.eyther.lumbridge.launcher.delegate.tools

import android.content.Context
import android.util.Log
import com.eyther.lumbridge.R
import com.eyther.lumbridge.domain.model.loan.Loan
import com.eyther.lumbridge.domain.model.loan.LoanCategory
import com.eyther.lumbridge.domain.model.loan.LoanInterestRate
import com.eyther.lumbridge.domain.model.loan.LoanType
import com.eyther.lumbridge.domain.repository.loan.LoanRepository
import com.eyther.lumbridge.domain.repository.preferences.PreferencesRepository
import com.eyther.lumbridge.domain.repository.snapshotsalary.SnapshotSalaryRepository
import com.eyther.lumbridge.usecase.finance.GetNetSalaryUseCase
import com.eyther.lumbridge.usecase.snapshotsalary.SaveSnapshotNetSalaryUseCase
import com.eyther.lumbridge.usecase.user.financials.GetUserFinancials
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DataStoreMigrationHelper @Inject constructor(
    @ApplicationContext private val context: Context,
    private val loanRepository: LoanRepository,
    private val getUserFinancials: GetUserFinancials,
    private val getNetSalaryUseCase: GetNetSalaryUseCase,
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
    suspend fun tryMigrateMortgage() = kotlin.runCatching {
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

        val loan = Loan(
            name = context.getString(R.string.mortgage),
            startDate = currentMortgage.startDate,
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
            loanType = loanType
        )

        loanRepository.saveLoan(loan)
        appSettingsRepository.saveCompletedMortgageMigration()

        Log.d(TAG, "✅ Mortgage migration completed successfully")
    }

    suspend fun tryMigrateFirstSnapshotSalary() = kotlin.runCatching {
        if (appSettingsRepository.getCompletedNetSalarySnapshotMigration()) {
            Log.d(TAG, "⏩ First snapshot salary migration has already been completed")
            return@runCatching
        }

        val userFinancials = getUserFinancials()

        if (userFinancials == null) {
            Log.d(TAG, "⏩ No user financials found, skipping migration. These will be created when the user fills this data.")
            return@runCatching
        }

        if (snapshotSalaryRepository.getAllSnapshotNetSalaries().isEmpty()) {
            Log.d(TAG, "⏩ User financials found, but no snapshot salary found, adding first snapshot salary as current salary.")

            val netSalary = getNetSalaryUseCase(userFinancials)

            saveSnapshotNetSalaryUseCase(
                netSalary.monthlyNetSalary
            )

            Log.d(TAG, "✅ First snapshot salary migration completed successfully")
        } else {
            Log.d(TAG, "⏩ User financials found, but snapshot salary already exists, skipping migration.")
        }

        appSettingsRepository.saveCompletedNetSalarySnapshotMigration()
    }
}
