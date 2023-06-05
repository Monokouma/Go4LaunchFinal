package com.despaircorp.data.user

import com.despaircorp.data.utils.TestCoroutineRule
import com.despaircorp.domain.user.model.UserEntity
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UserRepositoryFirestoreUnitTest {
    companion object {
        private const val DEFAULT_ID = "DEFAULT_ID"
        private const val DEFAULT_NAME = "DEFAULT_NAME"
        private const val DEFAULT_EMAIL = "DEFAULT_EMAIL"
        private const val DEFAULT_PHOTO_URL = "DEFAULT_PHOTO_URL"
    }
    
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    private val firestore: FirebaseFirestore = mockk()
    
    private val userRepositoryFirestore = UserRepositoryFirestore(
        firestore,
    )
    
    @Before
    fun setup() {

    }
    
    @Test
    fun `nominal case - get user`() = testCoroutineRule.runTest {
        val result = userRepositoryFirestore.getUser(provideUserEntity().id)
        println(result)
        
    }
    
    //Region out
    private fun provideUserEntity() = UserEntity(
        id = DEFAULT_ID,
        name = DEFAULT_NAME,
        email = DEFAULT_EMAIL,
        photoUrl = DEFAULT_PHOTO_URL,
        isEating = false
    )
    
    private fun provideUserDto() = UserDto(
        uuid = DEFAULT_ID,
        name = DEFAULT_NAME,
        emailAddress = DEFAULT_EMAIL,
        picture = DEFAULT_PHOTO_URL,
        isEating = false
    )
    //end Region out
}