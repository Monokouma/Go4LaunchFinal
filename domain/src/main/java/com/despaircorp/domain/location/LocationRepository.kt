package com.despaircorp.domain.location

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun getUserCurrentLocationFlow(): Flow<Location>

    suspend fun getDistanceBetweenPlaceAndUser(
        userLat: Double,
        userLong: Double,
        restaurantLat: Double,
        restaurantLong: Double,
    ): Int
}