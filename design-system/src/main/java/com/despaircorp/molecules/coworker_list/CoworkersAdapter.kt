package com.despaircorp.molecules.coworker_list

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.despaircorp.atoms.coworker.CoworkerRow
import com.despaircorp.atoms.coworker.CoworkerRowViewState

class CoworkersAdapter : ListAdapter<CoworkerRowViewState, CoworkersAdapter.CoworkerViewHolder>(WorkmateDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CoworkerViewHolder(
        CoworkerRow(parent.context),
    )

    override fun onBindViewHolder(holder: CoworkerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CoworkerViewHolder(
        private val coworkerRow: CoworkerRow,
    ) : RecyclerView.ViewHolder(coworkerRow.rootView) {
        fun bind(item: CoworkerRowViewState) {
            coworkerRow.bind(item)
        }
    }

    object WorkmateDiffUtil : DiffUtil.ItemCallback<CoworkerRowViewState>() {
        override fun areItemsTheSame(oldItem: CoworkerRowViewState, newItem: CoworkerRowViewState): Boolean = oldItem == newItem
        override fun areContentsTheSame(oldItem: CoworkerRowViewState, newItem: CoworkerRowViewState): Boolean = oldItem == newItem
    }
}