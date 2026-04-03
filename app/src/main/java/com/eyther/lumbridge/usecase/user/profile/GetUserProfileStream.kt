package com.eyther.lumbridge.usecase.user.profile

import com.eyther.lumbridge.domain.model.user.UserProfileDomain
import com.eyther.lumbridge.domain.repository.user.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserProfileStream @Inject constructor(private val userRepository: UserRepository) {

    /**
     * Attempts to get the user profile from the repository.
     * @return a stream of the user profile domain model
     */
    operator fun invoke(): Flow<UserProfileDomain?> {
        return userRepository.getUserProfileFlow()
    }
}
