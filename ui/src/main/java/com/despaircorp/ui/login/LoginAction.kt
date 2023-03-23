package com.despaircorp.ui.login

sealed class LoginAction {
    object GoToMainActivity : LoginAction()
    object ErrorMessage : LoginAction()
}
