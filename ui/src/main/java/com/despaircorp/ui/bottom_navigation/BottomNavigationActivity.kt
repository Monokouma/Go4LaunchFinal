package com.despaircorp.ui.bottom_navigation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.despaircorp.ui.R
import com.despaircorp.ui.databinding.BottomNavigationActivityBinding
import com.despaircorp.ui.databinding.HeaderNavigationDrawerBinding
import com.despaircorp.ui.map.MapViewFragment
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
                    loadFragment(MapViewFragment())
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
                    loadFragment(MapViewFragment())
                    true
                }
            }
        }
    
        val headerBinding =
            HeaderNavigationDrawerBinding.bind(
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
    
    companion object {
        fun navigate(context: Context): Intent {
            return Intent(context, BottomNavigationActivity::class.java)
        }
    }
}