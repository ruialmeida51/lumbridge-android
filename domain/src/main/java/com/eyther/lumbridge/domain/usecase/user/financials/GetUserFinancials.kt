package com.eyther.lumbridge.domain.usecase.user.financials

import com.eyther.lumbridge.domain.model.user.UserFinancialsDomain
import com.eyther.lumbridge.domain.repository.user.UserRepository
import javax.inject.Inject

class GetUserFinancials @Inject constructor(
    private val userRepository: UserRepository
) {
    /**
     * Tries to get the user financials.
     *
     * @return the user financials.
     */
    suspend operator fun invoke(): UserFinancialsDomain? = userRepository.getUserFinancials()
}
