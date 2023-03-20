package com.despaircorp.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.despaircorp.ui.R
import com.despaircorp.ui.databinding.ActivityLoginBinding
import com.despaircorp.ui.utils.viewBinding

class LoginActivity : AppCompatActivity() {
    private val binding by viewBinding { ActivityLoginBinding.inflate(it) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}