package com.despaircorp.domain.user

import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    fun invoke() {
    
    }
}