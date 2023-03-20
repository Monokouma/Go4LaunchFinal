package com.despaircorp.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.despaircorp.domain.user.SaveUserUseCase
import com.despaircorp.ui.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val saveUserUseCase: SaveUserUseCase
): ViewModel() {
    
    val loginViewActionLiveData = MutableLiveData<Event<LoginAction>>()
    
    fun onUserConnected() {
        viewModelScope.launch(Dispatchers.IO) {
            saveUserUseCase.invoke()
            
            withContext(Dispatchers.Main) {
                loginViewActionLiveData.value = Event(LoginAction.GoToMainActivity)
            }
        }
    }
}