package com.despaircorp.ui.workmates

import androidx.lifecycle.ViewModel
import com.despaircorp.domain.coworkers.GetCoworkersFromFirebaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WorkmatesViewModel @Inject constructor(
    private val getCoworkersFromFirebaseUseCase: GetCoworkersFromFirebaseUseCase
): ViewModel() {
    
    fun test() {
        getCoworkersFromFirebaseUseCase.invoke()
    }
}