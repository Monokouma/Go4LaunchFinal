package com.despaircorp.ui.restaurants

import android.location.Location
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveDataScope
import com.despaircorp.domain.location.GetDistanceBetweenUserAndPlaces
import com.despaircorp.domain.location.GetUserLocationUseCase
import com.despaircorp.domain.restaurants.GetNearbyRestaurantsWithUserLocationUseCase
import com.despaircorp.domain.user.UserRepository
import com.despaircorp.ui.utils.TestCoroutineRule
import com.despaircorp.ui.utils.observeForTesting
import io.mockk.coJustRun
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import org.junit.Before
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import com.despaircorp.domain.location.LocationRepository
import com.despaircorp.domain.restaurants.model.RestaurantEntity
import io.mockk.coEvery
import io.mockk.coJustAwait
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.junit.Rule
import org.junit.Test

class RestaurantsViewModelTest {
    
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    private val getNearbyRestaurantsWithUserLocationUseCase: GetNearbyRestaurantsWithUserLocationUseCase = mockk()
    private val getUserLocationUseCase: GetUserLocationUseCase = mockk()
    private val getDistanceBetweenUserAndPlaces: GetDistanceBetweenUserAndPlaces = mockk()
    private val locationRepository: LocationRepository = mockk()
    
    private val restaurantsViewModel = RestaurantsViewModel(
        getNearbyRestaurantsWithUserLocationUseCase,
        getUserLocationUseCase,
        getDistanceBetweenUserAndPlaces,
        testCoroutineRule.getTestCoroutineDispatcherProvider()
    )
    
    @Before
    fun setup() {
        coJustAwait { getNearbyRestaurantsWithUserLocationUseCase.invoke() }
        coJustAwait { getUserLocationUseCase.invoke() }
    
    }
   
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun nominalCase() = testCoroutineRule.runTest {
        val viewModel = mockk<RestaurantsViewModel>(relaxed = true)
        every { getNearbyRestaurantsWithUserLocationUseCase.invoke() } returns flowOf(listOf(
            RestaurantEntity(
                "1",
                "foo",
                null,
                12.1,
                12.2,
                true,
                null,
                "lol",
                null
            )
        ))
        viewModel.viewState.observeForTesting(this) {
           assertThat(it.value).isEqualTo(RestaurantsViewState(emptyList(), true))
        }
        
    }
}