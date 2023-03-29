package com.despaircorp.domain.user

import com.despaircorp.domain.authentication.AuthenticationRepository
import javax.inject.Inject

class SaveCurrentUserUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val userRepository: UserRepository
) {
    suspend fun invoke(): Boolean {
        val user = authenticationRepository.getUserFlow()

        return if (user != null) {
            userRepository.saveUser(user)
        } else {
            false
        }
    }
}