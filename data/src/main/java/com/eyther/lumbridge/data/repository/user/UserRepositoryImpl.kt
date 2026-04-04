package com.eyther.lumbridge.data.repository.user

import com.eyther.lumbridge.data.datasource.user.local.UserFinancialsLocalDataSource
import com.eyther.lumbridge.data.datasource.user.local.UserProfileLocalDataSource
import com.eyther.lumbridge.data.mapper.user.toCached
import com.eyther.lumbridge.data.mapper.user.toDomain
import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.domain.model.user.UserFinancialsDomain
import com.eyther.lumbridge.domain.model.user.UserProfileDomain
import com.eyther.lumbridge.domain.repository.user.UserRepository
import com.eyther.lumbridge.shared.di.model.Schedulers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userProfileLocalDataSource: UserProfileLocalDataSource,
    private val userFinancialsLocalDataSource: UserFinancialsLocalDataSource,
    private val schedulers: Schedulers
) : UserRepository {

    override fun getUserProfileFlow() = userProfileLocalDataSource.userProfileFlow.map { it?.toDomain() }

    override fun getUserFinancialsFlow() = userFinancialsLocalDataSource.userFinancialsFlow.map { it?.toDomain() }

    override suspend fun getUserProfile() = withContext(schedulers.io) {
        getUserProfileFlow().firstOrNull()
    }

    override suspend fun getUserFinancials() = withContext(schedulers.io) {
        userFinancialsLocalDataSource.userFinancialsFlow.map { it?.toDomain() }.firstOrNull()
    }

    override suspend fun saveUserProfile(user: UserProfileDomain) = withContext(schedulers.io) {
        userProfileLocalDataSource.saveUserData(user.toCached())
    }

    override suspend fun saveUserFinancials(user: UserFinancialsDomain) = withContext(schedulers.io) {
        userFinancialsLocalDataSource.saveUserFinancials(user.toCached())
    }

    override suspend fun getUserLocale(): SupportedLocales? = withContext(schedulers.io) {
        getUserProfile()?.locale
    }
}
