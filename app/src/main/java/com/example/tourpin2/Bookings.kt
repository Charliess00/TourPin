package com.example.tourpin2

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tourpin2.`class`.BookingTour
import com.example.tourpin2.adapters.BookingsAdapter
import com.example.tourpin2.adapters.OrdersAdapter
import com.example.tourpin2.adapters.ProposalsAdapter
import com.example.tourpin2.dialog.LoadingDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Bookings : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var adapter: BookingsAdapter
    private var auth = FirebaseAuth.getInstance()
    private val cUser = auth.currentUser
    private lateinit var bookingList: MutableList<BookingTour>
    private lateinit var loading: LoadingDialog
    var databaseRef = FirebaseDatabase.getInstance().getReference("Booking")
    private lateinit var imageViewNoBookings: ImageView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bookingList = mutableListOf()
        adapter = BookingsAdapter(bookingList)

        val recyclerView =  view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = adapter
        imageViewNoBookings = view.findViewById(R.id.imageViewNoBookings)

        loading = LoadingDialog(requireActivity())

        loading.start()
        fetchBooking()
    }

    private fun fetchBooking() {

        databaseRef.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                bookingList.clear()

                snapshot.children.forEach { dataSnapshot ->
                    val booking = dataSnapshot.getValue(BookingTour::class.java)
                    if (booking != null && booking.uid == cUser?.uid.toString()) {

                        bookingList.add(booking)
                        adapter.notifyItemInserted(bookingList.size - 1)
                    }
                }
                adapter.notifyDataSetChanged()

                if (bookingList.isEmpty()) {
                    imageViewNoBookings.visibility = View.VISIBLE
                } else {
                    imageViewNoBookings.visibility = View.GONE
                }

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bookings, container, false)
    }
}