package dev.leonardom.evaluacionupax.feature_location.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import dev.leonardom.evaluacionupax.R
import dev.leonardom.evaluacionupax.databinding.FragmentLocationBinding
import kotlinx.coroutines.flow.collect
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class LocationFragment: Fragment() {

    private var _binding: FragmentLocationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LocationViewModel by viewModels()

    private lateinit var map: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationBinding.inflate(inflater, container, false)

        val supportMapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        supportMapFragment?.let {
            it.getMapAsync { googleMap ->
                map = googleMap
            }
        } ?: kotlin.run {
            Log.d("LocationFragment", "SupportMapFragment null")
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.lastLocation.collect { location ->
                location?.let {
                    val markerPosition = LatLng(location.latitude, location.longitude)

                    map.addMarker(
                        MarkerOptions()
                            .position(markerPosition)
                            .title("Last Location")
                    )
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(markerPosition, 10F))

                    binding.textViewLastLocationLatitude.text = "Latitud: ${location.latitude}"
                    binding.textViewLastLocationLongitude.text = "Longitud: ${location.longitude}"

                    location.timestamp?.let {
                        val dateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.ROOT)
                        val date = it.toDate()
                        binding.textViewLastLocationDate.text = "Fecha: ${dateFormat.format(date)}"
                    }
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.getLastLocation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}