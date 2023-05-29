package com.despaircorp.domain.location

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.despaircorp.domain.location.model.Latitude
import com.despaircorp.domain.location.model.LocationEntity
import com.despaircorp.domain.location.model.Longitude
import com.despaircorp.domain.restaurants.GetNearbyRestaurantsWithUserLocationUseCaseTest
import com.despaircorp.domain.utils.TestCoroutineRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetUserLocationUseCaseUnitTest {
    
    companion object {
        private const val DEFAULT_LATITUDE = 48.857920
        private const val DEFAULT_LONGITUDE = 2.295048
        
    }
    
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    private val locationRepository: LocationRepository = mockk()
    
    private val getUserLocationUseCase = GetUserLocationUseCase(
        locationRepository,
    )
    
    @Before
    fun setup() {
        coEvery { locationRepository.getUserCurrentLocationFlow() } returns flowOf(getDefaultLocationEntity())
    }
    
    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        getUserLocationUseCase.invoke().test {
            val result = awaitItem()
            awaitComplete()
            
            assertThat(result).isEqualTo(getDefaultLocationEntity())
            coVerify(exactly = 1) {
                locationRepository.getUserCurrentLocationFlow()
            }
            confirmVerified(locationRepository)
        }
    }
    
    //Region IN
    private fun getDefaultLocationEntity() = LocationEntity(
        latitude = Latitude(DEFAULT_LATITUDE),
        longitude = Longitude(DEFAULT_LONGITUDE),
    )
    //end Region IN
}