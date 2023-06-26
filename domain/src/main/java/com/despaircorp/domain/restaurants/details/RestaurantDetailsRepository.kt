package com.despaircorp.domain.restaurants.details

import com.despaircorp.domain.restaurants.details.model.RestaurantDetailsEntity

interface RestaurantDetailsRepository {
    suspend fun getRestaurantDetailsByPlaceId(placeId: String): RestaurantDetailsEntity?
}