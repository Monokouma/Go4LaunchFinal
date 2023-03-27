package com.despaircorp.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.despaircorp.ui.databinding.MapFragmentBinding

import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapViewFragment : Fragment(), OnMapReadyCallback, OnMapsSdkInitializedCallback {

    private lateinit var binding: MapFragmentBinding
    private val mapViewViewModel: MapViewModel by viewModels()
    private lateinit var map: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MapFragmentBinding.inflate(inflater, container, false)
        MapsInitializer.initialize(requireContext(), MapsInitializer.Renderer.LATEST, this)
        if (savedInstanceState == null) {
            val supportMapFragment = SupportMapFragment()
            parentFragmentManager.beginTransaction()
                .replace(binding.supportMapFrameLayout.id, supportMapFragment)
                .commit()
            supportMapFragment.getMapAsync(this)
        }
        return binding.root
    }

    @SuppressLint("NewApi")
    override fun onMapReady(p0: GoogleMap) {
        map = p0
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
            return
        }

        map.setMinZoomPreference(4.0f)
        map.setMaxZoomPreference(21.0f)
    }

    override fun onMapsSdkInitialized(p0: MapsInitializer.Renderer) {

    }

}