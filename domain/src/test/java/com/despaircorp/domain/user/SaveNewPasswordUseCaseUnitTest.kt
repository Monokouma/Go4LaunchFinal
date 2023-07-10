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

class SaveNewPasswordUseCaseUnitTest {
    
    companion object {
        const val NEW_DEFAULT_PASSWORD = "NEW_DEFAULT_PASSWORD"
    }
    
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    private val userRepository: UserRepository = mockk()
    
    private val saveNewPasswordUseCase = SaveNewPasswordUseCase(
        userRepository = userRepository,
    )
    
    @Before
    fun setup() {
        coEvery { userRepository.saveNewPassword(NEW_DEFAULT_PASSWORD) } returns true
    }
    
    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        val result = saveNewPasswordUseCase.invoke(NEW_DEFAULT_PASSWORD)
        assertThat(result).isEqualTo(true)
        
        coVerify(exactly = 1) {
            userRepository.saveNewPassword(NEW_DEFAULT_PASSWORD)
        }
        
        confirmVerified(userRepository)
    }
    
    @Test
    fun `nominal case - returning false`() = testCoroutineRule.runTest {
        coEvery { userRepository.saveNewPassword(NEW_DEFAULT_PASSWORD) } returns false
        
        val result = saveNewPasswordUseCase.invoke(NEW_DEFAULT_PASSWORD)
        assertThat(result).isEqualTo(false)
        
        coVerify(exactly = 1) {
            userRepository.saveNewPassword(NEW_DEFAULT_PASSWORD)
        }
        
        confirmVerified(userRepository)
    }
}