package com.despaircorp.data.restaurants

import com.despaircorp.data.retrofit.GooglePlacesApi
import com.despaircorp.data.utils.TestCoroutineRule
import com.despaircorp.domain.location.model.Latitude
import com.despaircorp.domain.location.model.LocationEntity
import com.despaircorp.domain.location.model.Longitude
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RestaurantsRepositoryRetrofitUnitTest {
    
    companion object {
        private const val DEFAULT_LATITUDE = 48.857920
        private const val DEFAULT_LONGITUDE = 2.295048
        private const val DEFAULT_RADIUS = 1_000
        private const val DEFAULT_API_KEY = "AIzaSyBKiwewtTkztYvFNYqUG0jQUWzUnmfHBWM"
        private const val DEFAULT_TYPE = "restaurant"
        
        private const val DEFAULT_PLACE_ID = "DEFAULT_PLACE_ID"
        private const val DEFAULT_NAME = "DEFAULT_NAME"
        private const val DEFAULT_PHOTO_URL = "DEFAULT_PHOTO_URL"
        private const val DEFAULT_IS_OPENNED_NOW = true
        private const val DEFAULT_WORKMATE_INSIDE = 4
        private const val DEFAULT_VICINITY = "DEFAULT_VICINITY"
        private const val DEFAULT_RATING = 3.0
    
    }
    
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    private val placesApi: GooglePlacesApi = mockk(relaxed = true)
    
    private val restaurantsRepositoryRetrofit = RestaurantsRepositoryRetrofit(
        placesApi = placesApi,
    )
    
    @Before
    fun setup() {
        
        coEvery {
            placesApi.getPlaces(
                location = "${DEFAULT_LATITUDE}, ${DEFAULT_LONGITUDE}",
                radius = DEFAULT_RADIUS,
                apiKey = DEFAULT_API_KEY,
                type = DEFAULT_TYPE
            )
        } returns provideDefaultRestaurantsDto()
    }
    
    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        val res = restaurantsRepositoryRetrofit.getNearbyRestaurantsList(
            getDefaultLocationEntity()
        )
        println(res)
    }
    
    //Region OUT
    private fun provideDefaultRestaurantsDto(): RestaurantsDto {
        return RestaurantsDto(
            results = listOf(
                ResultsItem(
                    placeId = DEFAULT_PLACE_ID,
                    name = DEFAULT_NAME,
                    photos = providePhotoItem(),
                    geometry = provideGeometry(),
                    openingHours = provideOpenningHour(),
                    rating = DEFAULT_RATING,
                    vicinity = DEFAULT_VICINITY,
                )
            )
        )
    }
    
    private fun provideGeometry(): Geometry {
        return Geometry(
            location = getDefaultLocation()
        )
    }
    
    private fun provideOpenningHour(): OpeningHours {
        return OpeningHours(
            openNow = DEFAULT_IS_OPENNED_NOW
        )
    }
    
    private fun getDefaultLocation() = Location(
        lat = Latitude(DEFAULT_LATITUDE),
        lng = Longitude(DEFAULT_LONGITUDE),
    )
    
    private fun getDefaultLocationEntity() = LocationEntity(
        latitude = Latitude(DEFAULT_LATITUDE),
        longitude = Longitude(DEFAULT_LONGITUDE),
    )
    
    private fun providePhotoItem(): List<PhotosItem> = List(size = 3) { index ->
        PhotosItem(
           photoReference = DEFAULT_PHOTO_URL
        )
    }
    //end Region OUT
}