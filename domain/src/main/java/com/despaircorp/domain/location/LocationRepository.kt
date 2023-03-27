package com.despaircorp.domain.location

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun getLocation(): Flow<Location>
}