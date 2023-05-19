package com.despaircorp.domain.location

import com.despaircorp.domain.location.model.LocationEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserLocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    fun invoke(): Flow<LocationEntity> = locationRepository.getUserCurrentLocationFlow()
}