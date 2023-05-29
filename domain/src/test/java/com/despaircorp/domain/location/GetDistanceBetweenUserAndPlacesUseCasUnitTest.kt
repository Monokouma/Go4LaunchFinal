package com.despaircorp.domain.location

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.despaircorp.domain.location.model.Latitude
import com.despaircorp.domain.location.model.LocationEntity
import com.despaircorp.domain.location.model.Longitude
import com.despaircorp.domain.utils.TestCoroutineRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetDistanceBetweenUserAndPlacesUseCasUnitTest {
    companion object {
        private const val DEFAULT_LATITUDE = 48.857920
        private const val DEFAULT_LONGITUDE = 2.295048
        private const val DEFAULT_RESTAURANT_LATITUDE = 48.857020
        private const val DEFAULT_RESTAURANT_LONGITUDE = 2.292048
        private const val DEFAULT_DISTANCE = 100
    }
    
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    private val locationRepository: LocationRepository = mockk()
    
    private val getDistanceBetweenUserAndPlacesUseCase = GetDistanceBetweenUserAndPlacesUseCase(
        locationRepository,
    )
    
    @Before
    fun setup() {
        coEvery { locationRepository.getUserCurrentLocationFlow() } returns flowOf(getDefaultLocationEntity())
        coEvery {
            locationRepository.getDistanceBetweenPlaceAndUser(
                getDefaultLocationEntity(),
                DEFAULT_RESTAURANT_LATITUDE,
                DEFAULT_RESTAURANT_LONGITUDE
            )
        } returns DEFAULT_DISTANCE
    }
    
    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        val result = getDistanceBetweenUserAndPlacesUseCase.invoke(
            getDefaultLocationEntity(),
            DEFAULT_RESTAURANT_LATITUDE,
            DEFAULT_RESTAURANT_LONGITUDE
        )
        
        assertThat(result).isEqualTo(DEFAULT_DISTANCE)
        coVerify(exactly = 1) {
            locationRepository.getDistanceBetweenPlaceAndUser(
                getDefaultLocationEntity(),
                DEFAULT_RESTAURANT_LATITUDE,
                DEFAULT_RESTAURANT_LONGITUDE
            )
        }
        confirmVerified(locationRepository)
    }
    
    //Region IN
    private fun getDefaultLocationEntity() = LocationEntity(
        latitude = Latitude(DEFAULT_LATITUDE),
        longitude = Longitude(DEFAULT_LONGITUDE),
    )
    //end Region IN
}