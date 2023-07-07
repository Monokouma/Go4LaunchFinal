package com.despaircorp.ui.restaurants.details

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.despaircorp.ui.databinding.ActivityRestaurantDetailsBinding
import com.despaircorp.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantDetailsActivity : AppCompatActivity() {
    private val binding by viewBinding { ActivityRestaurantDetailsBinding.inflate(it) }
    private val viewModel: RestaurantDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.restaurantDetailsToolBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.restaurantDetailsToolBar.setNavigationOnClickListener {
            finish()
        }

        viewModel.viewState.observe(this) { viewState ->
            binding.restaurantDetailsTextViewRestaurantName.text = viewState.name
            binding.restaurantDetailsRatingBar.rating = viewState.rating?.toFloat() ?: 0f
            Glide.with(binding.restaurantDetailsImageViewRestaurantImage)
                .load(viewState.photoUrl)
                .into(binding.restaurantDetailsImageViewRestaurantImage)
            binding.restaurantDetailsTextViewRestaurantType.text = viewState.vicinity

            binding.restaurantDetailsViewWebsite.setOnClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(viewState.websiteUrl))
                startActivity(browserIntent)
            }

            binding.restaurantDetailsViewStar.setOnClickListener {

            }

            binding.restaurantDetailsViewPhone.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:" + viewState.phoneNumber)
                startActivity(intent)
            }
        }
    }

    companion object {
        const val ARG_PLACE_ID = "ARG_PLACE_ID"

        fun navigate(
            context: Context,
            placeId: String
        ): Intent = Intent(
            context,
            RestaurantDetailsActivity::class.java
        ).apply {
            putExtra(ARG_PLACE_ID, placeId)
        }
    }
}