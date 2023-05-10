package com.despaircorp.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.despaircorp.domain.restaurants.GetNearbyRestaurantsWithUserLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getNearbyRestaurantsWithUserLocationUseCase: GetNearbyRestaurantsWithUserLocationUseCase,
) : ViewModel() {

    val viewStateLiveData: LiveData<MapViewState> = liveData(Dispatchers.IO) {
        val mapViewStateItem = mutableListOf<MapViewStateItem>()
        
        getNearbyRestaurantsWithUserLocationUseCase.invoke().collect { restaurants ->
            restaurants.forEach {
                mapViewStateItem.add(
                    MapViewStateItem(
                        placeId = it.id,
                        name = it.name,
                        latitude = it.latitude,
                        longitude = it.longitude
                    )
                )
            }
            emit(
                MapViewState(
                    mapViewStateItems = mapViewStateItem
                )
            )
        }
    }
}