package com.despaircorp.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(

) : ViewModel() {

    val viewStateLiveData: LiveData<MapViewState> = liveData(Dispatchers.IO) {
        emit(
            MapViewState(
                listOf(
                    MapViewStateItem(
                        "ezae",
                        "Ninkasi Gerland by Nino",
                        45.7295887,
                        4.8311896,
                    )
                )
            )
        )
    }
}