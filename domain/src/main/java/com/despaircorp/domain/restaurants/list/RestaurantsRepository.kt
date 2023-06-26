package com.despaircorp.domain.restaurants.list

import com.despaircorp.domain.location.model.LocationEntity
import com.despaircorp.domain.restaurants.list.model.RestaurantEntity

interface RestaurantsRepository {

    suspend fun getNearbyRestaurantsList(location: LocationEntity): List<RestaurantEntity>
}