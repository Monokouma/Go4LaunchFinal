package com.despaircorp.domain.restaurants.details

import com.despaircorp.domain.restaurants.details.model.RestaurantDetailsEntity
import javax.inject.Inject

class GetRestaurantDetailsByPlaceIdUseCase @Inject constructor(
    private val restaurantDetailsRepository: RestaurantDetailsRepository,
) {
    suspend fun invoke(placeId: String): RestaurantDetailsEntity? {
        return restaurantDetailsRepository.getRestaurantDetailsByPlaceId(placeId)
    }
}