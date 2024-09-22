package com.example.logggg

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter(private val items: List<Item>, private val onItemClick: (Item) -> Unit) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtItemName: TextView = itemView.findViewById(R.id.txtItemName)
        private val txtPrice: TextView = itemView.findViewById(R.id.txtPrice)

        init {
            itemView.setOnClickListener {
                onItemClick(items[adapterPosition])
            }
        }

        fun bind(item: Item) {
            txtItemName.text = item.name
            txtPrice.text = "Price: $${String.format("%.2f", item.price)}"
        }
    }
}
