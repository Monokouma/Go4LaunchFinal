package com.despaircorp.ui.workmates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.despaircorp.ui.databinding.CoworkersItemsBinding

class WorkmatesAdapter(): ListAdapter<WorkmatesViewStateItems, WorkmatesAdapter.WorkmatesViewHolder>(WorkmateDiffUtil) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = WorkmatesViewHolder (
        CoworkersItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )
    
    override fun onBindViewHolder(holder: WorkmatesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    class WorkmatesViewHolder(private val binding: CoworkersItemsBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(
            workmatesViewStateItems: WorkmatesViewStateItems
        ) {
        
        }
    }
    
    object WorkmateDiffUtil: DiffUtil.ItemCallback<WorkmatesViewStateItems>() {
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