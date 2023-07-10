package com.despaircorp.domain.user

import javax.inject.Inject

class SaveNewPasswordUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend fun invoke(password: String): Boolean = userRepository.saveNewPassword(password)
}