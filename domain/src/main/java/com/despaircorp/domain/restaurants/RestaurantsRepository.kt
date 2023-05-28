package com.despaircorp.domain.restaurants

import com.despaircorp.domain.location.model.LocationEntity
import com.despaircorp.domain.restaurants.model.RestaurantEntity

interface RestaurantsRepository {

    suspend fun getNearbyRestaurantsList(location: LocationEntity): List<RestaurantEntity>
}