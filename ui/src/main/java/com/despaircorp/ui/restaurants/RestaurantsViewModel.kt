package com.despaircorp.ui.restaurants

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.despaircorp.domain.location.GetUserLocationUseCase
import com.despaircorp.domain.restaurants.GetNearbyRestaurantsWithUserLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class RestaurantsViewModel @Inject constructor(
    private val getNearbyRestaurantsWithUserLocationUseCase: GetNearbyRestaurantsWithUserLocationUseCase,
) : ViewModel() {

    val viewState: LiveData<String> = liveData(Dispatchers.IO) {
        getNearbyRestaurantsWithUserLocationUseCase.invoke().collect {
            Log.i("Monokouma", it.toString())
        }
        
        emit("test")
    }

}