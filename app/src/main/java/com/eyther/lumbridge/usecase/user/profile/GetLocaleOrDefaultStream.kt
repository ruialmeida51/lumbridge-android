package com.eyther.lumbridge.usecase.user.profile

import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.domain.repository.user.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetLocaleOrDefaultStream @Inject constructor(
    private val userRepository: UserRepository
) {
    /**
     * Tries to get the user current locale. If it doesn't exist, try to get the user's current
     * location and assign a default Locale.
     *
     * @return the saved locale or a best effort locale.
     */
    operator fun invoke(): Flow<SupportedLocales> {
        return userRepository.getUserProfileFlow()
            .map { it?.locale ?: SupportedLocales.PORTUGAL }
    }
}
