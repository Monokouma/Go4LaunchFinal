package com.despaircorp.data.user

import com.despaircorp.domain.authentication.model.UserEntity
import com.despaircorp.domain.user.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.tasks.await
import java.util.concurrent.Executor
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
            picture = userEntity.photoUrl
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
    
    override suspend fun getUser(uuid: String): UserEntity? {
        val documentSnapshot = firestore.collection("users")
            .document(uuid)
            .get()
            .await()
    
        val userDto = documentSnapshot.toObject<UserDto>()
        
        return try {
            UserEntity(
                id = userDto?.uuid ?: return null,
                name = userDto.name ?: return null,
                email = userDto.emailAddress ?: return null,
                photoUrl = userDto.picture,
            )
        } catch (e: Exception) {
            coroutineContext.ensureActive()
            null
        }
        
    }
    
}