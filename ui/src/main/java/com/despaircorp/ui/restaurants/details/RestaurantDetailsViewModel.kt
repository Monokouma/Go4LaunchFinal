package com.despaircorp.ui.restaurants.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.despaircorp.domain.restaurants.details.GetRestaurantDetailsByPlaceIdUseCase
import com.despaircorp.ui.BuildConfig
import com.despaircorp.ui.utils.CoroutineDispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RestaurantDetailsViewModel @Inject constructor(
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val getRestaurantDetailsByPlaceIdUseCase: GetRestaurantDetailsByPlaceIdUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val viewState: LiveData<RestaurantDetailsViewState> = liveData(coroutineDispatcherProvider.io) {
        val placeId = savedStateHandle.get<String>(RestaurantDetailsActivity.ARG_PLACE_ID) ?: return@liveData

        val restaurantDetails = getRestaurantDetailsByPlaceIdUseCase.invoke(placeId) ?: return@liveData

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