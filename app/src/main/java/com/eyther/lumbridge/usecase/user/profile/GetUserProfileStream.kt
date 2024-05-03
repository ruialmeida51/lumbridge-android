package com.eyther.lumbridge.usecase.user.profile

import com.eyther.lumbridge.domain.repository.user.UserRepository
import com.eyther.lumbridge.mapper.user.toUi
import com.eyther.lumbridge.model.user.UserProfileUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetUserProfileStream @Inject constructor(private val userRepository: UserRepository) {

    /**
     * Attempts to get the user profile from the repository and maps it to the UI model.
     * @return a stream of the user profile UI model
     */
    operator fun invoke(): Flow<UserProfileUi?> {
        return userRepository.getUserProfileFlow().map { it?.toUi() }
    }
}
