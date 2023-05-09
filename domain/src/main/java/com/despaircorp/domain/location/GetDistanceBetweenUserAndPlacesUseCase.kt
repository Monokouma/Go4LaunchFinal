package com.despaircorp.domain.location

import javax.inject.Inject

class GetDistanceBetweenUserAndPlacesUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    suspend fun invoke(
        userLat: Double,
        userLong: Double,
        restaurantLat: Double,
        restaurantLong: Double,
    ): Int = locationRepository.getDistanceBetweenPlaceAndUser(
        userLat,
        userLong,
        restaurantLat,
        restaurantLong,
    )
}