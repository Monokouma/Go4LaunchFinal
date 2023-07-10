package com.despaircorp.ui.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.despaircorp.domain.user.GetUserFlowUseCase
import com.despaircorp.domain.user.SaveNewEmailAddressUseCase
import com.despaircorp.domain.user.SaveNewPasswordUseCase
import com.despaircorp.domain.user.SaveNewUserNameUseCase
import com.despaircorp.domain.user.SaveNotificationReceivingStateUseCase
import com.despaircorp.domain.utils.CoroutineDispatcherProvider
import com.despaircorp.ui.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserSettingsViewModel @Inject constructor(
    private val getUserFlowUseCase: GetUserFlowUseCase,
    private val saveNewUserNameUseCase: SaveNewUserNameUseCase,
    private val saveNewEmailAddressIUseCase: SaveNewEmailAddressUseCase,
    private val saveNewPasswordUsedCase: SaveNewPasswordUseCase,
    private val dispatcherProvider: CoroutineDispatcherProvider,
    private val saveNotificationReceivingStateUseCase: SaveNotificationReceivingStateUseCase,
) : ViewModel() {
    
    val viewAction = MutableLiveData<Event<UserSettingsAction>>()
    
    private val newUsernameMutableLiveData = MutableLiveData<String>()
    private val newEmailMutableLiveData = MutableLiveData<String>()
    private var newPassword: String? = null
    
    val viewState = liveData(dispatcherProvider.io) {
        getUserFlowUseCase.invoke().collect {
            if (it == null) {
                return@collect
            }
            emit(
                UserSettingsViewState(
                    it.hadNotificationOn
                )
            )
        }
    }
    
    fun onSwitchCheckedChange(isChecked: Boolean) {
        viewModelScope.launch(dispatcherProvider.io) {
            saveNotificationReceivingStateUseCase.invoke(isChecked)
        }
    }
    
    fun onUsernameTextChanged(newUsername: String) {
        newUsernameMutableLiveData.value = newUsername
    }
    
    fun onEmailTextChanged(newEmail: String) {
        newEmailMutableLiveData.value = newEmail
    }
    
    fun onPasswordTextChanged(newPassword: String) {
        this.newPassword = newPassword
    }
    
    fun onUsernameEditEndClicked() {
        if (newUsernameMutableLiveData.value.isNullOrEmpty()) {
            viewAction.value = Event(UserSettingsAction.UsernameError)
        } else {
            viewModelScope.launch {
                val success =
                    saveNewUserNameUseCase.invoke(newUsernameMutableLiveData.value ?: return@launch)
                
                withContext(dispatcherProvider.main) {
                    viewAction.value = Event(
                        content = if (success) {
                            UserSettingsAction.UsernameSucess
                        } else {
                            UserSettingsAction.UsernameError
                        }
                    )
                }
            }
        }
    }
    
    fun onEmailEditEndClicked() {
        if (newEmailMutableLiveData.value.isNullOrEmpty()) {
            viewAction.value = Event(UserSettingsAction.EmailError)
        } else {
            viewModelScope.launch(dispatcherProvider.io) {
                if (saveNewEmailAddressIUseCase.invoke(
                        newEmailMutableLiveData.value ?: return@launch
                    )
                ) {
                    withContext(dispatcherProvider.main) {
                        viewAction.value = Event(UserSettingsAction.EmailSucess)
                    }
                } else {
                    withContext(dispatcherProvider.main) {
                        viewAction.value = Event(UserSettingsAction.EmailError)
                    }
                }
            }
            
        }
    }
    
    fun onPasswordEditEndClicked() {
        val capturedNewPassword = newPassword
        
        if (capturedNewPassword.isNullOrBlank()) {
            viewAction.value = Event(UserSettingsAction.PasswordError)
        } else {
            viewModelScope.launch(dispatcherProvider.io) {
                val success = saveNewPasswordUsedCase.invoke(capturedNewPassword)
                
                withContext(dispatcherProvider.main) {
                    viewAction.value = if (success) {
                        Event(UserSettingsAction.PasswordSucess)
                    } else {
                        Event(UserSettingsAction.PasswordError)
                    }
                }
            }
        }
    }
}