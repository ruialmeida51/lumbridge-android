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
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DataStoreMigrationHelper @Inject constructor(
    @ApplicationContext private val context: Context,
    private val loanRepository: LoanRepository,
    private val appSettingsRepository: PreferencesRepository
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
    suspend fun tryMigrateMortgage() {
        // If the migration has already been completed, we can skip this step
        if (appSettingsRepository.getCompletedMortgageMigration()) {
            Log.d(TAG, "⏩ Mortgage migration has already been completed")
            return
        }

        val currentMortgage = loanRepository.getMortgageLoan()

        // If there is no mortgage, we can safely skip any migration as the user never had a mortgage
        // to begin with
        if (currentMortgage == null) {
            Log.d(TAG, "⏩ No mortgage found, skipping migration")
            appSettingsRepository.saveCompletedMortgageMigration()
            return
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
}
