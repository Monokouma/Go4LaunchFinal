package com.despaircorp.data.user

import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.despaircorp.data.utils.TestCoroutineRule
import com.despaircorp.domain.user.model.UserEntity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runCurrent
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UserRepositoryFirestoreUnitTest {
    companion object {
        private const val DEFAULT_ID = "DEFAULT_ID"
        private const val DEFAULT_NAME = "DEFAULT_NAME"
        private const val DEFAULT_EMAIL = "DEFAULT_EMAIL"
        private const val DEFAULT_PHOTO_URL = "DEFAULT_PHOTO_URL"
        private const val DEFAULT_USERNAME = "DEFAULT_USERNAME"
        private const val DEFAULT_PASSWORD = "DEFAULT_PASSWORD"
        private const val DEFAULT_NOTIFICATION_STATE = true

    }

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val firestore: FirebaseFirestore = mockk()
    private val auth: FirebaseAuth = mockk()

    private val userRepositoryFirestore = UserRepositoryFirestore(
        firestore,
        auth,
        testCoroutineRule.getTestCoroutineDispatcherProvider()
    )

    @Before
    fun setup() {
        coEvery {
            firestore
                .collection("users")
                .document(DEFAULT_ID)
                .set(provideUserDto())
        } returns getDefaultSetUserTask()

        coEvery { auth.currentUser?.uid } returns DEFAULT_ID

        coEvery { auth.currentUser?.updateEmail(DEFAULT_EMAIL) } returns getDefaultSetUserTask()

        coEvery { auth.currentUser?.updatePassword(DEFAULT_PASSWORD) } returns getDefaultSetUserTask()

        coEvery {
            firestore
                .collection("users")
                .document(DEFAULT_ID)
                .update("name", DEFAULT_USERNAME)
        } returns getDefaultSetUserTask()

        coEvery {
            firestore
                .collection("users")
                .document(DEFAULT_ID)
                .update("emailAddress", DEFAULT_EMAIL)
        } returns getDefaultSetUserTask()

        coEvery {
            firestore
                .collection("users")
                .document(DEFAULT_ID)
                .update("hadNotificationOn", DEFAULT_NOTIFICATION_STATE)
        } returns getDefaultSetUserTask()
    }

    // region
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
    fun `user change is username with success`() = testCoroutineRule.runTest {
        val result = userRepositoryFirestore.saveNewUserName(DEFAULT_USERNAME)

        assertThat(result).isTrue()

        coVerify(exactly = 1) {
            firestore
                .collection("users")
                .document(DEFAULT_ID)
                .update("name", DEFAULT_USERNAME)
        }
        confirmVerified(firestore)
    }

    @Test
    fun `user change is email with success`() = testCoroutineRule.runTest {
        val result = userRepositoryFirestore.saveNewEmail(DEFAULT_EMAIL)

        assertThat(result).isTrue()

        coVerify(exactly = 1) {
            firestore
                .collection("users")
                .document(DEFAULT_ID)
                .update("emailAddress", DEFAULT_EMAIL)


        }

        confirmVerified(firestore)
    }

    @Test
    fun `user change is password with success`() = testCoroutineRule.runTest {
        val result = userRepositoryFirestore.saveNewPassword(DEFAULT_PASSWORD)

        assertThat(result).isTrue()

        coVerify(exactly = 1) {
            auth.currentUser?.updatePassword(DEFAULT_PASSWORD)
        }

        confirmVerified(auth)
    }

    @Test
    fun `user change is notification test with success`() = testCoroutineRule.runTest {
        val result = userRepositoryFirestore.saveNewNotificationReceivingState(
            DEFAULT_NOTIFICATION_STATE
        )

        assertThat(result).isTrue()

        coVerify(exactly = 1) {
            firestore
                .collection("users")
                .document(DEFAULT_ID)
                .update("hadNotificationOn", DEFAULT_NOTIFICATION_STATE)
        }
        confirmVerified(firestore)
    }

    @Test
    fun `user change is username with failure`() = testCoroutineRule.runTest {
        // Given
        every {
            firestore
                .collection("users")
                .document(DEFAULT_ID)
                .update("name", DEFAULT_USERNAME)
        } throws CancellationException()

        // When
        val result = userRepositoryFirestore.saveNewUserName(DEFAULT_USERNAME)

        // Then
        assertThat(result).isFalse()
    }

    @Test
    fun `user change while user is disconnected`() = testCoroutineRule.runTest {
        // Given
        every { auth.currentUser } returns null

        // When
        val result = userRepositoryFirestore.saveNewUserName(DEFAULT_USERNAME)

        // Then
        assertThat(result).isFalse()
    }

    @Test
    fun `user change while user doesn't have an uid`() = testCoroutineRule.runTest {
        // Given
        every { auth.currentUser?.uid } returns null

        // When
        val result = userRepositoryFirestore.saveNewUserName(DEFAULT_USERNAME)

        // Then
        assertThat(result).isFalse()
    }

    @Test
    fun `user change is email with failure`() = testCoroutineRule.runTest {
        // Given
        coEvery {
            firestore
                .collection("users")
                .document(DEFAULT_ID)
                .update("emailAddress", DEFAULT_EMAIL)
        } throws CancellationException()

        // When
        val result = userRepositoryFirestore.saveNewEmail(DEFAULT_EMAIL)

        // Then
        assertThat(result).isFalse()
    }

    @Test
    fun `user change is password with failure`() = testCoroutineRule.runTest {
        coEvery {
            auth.currentUser?.updatePassword(DEFAULT_PASSWORD)
        } throws CancellationException()

        val result = userRepositoryFirestore.saveNewPassword(DEFAULT_PASSWORD)

        assertThat(result).isFalse()
    }

    @Test
    fun `user change is notification test with failure`() = testCoroutineRule.runTest {
        // Given
        coEvery {
            firestore
                .collection("users")
                .document(DEFAULT_ID)
                .update("hadNotificationOn", DEFAULT_NOTIFICATION_STATE)
        } throws CancellationException()

        // When
        val result = userRepositoryFirestore.saveNewNotificationReceivingState(
            DEFAULT_NOTIFICATION_STATE
        )

        // Then
        assertThat(result).isFalse()
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
    //endregion

    @Test
    fun `nominal case - get user`() = testCoroutineRule.runTest {
        // Given
        val documentSnapshotUserDto = mockk<DocumentSnapshot> {
            every { toObject(UserDto::class.java) } returns provideUserDto()
        }

        runTestForGetUser(documentSnapshotUserDto) {
            val userEntity = awaitItem()
            cancel()

            assertEquals(provideUserEntity(), userEntity)
            verify(exactly = 1) {
                firestore.collection("users")
                    .document(DEFAULT_ID)
                    .addSnapshotListener(any())
            }
        }
    }

    @Test
    fun `error case - request exception`() = testCoroutineRule.runTest {
        runTestForGetUser(documentSnapshotUserDto = null) {
            expectNoEvents()
            cancel()
        }
    }

    @Test
    fun `error case - parsing exception`() = testCoroutineRule.runTest {
        // Given
        val documentSnapshotUserDto = mockk<DocumentSnapshot> {
            every { toObject(UserDto::class.java) } throws Exception()
        }

        runTestForGetUser(documentSnapshotUserDto) {
            expectNoEvents()
            cancel()
        }
    }

    private suspend fun TestScope.runTestForGetUser(
        documentSnapshotUserDto: DocumentSnapshot?,
        assertBlock: suspend ReceiveTurbine<UserEntity?>.() -> Unit
    ) {
        val listenerRegistration = mockk<ListenerRegistration>()
        justRun { listenerRegistration.remove() }

        val slot = slot<EventListener<DocumentSnapshot>>()
        every {
            firestore.collection("users")
                .document(DEFAULT_ID)
                .addSnapshotListener(capture(slot))
        } returns listenerRegistration

        userRepositoryFirestore.getUser(DEFAULT_ID).test {
            runCurrent()

            slot.captured.onEvent(documentSnapshotUserDto, null)

            assertBlock()

            verify(exactly = 1) {
                listenerRegistration.remove()
            }
        }
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