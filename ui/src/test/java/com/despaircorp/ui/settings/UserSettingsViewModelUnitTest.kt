package com.despaircorp.ui.settings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.despaircorp.domain.user.GetUserFlowUseCase
import com.despaircorp.domain.user.SaveNewEmailAddressIUseCase
import com.despaircorp.domain.user.SaveNewPasswordUsedCase
import com.despaircorp.domain.user.SaveNewUserNameUseCase
import com.despaircorp.domain.user.SaveNotificationReceivingStateUseCase
import com.despaircorp.domain.user.model.UserEntity
import com.despaircorp.ui.utils.TestCoroutineRule
import com.despaircorp.ui.utils.observeForTesting
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UserSettingsViewModelUnitTest {
    
    companion object {
        private const val DEFAULT_ID = "DEFAULT_ID"
        private const val DEFAULT_NAME = "DEFAULT_NAME"
        private const val DEFAULT_EMAIL = "DEFAULT_EMAIL"
        private const val DEFAULT_PHOTO_URL = "DEFAULT_PHOTO_URL"
        private const val DEFAULT_IS_USER_ENABLED_NOTIF = true
        private const val DEFAULT_PASSWORD = "DEFAULT_PASSWORD"
    }
    
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    
    private val getUserFlowUseCase: GetUserFlowUseCase = mockk()
    private val saveNewUserNameUseCase: SaveNewUserNameUseCase = mockk()
    private val saveNewEmailAddressIUseCase: SaveNewEmailAddressIUseCase = mockk()
    private val saveNewPasswordUsedCase: SaveNewPasswordUsedCase = mockk()
    private val saveNotificationReceivingStateUseCase: SaveNotificationReceivingStateUseCase =
        mockk()
    
    private val viewModel: UserSettingsViewModel = UserSettingsViewModel(
        getUserFlowUseCase = getUserFlowUseCase,
        saveNewUserNameUseCase = saveNewUserNameUseCase,
        saveNewEmailAddressIUseCase = saveNewEmailAddressIUseCase,
        saveNewPasswordUsedCase = saveNewPasswordUsedCase,
        dispatcherProvider = testCoroutineRule.getTestCoroutineDispatcherProvider(),
        saveNotificationReceivingStateUseCase = saveNotificationReceivingStateUseCase,
    )
    
    @Before
    fun setup() {
        coEvery { getUserFlowUseCase.invoke() } returns flowOf(provideUserEntity())
        coEvery { saveNewUserNameUseCase.invoke(DEFAULT_NAME) } returns true
        coEvery { saveNewEmailAddressIUseCase.invoke(DEFAULT_EMAIL) } returns true
        coEvery { saveNewPasswordUsedCase.invoke(DEFAULT_PASSWORD) } returns true
    }
    
    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        viewModel.viewState.observeForTesting(this) {
            assertThat(it.value).isEqualTo(provideUserSettingsViewState())
        }
    }
    
    @Test
    fun `user modified is username should trigger action`() = testCoroutineRule.runTest {
        //Given
        viewModel.onUsernameTextChanged(DEFAULT_NAME)
        
        //When
        viewModel.onUsernameEditEndClicked()
        
        //Then
        viewModel.viewAction.observeForTesting(this) {
            assertThat(it.value?.getContentIfNotHandled()).isEqualTo(UserSettingsAction.UsernameSucess)
        }
    }
    
    @Test
    fun `user modified is email should trigger action`() = testCoroutineRule.runTest {
        //Given
        viewModel.onEmailTextChanged(DEFAULT_EMAIL)
        
        //When
        viewModel.onEmailEditEndClicked()
        
        //Then
        viewModel.viewAction.observeForTesting(this) {
            assertThat(it.value?.getContentIfNotHandled()).isEqualTo(UserSettingsAction.EmailSucess)
        }
    }
    
    @Test
    fun `user modified is password should trigger action`() = testCoroutineRule.runTest {
        //Given
        viewModel.onPasswordTextChanged(DEFAULT_PASSWORD)
        
        //When
        viewModel.onPasswordEditEndClicked()
        
        //Then
        viewModel.viewAction.observeForTesting(this) {
            assertThat(it.value?.getContentIfNotHandled()).isEqualTo(UserSettingsAction.PasswordSucess)
        }
    }
    
    @Test
    fun `user modified is username should trigger error`() = testCoroutineRule.runTest {
        //Given
        viewModel.onUsernameTextChanged("")
        
        //When
        viewModel.onUsernameEditEndClicked()
        
        //Then
        viewModel.viewAction.observeForTesting(this) {
            assertThat(it.value?.getContentIfNotHandled()).isEqualTo(UserSettingsAction.UsernameError)
        }
    }
    
    @Test
    fun `user modified is email should trigger error`() = testCoroutineRule.runTest {
        //Given
        viewModel.onEmailTextChanged("")
        
        //When
        viewModel.onEmailEditEndClicked()
        
        //Then
        viewModel.viewAction.observeForTesting(this) {
            assertThat(it.value?.getContentIfNotHandled()).isEqualTo(UserSettingsAction.EmailError)
        }
    }
    
    @Test
    fun `user modified is password should trigger error`() = testCoroutineRule.runTest {
        //Given
        viewModel.onPasswordTextChanged("")
        
        //When
        viewModel.onPasswordEditEndClicked()
        
        //Then
        viewModel.viewAction.observeForTesting(this) {
            assertThat(it.value?.getContentIfNotHandled()).isEqualTo(UserSettingsAction.PasswordError)
        }
    }
    
    //Region OUT
    private fun provideUserSettingsViewState() = UserSettingsViewState(
        isNotificationEnabled = DEFAULT_IS_USER_ENABLED_NOTIF
    )
    
    private fun provideUserEntity() = UserEntity(
        id = DEFAULT_ID,
        name = DEFAULT_NAME,
        email = DEFAULT_EMAIL,
        photoUrl = DEFAULT_PHOTO_URL,
        eating = false,
        hadNotificationOn = true
    )
    
    
    //End Region OUT
}