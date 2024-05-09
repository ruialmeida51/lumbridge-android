package com.eyther.lumbridge.usecase.user.mortgage

import com.eyther.lumbridge.domain.repository.user.UserRepository
import com.eyther.lumbridge.mapper.user.toDomain
import com.eyther.lumbridge.model.user.UserMortgageUi
import javax.inject.Inject

class SaveUserMortgage @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userMortgageUi: UserMortgageUi) =
        userRepository.saveUserMortgage(userMortgageUi.toDomain())
}
