package com.despaircorp.data.user

import com.despaircorp.data.utils.CoroutineDispatcherProvider
import com.despaircorp.domain.user.UserRepository
import com.despaircorp.domain.user.model.UserEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class UserRepositoryFirestore @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val dispatcher: CoroutineDispatcherProvider
) : UserRepository {
    
    override suspend fun saveUser(userEntity: UserEntity): Boolean {
        val userDto = UserDto(
            uuid = userEntity.id,
            name = userEntity.name,
            emailAddress = userEntity.email,
            picture = userEntity.photoUrl,
            eating = false,
            hadNotificationOn = true
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
    
    override suspend fun saveNewUserName(userName: String): Boolean = withContext(dispatcher.io) {
        try {
            firestore
                .collection("users")
                .document(auth.currentUser?.uid ?: return@withContext false)
                .update("name", userName)
                .await()
            true
        } catch (e: Exception) {
            coroutineContext.ensureActive()
            e.printStackTrace()
            false
        }
    }
    
    override suspend fun saveNewEmail(email: String): Boolean = withContext(dispatcher.io) {
        try {
            auth.currentUser?.updateEmail(email) ?: return@withContext false

            firestore
                .collection("users")
                .document(auth.currentUser?.uid ?: return@withContext false)
                .update("emailAddress", email)
                .await()
            true
        } catch (e: Exception) {
            coroutineContext.ensureActive()
            e.printStackTrace()
            false
        }
    }

    // TODO Mono dans un autre repo
    override suspend fun saveNewPassword(password: String): Boolean {
        return try {
            auth.currentUser?.updatePassword(password) ?: return false
            true
        } catch (e: Exception) {
            coroutineContext.ensureActive()
            e.printStackTrace()
            false
        }
    }
    
    override suspend fun saveNewNotificationReceivingState(newState: Boolean): Boolean {
        return try {
            firestore
                .collection("users")
                .document(auth.currentUser?.uid ?: return false)
                .update("hadNotificationOn", newState)
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
                val userDto = try {
                    documentSnapshot?.toObject<UserDto>()
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
                
                trySend(
                    UserEntity(
                        id = userDto?.uuid ?: return@addSnapshotListener,
                        name = userDto.name ?: return@addSnapshotListener,
                        email = userDto.emailAddress ?: return@addSnapshotListener,
                        photoUrl = userDto.picture,
                        eating = userDto.eating ?: return@addSnapshotListener,
                        hadNotificationOn = userDto.hadNotificationOn ?: return@addSnapshotListener,
                    )
                )
                
                exception?.printStackTrace()
            }
        
        awaitClose { registration.remove() }
    }
}