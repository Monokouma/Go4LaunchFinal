package com.despaircorp.ui.workmates

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.despaircorp.domain.coworkers.GetCoworkersFromFirebaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WorkmatesViewModel @Inject constructor(
    private val getCoworkersFromFirebaseUseCase: GetCoworkersFromFirebaseUseCase,
) : ViewModel() {
    
    val workmatesViewStateLiveData: LiveData<WorkmatesViewState> =
        liveData() {
            getCoworkersFromFirebaseUseCase.invoke().collect { coworkers ->
                emit(
                    WorkmatesViewState(
                        workmatesViewStateItems = coworkers.map { coworkerEntity ->
                            WorkmatesViewStateItems(
                                name = coworkerEntity.name,
                                isEating = coworkerEntity.eating,
                                restaurantChoice = coworkerEntity.email,
                                image = coworkerEntity.photoUrl,
                            )
                        }
                    )
                )
            }
            
        }
    
    fun test() {
        getCoworkersFromFirebaseUseCase.invoke()
    }
}