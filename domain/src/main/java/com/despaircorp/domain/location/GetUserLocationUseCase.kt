package com.despaircorp.domain.location

import android.location.Location
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetUserLocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    suspend fun invoke(): Location {
        return locationRepository.getLocation().first()
    }
}