package com.despaircorp.data.user

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.despaircorp.data.utils.TestCoroutineRule
import com.despaircorp.domain.user.model.UserEntity
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.toObject
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.core.ValueClassSupport.boxedValue
import io.mockk.every
import io.mockk.just
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.Duration.Companion.seconds

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

    
    @Test
    fun `nominal case - get user`() = testCoroutineRule.runTest {
        // Mock Firestore dependencies
        val mockSnapshot = mockk<DocumentSnapshot>()
        val mockDocumentRef = mockk<DocumentReference>()
        val mockCollectionRef = mockk<CollectionReference>()
        val mockFirestore = mockk<FirebaseFirestore>()
        val mockRegistration = mockk<ListenerRegistration>()
    
        // Mock behavior
        every { mockSnapshot.toObject<UserDto>() } returns UserDto("123", "Alice", "alice@example.com", "picture_url")
        every { mockDocumentRef.addSnapshotListener(any()) } answers {
            val listener = firstArg<(DocumentSnapshot?, Exception?) -> Unit>()
            listener(mockSnapshot, null)
            mockRegistration
        }
        every { mockCollectionRef.document("123") } returns mockDocumentRef
        every { mockFirestore.collection("users") } returns mockCollectionRef
    
        // Test the function
        userRepositoryFirestore.getUser("123").test(timeout = 2.seconds) {
            val userEntity = awaitError()
           println(userEntity.localizedMessage)
            awaitComplete()
        }
    
        // Verify that the document method was called with the expected argument
        coVerify { mockCollectionRef.document("123") }
    
    }
    
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