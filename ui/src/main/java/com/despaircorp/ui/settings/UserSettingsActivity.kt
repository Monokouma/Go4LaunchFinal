package com.despaircorp.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.transition.TransitionManager
import com.despaircorp.ui.R
import com.despaircorp.ui.databinding.UserSettingsActivityBinding
import com.despaircorp.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UserSettingsActivity : AppCompatActivity() {
    private val binding by viewBinding { UserSettingsActivityBinding.inflate(it) }
    private val viewModel: UserSettingsViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.userSettingsToolBar)
        
        binding.userSettingsToolBar.setNavigationOnClickListener {
            finish()
        }
        
        binding.userSettingsNotifSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onSwitchCheckedChange(isChecked)
        }
        
        binding.userSettingsUsernameButton.setOnClickListener {
            changeVisibilityWithAnimation(binding.userSettingsUsernameTIL)
            changeVisibilityWithAnimation(binding.userSettingsUsernameSaveButton)
        }
        
        binding.userSettingsEmailButton.setOnClickListener {
            changeVisibilityWithAnimation(binding.userSettingsEmailSaveButton)
            changeVisibilityWithAnimation(binding.userSettingsEmailTIL)
        }
        
        binding.userSettingsPasswordButton.setOnClickListener {
            changeVisibilityWithAnimation(binding.userSettingsPasswordTIL)
            changeVisibilityWithAnimation(binding.userSettingsPasswordSaveButton)
        }
        
        binding.userSettingsUsernameSaveButton.setOnClickListener {
            viewModel.onUsernameEditEndClicked()
        }
        
        binding.userSettingsEmailSaveButton.setOnClickListener {
            viewModel.onEmailEditEndClicked()
        }
        
        binding.userSettingsPasswordSaveButton.setOnClickListener {
            viewModel.onPasswordEditEndClicked()
        }
        
        binding.userSettingsUsernameTIET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            
            override fun afterTextChanged(s: Editable?) {
                viewModel.onUsernameTextChanged(s.toString())
            }
            
        })
        
        binding.userSettingsEmailTIET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            
            override fun afterTextChanged(s: Editable?) {
                viewModel.onEmailTextChanged(s.toString())
            }
            
        })
        
        binding.userSettingsPasswordTIET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            
            override fun afterTextChanged(s: Editable?) {
                viewModel.onPasswordTextChanged(s.toString())
            }
            
        })
        
        viewModel.viewAction.observe(this) {
            when (it.getContentIfNotHandled()) {
                UserSettingsAction.UsernameError -> {
                    makeToast(getString(R.string.error_in_new_username))
                }
                
                UserSettingsAction.EmailError -> {
                    makeToast(getString(R.string.error_in_new_email))
                }
                
                UserSettingsAction.PasswordError -> {
                    makeToast(getString(R.string.error_in_new_password))
                }
                
                UserSettingsAction.UsernameSucess -> {
                    makeToast(getString(R.string.changed_with_success))
                    changeVisibilityWithAnimation(binding.userSettingsUsernameTIL)
                    changeVisibilityWithAnimation(binding.userSettingsUsernameSaveButton)
                }
                
                UserSettingsAction.EmailSucess -> {
                    makeToast(getString(R.string.changed_with_success))
                    changeVisibilityWithAnimation(binding.userSettingsEmailSaveButton)
                    changeVisibilityWithAnimation(binding.userSettingsEmailTIL)
                }
                
                UserSettingsAction.PasswordSucess -> {
                    makeToast(getString(R.string.changed_with_success))
                    changeVisibilityWithAnimation(binding.userSettingsPasswordTIL)
                    changeVisibilityWithAnimation(binding.userSettingsPasswordSaveButton)
                }
                
                else -> Unit
            }
        }
        
        viewModel.viewState.observe(this) {
            binding.userSettingsNotifSwitch.isChecked = it.isNotificationEnabled
        }
    }
    
    private fun makeToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    
    private fun changeVisibilityWithAnimation(view: View) {
        val isViewActuallyVisible = view.visibility == View.VISIBLE
        TransitionManager.endTransitions(binding.root)
        TransitionManager.beginDelayedTransition(binding.root)
        if (isViewActuallyVisible) {
            view.visibility = View.GONE
        } else {
            view.visibility = View.VISIBLE
        }
    }
    
    companion object {
        fun navigate(context: Context) = Intent(
            context,
            UserSettingsActivity::class.java
        )
    }
}