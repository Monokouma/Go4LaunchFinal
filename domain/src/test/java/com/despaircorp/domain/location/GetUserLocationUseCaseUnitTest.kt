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
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetUserLocationUseCaseUnitTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val userCurrentLocationFlow: Flow<LocationEntity> = mockk()

    private val locationRepository: LocationRepository = mockk()

    private val getUserLocationUseCase = GetUserLocationUseCase(
        locationRepository = locationRepository,
    )

    @Before
    fun setup() {
        coEvery { locationRepository.getUserCurrentLocationFlow() } returns userCurrentLocationFlow
    }

    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        //When
        val result = getUserLocationUseCase.invoke()

        //Then
        assertThat(result).isEqualTo(userCurrentLocationFlow)
        coVerify(exactly = 1) {
            locationRepository.getUserCurrentLocationFlow()
        }
        confirmVerified(locationRepository)
    }
}