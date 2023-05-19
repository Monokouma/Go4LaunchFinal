package com.despaircorp.data

import android.annotation.SuppressLint
import android.location.Location
import com.despaircorp.domain.location.LocationRepository
import com.despaircorp.domain.location.model.Latitude
import com.despaircorp.domain.location.model.LocationEntity
import com.despaircorp.domain.location.model.Longitude
import com.despaircorp.domain.utils.CoroutineDispatcherProvider
import com.google.android.gms.location.*
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocationProviderRepository @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : LocationRepository {

    @SuppressLint("MissingPermission")
    override fun getUserCurrentLocationFlow(): Flow<LocationEntity> = callbackFlow {
        val locationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let {
                    trySend(
                        LocationEntity(
                            latitude = Latitude(it.latitude),
                            longitude = Longitude(it.longitude),
                        )
                    )
                }
            }
        }

        val locationRequest = LocationRequest.Builder(PRIORITY_HIGH_ACCURACY, 60_000L)
            .build()

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            coroutineDispatcherProvider.io.asExecutor(),
            locationCallback,
        )

        awaitClose { fusedLocationProviderClient.removeLocationUpdates(locationCallback) }
    }.flowOn(coroutineDispatcherProvider.io)

    override suspend fun getDistanceBetweenPlaceAndUser(
        userLocation: LocationEntity,
        restaurantLat: Double,
        restaurantLong: Double,
    ): Int = withContext(Dispatchers.Default) {
        val result = FloatArray(1)
        Location.distanceBetween(
            userLocation.latitude.value,
            userLocation.longitude.value,
            restaurantLat,
            restaurantLong,
            result
        )
        result.first().toInt()
    }
}