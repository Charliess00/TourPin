package com.example.tourpin2

import ProposalsAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tourpin2.`class`.Proposals
import com.example.tourpin2.databinding.FragmentProposalBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Proposal : AppCompatActivity() {

    private lateinit var binding: FragmentProposalBinding
    private lateinit var adapter: ProposalsAdapter
    private lateinit var proposalsList: MutableList<Proposals>
    var databaseRef = FirebaseDatabase.getInstance().getReference("Proposal")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentProposalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btnBack = binding.back
        btnBack.setOnClickListener {
            finish()
        }

        val proposalKeys = intent.getStringArrayListExtra("proposalKeys")

        proposalsList = mutableListOf()
        adapter = ProposalsAdapter(proposalsList)

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        if (proposalKeys!= null) {
            fetchProposal(proposalKeys)
        }
    }

    fun fetchProposal(proposalKeys: ArrayList<String>) {
        proposalsList.clear()

        for (key in proposalKeys) {
            databaseRef.child(key)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val proposal = dataSnapshot.getValue(Proposals::class.java)
                        if (proposal!= null) {
                            Log.d("ProposalActivity", "Добавляем в адаптер: ${proposal.hotel_name}, ${proposal.hotel_img}")
                            proposalsList.add(proposal)
                            adapter.notifyItemInserted(proposalsList.size - 1) // Уведомляем адаптер о добавлении нового элемента
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.d("ProposalActivity", "Ошибка при получении данных: ${databaseError.message}")
                    }
                })
        }
    }
}

