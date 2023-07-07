package com.despaircorp.ui.settings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import assertk.assertThat
import com.despaircorp.domain.user.GetUserFlowUseCase
import com.despaircorp.domain.user.SaveNewEmailAddressIUseCase
import com.despaircorp.domain.user.SaveNewPasswordUsedCase
import com.despaircorp.domain.user.SaveNewUserNameUseCase
import com.despaircorp.domain.user.SaveNotificationReceivingStateUseCase
import com.despaircorp.domain.user.model.UserEntity
import com.despaircorp.ui.utils.TestCoroutineRule
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
    
    private val userSettingsViewModel: UserSettingsViewModel = UserSettingsViewModel(
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
    }
    
    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        getUserFlowUseCase.invoke().collect {
            assertThat(it).equals(provideUserEntity())
        }
    }
    
    //Region OUT
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