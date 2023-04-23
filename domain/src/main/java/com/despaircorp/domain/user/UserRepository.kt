package com.despaircorp.domain.user

import com.despaircorp.domain.user.model.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun saveUser(userEntity: UserEntity): Boolean
    
    fun getUser(uuid: String): Flow<UserEntity?>
}