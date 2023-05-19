package com.despaircorp.domain.location

import com.despaircorp.domain.location.model.LocationEntity
import javax.inject.Inject

class GetDistanceBetweenUserAndPlacesUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    suspend fun invoke(
        userLocation: LocationEntity,
        restaurantLat: Double,
        restaurantLong: Double,
    ): Int = locationRepository.getDistanceBetweenPlaceAndUser(
        userLocation,
        restaurantLat,
        restaurantLong,
    )
}