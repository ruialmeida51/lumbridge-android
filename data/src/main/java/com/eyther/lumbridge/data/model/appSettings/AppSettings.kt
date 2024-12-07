package com.eyther.lumbridge.data.model.appSettings

data class AppSettings(
    val isDarkMode: Boolean,
    val appLanguageCountryCode: String,
    val showAllocationsOnExpenses: Boolean = true,
    val addFoodCardToNecessitiesAllocation: Boolean = true,
    val completedMortgageMigration: Boolean = false,
    val completedNetSalarySnapshotMigration: Boolean = false,
    val completedAllocationSnapshotMigration: Boolean = false,
    val completedSalaryPercentageMigration: Boolean = false,
    val completeFoodCardSnapshotMigration: Boolean = false,
    val promptedAllowNotifications: Boolean = false
)
