package com.despaircorp.molecules.coworker_list

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.despaircorp.atoms.coworker.CoworkerRowViewState
import com.despaircorp.databinding.CoworkersListBinding

class CoworkersList @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : RecyclerView(context, attrs) {

    private val binding = CoworkersListBinding.inflate(LayoutInflater.from(context), this)
    private val coworkersAdapter = CoworkersAdapter()

    init {
        adapter = coworkersAdapter
    }

    fun bind(items: List<CoworkerRowViewState>) {
        coworkersAdapter.submitList(items)
    }
}