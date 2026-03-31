package com.eyther.lumbridge.domain.usecase.user.profile

import com.eyther.lumbridge.domain.repository.user.UserRepository
import com.eyther.lumbridge.domain.mapper.user.toDomain
import com.eyther.lumbridge.domain.model.user.UserProfileUi
import javax.inject.Inject

class SaveUserProfile @Inject constructor(private val userRepository: UserRepository) {

    /**
     * Attempts to save the user profile.
     *
     * @param userUi the user profile to save.
     */
    suspend operator fun invoke(userUi: UserProfileUi) {
        return userRepository.saveUserProfile(userUi.toDomain())
    }
}
