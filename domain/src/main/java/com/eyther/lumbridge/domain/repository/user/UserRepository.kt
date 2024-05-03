package com.eyther.lumbridge.domain.repository.user

import com.eyther.lumbridge.data.datasource.user.local.UserFinancialsLocalDataSource
import com.eyther.lumbridge.data.datasource.user.local.UserProfileLocalDataSource
import com.eyther.lumbridge.domain.mapper.user.toCached
import com.eyther.lumbridge.domain.mapper.user.toDomain
import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.domain.model.user.UserFinancialsDomain
import com.eyther.lumbridge.domain.model.user.UserProfileDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userProfileLocalDataSource: UserProfileLocalDataSource,
    private val userFinancialsLocalDataSource: UserFinancialsLocalDataSource
) {
    fun getUserProfileFlow() = userProfileLocalDataSource.userProfileFlow.map { it?.toDomain() }

    fun getUserFinancialsFlow() = userFinancialsLocalDataSource.userFinancialsFlow.map { it?.toDomain() }

    suspend fun saveUserProfile(user: UserProfileDomain) = withContext(Dispatchers.IO) {
        userProfileLocalDataSource.saveUserData(user.toCached())
    }

    suspend fun getUserProfile() = withContext(Dispatchers.IO) {
        getUserProfileFlow().firstOrNull()
    }

    suspend fun saveUserFinancials(user: UserFinancialsDomain) = withContext(Dispatchers.IO) {
        userFinancialsLocalDataSource.saveUserFinancials(user.toCached())
    }

    suspend fun getUserFinancials() = withContext(Dispatchers.IO) {
        userFinancialsLocalDataSource.userFinancialsFlow.map { it?.toDomain() }.firstOrNull()
    }

    suspend fun getUserLocale(): SupportedLocales? = withContext(Dispatchers.IO) {
       getUserProfile()?.locale
    }
}
