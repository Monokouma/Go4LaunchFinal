package com.despaircorp.ui.bottom_navigation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.despaircorp.domain.user.GetUserFlowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class BottomNavigationViewModel @Inject constructor(
    private val getUserFlowUseCase: GetUserFlowUseCase
): ViewModel() {

    val viewState: LiveData<BottomNavigationViewState> = liveData(Dispatchers.IO) {
        getUserFlowUseCase.invoke().collect {
            emit(
                BottomNavigationViewState(
                    it?.name ?: "Error",
                    it?.email ?: "Error",
                    it?.photoUrl
                )
            )
        }
    }
}