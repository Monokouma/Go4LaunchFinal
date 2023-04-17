package com.despaircorp.ui.bottom_navigation

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.despaircorp.ui.R
import com.despaircorp.ui.databinding.BottomNavigationActivityBinding
import com.despaircorp.ui.databinding.HeaderNavigationDrawerBinding
import com.despaircorp.ui.map.MapFragment
import com.despaircorp.ui.restaurants.RestaurantsFragment
import com.despaircorp.ui.utils.viewBinding
import com.despaircorp.ui.workmates.WorkmatesFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomNavigationActivity : AppCompatActivity() {
    private val binding by viewBinding { BottomNavigationActivityBinding.inflate(it) }
    private val viewModel: BottomNavigationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.bottomNavigationActToolbar)
        getLocationPermission()
        binding.bottomNavigationActToolbar.setNavigationOnClickListener {
            binding.bottomNavigationActAppbarDrawerLayout.open()
        }

        binding.bottomNavigationActNavigationViewProfile.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.your_lunch -> Log.i("Monokouma", "your lunch")
                R.id.settings -> Log.i("Monokouma", "settings")
                R.id.logout -> {
                    finish()
                }
            }
            binding.bottomNavigationActAppbarDrawerLayout.close()
            true
        }

        binding.bottomNavigationActBottomBar.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    loadFragment(MapFragment())
                    true
                }
                R.id.navigation_dashboard -> {
                    loadFragment(RestaurantsFragment())
                    true
                }
                R.id.navigation_notifications -> {
                    loadFragment(WorkmatesFragment())
                    true
                }
                else -> {
                    loadFragment(MapFragment())
                    true
                }
            }
        }

        val headerBinding = HeaderNavigationDrawerBinding.bind(
            binding.bottomNavigationActNavigationViewProfile.getHeaderView(0)
        )

        viewModel.viewState.observe(this) { viewState ->
            Glide.with(headerBinding.navigationDrawerImageViewUserImage)
                .load(viewState.userProfilePictureUrl)
                .into(headerBinding.navigationDrawerImageViewUserImage)

            headerBinding.navigationDrawerTextViewUserName.text = viewState.userName
            headerBinding.navigationDrawerTextViewUserMail.text = viewState.userEmailAddress
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(binding.bottomNavigationActFrameLayout.id, fragment)
            .commit()
    }
    
    private fun getLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
            return
        }
    }

    companion object {
        fun navigate(context: Context): Intent {
            return Intent(context, BottomNavigationActivity::class.java)
        }
    }
}