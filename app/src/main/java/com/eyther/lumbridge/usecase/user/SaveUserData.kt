package com.eyther.lumbridge.usecase.user

import com.eyther.lumbridge.domain.repository.user.UserRepository
import com.eyther.lumbridge.mapper.user.toDomain
import com.eyther.lumbridge.mapper.user.toUi
import com.eyther.lumbridge.model.user.UserUi
import javax.inject.Inject

class SaveUserData @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(userUi: UserUi) {
        return userRepository.saveUser(userUi.toDomain())
    }
}
