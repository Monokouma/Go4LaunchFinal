package com.despaircorp.ui.restaurants

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.despaircorp.domain.location.GetUserLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class RestaurantsViewModel @Inject constructor(
    private val getUserLocationUseCase: GetUserLocationUseCase
) : ViewModel() {

    val view = liveData(Dispatchers.IO) {
        getUserLocationUseCase.invoke().collect {
            emit(it)
        }
    }
}