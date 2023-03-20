package com.despaircorp.ui.main

sealed class LoginAction {
    object GoToMainActivity : LoginAction()
}
