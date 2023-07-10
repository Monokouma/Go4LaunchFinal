package com.despaircorp.domain.user

import javax.inject.Inject

class SaveNewEmailAddressUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend fun invoke(newEmail: String): Boolean = userRepository.saveNewEmail(email = newEmail)
}