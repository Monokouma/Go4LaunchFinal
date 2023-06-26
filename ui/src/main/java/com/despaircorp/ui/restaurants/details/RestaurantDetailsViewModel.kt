package com.despaircorp.ui.restaurants.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import com.despaircorp.domain.restaurants.details.GetRestaurantDetailsByPlaceIdUseCase
import com.despaircorp.ui.BuildConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RestaurantDetailsViewModel @Inject constructor(
    coroutineDispatcherProvider: com.despaircorp.domain.utils.CoroutineDispatcherProvider,
    private val getRestaurantDetailsByPlaceIdUseCase: GetRestaurantDetailsByPlaceIdUseCase
): ViewModel() {
    
    private val placeIdLiveData: MutableLiveData<String> = MutableLiveData()
    
    val viewState: LiveData<RestaurantDetailsViewState> = liveData(coroutineDispatcherProvider.io) {
        placeIdLiveData.asFlow().collect { placeId ->
            if (placeId == null) {
                return@collect
            }
            val restaurantDetails = getRestaurantDetailsByPlaceIdUseCase.invoke(placeId) ?: return@collect
    
            val pictureUrl = StringBuilder()
                .append("https://maps.googleapis.com/maps/api/place/photo?maxwidth=1920&maxheigth=1080&photo_reference=")
                .append(restaurantDetails.photoUrl)
                .append("&key=${BuildConfig.MAPS_API_KEY}")
                .toString()
            
            emit(
                RestaurantDetailsViewState(
                    name = restaurantDetails.name,
                    rating = restaurantDetails.rating,
                    photoUrl = pictureUrl,
                    vicinity = restaurantDetails.vicinity,
                    websiteUrl = restaurantDetails.websiteUrl,
                    phoneNumber = restaurantDetails.phoneNumber,
                    latitude = restaurantDetails.latitude,
                    longitude = restaurantDetails.longitude,
                    coworkersInside = emptyList(),
                )
            )
        }
    }
    
    fun onCreateActivity(placeId: String) {
        placeIdLiveData.value = placeId
    }
    
    
}