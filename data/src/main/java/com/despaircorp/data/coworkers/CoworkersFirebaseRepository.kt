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
import javax.inject.Singleton

@Singleton
class CoworkersFirebaseRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : CoworkersRepository {
    override fun getCoworkers(): Flow<List<CoworkerEntity>> = callbackFlow {
        val registration = firestore.collection("users")
            .addSnapshotListener { value, error ->
                error?.printStackTrace()
                value?.documents?.mapNotNull { document ->
                    val coworkersDto = try {
                        document?.toObject<CoworkersDto>()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        null
                    }

                    CoworkerEntity(
                        id = coworkersDto?.uuid ?: return@mapNotNull null,
                        name = coworkersDto.name ?: return@mapNotNull null,
                        email = coworkersDto.emailAddress ?: return@mapNotNull null,
                        photoUrl = coworkersDto.picture,
                        eating = coworkersDto.eating ?: return@mapNotNull null,
                        hadNotificationOn = coworkersDto.hadNotificationOn ?: return@mapNotNull null,
                    )
                }?.let {
                    trySend(it)
                }
            }
        awaitClose { registration.remove() }
    }.flowOn(coroutineDispatcherProvider.io)
}