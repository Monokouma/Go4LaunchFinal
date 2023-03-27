package com.despaircorp.domain.authentication

import com.despaircorp.domain.authentication.model.UserEntity

interface AuthenticationRepository {
    suspend fun getUser(): UserEntity?
    
}