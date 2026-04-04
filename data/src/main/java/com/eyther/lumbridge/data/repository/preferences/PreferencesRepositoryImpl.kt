package com.eyther.lumbridge.data.repository.preferences

import com.eyther.lumbridge.data.datasource.appSettings.local.AppSettingsLocalDataSource
import com.eyther.lumbridge.data.mapper.preferences.toCached
import com.eyther.lumbridge.data.mapper.preferences.toDomain
import com.eyther.lumbridge.domain.model.locale.SupportedLanguages
import com.eyther.lumbridge.domain.model.preferences.Preferences
import com.eyther.lumbridge.domain.repository.preferences.PreferencesRepository
import com.eyther.lumbridge.shared.di.model.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PreferencesRepositoryImpl @Inject constructor(
    private val appSettingsLocalDataSource: AppSettingsLocalDataSource,
    private val schedulers: Schedulers
) : PreferencesRepository {

    override suspend fun getPreferences(): Preferences? {
        return appSettingsLocalDataSource.appSettingsFlow.firstOrNull()?.toDomain()
    }

    override fun getPreferencesFlow(): Flow<Preferences?> {
        return appSettingsLocalDataSource.appSettingsFlow.map { it?.toDomain() }
    }

    override suspend fun updatePreferences(
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

    override suspend fun getCompletedMortgageMigration() = withContext(schedulers.io) {
        appSettingsLocalDataSource.getCompletedMortgageMigration()
    }

    override suspend fun getCompletedSalaryPercentageMigration() = withContext(schedulers.io) {
        appSettingsLocalDataSource.getCompletedSalaryPercentageMigration()
    }

    override suspend fun getCompletedSnapshotFoodCardMigration() = withContext(schedulers.io) {
        appSettingsLocalDataSource.getCompletedFoodCardAmountMigration()
    }

    override suspend fun getCompletedNetSalarySnapshotMigration() = withContext(schedulers.io) {
        appSettingsLocalDataSource.getCompletedNetSalarySnapshotMigration()
    }

    override suspend fun getCompletedAllocationSnapshotMigration() = withContext(schedulers.io) {
        appSettingsLocalDataSource.getCompletedAllocationSnapshotMigration()
    }

    override suspend fun saveCompletedMortgageMigration() = withContext(schedulers.io) {
        appSettingsLocalDataSource.saveCompletedMortgageMigration(true)
    }

    override suspend fun saveCompletedNetSalarySnapshotMigration() = withContext(schedulers.io) {
        appSettingsLocalDataSource.saveCompletedNetSalarySnapshotMigration(true)
    }

    override suspend fun saveCompletedAllocationSnapshotMigration() = withContext(schedulers.io) {
        appSettingsLocalDataSource.saveCompletedAllocationSnapshotMigration(true)
    }

    override suspend fun saveCompletedSnapshotFoodCardMigration() = withContext(schedulers.io) {
        appSettingsLocalDataSource.saveCompletedFoodCardAmountMigration(true)
    }

    override suspend fun saveCompletedSalaryPercentageMigration() = withContext(schedulers.io) {
        appSettingsLocalDataSource.saveCompletedSalaryPercentageMigration(true)
    }
}
