package com.eyther.lumbridge.domain.repository.user

import com.eyther.lumbridge.shared.di.model.Schedulers
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

interface UserRepository {
    fun getUserProfileFlow()
    fun getUserFinancialsFlow()
    suspend fun getUserProfile()
    suspend fun getUserFinancials()
    suspend fun saveUserProfile(user: UserProfileDomain)
    suspend fun saveUserFinancials(user: UserFinancialsDomain)
    suspend fun getUserLocale(): SupportedLocales?
}
