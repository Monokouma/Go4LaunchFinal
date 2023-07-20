package com.despaircorp.data.coworkers

import com.despaircorp.data.utils.CoroutineDispatcherProvider
import com.despaircorp.domain.coworkers.CoworkersRepository
import com.despaircorp.domain.coworkers.model.CoworkerEntity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CoworkersFirebaseRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : CoworkersRepository {
    override fun getCoworkers(): Flow<List<CoworkerEntity>> = callbackFlow {
        val coworkerEntity = mutableListOf<CoworkerEntity>()
        
        val registration = firestore.collection("users")
            .get()
            .addOnSuccessListener { documents ->
                documents.forEach { document ->
                    
                    val coworkersDto = try {
                        document?.toObject<CoworkersDto>()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        null
                    }
                    
                    coworkerEntity.add(
                        CoworkerEntity(
                            id = coworkersDto?.uuid ?: return@addOnSuccessListener,
                            name = coworkersDto.name ?: return@addOnSuccessListener,
                            email = coworkersDto.emailAddress ?: return@addOnSuccessListener,
                            photoUrl = coworkersDto.picture,
                            eating = coworkersDto.eating ?: return@addOnSuccessListener,
                            hadNotificationOn = coworkersDto.hadNotificationOn
                                ?: return@addOnSuccessListener,
                        )
                    )
                    
                }
                trySend(
                    coworkerEntity
                )
            }
        awaitClose { registration }
        
    }.flowOn(coroutineDispatcherProvider.io)
    
}