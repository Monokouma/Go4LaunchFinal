package com.despaircorp.data.user

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.despaircorp.data.utils.TestCoroutineRule
import com.despaircorp.domain.user.model.UserEntity
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.toObject
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.core.ValueClassSupport.boxedValue
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.collect
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
        coEvery {
            firestore
                .collection("users")
                .document(DEFAULT_ID)
                .set(provideUserDto())
        } returns getDefaultSetUserTask()
        
        coEvery {
            firestore.collection("users")
                .document(DEFAULT_ID)
                .get()
                .result
                .toObject<UserDto>()
        } returns provideUserDto()
    }

    @Test
    fun `nominal case - save user`() = testCoroutineRule.runTest {
        // When
        val result = userRepositoryFirestore.saveUser(provideUserEntity())

        // Then
        assertThat(result).isTrue()
        coVerify(exactly = 1) {
            firestore
                .collection("users")
                .document(DEFAULT_ID)
                .set(provideUserDto())
        }
        confirmVerified(firestore)
    }

    @Test
    fun `error case - save user's coroutine is cancelled during run`() = testCoroutineRule.runTest {
        // Given
        coEvery {
            firestore
                .collection("users")
                .document(DEFAULT_ID)
                .set(provideUserDto())
        } throws CancellationException()

        // When
        val result = userRepositoryFirestore.saveUser(provideUserEntity())

        // Then
        assertThat(result).isFalse()
    }

    @Test
    fun `error case - save user launches an exception`() = testCoroutineRule.runTest {
        // Given
        val exception = Exception()
        coEvery {
            firestore
                .collection("users")
                .document(DEFAULT_ID)
                .set(provideUserDto())
        } throws exception

        // When
        val result = userRepositoryFirestore.saveUser(provideUserEntity())

        // Then
        assertThat(result).isFalse()
    }

    @Test
    fun `error case - save user task launches an exception`() = testCoroutineRule.runTest {
        // Given
        val exception = Exception()
        coEvery {
            firestore
                .collection("users")
                .document(DEFAULT_ID)
                .set(provideUserDto())
        } returns getDefaultSetUserTask {
            every { this@getDefaultSetUserTask.exception } returns exception
        }

        // When
        val result = userRepositoryFirestore.saveUser(provideUserEntity())

        // Then
        assertThat(result).isFalse()
    }

    /*
    @Test
    fun `nominal case - get user`() = testCoroutineRule.runTest {
        val result = userRepositoryFirestore.getUser(DEFAULT_ID)
       
        assertThat(result).isEqualTo(provideUserEntity())
        coVerify(exactly = 1) {
            firestore.collection("users")
                .document(DEFAULT_ID)
                .get()
        }
        confirmVerified(firestore)
    }
     */
    private inline fun getDefaultSetUserTask(crossinline mockkBlock: Task<Void>.() -> Unit = {}): Task<Void> = mockk {
        every { isComplete } returns true
        every { exception } returns null
        every { isCanceled } returns false
        every { result } returns mockk()

        mockkBlock(this)
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