package com.example.tourpin2

import com.example.tourpin2.`class`.Orders
import com.example.tourpin2.adapters.OrdersAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tourpin2.dialog.LoadingDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Order : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var ordersList: MutableList<Orders>
    private lateinit var orderKeys: MutableList<String>
    private lateinit var adapter: OrdersAdapter
    private lateinit var loading: LoadingDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loading = LoadingDialog(requireActivity())
        loading.start()

        ordersList = mutableListOf()
        orderKeys = mutableListOf()
        adapter = OrdersAdapter(ordersList, orderKeys, parentFragmentManager, requireContext())

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        fetchOrders()
    }

    private fun fetchOrders() {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Order")
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid

        databaseReference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                ordersList.clear()
                orderKeys.clear()
                snapshot.children.forEach { dataSnapshot ->
                    val order = dataSnapshot.getValue(Orders::class.java)
                    if (order != null && order.uid == currentUserUid) {
                        ordersList.add(order)
                        orderKeys.add(dataSnapshot.key.toString())
                    }
                }
                adapter.notifyDataSetChanged()
                loading.dismiss()
            }

            override fun onCancelled(error: DatabaseError) {
                loading.error()
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    fun removeOrderFromAdapter(position: Int) {
        ordersList.removeAt(position)
        orderKeys.removeAt(position)
        adapter.notifyItemRemoved(position)
        adapter.notifyItemRangeChanged(position, ordersList.size)
    }
}
