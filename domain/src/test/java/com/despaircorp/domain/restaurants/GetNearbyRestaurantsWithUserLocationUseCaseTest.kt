package com.despaircorp.domain.restaurants

import android.location.Location
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.despaircorp.domain.location.GetUserLocationUseCase
import com.despaircorp.domain.utils.TestCoroutineRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class GetNearbyRestaurantsWithUserLocationUseCaseTest {
    companion object {
        private const val DEFAULT_LATITUDE = 48.857920
        private const val DEFAULT_LONGITUDE = 2.295048
    }
    
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    private val getUserLocationUseCase: GetUserLocationUseCase = mockk()
    private val restaurantsRepository: RestaurantsRepository = mockk()
    
    private val getNearbyRestaurantsWithUserLocationUseCase: GetNearbyRestaurantsWithUserLocationUseCase = GetNearbyRestaurantsWithUserLocationUseCase(
        restaurantsRepository = restaurantsRepository,
        getUserLocationUseCase = getUserLocationUseCase
    )
    @Before
    fun setup() {
        every { getUserLocationUseCase.invoke() } returns flowOf(getDefaultLocationEntity())
    
    }
    
    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
    
    }
    
    //region IN
    private fun getDefaultLocationEntity(): Location = mockk {
        every { latitude } returns DEFAULT_LATITUDE
        every { longitude } returns DEFAULT_LONGITUDE
    }
    //endregion IN
}