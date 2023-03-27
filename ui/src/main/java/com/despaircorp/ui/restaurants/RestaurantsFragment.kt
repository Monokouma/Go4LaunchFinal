package com.despaircorp.ui.restaurants

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.despaircorp.ui.R
import com.despaircorp.ui.databinding.MapFragmentBinding
import com.despaircorp.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantsFragment : Fragment(R.layout.restaurants_fragment) {
    private val binding by viewBinding { MapFragmentBinding.bind(it) }
    private val viewModel: RestaurantsViewModel by viewModels()
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
    }
}