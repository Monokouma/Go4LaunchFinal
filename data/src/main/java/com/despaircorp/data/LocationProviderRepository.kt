package com.despaircorp.data

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import android.util.Log
import com.despaircorp.data.restaurants.Geometry
import com.despaircorp.data.restaurants.RestaurantsDto
import com.despaircorp.domain.location.LocationRepository
import com.google.android.gms.location.*
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LocationProviderRepository @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
): LocationRepository {
    
    @SuppressLint("MissingPermission")
    override fun getUserCurrentLocationFlow(): Flow<Location> = callbackFlow {
        val locationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let {
                    trySend(it)
                }
            }
        }
    
        val locationRequest = LocationRequest.Builder(PRIORITY_HIGH_ACCURACY, 60_000L)
            .build()
    
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    
        awaitClose { fusedLocationProviderClient.removeLocationUpdates(locationCallback) }
    }.flowOn(Dispatchers.IO)
    
    override fun getDistanceBetweenPlaceAndUser(
        restaurantLat: Double,
        restaurantLong: Double,
        userLocation: Location
    ): Int {
        val placeLocation = Location("placeLocation")
        placeLocation.longitude = restaurantLong
        placeLocation.latitude = restaurantLat
        return userLocation.distanceTo(placeLocation).toInt()
    }
}