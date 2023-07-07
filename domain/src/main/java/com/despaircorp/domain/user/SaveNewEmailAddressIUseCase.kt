package com.despaircorp.domain.user

import javax.inject.Inject

class SaveNewEmailAddressIUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend fun invoke(newEmail: String): Boolean {
        return userRepository.saveNewEmail(email = newEmail)
    }
}