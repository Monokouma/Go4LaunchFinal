package com.despaircorp.data.coworkers

import android.util.Log
import com.despaircorp.data.user.UserDto
import com.despaircorp.domain.coworkers.CoworkersRepository
import com.despaircorp.domain.user.model.UserEntity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class CoworkersFirebaseRepository @Inject constructor(
    private val firestore: FirebaseFirestore
): CoworkersRepository {
    override fun getCoworkers() {
        Log.i("Monokouma", ",rkrkkr")
    }
    
}