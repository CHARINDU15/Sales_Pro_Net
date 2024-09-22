package com.example.logggg

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OrderAdapter(private val orderItems: List<OrderItem>) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemName: TextView = itemView.findViewById(R.id.itemName)
        val itemQuantity: TextView = itemView.findViewById(R.id.itemQuantity)
        val itemPrice: TextView = itemView.findViewById(R.id.itemPrice)
        val totalCost: TextView = itemView.findViewById(R.id.totalCost)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.order_item_view, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val orderItem = orderItems[position]
        holder.itemName.text = orderItem.item.name
        holder.itemQuantity.text = "Quantity: ${orderItem.quantity}"
        holder.itemPrice.text = "Price: ${orderItem.item.price}"
        holder.totalCost.text = "Total: ${orderItem.item.price * orderItem.quantity}"
    }

    override fun getItemCount(): Int = orderItems.size
}
