package com.despaircorp.ui.restaurants

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.despaircorp.domain.location.GetDistanceBetweenUserAndPlacesUseCase
import com.despaircorp.domain.location.GetUserLocationUseCase
import com.despaircorp.domain.restaurants.GetNearbyRestaurantsWithUserLocationUseCase
import com.despaircorp.ui.R
import com.despaircorp.domain.utils.CoroutineDispatcherProvider
import com.despaircorp.ui.utils.NativeText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class RestaurantsViewModel @Inject constructor(
    private val getNearbyRestaurantsWithUserLocationUseCase: GetNearbyRestaurantsWithUserLocationUseCase,
    private val getUserLocationUseCase: GetUserLocationUseCase,
    private val getDistanceBetweenUserAndPlacesUseCase: GetDistanceBetweenUserAndPlacesUseCase,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : ViewModel() {

    val viewState: LiveData<RestaurantsViewState> = liveData(coroutineDispatcherProvider.io) {
        if (latestValue == null) {
            emit(
                RestaurantsViewState(
                    restaurants = emptyList(),
                    isSpinnerVisible = true,
                )
            )
        }

        combine(
            getNearbyRestaurantsWithUserLocationUseCase.invoke(),
            getUserLocationUseCase.invoke()
        ) { nearbyRestaurants, userLocation ->

            emit(
                RestaurantsViewState(
                    restaurants = nearbyRestaurants.map { nearbyRestaurant ->
                        val distance = getDistanceBetweenUserAndPlacesUseCase.invoke(
                            userLat = userLocation.latitude,
                            userLong = userLocation.longitude,
                            restaurantLat = nearbyRestaurant.latitude,
                            restaurantLong = nearbyRestaurant.longitude,
                        )
                        val isOpenNow = nearbyRestaurant.isOpennedNow
                        val pictureUrl = StringBuilder()
                            .append("https://maps.googleapis.com/maps/api/place/photo?maxwidth=1920&maxheigth=1080&photo_reference=")
                            .append(nearbyRestaurant.photoUrl)
                            .append("&key=AIzaSyBKiwewtTkztYvFNYqUG0jQUWzUnmfHBWM")
                            .toString()

                        RestaurantsViewStateItems(
                            restaurantName = nearbyRestaurant.name,
                            restaurantDistance = "${distance}m",
                            restaurantImageUrl = pictureUrl,
                            restaurantAddressAndType = nearbyRestaurant.vicinity,
                            workmatesInside = "${nearbyRestaurant.workmateInside}",
                            restaurantSchedule = if (isOpenNow) {
                                NativeText.Resource(R.string.opened)
                            } else {
                                NativeText.Resource(R.string.closed)
                            },
                            restaurantStar = nearbyRestaurant.rating,
                            openedTextColorRes = if (isOpenNow) {
                                R.color.shamrock_green
                            } else {
                                R.color.rusty_red
                            },
                            placeId = nearbyRestaurant.id
                        )
                    },
                    isSpinnerVisible = false,
                )
            )
        }.collect()
    }
}