package com.example.tourpin2.adapters

import Orders
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tourpin2.R
import com.example.tourpin2.dialog.DelOrderDialog

class OrdersAdapter(
    private val ordersList: MutableList<Orders>,
    private val orderKeys: MutableList<String>,
    private val fragmentManager: FragmentManager
) : RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder>() {

    inner class OrdersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cityView: TextView = itemView.findViewById(R.id.city)
        val countryView: TextView = itemView.findViewById(R.id.country)
        val dataView: TextView = itemView.findViewById(R.id.data)
        val personView: TextView = itemView.findViewById(R.id.person)
        val personDesView: TextView = itemView.findViewById(R.id.desPerson)
        val nightView: TextView = itemView.findViewById(R.id.desData)
        val imageView: ImageView = itemView.findViewById(R.id.ic_airplane)
        val btnRemove: ImageView = itemView.findViewById(R.id.btnRemove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_order, parent, false)
        return OrdersViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        val currentOrder = ordersList[position]
        holder.cityView.text = currentOrder.city
        holder.countryView.text = currentOrder.country
        holder.dataView.text = currentOrder.data
        val person: String = currentOrder.tourists_count

        holder.personView.text = "$person туристов"
        when (person) {
            "1" -> holder.personView.text = "$person турист"
            "5", "6" -> holder.personView.text = "$person туристов"
            else -> holder.personView.text = "$person туриста"
        }

        holder.personDesView.text = if (person == "1") "$person взрослый" else "$person взрослых"

        val first = currentOrder.nightFirst
        val second = currentOrder.nightSecond
        holder.nightView.text = if (first == second) "$first ноч." else "от $first до $second ноч."

        holder.imageView.setImageResource(R.drawable.ic_airplane)
        holder.btnRemove.setOnClickListener {
            showDeleteDialog(orderKeys[position], position)
        }
    }

    private fun showDeleteDialog(orderKey: String, position: Int) {
        val dialog = DelOrderDialog().apply {
            arguments = bundleOf(
                "orderKey" to orderKey,
                "position" to position
            )
        }
        dialog.show(fragmentManager, dialog.tag)
    }

    override fun getItemCount(): Int = ordersList.size
}

