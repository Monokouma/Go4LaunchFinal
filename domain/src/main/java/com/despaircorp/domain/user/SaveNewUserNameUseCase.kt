package com.despaircorp.domain.user

import javax.inject.Inject

class SaveNewUserNameUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend fun invoke(newUserName: String): Boolean = userRepository.saveNewUserName(newUserName)
}