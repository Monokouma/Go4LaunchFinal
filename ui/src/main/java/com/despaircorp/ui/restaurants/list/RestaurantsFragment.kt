package com.despaircorp.ui.restaurants.list

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.despaircorp.ui.R
import com.despaircorp.ui.databinding.RestaurantsFragmentBinding
import com.despaircorp.ui.restaurants.details.RestaurantDetailsActivity
import com.despaircorp.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantsFragment : Fragment(R.layout.restaurants_fragment), RestaurantsListener {
    private val binding by viewBinding { RestaurantsFragmentBinding.bind(it) }
    private val viewModel: RestaurantsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val restaurantAdapter = RestaurantsAdapter(this)
        binding.restaurantListFragRecyclerView.adapter = restaurantAdapter
        viewModel.viewState.observe(viewLifecycleOwner) {
            restaurantAdapter.submitList(it.restaurants)
            binding.restaurantListFragProgressIndicator.isVisible = it.isSpinnerVisible
        }
    }

    override fun onRestaurantClick(placeId: String) {
        startActivity(RestaurantDetailsActivity.navigate(requireContext(), placeId = placeId))
    }
}