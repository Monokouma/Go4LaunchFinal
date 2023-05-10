package com.despaircorp.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.despaircorp.domain.user.SaveCurrentUserUseCase
import com.despaircorp.ui.utils.CoroutineDispatcherProvider
import com.despaircorp.ui.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val saveCurrentUserUseCase: SaveCurrentUserUseCase,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : ViewModel() {

    val loginViewActionLiveData = MutableLiveData<Event<LoginAction>>()

    fun onUserConnected() {
        viewModelScope.launch(coroutineDispatcherProvider.io) {
            val success = saveCurrentUserUseCase.invoke()

            withContext(Dispatchers.Main) {
                loginViewActionLiveData.value = if (success) {
                    Event(LoginAction.GoToMainActivity)
                } else {
                    Event(LoginAction.ErrorMessage)
                }
            }
        }
    }
}