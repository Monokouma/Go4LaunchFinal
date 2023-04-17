package com.despaircorp.data.restaurants

import android.location.Location
import com.despaircorp.data.retrofit.GooglePlacesApi
import com.despaircorp.domain.restaurants.RestaurantsRepository
import com.despaircorp.domain.restaurants.model.RestaurantEntity
import javax.inject.Inject

class RestaurantsRepositoryRetrofit @Inject constructor(
    private val placesApi: GooglePlacesApi,
) : RestaurantsRepository {

    override suspend fun getNearbyRestaurantsList(location: Location): List<RestaurantEntity> {
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
                photoUrl = result.photos?.firstOrNull()?.photoReference?.let { photoReference ->
                    "http://$photoReference"
                }
            )
        }
    }
}