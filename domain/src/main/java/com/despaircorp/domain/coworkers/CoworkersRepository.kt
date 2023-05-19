package com.despaircorp.domain.coworkers

import com.despaircorp.domain.user.model.UserEntity
import kotlinx.coroutines.flow.Flow

interface CoworkersRepository {
    fun getCoworkers(): Flow<List<UserEntity>>
}