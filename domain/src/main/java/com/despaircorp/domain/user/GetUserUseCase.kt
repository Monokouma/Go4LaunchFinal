package com.despaircorp.domain.user

import com.despaircorp.domain.authentication.AuthenticationRepository
import com.despaircorp.domain.authentication.model.UserEntity
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val authenticationRepository: AuthenticationRepository
) {
    suspend fun invoke(): UserEntity? {
        val user = authenticationRepository.getUser()
        return if (user != null) {
            userRepository.getUser(user.id)
        } else {
            null
        }
        
    }
}