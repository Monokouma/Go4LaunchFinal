package com.despaircorp.ui.restaurants.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.despaircorp.ui.databinding.RestaurantsItemsBinding
import com.despaircorp.ui.utils.setText
import com.despaircorp.ui.utils.setTextColorRes


class RestaurantsAdapter(
    private val restaurantsListener: RestaurantsListener
) : ListAdapter<RestaurantsViewStateItems, RestaurantsAdapter.RestaurantViewHolder>(
    RestaurantDiffUtil
) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RestaurantViewHolder(
        RestaurantsItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )
    
    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        holder.bind(getItem(position), restaurantsListener)
    }
    
    class RestaurantViewHolder(private val binding: RestaurantsItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(
            restaurantListViewStateItems: RestaurantsViewStateItems,
            listener: RestaurantsListener
        ) {
            binding.root.setOnClickListener {
                listener.onRestaurantClick(restaurantListViewStateItems.placeId)
            }
            Glide.with(binding.restaurantsItemsImageViewPhoto)
                .load(restaurantListViewStateItems.restaurantImageUrl)
                .into(binding.restaurantsItemsImageViewPhoto)
            binding.restaurantListItemsRestaurantNameTv.text =
                restaurantListViewStateItems.restaurantName
            binding.restaurantListItemsRestaurantDistanceTv.text =
                restaurantListViewStateItems.restaurantDistance
            binding.restaurantListItemsRestaurantAdressAndTypeTv.text =
                restaurantListViewStateItems.restaurantAddressAndType
            binding.restaurantListItemsWorkmateInsideTv.text =
                restaurantListViewStateItems.workmatesInside
            binding.restaurantListItemsRestaurantOpenSchedule.setText(restaurantListViewStateItems.restaurantSchedule)
            binding.restaurantListItemsRestaurantOpenSchedule.setTextColorRes(
                restaurantListViewStateItems.openedTextColorRes
            )
            binding.restaurantListItemsRestaurantStarRating.rating =
                restaurantListViewStateItems.restaurantStar?.toFloat() ?: 0f
        }
    }
    
    object RestaurantDiffUtil : DiffUtil.ItemCallback<RestaurantsViewStateItems>() {
        override fun areItemsTheSame(
            oldItem: RestaurantsViewStateItems,
            newItem: RestaurantsViewStateItems
        ): Boolean =
            oldItem.placeId == newItem.placeId
        
        override fun areContentsTheSame(
            oldItem: RestaurantsViewStateItems,
            newItem: RestaurantsViewStateItems
        ) = oldItem == newItem
    }
}