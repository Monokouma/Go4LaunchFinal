package com.despaircorp.data.user

import com.despaircorp.domain.user.UserRepository
import com.despaircorp.domain.user.model.UserEntity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class UserRepositoryFirestore @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserRepository {
    override suspend fun saveUser(userEntity: UserEntity): Boolean {
        val userDto = UserDto(
            uuid = userEntity.id,
            name = userEntity.name,
            emailAddress = userEntity.email,
            picture = userEntity.photoUrl,
            isEating = false
        )

        return try {
            firestore
                .collection("users")
                .document(userEntity.id)
                .set(userDto)
                .await()
            true
        } catch (e: Exception) {
            coroutineContext.ensureActive()
            e.printStackTrace()
            false
        }
    }

    override fun getUser(uuid: String): Flow<UserEntity?> = callbackFlow {
        val registration = firestore.collection("users")
            .document(uuid)
            .addSnapshotListener { documentSnapshot, exception ->
                if (documentSnapshot != null) {
                    try {
                        val userDto = documentSnapshot.toObject<UserDto>()

                        trySend(
                            UserEntity(
                                id = userDto?.uuid ?: return@addSnapshotListener,
                                name = userDto.name ?: return@addSnapshotListener,
                                email = userDto.emailAddress ?: return@addSnapshotListener,
                                photoUrl = userDto.picture,
                                isEating = false
                            )
                        )
                    } catch (e: Exception) {
                        coroutineContext.ensureActive()
                        e.printStackTrace()
                    }
                }

                exception?.printStackTrace()
            }

        awaitClose { registration.remove() }
    }
}