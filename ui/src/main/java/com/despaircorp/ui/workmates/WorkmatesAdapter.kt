package com.despaircorp.ui.workmates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.despaircorp.ui.databinding.CoworkersItemsBinding

class WorkmatesAdapter : ListAdapter<WorkmatesViewStateItem, WorkmatesAdapter.WorkmatesViewHolder>(WorkmateDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = WorkmatesViewHolder(
        CoworkersItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false),
    )

    override fun onBindViewHolder(holder: WorkmatesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class WorkmatesViewHolder(
        private val binding: CoworkersItemsBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: WorkmatesViewStateItem) {
            binding.coworkersItemsTextViewUserName.setText(item.sentence)
//            if (workmatesViewStateItem.isEating) {
//                binding.coworkersItemsTextViewUserName.text = StringBuilder()
//                    .append(workmatesViewStateItem.name)
//                    .append(" ")
//                    .append(binding.coworkersItemsTextViewUserName.context.getString(R.string.eating_at))
//                    .append(" ")
//                    .append(workmatesViewStateItem.restaurantChoice)
//                    .toString()
//            } else {
//                binding.coworkersItemsTextViewUserName.text = StringBuilder()
//                    .append(workmatesViewStateItem.name)
//                    .append(" ")
//                    .append(context.getString(R.string.not_eating))
//                    .toString()
//            }


            Glide.with(binding.coworkersItemsImageViewUserImage)
                .load(item.image)
                .into(binding.coworkersItemsImageViewUserImage)
        }
    }

    object WorkmateDiffUtil : DiffUtil.ItemCallback<WorkmatesViewStateItem>() {
        override fun areItemsTheSame(oldItem: WorkmatesViewStateItem, newItem: WorkmatesViewStateItem): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: WorkmatesViewStateItem, newItem: WorkmatesViewStateItem): Boolean = oldItem == newItem
    }
}