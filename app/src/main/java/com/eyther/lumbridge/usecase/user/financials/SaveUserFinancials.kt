package com.eyther.lumbridge.usecase.user.financials

import com.eyther.lumbridge.domain.repository.user.UserRepository
import com.eyther.lumbridge.mapper.user.toDomain
import com.eyther.lumbridge.model.user.UserFinancialsUi
import com.eyther.lumbridge.model.user.UserProfileUi
import javax.inject.Inject

class SaveUserFinancials @Inject constructor(private val userRepository: UserRepository) {

    /**
     * Attempts to save the user financial profile.
     *
     * @param userFinancialsUi the user financials to save.
     */
    suspend operator fun invoke(userFinancialsUi: UserFinancialsUi) {
        return userRepository.saveUserFinancials(userFinancialsUi.toDomain())
    }
}
