package com.despaircorp.domain.user

import javax.inject.Inject

class SaveNotificationReceivingStateUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend fun invoke(isNotificationEnabled: Boolean): Boolean {
        return userRepository.saveNewNotificationReceivingState(isNotificationEnabled)
    }
}