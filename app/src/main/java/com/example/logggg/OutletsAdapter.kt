package com.example.logggg

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class OutletsAdapter(
    private val outlets: List<Outlet>,
    private val onClick: (Outlet) -> Unit
) : RecyclerView.Adapter<OutletsAdapter.OutletViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OutletViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_outlet, parent, false)
        return OutletViewHolder(view)
    }

    override fun onBindViewHolder(holder: OutletViewHolder, position: Int) {
        val outlet = outlets[position]
        holder.bind(outlet, onClick)
    }

    override fun getItemCount() = outlets.size

    class OutletViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.textViewOutletName)
        private val categoryTextView: TextView = itemView.findViewById(R.id.textViewOutletCategory)

        fun bind(outlet: Outlet, onClick: (Outlet) -> Unit) {
            nameTextView.text = outlet.name
            categoryTextView.text = outlet.category
            itemView.setOnClickListener { onClick(outlet) }
        }
    }
}
