package com.despaircorp.ui.login

import android.util.Log
import android.widget.Toast
import androidx.annotation.Keep
import androidx.annotation.Nullable
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.despaircorp.domain.user.SaveCurrentUserUseCase
import com.despaircorp.ui.bottom_navigation.BottomNavigationActivity
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Rule
import kotlin.random.Random
import kotlin.random.nextUInt


class LoginViewModelTest {
    
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val saveCurrentUserUseCase: SaveCurrentUserUseCase = mockk()
    
    private val viewModel = LoginViewModel(
        saveCurrentUserUseCase,
        testCoroutineRule.getTestCoroutineDispatcherProvider()
    )
    
    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        coEvery { saveCurrentUserUseCase.invoke() } returns true
        viewModel.onUserConnected()
        viewModel.loginViewActionLiveData.observeForTesting(this) {
            assertThat(it.value?.getContentIfNotHandled()).isEqualTo(LoginAction.GoToMainActivity)
        }
    }
    
    @Test
    fun `auth case fail`() = testCoroutineRule.runTest {
        coEvery { saveCurrentUserUseCase.invoke() } returns false
        viewModel.onUserConnected()
        viewModel.loginViewActionLiveData.observeForTesting(this) {
            assertThat(it.value?.getContentIfNotHandled()).isEqualTo(LoginAction.ErrorMessage)
        }
    }
}