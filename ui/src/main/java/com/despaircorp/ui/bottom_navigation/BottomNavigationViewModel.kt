package com.despaircorp.ui.bottom_navigation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.despaircorp.domain.user.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class BottomNavigationViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase
): ViewModel() {

    val viewState: LiveData<BottomNavigationViewState> = liveData(Dispatchers.IO) {
        val user = getUserUseCase.invoke()
        
        emit(
            BottomNavigationViewState(
                user?.name ?: "Error",
                user?.email ?: "Error",
                user?.photoUrl
            )
        )
    }
}