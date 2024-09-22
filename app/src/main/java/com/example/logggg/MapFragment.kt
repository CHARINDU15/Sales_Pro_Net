package com.example.logggg

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.PolyUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL



class MapFragment : Fragment(R.layout.fragment_map), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var outletLocation: LatLng
    private lateinit var currentLocation: LatLng

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragmentContainer) as SupportMapFragment
        mapFragment.getMapAsync(this)

        arguments?.let {
            val outletLat = it.getDouble("outletLat")
            val outletLon = it.getDouble("outletLon")
            outletLocation = LatLng(outletLat, outletLon)

            val currentLat = it.getDouble("currentLat")
            val currentLon = it.getDouble("currentLon")
            currentLocation = LatLng(currentLat, currentLon)
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isMyLocationButtonEnabled = true
        testStaticData()
        // Add markers and routes after the map is ready
        showRoute(currentLocation, outletLocation)
    }


    private fun showRoute(origin: LatLng, destination: LatLng) {
        val directionsUrl = "https://maps.googleapis.com/maps/api/directions/json?" +
                "origin=${origin.latitude},${origin.longitude}&destination=${destination.latitude},${destination.longitude}&key=AIzaSyD9mqSsEYP4w6vsIv5e3IqlldJeup5kua4"

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val result = URL(directionsUrl).readText()
                Log.d("MapFragment", "API Response: $result") // Log the entire response

                val jsonResponse = JSONObject(result)
                val routes = jsonResponse.optJSONArray("routes")

                if (routes != null && routes.length() > 0) {
                    val points = routes.getJSONObject(0).optJSONObject("overview_polyline")?.optString("points")
                    if (points != null) {
                        val polyline = PolyUtil.decode(points)
                        withContext(Dispatchers.Main) {
                            googleMap.addPolyline(PolylineOptions().addAll(polyline))
                            googleMap.addMarker(MarkerOptions().position(origin).title("Your Location"))
                            googleMap.addMarker(MarkerOptions().position(destination).title("Outlet Location"))
                            val bounds = LatLngBounds.Builder().include(origin).include(destination).build()
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
                        }
                    } else {
                        Log.e("MapFragment", "No overview_polyline points found in response.")
                    }
                } else {
                    Log.e("MapFragment", "No routes found in response.")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error fetching route: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun testStaticData() {
        val origin = LatLng(-34.0, 151.0) // Example coordinates
        val destination = LatLng(-35.0, 150.0) // Example coordinates

        googleMap.addMarker(MarkerOptions().position(origin).title("Your Location"))
        googleMap.addMarker(MarkerOptions().position(destination).title("Destination"))
        val bounds = LatLngBounds.Builder().include(origin).include(destination).build()
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
    }

    companion object {
        fun newInstance(currentLat: Double, currentLon: Double, outletLat: Double, outletLon: Double): MapFragment {
            return MapFragment().apply {
                arguments = Bundle().apply {
                    putDouble("currentLat", currentLat)
                    putDouble("currentLon", currentLon)
                    putDouble("outletLat", outletLat)
                    putDouble("outletLon", outletLon)
                }
            }
        }
    }
}
