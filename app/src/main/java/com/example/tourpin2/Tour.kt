package com.example.tourpin2

import Agent
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.tourpin2.`class`.Additions
import com.example.tourpin2.`class`.Tours
import com.example.tourpin2.databinding.ActivityTourBinding
import com.example.tourpin2.dialog.LoadingDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class Tour : AppCompatActivity() {

    private lateinit var binding: ActivityTourBinding
    private lateinit var loading: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTourBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loading = LoadingDialog(this)
        loading.start()

        val selectedProposalKey = intent.getStringExtra("selectedProposalKey")

        val btnBack = binding.back
        btnBack.setOnClickListener {
            finish()
        }

        loadTourData(selectedProposalKey)
    }

    private fun loadTourData(key: String?) {
        val databaseRef = FirebaseDatabase.getInstance().getReference("Proposal/$key")
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                val tour = snapshot.getValue(Tours::class.java)
                if (tour!= null) {
                    binding.hotelName.text = tour.hotel_name
                    binding.hotelDesc.text = tour.hotel_desc
                    binding.country.text = tour.country
                    binding.tourCity.text = tour.city_tour
                    binding.data.text = tour.data_start
                    binding.night.text = "${tour.night} ноч."

                    binding.person.text = tour.person.toString()
                    binding.desPerson.text = if (tour.person == 1) "${tour.person} взрослый" else "${tour.person} взрослых"

                    binding.person.text = "${tour.person} туристов"
                    when (tour.person) {
                        1 -> binding.person.text = "${tour.person} турист"
                        5, 6 -> binding.person.text = "${tour.person} туристов"
                        else -> binding.person.text = "${tour.person} туриста"
                    }

                    Picasso.get().load(tour.hotel_img).into(binding.img)
                    binding.price.text = formatNumber(tour.price)


                    val additionsSnapshot = snapshot.child("additions").getValue(Additions::class.java)
                    if (additionsSnapshot!= null) {
                        binding.habitation.text = additionsSnapshot.habitation
                        binding.food.text = additionsSnapshot.food
                        binding.flight.visibility = if (additionsSnapshot.flight) View.VISIBLE else View.GONE
                        binding.transfer.visibility = if (additionsSnapshot.transfer) View.VISIBLE else View.GONE

                        binding.additionsCon.visibility = if (binding.flight.visibility == View.VISIBLE || binding.transfer.visibility == View.VISIBLE) View.VISIBLE else View.GONE

                        binding.insurance.visibility = if (additionsSnapshot.insurance) View.VISIBLE else View.GONE
                    }


                    getAgent(tour.uid)
                }
            }

            private fun getAgent(uid: String) {
                val dr = FirebaseDatabase.getInstance().getReference("Agent/$uid")

                dr.addValueEventListener(object : ValueEventListener {
                    @SuppressLint("SetTextI18n")
                    override fun onDataChange(snapshot: DataSnapshot){
                        val agent = snapshot.getValue(Agent::class.java)

                        if (agent != null){
                            binding.agent.text = "${agent.name} ${agent.surname}"

                            val formattedText = "${agent.name.first()}${agent.surname.first()}"
                            binding.profileText.text = formattedText
                        }

                        loading.dismiss()
                    }
                    override fun onCancelled(error: DatabaseError) {
                        loading.error()
                    }
                })
            }


            fun formatNumber(number: Int): String {
                val numberAsString = number.toString()
                var formatted = ""
                for (i in numberAsString.indices step 3) {
                    formatted += numberAsString.substring(i, minOf(i + 3, numberAsString.length)) + " "
                }

                return formatted + "₽"
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}
