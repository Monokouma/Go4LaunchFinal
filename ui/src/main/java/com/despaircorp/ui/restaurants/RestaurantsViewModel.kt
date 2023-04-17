package com.despaircorp.ui.restaurants

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.despaircorp.domain.restaurants.GetNearbyRestaurantsWithUserLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RestaurantsViewModel @Inject constructor(
    private val getNearbyRestaurantsWithUserLocationUseCase: GetNearbyRestaurantsWithUserLocationUseCase,
) : ViewModel() {

    val viewState: LiveData<String> = liveData {
        getNearbyRestaurantsWithUserLocationUseCase.invoke().collect {
            Log.i("Monokouma", it.toString())
        }

        emit("test")
    }

}