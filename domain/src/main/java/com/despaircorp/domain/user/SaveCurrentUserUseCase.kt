package com.despaircorp.domain.user

import com.despaircorp.domain.authentication.AuthenticationRepository
import com.despaircorp.domain.user.model.UserEntity
import javax.inject.Inject

class SaveCurrentUserUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val userRepository: UserRepository
) {
    suspend fun invoke(): Boolean {
        val user = authenticationRepository.getUser()

        return if (user != null) {
            userRepository.saveUser(
                UserEntity(
                    id = user.id,
                    name = user.name,
                    email = user.email,
                    photoUrl = user.photoUrl,
                )
            )
        } else {
            false
        }
    }
}