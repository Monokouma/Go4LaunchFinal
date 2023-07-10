package com.despaircorp.ui.settings

sealed class UserSettingsAction {
    
    object UsernameError : UserSettingsAction()
    object UsernameSucess : UserSettingsAction()
    
    object EmailError : UserSettingsAction()
    object EmailSucess : UserSettingsAction()
    
    object PasswordError : UserSettingsAction()
    object PasswordSucess : UserSettingsAction()
}