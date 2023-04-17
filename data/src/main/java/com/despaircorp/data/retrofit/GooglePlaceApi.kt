package com.despaircorp.data.retrofit

import com.despaircorp.data.restaurants.RestaurantsDto
import retrofit2.http.GET
import retrofit2.http.Query

interface GooglePlaceApi {
    @GET("maps/api/place/nearbysearch/json")
    suspend fun getPlaces(
        @Query("location") location: String,
        @Query("radius") radius: Int,
        @Query("key") apiKey: String,
        @Query("type") type: String
    ): RestaurantsDto
}