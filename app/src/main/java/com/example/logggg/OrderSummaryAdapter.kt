package com.example.logggg

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OrderSummaryAdapter(private val orderItems: List<OrderItem>) : RecyclerView.Adapter<OrderSummaryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_summary_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val orderItem = orderItems[position]
        holder.bind(orderItem)
    }

    override fun getItemCount(): Int = orderItems.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtItemName: TextView = itemView.findViewById(R.id.txtItemName)
        private val txtQuantity: TextView = itemView.findViewById(R.id.txtQuantity)
        private val txtCost: TextView = itemView.findViewById(R.id.txtCost)

        fun bind(orderItem: OrderItem) {
            txtItemName.text = orderItem.item.name
            txtQuantity.text = "Quantity: ${orderItem.quantity}"
            txtCost.text = "Cost: $${String.format("%.2f", orderItem.totalCost)}"
        }
    }
}
