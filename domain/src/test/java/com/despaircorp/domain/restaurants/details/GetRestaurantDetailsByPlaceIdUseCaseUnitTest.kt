package com.despaircorp.domain.restaurants.details

import com.despaircorp.domain.location.model.Latitude
import com.despaircorp.domain.location.model.Longitude
import com.despaircorp.domain.restaurants.details.model.RestaurantDetailsEntity
import com.despaircorp.domain.utils.TestCoroutineRule
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo

class GetRestaurantDetailsByPlaceIdUseCaseUnitTest {
    
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
    
    private val restaurantDetailsRepository: RestaurantDetailsRepository = mockk()
    
    private val getRestaurantDetailsByPlaceIdUseCase = GetRestaurantDetailsByPlaceIdUseCase(
        restaurantDetailsRepository = restaurantDetailsRepository,
    )
    
    @Before
    fun setup() {
        coEvery { restaurantDetailsRepository.getRestaurantDetailsByPlaceId(DEFAULT_PLACE_ID) } returns provideDefaultRestaurantDetailsEntity()
    }
    
    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        val result = getRestaurantDetailsByPlaceIdUseCase.invoke(DEFAULT_PLACE_ID)
        
        assertThat(result).isEqualTo(provideDefaultRestaurantDetailsEntity())
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
    //end Region OUT
}