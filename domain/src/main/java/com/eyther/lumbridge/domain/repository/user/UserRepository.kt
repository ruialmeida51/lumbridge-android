package com.eyther.lumbridge.domain.repository.user

import com.eyther.lumbridge.data.datasource.user.local.UserLocalDataSource
import com.eyther.lumbridge.domain.mapper.user.toCached
import com.eyther.lumbridge.domain.mapper.user.toDomain
import com.eyther.lumbridge.domain.model.user.UserDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userLocalDataSource: UserLocalDataSource
) {

    suspend fun saveUser(user: UserDomain) = withContext(Dispatchers.IO) {
        userLocalDataSource.saveUserData(user.toCached())
    }

    suspend fun getUser(): UserDomain? = withContext(Dispatchers.IO) {
        userLocalDataSource.userPreferencesFlow.firstOrNull()?.toDomain()
    }
}
