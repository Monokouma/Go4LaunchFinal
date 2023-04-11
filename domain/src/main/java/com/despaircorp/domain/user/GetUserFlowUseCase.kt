package com.despaircorp.domain.user

import com.despaircorp.domain.authentication.AuthenticationRepository
import com.despaircorp.domain.user.model.UserEntity
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class GetUserFlowUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val authenticationRepository: AuthenticationRepository
) {
    fun invoke(): Flow<UserEntity?> = authenticationRepository.getUserFlow().transformLatest { authenticatedUser ->
        if (authenticatedUser == null) {
            emit(null)
        } else {
            userRepository.getUser(authenticatedUser.id).collect {
                emit(it)
            }
        }
    }
}