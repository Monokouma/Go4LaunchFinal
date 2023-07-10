package com.despaircorp.data.restaurants.details

import com.despaircorp.data.BuildConfig
import com.despaircorp.data.retrofit.GooglePlacesApi
import com.despaircorp.domain.location.model.Latitude
import com.despaircorp.domain.location.model.Longitude
import com.despaircorp.domain.restaurants.details.RestaurantDetailsRepository
import com.despaircorp.domain.restaurants.details.model.RestaurantDetailsEntity
import javax.inject.Inject

class RestaurantDetailsRetrofitRepository @Inject constructor(
    private val placesApi: GooglePlacesApi,
) : RestaurantDetailsRepository {

    override suspend fun getRestaurantDetailsByPlaceId(placeId: String): RestaurantDetailsEntity? {
        val restaurantDetailsDto = placesApi.getPlacesDetails(
            apiKey = BuildConfig.MAPS_API_KEY,
            placeId = placeId
        )

        return RestaurantDetailsEntity(
            name = restaurantDetailsDto?.result?.name ?: return null,
            rating = restaurantDetailsDto.result.rating as Double,
            photoUrl = restaurantDetailsDto.result.photos?.firstOrNull()?.photoReference,
            vicinity = restaurantDetailsDto.result.vicinity ?: return null,
            websiteUrl = restaurantDetailsDto.result.url,
            phoneNumber = restaurantDetailsDto.result.formattedPhoneNumber,
            latitude = restaurantDetailsDto.result.geometry?.location?.lat?.let { Latitude(it) } ?: return null,
            longitude = restaurantDetailsDto.result.geometry.location.lng?.let { Longitude(it) } ?: return null,
        )
    }
}