package com.despaircorp.data.restaurants

import android.util.Log
import com.despaircorp.data.retrofit.GooglePlacesApi
import com.despaircorp.domain.location.model.LocationEntity
import com.despaircorp.domain.restaurants.RestaurantsRepository
import com.despaircorp.domain.restaurants.model.RestaurantEntity
import javax.inject.Inject

class RestaurantsRepositoryRetrofit @Inject constructor(
    private val placesApi: GooglePlacesApi,
) : RestaurantsRepository {

    override suspend fun getNearbyRestaurantsList(location: LocationEntity): List<RestaurantEntity> {
        val restaurantsDto = placesApi.getPlaces(
            location = "${location.latitude}, ${location.longitude}",
            radius = 1_000,
            apiKey = "AIzaSyBKiwewtTkztYvFNYqUG0jQUWzUnmfHBWM",
            type = "restaurant"
        )
        
        return restaurantsDto.results.mapNotNull { result ->
            RestaurantEntity(
                id = result.placeId ?: return@mapNotNull null,
                name = result.name ?: return@mapNotNull null,
                photoUrl = result.photos?.firstOrNull()?.photoReference,
                latitude = result.geometry?.location?.lat as Double,
                longitude = result.geometry.location.lng as Double,
                isOpennedNow = result.openingHours?.openNow == true,
                workmateInside = 4,
                vicinity = result.vicinity ?: return@mapNotNull null,
                rating = result.rating as Double
            )
        }
    }
}