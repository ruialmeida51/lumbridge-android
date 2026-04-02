package com.eyther.lumbridge.domain.repository.user

import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.domain.model.user.UserFinancialsDomain
import com.eyther.lumbridge.domain.model.user.UserProfileDomain
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserProfileFlow(): Flow<UserProfileDomain?>
    fun getUserFinancialsFlow(): Flow<UserFinancialsDomain?>
    suspend fun getUserProfile(): UserProfileDomain?
    suspend fun getUserFinancials(): UserFinancialsDomain?
    suspend fun saveUserProfile(user: UserProfileDomain)
    suspend fun saveUserFinancials(user: UserFinancialsDomain)
    suspend fun getUserLocale(): SupportedLocales?
}
