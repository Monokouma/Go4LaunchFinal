package com.despaircorp.ui.workmates

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.despaircorp.domain.coworkers.GetCoworkersFromFirebaseUseCase
import com.despaircorp.domain.utils.CoroutineDispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WorkmatesViewModel @Inject constructor(
    private val getCoworkersFromFirebaseUseCase: GetCoworkersFromFirebaseUseCase,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    
    ): ViewModel() {
    
    val workmatesViewStateLiveData: LiveData<WorkmatesViewState> = liveData(coroutineDispatcherProvider.io) {
        getCoworkersFromFirebaseUseCase.invoke()
        
    }
    
    fun test() {
        getCoworkersFromFirebaseUseCase.invoke()
    }
}