package com.example.logggg



import android.os.Build.VERSION_CODES.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton




class OutletsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var outletsAdapter: OutletsAdapter
    private var outlets: List<Outlet> = listOf() // Initialize with empty list

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(com.example.logggg.R.layout.fragment_outlets, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(com.example.logggg.R.id.recyclerViewOutlets)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Hardcoded sample data
        outlets = listOf(
            Outlet("1", "FOOD CITY WAGE", "Category A", "Galle", 6.0535, 80.2210),
            Outlet("2", "FOOD BBB", "Category B", "Kandy", 7.2906, 80.6337),
            Outlet("3", "FOOD CCCC", "Category C", "Colombo", 6.9271, 79.8612)
        )

        outletsAdapter = OutletsAdapter(outlets) { outlet ->
            // Handle outlet click
            openOutletDetails(outlet)
        }
        recyclerView.adapter = outletsAdapter

        view.findViewById<FloatingActionButton>(com.example.logggg.R.id.fabAddOutlet).setOnClickListener {
            // Open AddOutletFragment
            openAddOutlet()
        }
    }

    private fun openOutletDetails(outlet: Outlet) {
        val fragment = OutletDetailsFragment.newInstance(outlet)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(com.example.logggg.R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun openAddOutlet() {
        val fragment = AddOutletFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(com.example.logggg.R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}
