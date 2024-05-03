package com.eyther.lumbridge.usecase.user.financials

import com.eyther.lumbridge.domain.repository.user.UserRepository
import com.eyther.lumbridge.mapper.user.toUi
import javax.inject.Inject

class GetUserFinancials @Inject constructor(
    private val userRepository: UserRepository
) {
    /**
     * Tries to get the user financials.
     *
     * @return the user financials.
     */
    suspend operator fun invoke() = userRepository.getUserFinancials()?.toUi()
}
