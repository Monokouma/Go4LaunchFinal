package com.despaircorp.ui.workmates

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.despaircorp.ui.R
import com.despaircorp.ui.databinding.WorkmatesFragmentBinding
import com.despaircorp.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WorkmatesFragment : Fragment(R.layout.workmates_fragment) {
    private val binding by viewBinding { WorkmatesFragmentBinding.bind(it) }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
    }
}