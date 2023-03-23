package com.despaircorp.data.user

import com.despaircorp.domain.authentication.model.UserEntity
import com.despaircorp.domain.user.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ensureActive
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
}