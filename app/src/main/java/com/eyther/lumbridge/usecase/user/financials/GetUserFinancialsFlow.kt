package com.eyther.lumbridge.usecase.user.financials

import com.eyther.lumbridge.domain.repository.user.UserRepository
import com.eyther.lumbridge.mapper.user.toUi
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetUserFinancialsFlow @Inject constructor(
    private val userRepository: UserRepository
) {
    /**
     * Tries to get the user financials.
     *
     * @return the user financials.
     */
    operator fun invoke() = userRepository.getUserFinancialsFlow().map { it?.toUi() }
}
