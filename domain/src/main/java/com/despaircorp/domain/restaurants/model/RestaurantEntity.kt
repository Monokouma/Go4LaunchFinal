package com.despaircorp.domain.restaurants.model

import com.despaircorp.domain.location.model.Latitude
import com.despaircorp.domain.location.model.Longitude


data class RestaurantEntity(
    val id: String,
    val name: String,
    val photoUrl: String?,
    val latitude: Latitude,
    val longitude: Longitude,
    val isOpenedNow: Boolean,
    val workmateInside: Int?,
    val vicinity: String,
    val rating: Double?,
)
