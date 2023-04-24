package com.despaircorp.ui.login

import android.util.Log
import androidx.annotation.Keep
import androidx.annotation.Nullable
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.despaircorp.domain.user.SaveCurrentUserUseCase
import com.despaircorp.ui.utils.Event
import com.despaircorp.ui.utils.TestCoroutineRule
import com.despaircorp.ui.utils.observeForTesting
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.core.ValueClassSupport.boxedValue
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Rule


class LoginViewModelTest {
    
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    private val firebaseAuthWrapper = mockk<FirebaseAuthWrapper>()
    private val saveCurrentUserUseCase: SaveCurrentUserUseCase = mockk()
    private val loginViewModel = LoginViewModel(
        saveCurrentUserUseCase,
        testCoroutineRule.getTestCoroutineDispatcherProvider()
    )
    
    @Before
    fun setup() {
        coJustRun { saveCurrentUserUseCase.invoke() }
    }
    
    @Test
    fun  isUserAuthenticated() {
        //Given
        val fakeUser = mockk<FirebaseUser>()
        every { firebaseAuthWrapper.currentUser() } returns fakeUser
        
        //When
        val user = firebaseAuthWrapper.currentUser()
        
        //Then
        assertNotNull(user)
    }
    
    @Test
    fun isUserNotAuthenticated() {
        //Given
        every { firebaseAuthWrapper.currentUser() } returns null
        
        //When
        val user = firebaseAuthWrapper.currentUser()
        
        //Then
        assertNull(user)
    }
    
    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun userNotAuthShouldReturnErrorAction() = testCoroutineRule.runTest {
        coEvery { saveCurrentUserUseCase.invoke() } returns false
        
        loginViewModel.loginViewActionLiveData.observeForTesting(this) {
            println(it.value?.boxedValue)
        }
    }
    
}