package com.example.tourpin2

import android.content.Intent
import com.example.tourpin2.adapters.ProposalsAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tourpin2.`class`.Proposals
import com.example.tourpin2.databinding.FragmentProposalBinding
import com.example.tourpin2.dialog.LoadingDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Proposal : AppCompatActivity() {

    private lateinit var binding: FragmentProposalBinding
    private lateinit var adapter: ProposalsAdapter
    private lateinit var proposalsList: MutableList<Proposals>
    private lateinit var loadingDialog: LoadingDialog
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
        adapter = ProposalsAdapter(proposalsList, object : ProposalsAdapter.OnProposalItemClickListener {
            override fun onProposalItemClick(position: Int) {
                val selectedKey = proposalKeys?.get(position)
                val intent = Intent(this@Proposal, Tour::class.java)
                intent.putExtra("proposalKey", selectedKey)
                startActivity(intent)
            }
        })

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        loadingDialog = LoadingDialog(this)

        if (proposalKeys!= null && proposalKeys.isNotEmpty()) {
            loadingDialog.start()
            fetchProposal(proposalKeys)
        } else{
            loadingDialog.dismiss()
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
                            proposalsList.add(proposal)
                            adapter.notifyItemInserted(proposalsList.size - 1)
                        }
                        loadingDialog.dismiss()
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        loadingDialog.error()
                    }
                })
        }
    }
}

