package com.despaircorp.ui.restaurants.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import com.despaircorp.domain.location.model.Latitude
import com.despaircorp.domain.location.model.Longitude
import com.despaircorp.domain.restaurants.details.GetRestaurantDetailsByPlaceIdUseCase
import com.despaircorp.domain.restaurants.details.model.RestaurantDetailsEntity
import com.despaircorp.ui.BuildConfig
import com.despaircorp.ui.utils.TestCoroutineRule
import com.despaircorp.ui.utils.observeForTesting
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RestaurantDetailsViewModelTest {
    
    companion object {
        private const val DEFAULT_PLACE_ID = "DEFAULT_PLACE_ID"
        private const val DEFAULT_LATITUDE = 48.857920
        private const val DEFAULT_LONGITUDE = 2.295048
        private const val DEFAULT_NAME = "DEFAULT_NAME"
        private const val DEFAULT_RATING = 3.0
        private const val DEFAULT_PHOTO_URL = "DEFAULT_PHOTO_URL"
        private const val DEFAULT_VICINITY = "DEFAULT_VICINITY"
        private const val DEFAULT_WEBSITE_URL = "DEFAULT_WEBSITE_URL"
        private const val DEFAULT_PHONE_NUMBER = "DEFAULT_PHONE_NUMBER"
        
        const val ARG_PLACE_ID = "ARG_PLACE_ID"
    }
    
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    private val getRestaurantDetailsByPlaceIdUseCase: GetRestaurantDetailsByPlaceIdUseCase = mockk()
    
    
    val viewModel = RestaurantDetailsViewModel(
        coroutineDispatcherProvider = testCoroutineRule.getTestCoroutineDispatcherProvider(),
        getRestaurantDetailsByPlaceIdUseCase = getRestaurantDetailsByPlaceIdUseCase,
        savedStateHandle = SavedStateHandle().apply {
            set(ARG_PLACE_ID, DEFAULT_PLACE_ID)
        },
    )
    
    @Before
    fun setup() {
        coEvery { getRestaurantDetailsByPlaceIdUseCase.invoke(DEFAULT_PLACE_ID) } returns provideDefaultRestaurantDetailsEntity()
        
    }
    
    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        //When
        viewModel.viewState.observeForTesting(this) {
            //Then
            assertThat(it.value).isEqualTo(provideDefaultRestaurantDetailsViewState())
        }
    }
    
    @Test
    fun `edge case - placeId is null`() = testCoroutineRule.runTest {
        
        
        coEvery { getRestaurantDetailsByPlaceIdUseCase.invoke(DEFAULT_PLACE_ID) } returns null
        
        viewModel.viewState.observeForTesting(this) {
            assertThat(it.value).isNull()
        }
    }
    
    //Region OUT
    private fun provideDefaultRestaurantDetailsEntity(): RestaurantDetailsEntity {
        
        return RestaurantDetailsEntity(
            name = DEFAULT_NAME,
            rating = DEFAULT_RATING,
            photoUrl = DEFAULT_PHOTO_URL,
            vicinity = DEFAULT_VICINITY,
            websiteUrl = DEFAULT_WEBSITE_URL,
            phoneNumber = DEFAULT_PHONE_NUMBER,
            latitude = Latitude(DEFAULT_LATITUDE),
            longitude = Longitude(DEFAULT_LONGITUDE),
        )
    }
    
    private fun provideDefaultRestaurantDetailsViewState(): RestaurantDetailsViewState {
        val pictureUrl = StringBuilder()
            .append("https://maps.googleapis.com/maps/api/place/photo?maxwidth=1920&maxheigth=1080&photo_reference=")
            .append(DEFAULT_PHOTO_URL)
            .append("&key=${BuildConfig.MAPS_API_KEY}")
            .toString()
        
        return RestaurantDetailsViewState(
            name = DEFAULT_NAME,
            rating = DEFAULT_RATING,
            photoUrl = pictureUrl,
            vicinity = DEFAULT_VICINITY,
            websiteUrl = DEFAULT_WEBSITE_URL,
            phoneNumber = DEFAULT_PHONE_NUMBER,
            latitude = Latitude(DEFAULT_LATITUDE),
            longitude = Longitude(DEFAULT_LONGITUDE),
            coworkersInside = emptyList()
        )
    }
    //end Region OUT
}