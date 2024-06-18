package com.example.tourpin2.dialog

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import com.example.tourpin2.Order
import com.example.tourpin2.R
import com.example.tourpin2.`class`.Proposals
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DelOrderDialog : BottomSheetDialogFragment() {

    private var orderKey: String? = null
    private var position: Int? = null
    private lateinit var loading: LoadingDialog
    val drOrder = FirebaseDatabase.getInstance().getReference("Order")
    val drProposal = FirebaseDatabase.getInstance().getReference("Proposal")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            orderKey = it.getString("orderKey")
            position = it.getInt("position")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_deleted, container, false)
        val btnDel = view.findViewById<FrameLayout>(R.id.btnDel)

        view.findViewById<ImageButton>(R.id.btnDismiss).setOnClickListener {
            dismiss()
        }

        btnDel.setOnClickListener {
            orderKey?.let { key ->
                loading = LoadingDialog(requireActivity())
                loading.start()
                deleteOrder(key)
            }
        }

        return view
    }

    private fun deleteOrder(orderKey: String) {
        // Сначала пытаемся удалить Заказ сразу же
        drOrder.child(orderKey).removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                position?.let {
                    (targetFragment as? Order)?.removeOrderFromAdapter(it)
                    loading.dismiss()
                    dismiss()
                }
            } else {
                loading.error()
            }
        }

        // Затем проверяем наличие связанных Предложений и обрабатываем их отдельно
        drProposal.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { dataSnapshot ->
                    val proposal = dataSnapshot.getValue(Proposals::class.java)
                    if (proposal!= null && proposal.order_ID == orderKey) {
                        val proposalKey = dataSnapshot.key.toString()
                        removeProposalAndOrder(proposalKey, orderKey)
                    }
                }
            }

            private fun removeProposalAndOrder(
                proposalKey: String,
                orderKey: String
            ) {
                drProposal.child(proposalKey).removeValue().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // После успешного удаления Предложения также пытаемся удалить Заказ снова
                        drOrder.child(orderKey).removeValue().addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                position?.let {
                                    (targetFragment as? Order)?.removeOrderFromAdapter(it)
                                    loading.dismiss()
                                    dismiss()
                                }
                            } else {
                                loading.error()
                            }
                        }
                    } else {
                        loading.error()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                loading.error()
            }
        })
    }
}
