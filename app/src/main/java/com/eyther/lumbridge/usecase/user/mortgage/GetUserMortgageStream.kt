package com.eyther.lumbridge.usecase.user.mortgage

import com.eyther.lumbridge.domain.repository.user.UserRepository
import com.eyther.lumbridge.mapper.user.toUi
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetUserMortgageStream @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke() = userRepository.getUserMortgageFlow().map { it?.toUi() }
}
