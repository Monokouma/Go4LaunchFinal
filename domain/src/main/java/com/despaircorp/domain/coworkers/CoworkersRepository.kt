package com.despaircorp.domain.coworkers

import com.despaircorp.domain.coworkers.model.CoworkerEntity
import kotlinx.coroutines.flow.Flow

interface CoworkersRepository {
    fun getCoworkers(): Flow<List<CoworkerEntity>>
}