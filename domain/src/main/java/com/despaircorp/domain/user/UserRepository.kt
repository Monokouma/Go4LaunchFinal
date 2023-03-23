package com.despaircorp.domain.user

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun saveUser(): Flow<Boolean>
}