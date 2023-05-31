package com.despaircorp.ui.map

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.despaircorp.domain.location.model.Latitude
import com.despaircorp.domain.location.model.Longitude
import com.despaircorp.domain.restaurants.GetNearbyRestaurantsWithUserLocationUseCase
import com.despaircorp.domain.restaurants.model.RestaurantEntity
import com.despaircorp.ui.utils.TestCoroutineRule
import com.despaircorp.ui.utils.observeForTesting
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MapViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val getNearbyRestaurantsWithUserLocationUseCase: GetNearbyRestaurantsWithUserLocationUseCase = mockk()

    private val viewModel = MapViewModel(
        getNearbyRestaurantsWithUserLocationUseCase,
        testCoroutineRule.getTestCoroutineDispatcherProvider()
    )

    @Before
    fun setup() {
        every { getNearbyRestaurantsWithUserLocationUseCase.invoke() } returns flowOf(getDefaultRestaurantEntities())
    }

    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        viewModel.viewStateLiveData.observeForTesting(this) {
            // Then
            assertThat(it.value?.mapViewStateItems?.size).isEqualTo(3) // TODO MONO étoffer la couverture des branches
        }
    }
    //Region IN

    private fun getDefaultRestaurantEntities(): List<RestaurantEntity> = List(3) { index ->
        RestaurantEntity(
            id = "${DEFAULT_ID}$index",
            name = "${DEFAULT_NAME}$index",
            photoUrl = "${DEFAULT_PHOTO_URL}$index",
            latitude = Latitude(DEFAULT_LATITUDE),
            longitude = Longitude(DEFAULT_LONGITUDE),
            isOpenedNow = false,
            workmateInside = index.takeIf { it != 0 },
            vicinity = "${DEFAULT_VICINITY}$index",
            rating = (3.0 + index).coerceAtMost(5.0),
        )
    }

    //End Region IN

    companion object {
        private const val DEFAULT_ID = "DEFAULT_ID"
        private const val DEFAULT_NAME = "DEFAULT_NAME"
        private const val DEFAULT_PHOTO_URL = "DEFAULT_PHOTO_URL"
        private const val DEFAULT_VICINITY = "DEFAULT_VICINITY"

        private const val DEFAULT_LATITUDE = 48.857920
        private const val DEFAULT_LONGITUDE = 2.295048
    }
}