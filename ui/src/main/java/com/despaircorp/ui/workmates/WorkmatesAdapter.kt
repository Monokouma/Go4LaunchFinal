package com.despaircorp.ui.workmates

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.despaircorp.ui.R
import com.despaircorp.ui.databinding.CoworkersItemsBinding

class WorkmatesAdapter() :
    ListAdapter<WorkmatesViewStateItems, WorkmatesAdapter.WorkmatesViewHolder>(WorkmateDiffUtil) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = WorkmatesViewHolder(
        binding = CoworkersItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        context = parent.context
    )
    
    override fun onBindViewHolder(holder: WorkmatesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    class WorkmatesViewHolder(
        private val binding: CoworkersItemsBinding,
        private val context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            workmatesViewStateItems: WorkmatesViewStateItems
        ) {
            if (workmatesViewStateItems.isEating) {
                binding.coworkersItemsTextViewUserName.text = StringBuilder()
                    .append(workmatesViewStateItems.name)
                    .append(" ")
                    .append(context.getString(R.string.eating_at))
                    .append(" ")
                    .append(workmatesViewStateItems.restaurantChoice)
                    .toString()
            } else {
                binding.coworkersItemsTextViewUserName.text = StringBuilder()
                    .append(workmatesViewStateItems.name)
                    .append(" ")
                    .append(context.getString(R.string.not_eating))
                    .toString()
            }
            
            
            Glide.with(binding.coworkersItemsImageViewUserImage).load(workmatesViewStateItems.image)
                .into(binding.coworkersItemsImageViewUserImage)
        }
    }
    
    object WorkmateDiffUtil : DiffUtil.ItemCallback<WorkmatesViewStateItems>() {
        override fun areItemsTheSame(
            oldItem: WorkmatesViewStateItems,
            newItem: WorkmatesViewStateItems
        ): Boolean = oldItem.name == newItem.name
        
        
        override fun areContentsTheSame(
            oldItem: WorkmatesViewStateItems,
            newItem: WorkmatesViewStateItems
        ): Boolean = oldItem == newItem
        
    }
}