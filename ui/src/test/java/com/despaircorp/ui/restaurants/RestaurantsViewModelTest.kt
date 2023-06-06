package com.despaircorp.ui.restaurants


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.despaircorp.domain.location.GetDistanceBetweenUserAndPlacesUseCase
import com.despaircorp.domain.location.GetUserLocationUseCase
import com.despaircorp.domain.location.model.Latitude
import com.despaircorp.domain.location.model.LocationEntity
import com.despaircorp.domain.location.model.Longitude
import com.despaircorp.domain.restaurants.GetNearbyRestaurantsWithUserLocationUseCase
import com.despaircorp.domain.restaurants.model.RestaurantEntity
import com.despaircorp.ui.BuildConfig
import com.despaircorp.ui.R
import com.despaircorp.ui.utils.NativeText
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
        every { getNearbyRestaurantsWithUserLocationUseCase.invoke() } returns flowOf(getDefaultOpenedRestaurantEntities())
        every { getUserLocationUseCase.invoke() } returns flowOf(getDefaultLocationEntity())
        coEvery {
            getDistanceBetweenUserAndPlacesUseCase.invoke(
                userLocation = getDefaultLocationEntity(),
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
            assertThat(it.value).isEqualTo(
                RestaurantsViewState(
                    restaurants = emptyList(),
                    isSpinnerVisible = true
                )
            )
        }
    }

    @Test
    fun `restaurants is opened`() = testCoroutineRule.runTest {
        viewModel.viewState.observeForTesting(this) {
            // Then
            assertThat(it.value?.restaurants).isEqualTo(
                getDefaultOpenedRestaurantViewStateItems()
            )
        }
    }

    @Test
    fun `restaurants is closed`() = testCoroutineRule.runTest {
        every { getNearbyRestaurantsWithUserLocationUseCase.invoke() } returns flowOf(getDefaultClosedRestaurantEntities())
        viewModel.viewState.observeForTesting(this) {
            // Then
            assertThat(it.value?.restaurants).isEqualTo(
                getDefaultClosedRestaurantViewStateItems()
            )
        }
    }

    // region IN
    private fun getDefaultClosedRestaurantEntities(): List<RestaurantEntity> = List(3) { index ->
        RestaurantEntity(
            id = "$DEFAULT_ID$index",
            name = "$DEFAULT_NAME$index",
            photoUrl = "$DEFAULT_PHOTO_URL$index",
            latitude = Latitude(DEFAULT_LATITUDE),
            longitude = Longitude(DEFAULT_LONGITUDE),
            isOpenedNow = false,
            workmateInside = index.takeIf { it != 0 },
            vicinity = "$DEFAULT_VICINITY$index",
            rating = (3.0 + index).coerceAtMost(5.0),
        )
    }

    private fun getDefaultOpenedRestaurantEntities(): List<RestaurantEntity> = List(3) { index ->
        RestaurantEntity(
            id = "$DEFAULT_ID$index",
            name = "$DEFAULT_NAME$index",
            photoUrl = "$DEFAULT_PHOTO_URL$index",
            latitude = Latitude(DEFAULT_LATITUDE),
            longitude = Longitude(DEFAULT_LONGITUDE),
            isOpenedNow = true,
            workmateInside = index.takeIf { it != 0 },
            vicinity = "$DEFAULT_VICINITY$index",
            rating = (3.0 + index).coerceAtMost(5.0),
        )
    }

    private fun getDefaultLocationEntity() = LocationEntity(
        latitude = Latitude(DEFAULT_LATITUDE),
        longitude = Longitude(DEFAULT_LONGITUDE),
    )
    // endregion IN

    // region OUT
    private fun getDefaultClosedRestaurantViewStateItems(): List<RestaurantsViewStateItems> {
        val restaurants = mutableListOf<RestaurantsViewStateItems>()

        getDefaultClosedRestaurantEntities().forEach {
            val photoUrl = StringBuilder()
                .append("https://maps.googleapis.com/maps/api/place/photo?maxwidth=1920&maxheigth=1080&photo_reference=")
                .append(it.photoUrl)
                .append("&key=${BuildConfig.MAPS_API_KEY}")
                .toString()

            restaurants.add(
                RestaurantsViewStateItems(
                    restaurantName = it.name,
                    restaurantDistance = "0m",
                    restaurantImageUrl = photoUrl,
                    restaurantAddressAndType = it.vicinity,
                    workmatesInside = it.workmateInside.toString(),
                    restaurantSchedule = NativeText.Resource(R.string.closed),
                    restaurantStar = it.rating,
                    openedTextColorRes = R.color.rusty_red,
                    placeId = it.id
                )
            )
        }

        return restaurants
    }

    private fun getDefaultOpenedRestaurantViewStateItems(): List<RestaurantsViewStateItems> {
        val restaurants = mutableListOf<RestaurantsViewStateItems>()

        getDefaultOpenedRestaurantEntities().forEach {
            val photoUrl = StringBuilder()
                .append("https://maps.googleapis.com/maps/api/place/photo?maxwidth=1920&maxheigth=1080&photo_reference=")
                .append(it.photoUrl)
                .append("&key=${BuildConfig.MAPS_API_KEY}")
                .toString()

            restaurants.add(
                RestaurantsViewStateItems(
                    restaurantName = it.name,
                    restaurantDistance = "0m",
                    restaurantImageUrl = photoUrl,
                    restaurantAddressAndType = it.vicinity,
                    workmatesInside = it.workmateInside.toString(),
                    restaurantSchedule = NativeText.Resource(R.string.opened),
                    restaurantStar = it.rating,
                    openedTextColorRes = R.color.shamrock_green,
                    placeId = it.id
                )
            )
        }

        return restaurants
    }
    // endregion OUT
}