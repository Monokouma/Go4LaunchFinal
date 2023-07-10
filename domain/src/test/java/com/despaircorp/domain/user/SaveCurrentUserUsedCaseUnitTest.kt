package com.despaircorp.domain.user

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.despaircorp.domain.authentication.AuthenticationRepository
import com.despaircorp.domain.authentication.model.AuthenticatedUser
import com.despaircorp.domain.user.model.UserEntity
import com.despaircorp.domain.utils.TestCoroutineRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SaveCurrentUserUsedCaseUnitTest {
    
    companion object {
        private const val DEFAULT_ID = "DEFAULT_ID"
        private const val DEFAULT_NAME = "DEFAULT_NAME"
        private const val DEFAULT_EMAIL = "DEFAULT_EMAIL"
        private const val DEFAULT_PHOTO_URL = "DEFAULT_PHOTO_URL"
    }
    
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    private val userRepository: UserRepository = mockk()
    private val authenticationRepository: AuthenticationRepository = mockk()
    
    private val saveCurrentUserUseCase = SaveCurrentUserUseCase(
        authenticationRepository,
        userRepository
    )
    
    @Before
    fun setup() {
        coEvery { authenticationRepository.getUser() } returns provideAuthenticatedUser()
        coEvery { userRepository.saveUser(provideUserEntity()) } returns true
    }
    
    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        //When
        val result = saveCurrentUserUseCase.invoke()
        
        //Then
        assertThat(result).isEqualTo(true)
        coVerify(exactly = 1) {
            userRepository.saveUser(provideUserEntity())
            authenticationRepository.getUser()
        }
        
        confirmVerified(userRepository, authenticationRepository)
    }
    
    @Test
    fun `edge case - null authenticated user`() = testCoroutineRule.runTest {
        //Given
        coEvery { authenticationRepository.getUser() } returns null
        
        //When
        val result = saveCurrentUserUseCase.invoke()
        
        //Then
        assertThat(result).isEqualTo(false)
        coVerify(exactly = 1) {
            authenticationRepository.getUser()
        }
        
        confirmVerified(userRepository, authenticationRepository)
    }
    
    //region IN
    private fun provideAuthenticatedUser() = AuthenticatedUser(
        id = DEFAULT_ID,
        name = DEFAULT_NAME,
        email = DEFAULT_EMAIL,
        photoUrl = DEFAULT_PHOTO_URL
    )
    
    private fun provideUserEntity() = UserEntity(
        id = DEFAULT_ID,
        name = DEFAULT_NAME,
        email = DEFAULT_EMAIL,
        photoUrl = DEFAULT_PHOTO_URL,
        eating = false,
        hadNotificationOn = true
    )
    //end region IN
}