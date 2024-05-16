package com.example.tourpin2.dialog

import CityAdapterList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ListView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.tourpin2.Listiner
import com.example.tourpin2.R
import com.example.tourpin2.model.CityItem
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CitySearchDialog : BottomSheetDialogFragment() {

    private var listener: Listiner? = null

    fun setListener(listener: Listiner) {
        this.listener = listener
    }

    private lateinit var cityList: ListView


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
        return inflater.inflate(R.layout.dialog_city_search, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cityList = view.findViewById(R.id.cityListView)

        val btnBack = view.findViewById<ImageButton>(R.id.btn_back)
        btnBack.setOnClickListener {
            dismiss()
        }

        val cities = listOf("Москва", "Санкт-Петербург", "Новосибирск", "Екатеринбург", "Казань")

        val cityAdapter = CityAdapterList(requireContext(), cities)
        cityList.adapter = cityAdapter

        cityList.setOnItemClickListener { _, _, position, _ ->
            val selectedCityName = cities[position]
            val selectedCity = CityItem(position + 1, selectedCityName)
            Log.d("CitySelected", "Selected city: $selectedCity")
            listener?.onCitySelected(selectedCity)
            dismiss()
        }

    }
}
