package com.despaircorp.domain.restaurants

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.despaircorp.domain.location.GetUserLocationUseCase
import com.despaircorp.domain.location.model.Latitude
import com.despaircorp.domain.location.model.LocationEntity
import com.despaircorp.domain.location.model.Longitude
import com.despaircorp.domain.restaurants.model.RestaurantEntity
import com.despaircorp.domain.utils.TestCoroutineRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.currentTime
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetNearbyRestaurantsWithUserLocationUseCaseTest {
    companion object {
        private const val DEFAULT_LATITUDE = 48.857920
        private const val DEFAULT_LONGITUDE = 2.295048

        private const val DEFAULT_ID = "DEFAULT_ID"
        private const val DEFAULT_NAME = "DEFAULT_NAME"
        private const val DEFAULT_PHOTO_URL = "DEFAULT_PHOTO_URL"
        private const val DEFAULT_VICINITY = "DEFAULT_VICINITY"
    }

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val getUserLocationUseCase: GetUserLocationUseCase = mockk()
    private val restaurantsRepository: RestaurantsRepository = mockk()

    private val getNearbyRestaurantsWithUserLocationUseCase = GetNearbyRestaurantsWithUserLocationUseCase(
        restaurantsRepository = restaurantsRepository,
        getUserLocationUseCase = getUserLocationUseCase
    )

    @Before
    fun setup() {
        every { getUserLocationUseCase.invoke() } returns flowOf(getDefaultLocationEntity())
        coEvery { restaurantsRepository.getNearbyRestaurantsList(getDefaultLocationEntity()) } returns getDefaultRestaurantEntities()
    }

    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        // When
        getNearbyRestaurantsWithUserLocationUseCase.invoke().test {
            val result = awaitItem()
            awaitComplete()

            // Then
            assertThat(result).isEqualTo(getDefaultRestaurantEntities())
            coVerify(exactly = 1) {
                getUserLocationUseCase.invoke()
                restaurantsRepository.getNearbyRestaurantsList(getDefaultLocationEntity())
            }
            confirmVerified(getUserLocationUseCase, restaurantsRepository)
        }
    }

    @Test
    fun `edge case - no location`() = testCoroutineRule.runTest {
        // Given
        every { getUserLocationUseCase.invoke() } returns flowOf()

        // When
        getNearbyRestaurantsWithUserLocationUseCase.invoke().test {
            // Then
            awaitComplete()
        }
    }

    @Test
    fun `edge case - verify mapLatest backpressure handling`() = testCoroutineRule.runTest {
        // Given
        every { getUserLocationUseCase.invoke() } returns flow {
            emit(getDefaultLocationEntity())
            delay(50)
            emit(getDefaultLocationEntity())
        }

        coEvery { restaurantsRepository.getNearbyRestaurantsList(getDefaultLocationEntity()) } coAnswers {
            delay(200)
            getDefaultRestaurantEntities()
        }

        // When
        getNearbyRestaurantsWithUserLocationUseCase.invoke().test {
            val result = awaitItem()
            assertThat(currentTime).isEqualTo(250)

            awaitComplete()
            assertThat(currentTime).isEqualTo(250)

            // Then
            assertThat(result).isEqualTo(getDefaultRestaurantEntities())
            coVerify(exactly = 1) {
                getUserLocationUseCase.invoke()
            }
            coVerify(exactly = 2) {
                restaurantsRepository.getNearbyRestaurantsList(getDefaultLocationEntity())
            }
            confirmVerified(getUserLocationUseCase, restaurantsRepository)
        }
    }

    //region IN
    private fun getDefaultLocationEntity() = LocationEntity(
        latitude = Latitude(DEFAULT_LATITUDE),
        longitude = Longitude(DEFAULT_LONGITUDE),
    )

    //endregion IN
    private fun getDefaultRestaurantEntities(): List<RestaurantEntity> = List(3) { index ->
        RestaurantEntity(
            id = "$DEFAULT_ID$index",
            name = "$DEFAULT_NAME$index",
            photoUrl = "$DEFAULT_PHOTO_URL$index",
            latitude = DEFAULT_LATITUDE,
            longitude = DEFAULT_LONGITUDE,
            isOpennedNow = false,
            workmateInside = index.takeIf { it != 0 },
            vicinity = "$DEFAULT_VICINITY$index",
            rating = (3.0 + index).coerceAtMost(5.0),
        )
    }
}