package com.despaircorp.domain.restaurants

import android.location.Location
import com.despaircorp.domain.restaurants.model.RestaurantEntity
import kotlinx.coroutines.flow.Flow

interface NearbyRestaurantsRepository {
    
    fun getNearbyRestaurantsList(location: Location): Flow<List<RestaurantEntity>>
}