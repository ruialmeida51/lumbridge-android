package com.eyther.lumbridge.usecase.user

import com.eyther.lumbridge.domain.repository.user.UserRepository
import com.eyther.lumbridge.mapper.user.toUi
import com.eyther.lumbridge.model.user.UserUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetUserData @Inject constructor(private val userRepository: UserRepository) {
    operator fun invoke(): Flow<UserUi?> {
        return userRepository.getUser().map { it?.toUi() }
    }
}
