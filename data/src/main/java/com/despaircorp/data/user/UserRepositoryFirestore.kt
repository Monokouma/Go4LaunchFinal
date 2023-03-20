package com.despaircorp.data.user

import android.util.Log
import com.despaircorp.domain.user.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class UserRepositoryFirestore @Inject constructor(
    private val oAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
): UserRepository {
    
    override fun saveUser() {
        Log.i("Monokouma", oAuth.currentUser.toString())
    }
    
}