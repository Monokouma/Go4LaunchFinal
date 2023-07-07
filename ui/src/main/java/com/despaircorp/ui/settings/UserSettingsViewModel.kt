package com.despaircorp.ui.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.despaircorp.domain.user.GetUserFlowUseCase
import com.despaircorp.domain.user.SaveNewEmailAddressIUseCase
import com.despaircorp.domain.user.SaveNewPasswordUsedCase
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
    private val saveNewEmailAddressIUseCase: SaveNewEmailAddressIUseCase,
    private val saveNewPasswordUsedCase: SaveNewPasswordUsedCase,
    private val dispatcherProvider: CoroutineDispatcherProvider,
    private val saveNotificationReceivingStateUseCase: SaveNotificationReceivingStateUseCase,
) : ViewModel() {
    
    val viewAction = MutableLiveData<Event<UserSettingsAction>>()
    
    private val newUsernameMutableLiveData = MutableLiveData<String>()
    private val newEmailMutableLiveData = MutableLiveData<String>()
    private val newPasswordMutableLiveData = MutableLiveData<String>()
    
    val viewState = liveData<UserSettingsViewState>(dispatcherProvider.io) {
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
        newPasswordMutableLiveData.value = newPassword
    }
    
    fun onUsernameEditEndClicked() {
        if (newUsernameMutableLiveData.value.isNullOrEmpty()) {
            viewAction.value = Event(UserSettingsAction.UsernameError)
        } else {
            viewModelScope.launch(dispatcherProvider.io) {
                if (saveNewUserNameUseCase.invoke(
                        newUsernameMutableLiveData.value ?: return@launch
                    )
                ) {
                    withContext(dispatcherProvider.main) {
                        viewAction.value = Event(UserSettingsAction.UsernameSucess)
                    }
                } else {
                    withContext(dispatcherProvider.main) {
                        viewAction.value = Event(UserSettingsAction.UsernameError)
                    }
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
        if (newPasswordMutableLiveData.value.isNullOrEmpty()) {
            viewAction.value = Event(UserSettingsAction.PasswordError)
        } else {
            viewModelScope.launch(dispatcherProvider.io) {
                if (saveNewPasswordUsedCase.invoke(
                        newPasswordMutableLiveData.value ?: return@launch
                    )
                ) {
                    withContext(dispatcherProvider.main) {
                        viewAction.value = Event(UserSettingsAction.PasswordSucess)
                    }
                } else {
                    withContext(dispatcherProvider.main) {
                        viewAction.value = Event(UserSettingsAction.PasswordError)
                    }
                }
            }
        }
    }
    
}