package com.example.logggg



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SampleFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var orderSummary: TextView
    private val itemList = mutableListOf<Item>()
    private val orderItems = mutableMapOf<String, Int>() // Maps item IDs to quantities
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var outlet: Outlet
    private lateinit var recyclerViewOrderSummary: RecyclerView
    private lateinit var orderAdapter: OrderAdapter
    // Create a companion object to hold the key for the argument
    companion object {
        private const val ARG_OUTLET = "outlet"

        // Static method to create a new instance of the fragment with arguments
        fun newInstance(outlet: Outlet): SampleFragment {
            val fragment = SampleFragment()
            val args = Bundle().apply {
                // Add arguments to the Bundle
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_order_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewOrderSummary = view.findViewById(R.id.recyclerViewOrderSummary)
        recyclerViewOrderSummary.layoutManager = LinearLayoutManager(context)

        sharedViewModel.orderItems.observe(viewLifecycleOwner) { orderItems ->
            orderAdapter = OrderAdapter(orderItems)
            recyclerViewOrderSummary.adapter = orderAdapter
        }

        // Retrieve the arguments
        val outlet = arguments?.getParcelable<Outlet>(ARG_OUTLET)

        val btnAddItem: Button = view.findViewById(R.id.btnOpenItemList)
        btnAddItem.setOnClickListener {
             // Open item list
         showItemListDialog()
       }
    }

    private fun showItemListDialog() {
        val itemListFragment = ItemListFragment()
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, itemListFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun addItemToOrder(item: Item, quantity: Int) {
        orderItems[item.id] = (orderItems[item.id] ?: 0) + quantity
        updateOrderSummary()
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
}
