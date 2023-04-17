package com.despaircorp.domain.restaurants

import com.despaircorp.domain.location.GetUserLocationUseCase
import com.despaircorp.domain.restaurants.model.RestaurantEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetNearbyRestaurantsWithUserLocationUseCase @Inject constructor(
    private val nearbyRestaurantsRepository: NearbyRestaurantsRepository,
    private val getUserLocationUseCase: GetUserLocationUseCase
) {
    suspend fun invoke() = flow {
        getUserLocationUseCase.invoke().collectLatest { location ->
            nearbyRestaurantsRepository.getNearbyRestaurantsList(location).collectLatest {
                emit(it)
            }
        }
    }
}