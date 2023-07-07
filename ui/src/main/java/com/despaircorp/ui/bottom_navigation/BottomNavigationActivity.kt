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
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.despaircorp.ui.R
import com.despaircorp.ui.databinding.BottomNavigationActivityBinding
import com.despaircorp.ui.databinding.HeaderNavigationDrawerBinding
import com.despaircorp.ui.map.MapFragment
import com.despaircorp.ui.restaurants.list.RestaurantsFragment
import com.despaircorp.ui.settings.UserSettingsActivity
import com.despaircorp.ui.utils.viewBinding
import com.despaircorp.ui.workmates.WorkmatesFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomNavigationActivity : AppCompatActivity() {

    companion object {
        private const val KEY_BOTTOM_NAV_BAR_SELECTED_ITEM_ID = "KEY_BOTTOM_NAV_BAR_SELECTED_ITEM_ID"
        fun navigate(context: Context) = Intent(context, BottomNavigationActivity::class.java)
    }

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
                R.id.settings -> startActivity(UserSettingsActivity.navigate(this))
                R.id.logout -> {
                    finish()
                }
            }
            binding.bottomNavigationActAppbarDrawerLayout.close()
            true
        }

        binding.bottomNavigationActBottomBar.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_restaurants -> {
                    loadFragment(RestaurantsFragment())
                    true
                }
                R.id.navigation_workmates -> {
                    loadFragment(WorkmatesFragment())
                    true
                }
                else -> {
                    loadFragment(MapFragment())
                    true
                }
            }
        }
        binding.bottomNavigationActBottomBar.selectedItemId =
            savedInstanceState?.getInt(KEY_BOTTOM_NAV_BAR_SELECTED_ITEM_ID, R.id.navigation_map) ?: R.id.navigation_map

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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(KEY_BOTTOM_NAV_BAR_SELECTED_ITEM_ID, binding.bottomNavigationActBottomBar.selectedItemId)
    }

    private fun loadFragment(fragment: Fragment) {
        val previousFragment = supportFragmentManager.findFragmentByTag(fragment.javaClass.name)
        if (previousFragment == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(binding.bottomNavigationActFrameLayout.id, fragment, fragment.javaClass.name)
                .commit()
        }
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
}