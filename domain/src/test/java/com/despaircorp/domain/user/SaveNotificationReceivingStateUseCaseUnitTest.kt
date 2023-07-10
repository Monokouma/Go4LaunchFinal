package com.despaircorp.domain.user

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.despaircorp.domain.utils.TestCoroutineRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SaveNotificationReceivingStateUseCaseUnitTest {
    
    companion object {
        const val NEW_DEFAULT_NOTIFICATION_TEST = true
    }
    
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    private val userRepository: UserRepository = mockk()
    
    private val saveNotificationReceivingStateUseCase = SaveNotificationReceivingStateUseCase(
        userRepository = userRepository,
    )
    
    @Before
    fun setup() {
        coEvery { userRepository.saveNewNotificationReceivingState(NEW_DEFAULT_NOTIFICATION_TEST) } returns true
    }
    
    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        val result = saveNotificationReceivingStateUseCase.invoke(NEW_DEFAULT_NOTIFICATION_TEST)
        assertThat(result).isEqualTo(true)
        
        coVerify(exactly = 1) {
            userRepository.saveNewNotificationReceivingState(NEW_DEFAULT_NOTIFICATION_TEST)
        }
        
        confirmVerified(userRepository)
    }
    
    @Test
    fun `nominal case - returning false`() = testCoroutineRule.runTest {
        coEvery { userRepository.saveNewNotificationReceivingState(NEW_DEFAULT_NOTIFICATION_TEST) } returns false
        
        val result = saveNotificationReceivingStateUseCase.invoke(NEW_DEFAULT_NOTIFICATION_TEST)
        assertThat(result).isEqualTo(false)
        
        coVerify(exactly = 1) {
            userRepository.saveNewNotificationReceivingState(NEW_DEFAULT_NOTIFICATION_TEST)
        }
        
        confirmVerified(userRepository)
    }
}