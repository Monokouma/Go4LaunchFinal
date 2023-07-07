package com.despaircorp.data.user

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.despaircorp.data.utils.TestCoroutineRule
import com.despaircorp.domain.user.model.UserEntity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import kotlinx.coroutines.CancellationException
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Ignore
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
    private val auth: FirebaseAuth = mockk()
    
    private val userRepositoryFirestore = UserRepositoryFirestore(
        firestore,
        auth
    )
    
    @Before
    fun setup() {
        coEvery {
            firestore
                .collection("users")
                .document(DEFAULT_ID)
                .set(provideUserDto())
        } returns getDefaultSetUserTask()
        
        mockkStatic(FirebaseFirestore::class)
        mockkStatic(DocumentSnapshot::class)
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
    
    
    @Ignore
    @Test
    fun `nominal case - get user`() = testCoroutineRule.runTest {
        // Mocking dependencies
        val collectionReference: CollectionReference = mockk()
        val documentReference: DocumentReference = mockk()
        val documentSnapshot: DocumentSnapshot = mockk()
        
        // Mock UserDto
        val userDto = UserDto(DEFAULT_ID, DEFAULT_NAME, DEFAULT_EMAIL, DEFAULT_PHOTO_URL, false)
        
        val eventListenerSlot = slot<EventListener<DocumentSnapshot>>()
        justRun {
            documentReference.addSnapshotListener(capture(eventListenerSlot))
        }
        every { documentSnapshot.toObject<UserDto>() } returns userDto
        every { documentSnapshot.exists() } returns true
        every { firestore.collection("users") } returns collectionReference
        every { collectionReference.document(any()) } returns documentReference
        
        // Simulate the snapshot listener
        eventListenerSlot.captured.onEvent(documentSnapshot, null)
        
        // Calling the function to be tested
        val flow = userRepositoryFirestore.getUser(DEFAULT_ID)
        
        // Use turbine to test emissions
        flow.test {
            val userEntity = awaitItem()
            assertEquals(DEFAULT_ID, userEntity?.id)
            assertEquals(DEFAULT_NAME, userEntity?.name)
            assertEquals(DEFAULT_EMAIL, userEntity?.email)
            assertEquals(DEFAULT_PHOTO_URL, userEntity?.photoUrl)
            assertEquals(false, userEntity?.eating)
            awaitComplete()
        }
        
        // Verifying the interactions
        coVerify { documentReference.addSnapshotListener(any()) }
        
    }
    
    private inline fun getDefaultSetUserTask(crossinline mockkBlock: Task<Void>.() -> Unit = {}): Task<Void> =
        mockk {
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
        eating = false,
        hadNotificationOn = true
    )
    
    private fun provideUserDto() = UserDto(
        uuid = DEFAULT_ID,
        name = DEFAULT_NAME,
        emailAddress = DEFAULT_EMAIL,
        picture = DEFAULT_PHOTO_URL,
        eating = false,
        hadNotificationOn = true
    )
    //end Region out
}