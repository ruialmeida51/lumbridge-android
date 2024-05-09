package com.eyther.lumbridge.usecase.user.mortgage

import com.eyther.lumbridge.domain.repository.user.UserRepository
import com.eyther.lumbridge.mapper.user.toUi
import javax.inject.Inject

class GetUserMortgage @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke() = userRepository.getUserMortgage()?.toUi()
}
