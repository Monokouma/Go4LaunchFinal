package com.despaircorp.domain.user

import javax.inject.Inject

class SaveUserUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    fun invoke() {
        userRepository.saveUser()
    }
}