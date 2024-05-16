package com.example.tourpin2.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.tourpin2.Listiner
import com.example.tourpin2.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PersonCountDialog(private var personCount: Int) : BottomSheetDialogFragment() {

    private var listener: Listiner? = null

    fun setListener(listener: Listiner) {
        this.listener = listener
    }

    override fun onStart() {
        super.onStart()
        view?.post {
            val parent = view?.parent as View
            val params = parent.layoutParams as CoordinatorLayout.LayoutParams
            val behavior = params.behavior as BottomSheetBehavior
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.isDraggable = false
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_person_count, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnAdd = view.findViewById<ImageButton>(R.id.btn_add)
        val btnSub = view.findViewById<ImageButton>(R.id.btn_sub)

        btnAdd.setOnClickListener {
            if (personCount < 6) {
                personCount++
                updateUI()
            }
        }

        btnSub.setOnClickListener {
            if (personCount > 1) {
                personCount--
                updateUI()
            }
        }

        updateUI()

        val saveButton = view.findViewById<FrameLayout>(R.id.button)
        saveButton.setOnClickListener {
            listener?.onPersonSelected(personCount)
            dismiss()
        }
    }

    private fun updateUI() {
        val btnAdd = view?.findViewById<ImageButton>(R.id.btn_add)
        val btnSub = view?.findViewById<ImageButton>(R.id.btn_sub)
        val countText = view?.findViewById<TextView>(R.id.count)

        btnAdd?.setImageResource(if (personCount < 6) R.drawable.ic_add else R.drawable.ic_add_unact)
        btnSub?.setImageResource(if (personCount > 1) R.drawable.ic_sub else R.drawable.ic_sub_unact)
        countText?.text = personCount.toString()
    }
}