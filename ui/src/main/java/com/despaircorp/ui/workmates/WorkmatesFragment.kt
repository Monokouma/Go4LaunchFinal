package com.despaircorp.ui.workmates

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
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

        viewModel.workmatesViewStateLiveData.observe(viewLifecycleOwner) {
            Log.i("Monokouma", it.toString())
            binding.workmatesCoworkerList.bind(it.coworkerRowViewStates)
        }
    }
}