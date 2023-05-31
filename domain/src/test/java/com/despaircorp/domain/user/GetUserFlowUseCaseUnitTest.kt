package com.despaircorp.domain.user

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import com.despaircorp.domain.authentication.AuthenticationRepository
import com.despaircorp.domain.authentication.model.AuthenticatedUser
import com.despaircorp.domain.user.model.UserEntity
import com.despaircorp.domain.utils.TestCoroutineRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetUserFlowUseCaseUnitTest {

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

    private val getUserFlowUseCase = GetUserFlowUseCase(
        userRepository,
        authenticationRepository,
    )

    @Before
    fun setup() {
        coEvery { authenticationRepository.getUserFlow() } returns flowOf(provideAuthenticatedUser())
        coEvery { userRepository.getUser(DEFAULT_ID) } returns flowOf(provideUserEntity())
    }

    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        //When
        getUserFlowUseCase.invoke().test {
            //Then
            val result = awaitItem()
            awaitComplete()
            assertThat(result).isEqualTo(provideUserEntity())
            coVerify(exactly = 1) {
                authenticationRepository.getUserFlow()
                userRepository.getUser(DEFAULT_ID)
            }
            confirmVerified(authenticationRepository, userRepository)
        }
    }

    @Test
    fun `edge case - user null`() = testCoroutineRule.runTest {
        //Given
        coEvery { authenticationRepository.getUserFlow() } returns flowOf(null)

        //When
        getUserFlowUseCase.invoke().test {
            //Then
            val result = awaitItem()
            awaitComplete()

            assertThat(result).isNull()
            coVerify(exactly = 1) {
                authenticationRepository.getUserFlow()
            }
            confirmVerified(authenticationRepository)
        }
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
        isEating = false
    )
    //end region IN
}