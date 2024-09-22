package com.example.logggg
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RoutesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var routesAdapter: RoutesAdapter
    private var routes: List<Route> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_routes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerViewRoutes)
        recyclerView.layoutManager = LinearLayoutManager(context)
        // Sample hardcoded routes with coordinates
        val routes = listOf(
            Route(
                "Route 1",
                "Colombo to Kaduwela",
                listOf(
                    LatLng(6.9271, 79.9614), // Colombo (Southwest corner)
                    LatLng(6.9271, 79.9700), // Colombo (Northwest corner)
                    LatLng(6.9330, 79.9700), // Kaduwela (Northeast corner)
                    LatLng(6.9330, 79.9614)  // Kaduwela (Southeast corner)
                )
            ),
            Route(
                "Route 2",
                "Kandy to Peradeniya",
                listOf(
                    LatLng(7.2906, 80.6337), // Kandy (Southwest corner)
                    LatLng(7.2906, 80.6430), // Kandy (Northwest corner)
                    LatLng(7.3000, 80.6430), // Peradeniya (Northeast corner)
                    LatLng(7.3000, 80.6337)  // Peradeniya (Southeast corner)
                )
            ),
            Route(
                "Route 3",
                "Galle to Hikkaduwa",
                listOf(
                    LatLng(6.0325, 80.2207), // Galle (Southwest corner)
                    LatLng(6.0325, 80.2400), // Galle (Northwest corner)
                    LatLng(6.0450, 80.2400), // Hikkaduwa (Northeast corner)
                    LatLng(6.0450, 80.2207)  // Hikkaduwa (Southeast corner)
                )
            ),
            Route(
                "Route 4",
                "Anuradhapura to Mihintale",
                listOf(
                    LatLng(8.3110, 80.4037), // Anuradhapura (Southwest corner)
                    LatLng(8.3110, 80.4130), // Anuradhapura (Northwest corner)
                    LatLng(8.3210, 80.4130), // Mihintale (Northeast corner)
                    LatLng(8.3210, 80.4037)  // Mihintale (Southeast corner)
                )
            )
        )




        routesAdapter = RoutesAdapter(routes) { route ->
            openRouteDetails(route)
        }
        recyclerView.adapter = routesAdapter

        view.findViewById<FloatingActionButton>(R.id.fabAddRoute).setOnClickListener {
            // Handle add route click
            Toast.makeText(context, "Add new route functionality", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openRouteDetails(route: Route) {
        val fragment = RouteDetailsFragment.newInstance(route)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}
