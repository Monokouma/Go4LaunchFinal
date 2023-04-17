package com.despaircorp.domain.restaurants

import android.location.Location
import com.despaircorp.domain.restaurants.model.RestaurantEntity

interface RestaurantsRepository {
    
    suspend fun getNearbyRestaurantsList(location: Location): List<RestaurantEntity>
}