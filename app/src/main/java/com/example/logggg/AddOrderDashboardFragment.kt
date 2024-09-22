package com.example.logggg

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AddOrderDashboardFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var orderSummary: TextView
    private val itemList = mutableListOf<Item>()
    private val orderItems = mutableMapOf<String, Int>() // Maps item IDs to quantities

    private lateinit var outlet: Outlet

    companion object {
        private const val ARG_OUTLET = "outlet"

        fun newInstance(outlet: Outlet): AddOrderDashboardFragment {
            val fragment = AddOrderDashboardFragment()
            val args = Bundle().apply {
                putParcelable(ARG_OUTLET, outlet)
            }
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_order_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        recyclerView = view.findViewById(R.id.recyclerViewItems)
        orderSummary = view.findViewById(R.id.recyclerViewOrderSummary)
        val btnAddItem: Button = view.findViewById(R.id.btnOpenItemList)

        // Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Sample item data
        itemList.addAll(listOf(
            Item("1", "Item A", 10.99),
            Item("2", "Item B", 5.49),
            Item("3", "Item C", 7.25)
        ))

        itemAdapter = ItemAdapter(itemList) { item ->
            showQuantityDialog(item)
        }
        recyclerView.adapter = itemAdapter

        btnAddItem.setOnClickListener {
            // Open item list
            showItemListDialog()
        }
    }

    fun addItemToOrder(item: Item, quantity: Int) {
        orderItems[item.id] = (orderItems[item.id] ?: 0) + quantity
        updateOrderSummary()
    }

    private fun showItemListDialog() {
        val items = itemList.map { it.name }.toTypedArray()
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Select an Item")
            .setItems(items) { _, which ->
                val selectedItem = itemList[which]
                showQuantityDialog(selectedItem)
            }
            .show()
    }

    private fun showQuantityDialog(item: Item) {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.item_quantity_dialog, null)
        val quantityInput: TextView = view.findViewById(R.id.edtQuantity)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Enter Quantity")
            .setView(view)
            .setPositiveButton("OK") { _, _ ->
                val quantityString = quantityInput.text.toString()
                val quantity = quantityString.toIntOrNull()
                if (quantity != null && quantity > 0) {
                    addItemToOrder(item, quantity)
                } else {
                    showToast("Please enter a valid quantity.")
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun updateOrderSummary() {
        var totalCost = 0.0
        val summary = StringBuilder()
        orderItems.forEach { (itemId, quantity) ->
            val item = itemList.find { it.id == itemId } ?: return@forEach
            val cost = item.price * quantity
            totalCost += cost
            summary.append("${item.name}: $quantity @ $${String.format("%.2f", item.price)} each = $${String.format("%.2f", cost)}\n")
        }
        summary.append("Total: $${String.format("%.2f", totalCost)}")
        orderSummary.text = summary.toString()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
