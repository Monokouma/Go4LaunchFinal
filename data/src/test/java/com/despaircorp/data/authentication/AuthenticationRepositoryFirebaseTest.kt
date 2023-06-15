package com.despaircorp.data.authentication

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import assertk.fail
import com.despaircorp.data.utils.TestCoroutineRule
import com.despaircorp.domain.authentication.model.AuthenticatedUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.flow.collect
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

class AuthenticationRepositoryFirebaseTest {
    companion object {
        private const val DEFAULT_UID = "DEFAULT_UID"
        private const val DEFAULT_DISPLAY_NAME = "DEFAULT_DISPLAY_NAME"
        private const val DEFAULT_EMAIL = "DEFAULT_EMAIL"
        private const val DEFAULT_PHOTO_URL = "DEFAULT_PHOTO_URL"
    }

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val firebaseAuth: FirebaseAuth = mockk()
    
    private val authenticationRepositoryFirebase = AuthenticationRepositoryFirebase(
        firebaseAuth = firebaseAuth,
        coroutineDispatcherProvider = testCoroutineRule.getTestCoroutineDispatcherProvider(),
    )

    @Before
    fun setUp() {
        every { firebaseAuth.currentUser } returns getDefaultFirebaseUser()
    }

    @Test
    fun `nominal case - getUser`() = testCoroutineRule.runTest {
        // When
        val result = authenticationRepositoryFirebase.getUser()

        // Then
        assertThat(result).isEqualTo(getDefaultAuthenticatedUser())
    }
    
    @Test
    fun `nominal case - getUser flow`() = testCoroutineRule.runTest {
        val mockAuth = mockk<FirebaseAuth>()
    
        // Capture the AuthStateListener
        val authStateListenerSlot = slot<FirebaseAuth.AuthStateListener>()
    
        // Expect the addAuthStateListener function to be called, and capture the argument
        every { mockAuth.addAuthStateListener(capture(authStateListenerSlot)) } just Runs
    
        authenticationRepositoryFirebase.getUserFlow()
    
        
        // Verify that the mockAuth.addAuthStateListener() function has been called
        verify(exactly = 1) { mockAuth.addAuthStateListener(any()) }
    
        // Now you can invoke the captured AuthStateListener if it was captured
        if (authStateListenerSlot.isCaptured) {
            authStateListenerSlot.captured.onAuthStateChanged(mockAuth)
        } else {
            fail("AuthStateListener was not captured")
        }
    
    }

    @Test
    fun `edge case - getUser returns null because firebaseUser is null`() = testCoroutineRule.runTest {
        // Given
        every { firebaseAuth.currentUser } returns null

        // When
        val result = authenticationRepositoryFirebase.getUser()

        // Then
        assertThat(result).isNull()
    }

    @Test
    fun `edge case - getUser returns null because firebaseUser's displayName is null`() = testCoroutineRule.runTest {
        // Given
        every { firebaseAuth.currentUser } returns getDefaultFirebaseUser(displayName = null)

        // When
        val result = authenticationRepositoryFirebase.getUser()

        // Then
        assertThat(result).isNull()
    }

    @Test
    fun `edge case - getUser returns null because firebaseUser's email is null`() = testCoroutineRule.runTest {
        // Given
        every { firebaseAuth.currentUser } returns getDefaultFirebaseUser(email = null)

        // When
        val result = authenticationRepositoryFirebase.getUser()

        // Then
        assertThat(result).isNull()
    }

    @Test
    fun `edge case - getUser doesn't return photoUrl because firebaseUser's photoUrl is null`() = testCoroutineRule.runTest {
        // Given
        every { firebaseAuth.currentUser } returns getDefaultFirebaseUser(photoUrl = null)

        // When
        val result = authenticationRepositoryFirebase.getUser()

        // Then
        assertThat(result).isEqualTo(
            getDefaultAuthenticatedUser().copy(
                photoUrl = null,
            )
        )
    }

    // region IN
    private fun getDefaultFirebaseUser(
        uid: String = DEFAULT_UID,
        displayName: String? = DEFAULT_DISPLAY_NAME,
        email: String? = DEFAULT_EMAIL,
        photoUrl: String? = DEFAULT_PHOTO_URL,
    ): FirebaseUser = mockk {
        every { this@mockk.uid } returns uid
        every { this@mockk.displayName } returns displayName
        every { this@mockk.email } returns email
        every { this@mockk.photoUrl?.toString() } returns photoUrl
    }
    // endregion IN

    // region OUT
    private fun getDefaultAuthenticatedUser() = AuthenticatedUser(
        id = DEFAULT_UID,
        name = DEFAULT_DISPLAY_NAME,
        email = DEFAULT_EMAIL,
        photoUrl = DEFAULT_PHOTO_URL,
    )
    // endregion OUT
}