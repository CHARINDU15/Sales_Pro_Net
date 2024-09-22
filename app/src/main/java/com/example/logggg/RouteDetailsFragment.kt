package com.example.logggg

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.gms.maps.model.LatLngBounds

class RouteDetailsFragment : Fragment(), OnMapReadyCallback {

    companion object {
        private const val ARG_ROUTE = "route"

        fun newInstance(route: Route): RouteDetailsFragment {
            val fragment = RouteDetailsFragment()
            val args = Bundle().apply {
                putParcelable(ARG_ROUTE, route)
            }
            fragment.arguments = args
            return fragment
        }
    }

    private var route: Route? = null
    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            route = it.getParcelable(ARG_ROUTE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_route_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.routeName).text = route?.routeName
        view.findViewById<TextView>(R.id.routeDescription).text = route?.routeDescription

        // Initialize the map
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        // Enable zoom controls
        googleMap.uiSettings.isZoomControlsEnabled = true

        // Clear any existing markers, polygons, or polylines
        googleMap.clear()

        // Get route details
        route?.let { route ->
            val coordinates = route.routeCoordinates

            // Ensure there are at least 4 coordinates to form a polygon
            if (coordinates.size >= 4) {
                // Add polygon to map
                val polygonOptions = PolygonOptions()
                    .addAll(coordinates)
                    .strokeWidth(2f)
                    .fillColor(Color.parseColor("#4DADD8E6")) // Updated color resource
                googleMap.addPolygon(polygonOptions)

                // Move camera to fit the polygon
                val boundsBuilder = LatLngBounds.Builder()
                for (point in coordinates) {
                    boundsBuilder.include(point)
                }
                val bounds = boundsBuilder.build()
                val padding = 100 // Padding around the polygon
                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding))

                // Add markers for outlets
                val outletMarkers = getOutletsForRoute(route)
                for (outlet in outletMarkers) {
                    googleMap.addMarker(MarkerOptions().position(outlet).title("Outlet"))
                }
            } else {
                // Handle the case where there are fewer than 4 coordinates
                Toast.makeText(requireContext(), "Not enough coordinates to display route area.", Toast.LENGTH_LONG).show()

                // Move camera to a default location or show a message
                val defaultLocation = LatLng(6.9271, 79.9614) // Default location (e.g., Colombo)
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10f))
            }
        } ?: run {
            // Handle the case where route is null
            Toast.makeText(requireContext(), "Route data is not available.", Toast.LENGTH_LONG).show()
        }
    }


    private fun getOutletsForRoute(route: Route): List<LatLng> {
        return when (route.routeName) {
            "Route 1" -> listOf(
                LatLng(6.9280, 79.9630), // Near Colombo
                LatLng(6.9300, 79.9645), // Near Colombo
                LatLng(6.9320, 79.9680), // Near Kaduwela
                LatLng(6.9310, 79.9650), // Near Colombo
                LatLng(6.9290, 79.9660), // Near Colombo
                LatLng(6.9305, 79.9670), // Near Kaduwela
                LatLng(6.9315, 79.9685), // Near Kaduwela
                LatLng(6.9275, 79.9625), // Near Colombo
                LatLng(6.9285, 79.9640), // Near Colombo
                LatLng(6.9325, 79.9700),
                LatLng(6.9388, 79.9729),
                LatLng(6.9400, 79.9700)

            )
            "Route 2" -> listOf(
                LatLng(7.2920, 80.6350), // Near Kandy
                LatLng(7.2915, 80.6380), // Near Kandy
                LatLng(7.2950, 80.6400), // Near Peradeniya
                LatLng(7.2930, 80.6415), // Near Kandy
                LatLng(7.2970, 80.6375), // Near Peradeniya
                LatLng(7.2940, 80.6360), // Near Kandy
                LatLng(7.2960, 80.6405), // Near Peradeniya
                LatLng(7.2900, 80.6385), // Near Kandy
                LatLng(7.2980, 80.6355), // Near Peradeniya
                LatLng(7.2955, 80.6390)  // Near Peradeniya
            )
            "Route 3" -> listOf(
                LatLng(6.0350, 80.2250), // Near Galle
                LatLng(6.0360, 80.2275), // Near Galle
                LatLng(6.0420, 80.2320), // Near Hikkaduwa
                LatLng(6.0375, 80.2290), // Near Galle
                LatLng(6.0410, 80.2305), // Near Hikkaduwa
                LatLng(6.0400, 80.2240), // Near Galle
                LatLng(6.0430, 80.2285), // Near Hikkaduwa
                LatLng(6.0330, 80.2235), // Near Galle
                LatLng(6.0380, 80.2255), // Near Galle
                LatLng(6.0450, 80.2370)  // Near Hikkaduwa
            )
            "Route 4" -> listOf(
                LatLng(8.3130, 80.4070), // Near Anuradhapura
                LatLng(8.3150, 80.4085), // Near Anuradhapura
                LatLng(8.3190, 80.4100), // Near Mihintale
                LatLng(8.3175, 80.4120), // Near Anuradhapura
                LatLng(8.3205, 80.4075), // Near Mihintale
                LatLng(8.3160, 80.4095), // Near Anuradhapura
                LatLng(8.3185, 80.4115), // Near Mihintale
                LatLng(8.3120, 80.4060), // Near Anuradhapura
                LatLng(8.3215, 80.4105), // Near Mihintale
                LatLng(8.3145, 80.4080)  // Near Anuradhapura
            )
            else -> listOf() // No outlets for undefined routes
        }
    }

}
