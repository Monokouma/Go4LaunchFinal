package com.despaircorp.domain.coworkers

import android.util.Log
import com.despaircorp.domain.user.model.UserEntity
import javax.inject.Inject

class GetCoworkersFromFirebaseUseCase @Inject constructor(
    private val coworkersRepository: CoworkersRepository
) {
    fun invoke() {
        coworkersRepository.getCoworkers()
    }
}