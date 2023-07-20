package com.despaircorp.domain.coworkers

import com.despaircorp.domain.coworkers.model.CoworkerEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transformLatest
import javax.inject.Inject

class GetCoworkersFromFirebaseUseCase @Inject constructor(
    private val coworkersRepository: CoworkersRepository
) {
    fun invoke(): Flow<List<CoworkerEntity>> = coworkersRepository.getCoworkers().transformLatest {
        emit(it)
    }
}