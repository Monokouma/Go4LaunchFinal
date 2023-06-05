package com.despaircorp.data.restaurants

import android.app.Application
import com.despaircorp.data.BuildConfig
import com.despaircorp.data.R
import com.despaircorp.data.retrofit.GooglePlacesApi
import com.despaircorp.domain.location.model.Latitude
import com.despaircorp.domain.location.model.LocationEntity
import com.despaircorp.domain.location.model.Longitude
import com.despaircorp.domain.restaurants.RestaurantsRepository
import com.despaircorp.domain.restaurants.model.RestaurantEntity
import javax.inject.Inject

class RestaurantsRepositoryRetrofit @Inject constructor(
    private val placesApi: GooglePlacesApi,
) : RestaurantsRepository {

    override suspend fun getNearbyRestaurantsList(location: LocationEntity): List<RestaurantEntity> {
        val restaurantsDto = placesApi.getPlaces(
            location = "${location.latitude.value}, ${location.longitude.value}",
            radius = 1_000,
            apiKey = "AIzaSyBKiwewtTkztYvFNYqUG0jQUWzUnmfHBWM",
            type = "restaurant"
        )

        return restaurantsDto.results.mapNotNull { result ->
            RestaurantEntity(
                id = result.placeId ?: return@mapNotNull null,
                name = result.name ?: return@mapNotNull null,
                photoUrl = result.photos?.firstOrNull()?.photoReference,
                latitude = result.geometry?.location?.lat?.let { Latitude(it) } ?:return@mapNotNull null,
                longitude = result.geometry.location.lng?.let { Longitude(it) } ?:return@mapNotNull null,
                isOpenedNow = result.openingHours?.openNow == true,
                workmateInside = 4,
                vicinity = result.vicinity ?: return@mapNotNull null,
                rating = result.rating as Double
            )
        }
    }
}