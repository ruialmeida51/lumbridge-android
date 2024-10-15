package com.eyther.lumbridge.domain.repository.user

import com.eyther.lumbridge.data.datasource.user.local.UserFinancialsLocalDataSource
import com.eyther.lumbridge.data.datasource.user.local.UserMortgageLocalDataSource
import com.eyther.lumbridge.data.datasource.user.local.UserProfileLocalDataSource
import com.eyther.lumbridge.domain.di.model.Schedulers
import com.eyther.lumbridge.domain.mapper.user.toCached
import com.eyther.lumbridge.domain.mapper.user.toDomain
import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.domain.model.user.UserFinancialsDomain
import com.eyther.lumbridge.domain.model.user.UserMortgageDomain
import com.eyther.lumbridge.domain.model.user.UserProfileDomain
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userProfileLocalDataSource: UserProfileLocalDataSource,
    private val userFinancialsLocalDataSource: UserFinancialsLocalDataSource,
    private val userMortgageLocalDataSource: UserMortgageLocalDataSource,
    private val schedulers: Schedulers
) {
    fun getUserProfileFlow() = userProfileLocalDataSource.userProfileFlow.map { it?.toDomain() }

    fun getUserFinancialsFlow() = userFinancialsLocalDataSource.userFinancialsFlow.map { it?.toDomain() }

    fun getUserMortgageFlow() = userMortgageLocalDataSource.userMortgageFlow.map { it?.toDomain() }

    suspend fun getUserProfile() = withContext(schedulers.io) {
        getUserProfileFlow().firstOrNull()
    }

    suspend fun getUserFinancials() = withContext(schedulers.io) {
        userFinancialsLocalDataSource.userFinancialsFlow.map { it?.toDomain() }.firstOrNull()
    }

    suspend fun getUserMortgage() = withContext(schedulers.io) {
        userMortgageLocalDataSource.userMortgageFlow.map { it?.toDomain() }.firstOrNull()
    }

    suspend fun saveUserProfile(user: UserProfileDomain) = withContext(schedulers.io) {
        userProfileLocalDataSource.saveUserData(user.toCached())
    }

    suspend fun saveUserFinancials(user: UserFinancialsDomain) = withContext(schedulers.io) {
        userFinancialsLocalDataSource.saveUserFinancials(user.toCached())
    }

    suspend fun saveUserMortgage(user: UserMortgageDomain) = withContext(schedulers.io) {
        userMortgageLocalDataSource.saveUserMortgage(user.toCached())
    }

    suspend fun getUserLocale(): SupportedLocales? = withContext(schedulers.io) {
        getUserProfile()?.locale
    }
}
