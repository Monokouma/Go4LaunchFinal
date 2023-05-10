package com.despaircorp.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.despaircorp.domain.restaurants.GetNearbyRestaurantsWithUserLocationUseCase
import com.despaircorp.ui.utils.CoroutineDispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
                            latitude = it.latitude,
                            longitude = it.longitude
                        )
                    }
                )
            )
        }
    }
}