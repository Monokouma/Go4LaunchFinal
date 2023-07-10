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


class SaveNewEmailAddressUseCaseUnitTest {
    
    companion object {
        const val NEW_DEFAULT_EMAIL = "NEW_DEFAULT_EMAIL"
    }
    
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    private val userRepository: UserRepository = mockk()
    
    private val saveNewEmailAddressCase = SaveNewEmailAddressUseCase(
        userRepository = userRepository,
    )
    
    @Before
    fun setup() {
        coEvery { userRepository.saveNewEmail(NEW_DEFAULT_EMAIL) } returns true
    }
    
    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        val result = saveNewEmailAddressCase.invoke(NEW_DEFAULT_EMAIL)
        assertThat(result).isEqualTo(true)
        
        coVerify(exactly = 1) {
            userRepository.saveNewEmail(NEW_DEFAULT_EMAIL)
        }
        
        confirmVerified(userRepository)
    }
    
    @Test
    fun `nominal case - returning false`() = testCoroutineRule.runTest {
        coEvery { userRepository.saveNewEmail(NEW_DEFAULT_EMAIL) } returns false
        
        val result = saveNewEmailAddressCase.invoke(NEW_DEFAULT_EMAIL)
        assertThat(result).isEqualTo(false)
        
        coVerify(exactly = 1) {
            userRepository.saveNewEmail(NEW_DEFAULT_EMAIL)
        }
        
        confirmVerified(userRepository)
    }
}
