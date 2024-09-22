package com.example.logggg

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import kotlin.math.pow
import kotlin.math.sqrt

class OutletSelectionFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var outletsAdapter: OutletsAdapter
    private var outlets: List<Outlet> = listOf()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLatitude: Double = 6.0326
    private var currentLongitude: Double = 80.2167

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_outlet_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        recyclerView = view.findViewById(R.id.recyclerViewOutlets)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Sample outlet data
        outlets = listOf(
            Outlet("1", "FOOD CITY WAGE", "Category A", "Galle", 6.0535, 80.2210),
            Outlet("2", "FOOD BBB", "Category B", "Kandy", 7.2906, 80.6337),
            Outlet("3", "FOOD CCCC", "Category C", "Colombo", 6.9271, 79.8612),
            Outlet("4", "NearOutlet", "Category D", "Current Location", currentLatitude, currentLongitude)
        )

        outletsAdapter = OutletsAdapter(outlets) { outlet ->
            // Handle the outlet click
            checkLocationAndProceed(outlet)
        }
        recyclerView.adapter = outletsAdapter
    }

    private fun checkLocationAndProceed(outlet: Outlet) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request permission
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1001
            )
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                //val distance = calculateDistance(it.latitude, it.longitude, outlet.latitude, outlet.longitude)
                val distance = calculateDistance(currentLatitude, currentLongitude, outlet.latitude, outlet.longitude)
                if (distance <= 100) {
                    // Navigate to AddOrderDashboardFragment
                    navigateToAddOrderDashboard(outlet)
                } else {
                    Toast.makeText(requireContext(), "You are too far from the outlet!", Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                Toast.makeText(requireContext(), "Unable to retrieve location", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val radius = 6371000.0 // Earth radius in meters
        val latDiff = Math.toRadians(lat2 - lat1)
        val lonDiff = Math.toRadians(lon2 - lon1)

        val a = Math.sin(latDiff / 2).pow(2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(lonDiff / 2).pow(2)

        val c = 2 * Math.atan2(sqrt(a), sqrt(1 - a))
        return radius * c
    }

    private fun navigateToAddOrderDashboard(outlet: Outlet) {
        // Create an instance of the fragment with the outlet data
        val fragment = SampleFragment.newInstance(outlet)

        // Replace the current fragment with the new one
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null) // Add to the back stack to enable back navigation
            .commit()
    }
}
