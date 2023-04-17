package com.despaircorp.data.authentication

import com.despaircorp.domain.authentication.AuthenticationRepository
import com.despaircorp.domain.authentication.model.AuthenticatedUser
import com.despaircorp.domain.user.model.UserEntity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class AuthenticationRepositoryFirebase @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : AuthenticationRepository {
    override fun getUserFlow(): Flow<AuthenticatedUser?> = callbackFlow {
        trySend(getUser())
        
        awaitClose {  }
    }

    override fun getUser(): AuthenticatedUser? = firebaseAuth.currentUser?.let {
        AuthenticatedUser(
            it.uid,
            it.displayName ?: return null,
            it.email ?: return null,
            it.photoUrl?.toString(),
        )
    }
}