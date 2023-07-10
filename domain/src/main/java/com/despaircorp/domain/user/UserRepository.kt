package com.despaircorp.domain.user

import com.despaircorp.domain.user.model.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun saveUser(userEntity: UserEntity): Boolean
    
    fun getUser(uuid: String): Flow<UserEntity?>
    
    suspend fun saveNewUserName(userName: String): Boolean
    
    suspend fun saveNewEmail(email: String): Boolean
    
    suspend fun saveNewPassword(password: String): Boolean
    
    suspend fun saveNewNotificationReceivingState(newState: Boolean): Boolean
}