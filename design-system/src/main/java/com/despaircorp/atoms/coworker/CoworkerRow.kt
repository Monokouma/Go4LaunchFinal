package com.despaircorp.atoms.coworker

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.despaircorp.databinding.CoworkerRowBinding

class CoworkerRow @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : ConstraintLayout(context, attrs) {

    private val binding = CoworkerRowBinding.inflate(LayoutInflater.from(context), this)

    fun bind(viewState: CoworkerRowViewState) {
        Glide.with(binding.coworkersItemsImageViewUserImage).load(viewState.imageUrl).into(binding.coworkersItemsImageViewUserImage)
        binding.coworkersItemsTextViewUserName.text = viewState.sentence
        setOnClickListener {
            viewState.onClick()
        }
    }
}