package com.example.tourpin2.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.Toast
import com.example.tourpin2.Order
import com.example.tourpin2.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.FirebaseDatabase

class DelOrderDialog : BottomSheetDialogFragment() {

    private var orderKey: String? = null
    private var position: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            orderKey = it.getString("orderKey")
            position = it.getInt("position")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_deleted, container, false)
        val btnDel = view.findViewById<FrameLayout>(R.id.btnDel)

        view.findViewById<ImageButton>(R.id.btnDismiss).setOnClickListener {
            dismiss()
        }

        btnDel.setOnClickListener {
            orderKey?.let { key ->
                deleteOrder(key)
            }
        }

        return view
    }

    private fun deleteOrder(orderKey: String) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Order")
        databaseReference.child(orderKey).removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                position?.let {
                    (targetFragment as? Order)?.removeOrderFromAdapter(it)
                }
                dismiss()
                Toast.makeText(context, "Заявка удалена", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Ошибка при удалении заявки", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
