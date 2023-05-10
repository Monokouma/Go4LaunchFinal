package com.despaircorp.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.despaircorp.domain.user.SaveCurrentUserUseCase
import com.despaircorp.ui.utils.TestCoroutineRule
import com.despaircorp.ui.utils.observeForTesting
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test


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

    @Before
    fun setUp() {
        coEvery { saveCurrentUserUseCase.invoke() } returns true
    }

    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        // When
        viewModel.onUserConnected()

        // Then
        viewModel.loginViewActionLiveData.observeForTesting(this) {
            assertThat(it.value?.getContentIfNotHandled()).isEqualTo(LoginAction.GoToMainActivity)
        }
    }
    
    @Test
    fun `auth case fail`() = testCoroutineRule.runTest {
        // Given
        coEvery { saveCurrentUserUseCase.invoke() } returns false

        // When
        viewModel.onUserConnected()

        // Then
        viewModel.loginViewActionLiveData.observeForTesting(this) {
            assertThat(it.value?.getContentIfNotHandled()).isEqualTo(LoginAction.ErrorMessage)
        }
    }
}