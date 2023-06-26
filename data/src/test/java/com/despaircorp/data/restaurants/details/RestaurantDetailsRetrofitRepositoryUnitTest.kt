package com.despaircorp.data.restaurants.details

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import com.despaircorp.data.retrofit.GooglePlacesApi
import com.despaircorp.data.utils.TestCoroutineRule
import com.despaircorp.domain.location.model.Latitude
import com.despaircorp.domain.location.model.Longitude
import com.despaircorp.domain.restaurants.details.model.RestaurantDetailsEntity
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RestaurantDetailsRetrofitRepositoryUnitTest {
    
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
        
    }
    
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    private val placesApi: GooglePlacesApi = mockk()
    
    private val restaurantDetailsRetrofitRepository = RestaurantDetailsRetrofitRepository(
        placesApi,
    )
    
    @Before
    fun setup() {
        coEvery { placesApi.getPlacesDetails(
            any(),
            DEFAULT_PLACE_ID
        ) } returns provideDefaultRestaurantDetailsDto()
    }
    
    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        val result = restaurantDetailsRetrofitRepository.getRestaurantDetailsByPlaceId(DEFAULT_PLACE_ID)
        
        assertThat(result).isEqualTo(provideDefaultRestaurantDetailsEntity())
    }
    
    @Test
    fun `edge case - null property`() = testCoroutineRule.runTest {
        coEvery { placesApi.getPlacesDetails(
            any(),
            DEFAULT_PLACE_ID
        ) } returns provideNullDefaultRestaurantDetailsDto()
        
        val result = restaurantDetailsRetrofitRepository.getRestaurantDetailsByPlaceId(
            DEFAULT_PLACE_ID)

        assertThat(result).isNull()
    }
    
    //Region OUT
    private fun provideDefaultRestaurantDetailsEntity() = RestaurantDetailsEntity(
        name = DEFAULT_NAME,
        rating = DEFAULT_RATING,
        photoUrl = DEFAULT_PHOTO_URL,
        vicinity = DEFAULT_VICINITY,
        websiteUrl = DEFAULT_WEBSITE_URL,
        phoneNumber = DEFAULT_PHONE_NUMBER,
        latitude = Latitude(DEFAULT_LATITUDE),
        longitude = Longitude(DEFAULT_LONGITUDE),
    )
    
    private fun provideNullDefaultRestaurantDetailsDto() = RestaurantDetailsDto(
        result = provideNullDefaultRestaurantDtoResult(),
    )
    
    private fun provideNullDefaultRestaurantDtoResult() = Result(
        name = null,
        rating = DEFAULT_RATING,
        vicinity = null,
        url = DEFAULT_WEBSITE_URL,
        formattedPhoneNumber = DEFAULT_PHONE_NUMBER,
        geometry = null,
        photos = null
    )
    
    private fun provideDefaultRestaurantDetailsDto() = RestaurantDetailsDto(
        result = provideDefaultRestaurantDtoResult(),
    )
    
    private fun provideDefaultRestaurantDtoResult() = Result(
        name = DEFAULT_NAME,
        rating = DEFAULT_RATING,
        vicinity = DEFAULT_VICINITY,
        url = DEFAULT_WEBSITE_URL,
        formattedPhoneNumber = DEFAULT_PHONE_NUMBER,
        geometry = provideDefaultRestaurantDtoGeometry(),
        photos = provideDefaultRestaurantDtoPhotoItems()
    )
    
    private fun provideDefaultRestaurantDtoPhotoItems() = List(size = 3) {
        PhotosItem(
            photoReference = DEFAULT_PHOTO_URL
        )
    }
    
    private fun provideDefaultRestaurantDtoGeometry() = Geometry(
        location = provideDefaultRestaurantDtoLocation()
    )
    
    private fun provideDefaultRestaurantDtoLocation() = Location(
        lat = DEFAULT_LATITUDE,
        lng = DEFAULT_LONGITUDE
    )
    
    //end Region OUT
}