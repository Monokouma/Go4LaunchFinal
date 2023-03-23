package com.despaircorp.domain.user

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SaveUserUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    suspend fun invoke() = flow<Boolean> {
        userRepository.saveUser().collect {
            emit(it)
        }
    }
}