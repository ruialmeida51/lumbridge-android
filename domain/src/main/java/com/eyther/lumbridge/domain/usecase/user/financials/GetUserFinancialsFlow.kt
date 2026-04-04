package com.eyther.lumbridge.domain.usecase.user.financials

import com.eyther.lumbridge.domain.model.user.UserFinancialsDomain
import com.eyther.lumbridge.domain.repository.user.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserFinancialsFlow @Inject constructor(
    private val userRepository: UserRepository
) {
    /**
     * Tries to get the user financials.
     *
     * @return the user financials.
     */
    operator fun invoke(): Flow<UserFinancialsDomain?> = userRepository.getUserFinancialsFlow()
}
