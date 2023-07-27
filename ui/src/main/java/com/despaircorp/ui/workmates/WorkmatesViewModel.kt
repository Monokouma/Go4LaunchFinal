package com.despaircorp.ui.workmates

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.despaircorp.atoms.coworker.CoworkerRowMapper
import com.despaircorp.domain.coworkers.GetCoworkersFromFirebaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WorkmatesViewModel @Inject constructor(
    private val coworkerRowMapper: CoworkerRowMapper,
    private val getCoworkersFromFirebaseUseCase: GetCoworkersFromFirebaseUseCase,
) : ViewModel() {

    val workmatesViewStateLiveData: LiveData<WorkmatesViewState> = liveData {
        getCoworkersFromFirebaseUseCase.invoke().collect { coworkers ->
            emit(
                WorkmatesViewState(
                    coworkerRowViewStates = coworkers.map { coworkerEntity ->
                        coworkerRowMapper.map(
                            imageUrl = coworkerEntity.photoUrl,
                            sentence = coworkerEntity.email,
                            onClick = {
                                android.util.Log.d("Mono", "WorkmatesViewModel.row clicked")
                            }
                        )
                    }
                )
            )
        }
    }
}