package com.despaircorp.domain.location

import com.despaircorp.domain.location.model.LocationEntity
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun getUserCurrentLocationFlow(): Flow<LocationEntity>

    suspend fun getDistanceBetweenPlaceAndUser(
        userLocation: LocationEntity,
        restaurantLat: Double,
        restaurantLong: Double,
    ): Int
}