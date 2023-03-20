package com.despaircorp.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.despaircorp.ui.R
import com.despaircorp.ui.databinding.ActivityLoginBinding
import com.despaircorp.ui.utils.viewBinding
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val binding by viewBinding { ActivityLoginBinding.inflate(it) }
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var callbackManager: CallbackManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        FacebookSdk.sdkInitialize(this)
        FirebaseApp.initializeApp(this)
        callbackManager = CallbackManager.Factory.create()
    
        binding.loginActFacebookButton.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(this, listOf("email", "public_profile"))
        }
    
        binding.loginActGoogleButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, GOOGLE_SIGN_IN)
        }
    
        binding.loginActGithubButton.setOnClickListener {
            val githubProvider = OAuthProvider.newBuilder("github.com")
            githubProvider.addCustomParameter("login", "your-email@gmail.com")
            githubProvider.scopes = listOf("user:email")
        
            val pendingResultTask = FirebaseAuth.getInstance().pendingAuthResult
            if (pendingResultTask != null) {
                pendingResultTask.addOnSuccessListener {
                    viewModel.onUserConnected()
                }.addOnFailureListener {
                    it.printStackTrace()
                }
            } else {
                FirebaseAuth.getInstance()
                    .startActivityForSignInWithProvider(this, githubProvider.build())
                    .addOnSuccessListener {
                        viewModel.onUserConnected()
                    }.addOnFailureListener {
                        it.printStackTrace()
                    }
            }
        }
    
        LoginManager.getInstance().registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    handleFacebookAccessToken(result.accessToken)
                }
            
                override fun onCancel() {
                    Toast.makeText(this@LoginActivity, "Login Cancel", Toast.LENGTH_LONG).show()
                }
            
                override fun onError(error: FacebookException) {
                    Toast.makeText(this@LoginActivity, error.message, Toast.LENGTH_LONG).show()
                }
            })
    
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("57559716164-d8pa0c4f9jr0ju11odg5amhaiphjsjp7.apps.googleusercontent.com")
            .requestEmail()
            .build()
    
        googleSignInClient = GoogleSignIn.getClient(this, gso)
    
        viewModel.loginViewActionLiveData.observe(this) {
            when (it.getContentIfNotHandled()) {
                LoginAction.GoToMainActivity -> Log.i("Monokouma", "startActivity(BottomNavigationActivity.navigate(this))")
                null -> Unit
            }
        }
    
        if (FirebaseAuth.getInstance().currentUser != null) {
           // startActivity(BottomNavigationActivity.navigate(this))
        }
    }
    
    @Suppress("OVERRIDE_DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        if (requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                e.printStackTrace()
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }
    
    private fun handleFacebookAccessToken(token: AccessToken) {
        
        val credential = FacebookAuthProvider.getCredential(token.token)
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                viewModel.onUserConnected()
            } else {
                // If sign in fails, display a message to the user.
                Toast.makeText(
                    this,
                    "Authentication failed.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
               // loginViewModel.onUserConnected()
            } else {
                task.exception?.printStackTrace()
            }
        }
    }
    
    companion object {
        private const val GOOGLE_SIGN_IN = 9001
    }
}