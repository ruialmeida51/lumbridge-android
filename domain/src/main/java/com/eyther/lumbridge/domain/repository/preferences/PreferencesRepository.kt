package com.eyther.lumbridge.domain.repository.preferences

import com.eyther.lumbridge.data.datasource.appSettings.local.AppSettingsLocalDataSource
import com.eyther.lumbridge.domain.mapper.preferences.toCached
import com.eyther.lumbridge.domain.mapper.preferences.toDomain
import com.eyther.lumbridge.domain.model.locale.SupportedLanguages
import com.eyther.lumbridge.domain.model.preferences.Preferences
import com.eyther.lumbridge.shared.di.model.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PreferencesRepository @Inject constructor(
    private val appSettingsLocalDataSource: AppSettingsLocalDataSource,
    private val schedulers: Schedulers
) {

    suspend fun getPreferences(): Preferences? {
        return appSettingsLocalDataSource.appSettingsFlow.firstOrNull()?.toDomain()
    }

    fun getPreferencesFlow(): Flow<Preferences?> {
        return appSettingsLocalDataSource.appSettingsFlow.map { it?.toDomain() }
    }

    /**
     * Update the preferences. Tries to get the current preferences and updates the values, if these do not exist
     * it creates a new one with the given values.
     *
     * @param isDarkMode The new value for the dark mode preference.
     * @param appLanguage The new value for the app language preference.
     */
    suspend fun updatePreferences(
        isDarkMode: Boolean,
        appLanguage: SupportedLanguages,
        showAllocationsOnExpenses: Boolean,
        addFoodCardToNecessitiesAllocation: Boolean
    ) = withContext(schedulers.io) {
        val currentPreferences = getPreferences()

        val newPreferences = currentPreferences?.copy(
            isDarkMode = isDarkMode,
            appLanguage = appLanguage,
            showAllocationsOnExpenses = showAllocationsOnExpenses,
            addFoodCardToNecessitiesAllocation = addFoodCardToNecessitiesAllocation
        ) ?: Preferences(
            isDarkMode = isDarkMode,
            appLanguage = appLanguage,
            showAllocationsOnExpenses = showAllocationsOnExpenses,
            addFoodCardToNecessitiesAllocation = addFoodCardToNecessitiesAllocation
        )

        appSettingsLocalDataSource.saveAppSettings(appSettings = newPreferences.toCached())
    }

    // region Get Migrations

    suspend fun getCompletedMortgageMigration() = withContext(schedulers.io) {
        appSettingsLocalDataSource.getCompletedMortgageMigration()
    }

    suspend fun getCompletedSalaryPercentageMigration() = withContext(schedulers.io) {
        appSettingsLocalDataSource.getCompletedSalaryPercentageMigration()
    }

    suspend fun getCompletedSnapshotFoodCardMigration() = withContext(schedulers.io) {
        appSettingsLocalDataSource.getCompletedFoodCardAmountMigration()
    }

    suspend fun getCompletedNetSalarySnapshotMigration() = withContext(schedulers.io) {
        appSettingsLocalDataSource.getCompletedNetSalarySnapshotMigration()
    }

    suspend fun getCompletedAllocationSnapshotMigration() = withContext(schedulers.io) {
        appSettingsLocalDataSource.getCompletedAllocationSnapshotMigration()
    }

    // endregion

    // region Save Migrations

    suspend fun saveCompletedMortgageMigration() = withContext(schedulers.io) {
        appSettingsLocalDataSource.saveCompletedMortgageMigration(true)
    }

    suspend fun saveCompletedNetSalarySnapshotMigration() = withContext(schedulers.io) {
        appSettingsLocalDataSource.saveCompletedNetSalarySnapshotMigration(true)
    }

    suspend fun saveCompletedAllocationSnapshotMigration() = withContext(schedulers.io) {
        appSettingsLocalDataSource.saveCompletedAllocationSnapshotMigration(true)
    }

    suspend fun saveCompletedSnapshotFoodCardMigration() = withContext(schedulers.io) {
        appSettingsLocalDataSource.saveCompletedFoodCardAmountMigration(true)
    }

    suspend fun saveCompletedSalaryPercentageMigration() = withContext(schedulers.io) {
        appSettingsLocalDataSource.saveCompletedSalaryPercentageMigration(true)
    }

    // endregion
}
