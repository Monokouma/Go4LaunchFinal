package com.despaircorp.data.user

import android.util.Log
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
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class UserRepositoryFirestore @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
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
    
    override suspend fun saveNewUserName(userName: String): Boolean {
        return try {
            firestore
                .collection("users")
                .document(auth.currentUser?.uid ?: return false)
                .update("name", userName)
                .await()
            true
        } catch (e: Exception) {
            coroutineContext.ensureActive()
            e.printStackTrace()
            false
        }
    }
    
    override suspend fun saveNewEmail(email: String): Boolean {
        return try {
            
            auth.currentUser?.updateEmail(email) ?: return false
            
            firestore
                .collection("users")
                .document(auth.currentUser?.uid ?: return false)
                .update("emailAddress", email)
                .await()
            true
        } catch (e: Exception) {
            coroutineContext.ensureActive()
            e.printStackTrace()
            false
        }
    }
    
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
                if (documentSnapshot != null) {
                    try {
                        val userDto = documentSnapshot.toObject<UserDto>()
                        Log.i("Monokouma", userDto.toString())
                        trySend(
                            UserEntity(
                                id = userDto?.uuid ?: return@addSnapshotListener,
                                name = userDto.name ?: return@addSnapshotListener,
                                email = userDto.emailAddress ?: return@addSnapshotListener,
                                photoUrl = userDto.picture,
                                eating = userDto.eating ?: return@addSnapshotListener,
                                hadNotificationOn = userDto.hadNotificationOn
                                    ?: return@addSnapshotListener
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