package com.eyther.lumbridge.usecase.user

import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.domain.repository.user.UserRepository
import javax.inject.Inject

class GetLocaleOrDefault @Inject constructor(
    private val userRepository: UserRepository
) {
    /**
     * Tries to get the user current locale. If it doesn't exist, try to get the user's current
     * location and assign a default Locale.
     *
     * @return the saved locale or a best effort locale.
     */
    suspend operator fun invoke(): SupportedLocales {
        return userRepository.getUserLocale() ?: SupportedLocales.PORTUGAL
    }
}
