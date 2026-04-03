package com.eyther.lumbridge.domain.usecase.user.profile

import com.eyther.lumbridge.domain.model.user.UserProfileDomain
import com.eyther.lumbridge.domain.repository.user.UserRepository
import javax.inject.Inject

class GetUserProfile @Inject constructor(private val userRepository: UserRepository) {

    /**
     * Attempts to get the user profile from the repository.
     */
    suspend operator fun invoke(): UserProfileDomain? {
        return userRepository.getUserProfile()
    }
}
