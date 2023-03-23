package com.despaircorp.data.user

import android.util.Log
import com.despaircorp.domain.user.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class UserRepositoryFirestore @Inject constructor(
    private val oAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
): UserRepository {
    
    override fun saveUser(): Flow<Boolean> = callbackFlow {
        val userDto = UserDto(
            uuid = oAuth.currentUser?.uid,
            name = oAuth.currentUser?.displayName,
            emailAddress = oAuth.currentUser?.email,
            picture = oAuth.currentUser?.photoUrl?.toString()
        )
    
        firestore.collection("users").document(oAuth.currentUser?.uid ?: "error")
            .set(userDto)
            .addOnSuccessListener {
                Log.i("Monokouma", "success")
                trySend(true)
            }
            .addOnFailureListener {
                Log.i("Monokouma", "failure")
                trySend(false)
                it.printStackTrace()
            }
        
        awaitClose { firestore.terminate() }
    }
    
}