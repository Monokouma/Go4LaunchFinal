package com.despaircorp.ui.workmates

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.despaircorp.ui.R
import com.despaircorp.ui.databinding.WorkmatesFragmentBinding
import com.despaircorp.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WorkmatesFragment : Fragment(R.layout.workmates_fragment) {
    private val binding by viewBinding { WorkmatesFragmentBinding.bind(it) }
    private val viewModel: WorkmatesViewModel by viewModels()
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.test()
        Log.i("Monokouma", "lol")
    }
}