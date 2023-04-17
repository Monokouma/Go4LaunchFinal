package com.despaircorp.data.restaurants

import android.location.Location
import com.despaircorp.data.BuildConfig
import com.despaircorp.data.LocationProviderRepository
import com.despaircorp.data.retrofit.GooglePlaceApi
import com.despaircorp.domain.restaurants.NearbyRestaurantsRepository
import com.despaircorp.domain.restaurants.model.RestaurantEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import java.util.concurrent.Executor
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class RestaurantsRepository @Inject constructor(
    private val placesApi: GooglePlaceApi,
): NearbyRestaurantsRepository {
    
    override fun getNearbyRestaurantsList(location: Location): Flow<List<RestaurantEntity>> = flow {
        
            placesApi.getPlaces(
                "${location.latitude}, ${location.longitude}",
                1_000,
                "AIzaSyBKiwewtTkztYvFNYqUG0jQUWzUnmfHBWM",
                type = "restaurant"
            ).results.let { resultsItems ->
                val restaurants: MutableList<RestaurantEntity> = ArrayList()
                if (resultsItems != null) {
                    for (results in resultsItems) {
                        if (results != null) {
                            restaurants.add(RestaurantEntity(
                                id = results.placeId ?: return@let,
                                name = results.name ?: return@let,
                                photoUrl = results.photos?.firstOrNull()?.photoReference?.let { photoReference ->
                                    "http://$photoReference"
                                }
                            ))
                        }
                    }
                    emit(restaurants)
                }
                
            }
        
    
    }
    
}