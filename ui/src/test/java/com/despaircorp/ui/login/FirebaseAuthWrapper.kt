package com.despaircorp.ui.login

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FirebaseAuthWrapper {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    
    fun currentUser(): FirebaseUser? {
        return auth.currentUser
    }
}