package com.example.logggg

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment

class SalesOrderFragment : Fragment() {

    private lateinit var totalSaleCard: CardView
    private lateinit var totalSaleLabel: TextView
    private lateinit var totalSaleValue: TextView
    private lateinit var totalDaySaleCard: CardView
    private lateinit var totalDaySaleLabel: TextView
    private lateinit var totalDaySaleValue: TextView
    private lateinit var toggleAddSalesOrder: Button
    private lateinit var toggleViewSalesOrder: Button
    private lateinit var toggleImmediateSalesOrder: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sales_order, container, false)

        // Initialize UI elements
        totalSaleCard = view.findViewById(R.id.totalSaleCard)
        totalSaleLabel = view.findViewById(R.id.totalSaleLabel)
        totalSaleValue = view.findViewById(R.id.totalSaleValue)
        totalDaySaleCard = view.findViewById(R.id.totalDaySaleCard)
        totalDaySaleLabel = view.findViewById(R.id.totalDaySaleLabel)
        totalDaySaleValue = view.findViewById(R.id.totalDaySaleValue)
        toggleAddSalesOrder = view.findViewById(R.id.toggleAddSalesOrder)
        toggleViewSalesOrder = view.findViewById(R.id.toggleViewSalesOrder)
        toggleImmediateSalesOrder = view.findViewById(R.id.toggleImmediateSalesOrder)

        // Set up button click listeners
        toggleAddSalesOrder.setOnClickListener {

            val fragment = OutletSelectionFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)  // Make sure this is your container for fragments
                .addToBackStack(null)  // Add to backstack if you want to allow the user to go back
                .commit()
        }

        toggleViewSalesOrder.setOnClickListener {
            // Handle View Sales Order button click
        }

        toggleImmediateSalesOrder.setOnClickListener {
            // Handle Add Immediate Sales Order button click
        }

        return view
    }
}
