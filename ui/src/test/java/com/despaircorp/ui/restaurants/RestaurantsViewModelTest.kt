package com.despaircorp.ui.restaurants

import android.location.Location
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.despaircorp.domain.location.GetDistanceBetweenUserAndPlacesUseCase
import com.despaircorp.domain.location.GetUserLocationUseCase
import com.despaircorp.domain.restaurants.GetNearbyRestaurantsWithUserLocationUseCase
import com.despaircorp.domain.restaurants.model.RestaurantEntity
import com.despaircorp.ui.utils.TestCoroutineRule
import com.despaircorp.ui.utils.observeForTesting
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RestaurantsViewModelTest {

    companion object {
        private const val DEFAULT_ID = "DEFAULT_ID"
        private const val DEFAULT_NAME = "DEFAULT_NAME"
        private const val DEFAULT_PHOTO_URL = "DEFAULT_PHOTO_URL"
        private const val DEFAULT_VICINITY = "DEFAULT_VICINITY"

        private const val DEFAULT_LATITUDE = 48.857920
        private const val DEFAULT_LONGITUDE = 2.295048
    }

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val getNearbyRestaurantsWithUserLocationUseCase: GetNearbyRestaurantsWithUserLocationUseCase = mockk()
    private val getUserLocationUseCase: GetUserLocationUseCase = mockk()
    private val getDistanceBetweenUserAndPlacesUseCase: GetDistanceBetweenUserAndPlacesUseCase = mockk()

    private val viewModel = RestaurantsViewModel(
        getNearbyRestaurantsWithUserLocationUseCase,
        getUserLocationUseCase,
        getDistanceBetweenUserAndPlacesUseCase,
        testCoroutineRule.getTestCoroutineDispatcherProvider()
    )

    @Before
    fun setup() {
        every { getNearbyRestaurantsWithUserLocationUseCase.invoke() } returns flowOf(getDefaultRestaurantEntities())
        every { getUserLocationUseCase.invoke() } returns flowOf(getDefaultLocationEntity())
        coEvery {
            getDistanceBetweenUserAndPlacesUseCase.invoke(
                userLat = DEFAULT_LATITUDE,
                userLong = DEFAULT_LONGITUDE,
                restaurantLat = DEFAULT_LATITUDE,
                restaurantLong = DEFAULT_LONGITUDE
            )
        } returns 0
    }

    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        // When
        viewModel.viewState.observeForTesting(this) {
            // Then
            assertThat(it.value?.restaurants?.size).isEqualTo(3)
        }
    }

    @Test
    fun `initial case`() = testCoroutineRule.runTest {
        // Given
        every { getNearbyRestaurantsWithUserLocationUseCase.invoke() } returns flow {
            delay(200)
            emit(emptyList())
        }
        every { getUserLocationUseCase.invoke() } returns flow {
            delay(10)
            emit(mockk())
        }

        // When
        viewModel.viewState.observeForTesting(this) {
            // Then
            assertThat(it.value).isEqualTo(RestaurantsViewState(emptyList(), true))
        }
    }

    // region IN
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

    private fun getDefaultLocationEntity(): Location = mockk {
        every { latitude } returns DEFAULT_LATITUDE
        every { longitude } returns DEFAULT_LONGITUDE
    }
    // endregion IN

    // region OUT
    // endregion OUT
}