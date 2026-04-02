package com.eyther.lumbridge.domain.repository.preferences

import com.eyther.lumbridge.domain.model.locale.SupportedLanguages
import com.eyther.lumbridge.domain.model.preferences.Preferences
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    suspend fun getPreferences(): Preferences?
    fun getPreferencesFlow(): Flow<Preferences?>
    suspend fun updatePreferences(
        isDarkMode: Boolean,
        appLanguage: SupportedLanguages,
        showAllocationsOnExpenses: Boolean,
        addFoodCardToNecessitiesAllocation: Boolean
    )
    suspend fun getCompletedMortgageMigration(): Boolean
    suspend fun getCompletedSalaryPercentageMigration(): Boolean
    suspend fun getCompletedSnapshotFoodCardMigration(): Boolean
    suspend fun getCompletedNetSalarySnapshotMigration(): Boolean
    suspend fun getCompletedAllocationSnapshotMigration(): Boolean
    suspend fun saveCompletedMortgageMigration()
    suspend fun saveCompletedNetSalarySnapshotMigration()
    suspend fun saveCompletedAllocationSnapshotMigration()
    suspend fun saveCompletedSnapshotFoodCardMigration()
    suspend fun saveCompletedSalaryPercentageMigration()
}
