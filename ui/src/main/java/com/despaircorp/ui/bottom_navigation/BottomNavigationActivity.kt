package com.despaircorp.ui.bottom_navigation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.despaircorp.ui.R
import com.despaircorp.ui.databinding.BottomNavigationActivityBinding
import com.despaircorp.ui.databinding.HeaderNavigationDrawerBinding
import com.despaircorp.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomNavigationActivity : AppCompatActivity() {
    private val binding by viewBinding { BottomNavigationActivityBinding.inflate(it) }
    private val viewModel: BottomBarViewModel by viewModels()
    
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
               //     loadFragment(MapViewFragment())
                    true
                }
                R.id.navigation_dashboard -> {
                //    loadFragment(RestaurantsFragment())
                    true
                }
                R.id.navigation_notifications -> {
                //    loadFragment(WorkmatesFragment())
                    true
                }
                else -> {
                //    loadFragment(MapViewFragment())
                    true
                }
            }
        }
    
        val headerBinding =
            HeaderNavigationDrawerBinding.bind(
                binding.bottomNavigationActNavigationViewProfile.getHeaderView(0)
            )
    }
    
    companion object {
        fun navigate(context: Context): Intent {
            return Intent(context, BottomNavigationActivity::class.java)
        }
    }
}