package com.despaircorp.domain.restaurants

import android.util.Log
import com.despaircorp.domain.location.GetUserLocationUseCase
import com.despaircorp.domain.restaurants.model.RestaurantEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class GetNearbyRestaurantsWithUserLocationUseCase @Inject constructor(
    private val restaurantsRepository: RestaurantsRepository,
    private val getUserLocationUseCase: GetUserLocationUseCase
) {
    fun invoke(): Flow<List<RestaurantEntity>> = getUserLocationUseCase.invoke().mapLatest { location ->
        restaurantsRepository.getNearbyRestaurantsList(
            location = location,
        )
    }
}