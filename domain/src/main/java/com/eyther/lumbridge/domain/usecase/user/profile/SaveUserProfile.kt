package com.eyther.lumbridge.domain.usecase.user.profile

import com.eyther.lumbridge.domain.model.user.UserProfileDomain
import com.eyther.lumbridge.domain.repository.user.UserRepository
import javax.inject.Inject

class SaveUserProfile @Inject constructor(private val userRepository: UserRepository) {

    /**
     * Attempts to save the user profile.
     *
     * @param userProfileDomain the user profile to save.
     */
    suspend operator fun invoke(userProfileDomain: UserProfileDomain) {
        return userRepository.saveUserProfile(userProfileDomain)
    }
}
