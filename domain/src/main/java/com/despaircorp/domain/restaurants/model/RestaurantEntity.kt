package com.despaircorp.domain.restaurants.model

import android.location.Location


data class RestaurantEntity(
    val id: String,
    val name: String,
    val photoUrl: String?,
    val latitude: Double,
    val longitude: Double,
    val isOpennedNow: Boolean,
    val workmateInside: Int?,
    val vicinity: String,
    val rating: Double?,
)
