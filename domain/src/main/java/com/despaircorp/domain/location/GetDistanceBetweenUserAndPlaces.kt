package com.despaircorp.domain.location

import android.location.Location
import javax.inject.Inject

class GetDistanceBetweenUserAndPlaces @Inject constructor(
    private val locationRepository: LocationRepository
) {
    fun invoke(latitude: Double, longitude: Double, location: Location): Int {
        return locationRepository.getDistanceBetweenPlaceAndUser(
            latitude,
            longitude,
            location
        )
    }
}