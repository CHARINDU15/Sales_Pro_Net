package com.example.logggg

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ItemListFragment : Fragment() {

    private lateinit var recyclerViewItems: RecyclerView
    private lateinit var itemAdapter: ItemAdapter
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val items = listOf(
        // Define your items here
        Item("1", "Item A", 10.0),
        Item("2", "Item B", 20.0),
        Item("3", "Item C", 30.0)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.item_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewItems = view.findViewById(R.id.recyclerViewItems)
        recyclerViewItems.layoutManager = LinearLayoutManager(context)

        itemAdapter = ItemAdapter(items) { item ->
            showQuantityDialog(item)
        }
        recyclerViewItems.adapter = itemAdapter
    }

    private fun showQuantityDialog(item: Item) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.item_quantity_dialog, null)
        val edtQuantity = dialogView.findViewById<EditText>(R.id.edtQuantity)
        val btnSubmitQuantity = dialogView.findViewById<Button>(R.id.btnSubmitQuantity)

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Enter Quantity")
            .setView(dialogView)
            .create()

        btnSubmitQuantity.setOnClickListener {
            val quantity = edtQuantity.text.toString().toIntOrNull() ?: 0
            if (quantity > 0) {
                sharedViewModel.addItemToOrder(item, quantity)
                dialog.dismiss()
                requireActivity().supportFragmentManager.popBackStack()
            } else {
                Toast.makeText(requireContext(), "Please enter a valid quantity", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }
}
