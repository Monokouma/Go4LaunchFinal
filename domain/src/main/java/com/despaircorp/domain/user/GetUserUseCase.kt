package com.despaircorp.domain.user

import com.despaircorp.domain.authentication.AuthenticationRepository
import com.despaircorp.domain.user.model.UserEntity
import kotlinx.coroutines.flow.*
import javax.inject.Inject

// TODO MONO DELETE THIS
class GetUserUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val authenticationRepository: AuthenticationRepository
) {
    fun invoke(): UserEntity? = authenticationRepository.getUser().let { authenticatedUser ->
        if (authenticatedUser == null) {
            null
        } else {
            UserEntity(
                id = authenticatedUser.id,
                name = authenticatedUser.name,
                email = authenticatedUser.email,
                photoUrl = authenticatedUser.photoUrl,
                isEating = false
            )
        }
    }
}