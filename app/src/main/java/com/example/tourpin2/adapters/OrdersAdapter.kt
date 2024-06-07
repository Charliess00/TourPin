package com.example.tourpin2.adapters

import com.example.tourpin2.`class`.Orders
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tourpin2.Order
import com.example.tourpin2.Proposal
import com.example.tourpin2.R
import com.example.tourpin2.dialog.DelOrderDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OrdersAdapter(
    private val ordersList: MutableList<Orders>,
    private val orderKeys: MutableList<String>,
    private val fragmentManager: FragmentManager,
    private val context: Context
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
        val button: FrameLayout = itemView.findViewById(R.id.button)
        val buttonTxt: TextView = itemView.findViewById(R.id.buttonTxt)
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

        // Получаем количество предложений
        getProposalsCount(orderKeys[position]) { count ->
            if (count > 0) {
                when (count) {
                    1 -> holder.buttonTxt.text = "$count предложение"
                    2, 3, 4 -> holder.buttonTxt.text = "$count предложения"
                    else -> holder.buttonTxt.text = "$count предложений"
                }
                holder.buttonTxt.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
                holder.button.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.context, R.color.act))
            } else {
                holder.buttonTxt.text = "Предложений пока нет"
                holder.buttonTxt.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.unact))
                holder.button.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.context, R.color.back))
            }
        }

        holder.button.setOnClickListener {
            if (holder.buttonTxt.text.toString()!= "Предложений пока нет") {
                getProposalKeysForOrder(orderKeys[position]) { keys ->
                    val intent = Intent(context, Proposal::class.java)
                    intent.putStringArrayListExtra("proposalKeys", ArrayList(keys))
                    context.startActivity(intent)
                }
            }
        }
    }

    private fun getProposalKeysForOrder(orderId: String, callback: (List<String>) -> Unit) {
        val proposalReference = FirebaseDatabase.getInstance().getReference("Proposal")
        proposalReference.orderByChild("order_ID").equalTo(orderId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val keys = mutableListOf<String>()
                    snapshot.children.forEach { child ->
                        val key = child.key?: ""
                        keys.add(key)
                    }
                    callback(keys)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Ошибка чтения данных", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun getProposalsCount(orderId: String, callback: (Int) -> Unit) {
        val proposalReference = FirebaseDatabase.getInstance().getReference("Proposal")
        proposalReference.orderByChild("order_ID").equalTo(orderId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    callback(snapshot.childrenCount.toInt())
                }

                override fun onCancelled(error: DatabaseError) {
                    // Обработка ошибок чтения данных
                }
            })
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

