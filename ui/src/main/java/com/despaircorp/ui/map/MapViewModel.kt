package com.despaircorp.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.despaircorp.domain.restaurants.list.GetNearbyRestaurantsWithUserLocationUseCase
import com.despaircorp.domain.utils.CoroutineDispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getNearbyRestaurantsWithUserLocationUseCase: GetNearbyRestaurantsWithUserLocationUseCase,
    testCoroutineDispatcherProvider: CoroutineDispatcherProvider,
) : ViewModel() {

    val viewStateLiveData: LiveData<MapViewState> = liveData(testCoroutineDispatcherProvider.io) {
        getNearbyRestaurantsWithUserLocationUseCase.invoke().collect { restaurants ->
            emit(
                MapViewState(
                    mapViewStateItems = restaurants.map {
                        MapViewStateItem(
                            placeId = it.id,
                            name = it.name,
                            latitude = it.latitude.value,
                            longitude = it.longitude.value,
                        )
                    }
                )
            )
        }
    }
}