package com.despaircorp.domain.user

import com.despaircorp.domain.authentication.model.UserEntity

interface UserRepository {
    suspend fun saveUser(userEntity: UserEntity): Boolean
}