package com.despaircorp.domain.authentication

import com.despaircorp.domain.authentication.model.AuthenticatedUser
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    fun getUserFlow(): Flow<AuthenticatedUser?>
    fun getUser(): AuthenticatedUser?
}