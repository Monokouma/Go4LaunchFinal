package com.despaircorp.domain.location

import android.location.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transformLatest
import javax.inject.Inject

class GetUserLocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    fun invoke(): Flow<Location> = locationRepository.getUserCurrentLocationFlow().transformLatest { location ->
        emit(location)
    }
}