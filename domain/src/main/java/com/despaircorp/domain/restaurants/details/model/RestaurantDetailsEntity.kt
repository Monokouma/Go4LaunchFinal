package com.despaircorp.domain.restaurants.details.model

import com.despaircorp.domain.coworkers.model.CoworkerEntity
import com.despaircorp.domain.location.model.Latitude
import com.despaircorp.domain.location.model.Longitude

data class RestaurantDetailsEntity (
    val name: String,
    val rating: Double?,
    val photoUrl: String?,
    val vicinity: String,
    val websiteUrl: String?,
    val phoneNumber: String?,
    val latitude: Latitude,
    val longitude: Longitude,
)