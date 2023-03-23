package com.despaircorp.data.authentication

import com.despaircorp.domain.authentication.AuthenticationRepository
import com.despaircorp.domain.authentication.model.UserEntity
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class AuthenticationRepositoryFirebase @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : AuthenticationRepository {
    override suspend fun getUser(): UserEntity? = firebaseAuth.currentUser?.let {
        UserEntity(
            it.uid,
            it.displayName ?: return null,
            it.email ?: return null,
            it.photoUrl?.toString(),
        )
    }
}