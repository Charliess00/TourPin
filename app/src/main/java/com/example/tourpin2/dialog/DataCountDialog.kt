package com.example.tourpin2.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.tourpin2.Listiner
import com.example.tourpin2.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.slider.RangeSlider
import java.text.SimpleDateFormat
import java.util.*

class DataCountDialog() : BottomSheetDialogFragment() {

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_data_count, container, false)

    }

    var selectedDate: String = ""

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnBack = view.findViewById<ImageButton>(R.id.btn_back)
        btnBack.setOnClickListener {
            dismiss()
        }

        val calendarView = view.findViewById<CalendarView>(R.id.calendarView)
        val today = SimpleDateFormat("dd MMMM", Locale.getDefault()).format(Date())
        selectedDate = today
        calendarView.minDate = calendarView.date

        calendarView.setOnDateChangeListener { _, year, monthOfYear, dayOfMonth ->
            // Обновление выбранной даты
            val newDate = SimpleDateFormat("dd MMMM", Locale.getDefault()).format(
                Calendar.getInstance().apply {
                    timeInMillis = System.currentTimeMillis()
                    set(year, monthOfYear, dayOfMonth)
                }.time
            )
            selectedDate = newDate
        }

        val btnSave = view.findViewById<FrameLayout>(R.id.button)
        btnSave.setOnClickListener {
            val seekBar = view.findViewById<RangeSlider>(R.id.seekBar)
            val startDate = seekBar?.values?.getOrNull(0)?.toInt() ?: 2
            val endDate = seekBar?.values?.getOrNull(1)?.toInt() ?: 14
            listener?.onDataSelected(selectedDate, startDate, endDate)
            dismiss()
        }
    }
}