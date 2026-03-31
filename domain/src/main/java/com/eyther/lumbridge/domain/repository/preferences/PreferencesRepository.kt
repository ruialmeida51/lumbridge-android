package com.eyther.lumbridge.domain.repository.preferences

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

interface PreferencesRepository {
    val currentPreferences
    val newPreferences
    suspend fun getPreferences(): Preferences?
    fun getPreferencesFlow(): Flow<Preferences?>
    suspend fun updatePreferences( isDarkMode: Boolean, appLanguage: SupportedLanguages, showAllocationsOnExpenses: Boolean, addFoodCardToNecessitiesAllocation: Boolean )
    suspend fun getCompletedMortgageMigration()
    suspend fun getCompletedSalaryPercentageMigration()
    suspend fun getCompletedSnapshotFoodCardMigration()
    suspend fun getCompletedNetSalarySnapshotMigration()
    suspend fun getCompletedAllocationSnapshotMigration()
    suspend fun saveCompletedMortgageMigration()
    suspend fun saveCompletedNetSalarySnapshotMigration()
    suspend fun saveCompletedAllocationSnapshotMigration()
    suspend fun saveCompletedSnapshotFoodCardMigration()
    suspend fun saveCompletedSalaryPercentageMigration()
}
