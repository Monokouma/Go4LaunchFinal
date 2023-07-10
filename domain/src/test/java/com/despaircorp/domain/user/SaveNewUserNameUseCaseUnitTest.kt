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

class SaveNewUserNameUseCaseUnitTest {
    
    companion object {
        const val NEW_DEFAULT_USERNAME = "NEW_DEFAULT_USERNAME"
    }
    
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    private val userRepository: UserRepository = mockk()
    
    private val saveNewUserNameUseCase = SaveNewUserNameUseCase(
        userRepository = userRepository,
    )
    
    @Before
    fun setup() {
        coEvery { userRepository.saveNewUserName(NEW_DEFAULT_USERNAME) } returns true
    }
    
    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        val result = saveNewUserNameUseCase.invoke(NEW_DEFAULT_USERNAME)
        assertThat(result).isEqualTo(true)
        
        coVerify(exactly = 1) {
            userRepository.saveNewUserName(NEW_DEFAULT_USERNAME)
        }
        
        confirmVerified(userRepository)
    }
    
    @Test
    fun `nominal case - returning false`() = testCoroutineRule.runTest {
        coEvery { userRepository.saveNewUserName(NEW_DEFAULT_USERNAME) } returns false
        
        val result = saveNewUserNameUseCase.invoke(NEW_DEFAULT_USERNAME)
        assertThat(result).isEqualTo(false)
        
        coVerify(exactly = 1) {
            userRepository.saveNewUserName(NEW_DEFAULT_USERNAME)
        }
        
        confirmVerified(userRepository)
    }
}