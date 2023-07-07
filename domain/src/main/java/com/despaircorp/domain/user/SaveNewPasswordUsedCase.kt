package com.despaircorp.domain.user

import javax.inject.Inject

class SaveNewPasswordUsedCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend fun invoke(password: String): Boolean {
        return userRepository.saveNewPassword(password)
    }
}