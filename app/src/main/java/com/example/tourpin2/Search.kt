package com.example.tourpin2

import CityAdapter
import CityItem
import CountryAdapter
import CountryItem
import CountrySearchDialog
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tourpin2.model.CitySearchDialog


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


interface OnCountrySelectedListener {
    fun onCountrySelected(country: CountryItem)
}

class Search : Fragment(), OnCountrySelectedListener {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var countryView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        countryView = view.findViewById(R.id.country_recyclerView)
        countryView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val countryList = mutableListOf(
            CountryItem(1, R.drawable.ic_flag_russia, "Россия"),
            CountryItem(2, R.drawable.ic_flag_turkey, "Турция"),
            CountryItem(3, R.drawable.ic_flag_egypt, "Египет"),
            // Добавьте другие элементы списка здесь
        )
        countryView.adapter = CountryAdapter(countryList)

        val btnMoreCont = view.findViewById<ImageButton>(R.id.btn_more_cont)
        btnMoreCont.setOnClickListener {
            val dialog = CountrySearchDialog()
            dialog.setListener(this) // Установка слушателя
            dialog.show(parentFragmentManager, "CountrySearchDialog")
        }

        val cityView = view.findViewById<RecyclerView>(R.id.city_recyclerView)
        cityView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val cityList = listOf(
            CityItem(1, "Симферополь"),
            CityItem(2, "Москва"),
            // Добавьте другие элементы списка здесь
        )

        cityView.adapter = CityAdapter(cityList)


        val btnMoreCity = view.findViewById<ImageButton>(R.id.btn_more_city)
        btnMoreCity.setOnClickListener {
            val dialog = CitySearchDialog()
            dialog.show(parentFragmentManager, "CitySearchDialog")
        }
    }


    @SuppressLint("InflateParams")
    private fun updateSelectedCountry(country: CountryItem) {
        val selectedCountryContainer =
            view?.findViewById<LinearLayout>(R.id.selectedCountryContainer)
        selectedCountryContainer?.removeAllViews() // Удаляем все предыдущие виджеты

        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_counter_act, null)

        val flagImageView = view.findViewById<ImageView>(R.id.flag)
        country.flagImage?.let { flagImageView.setImageResource(it) }

        val countryNameTextView = view.findViewById<TextView>(R.id.txt)
        countryNameTextView.text = country.countryName

        view.setOnClickListener {
            if (selectedCountryContainer != null) {
                clearDialogCountry(country, selectedCountryContainer)
            }
        }

        selectedCountryContainer?.addView(view) // Добавляем новый виджет
        selectedCountryContainer?.visibility = View.VISIBLE
    }


    override fun onCountrySelected(country: CountryItem) {
        // Получаем текущий адаптер и обновляем состояние выбранности
        val adapter = countryView.adapter as CountryAdapter
        adapter.updateCountrySelection(-1) // Сброс выбранности всех элементов
        // Устанавливаем выбранную страну как единственную выбранную
        country.isSelected = true
        Log.d("CountrySelected", "Received country: ${country.countryName}")

        // Проверяем, есть ли уже выбранная страна в selectedCountryContainer
        val selectedCountryContainer =
            view?.findViewById<LinearLayout>(R.id.selectedCountryContainer)
        if (selectedCountryContainer != null && selectedCountryContainer.childCount > 0) {
            // Если есть выбранная страна, сбрасываем её
            val currentSelectedCountry = selectedCountryContainer.getChildAt(0).tag as? CountryItem
            if (currentSelectedCountry != null) {
                clearDialogCountry(currentSelectedCountry, selectedCountryContainer)
            }
        }

        // Обновляем selectedCountryContainer с новой страной
        updateSelectedCountry(country)
    }

    private fun clearDialogCountry(country: CountryItem, selectedCountryContainer: LinearLayout){
        country.flagImage = null
        country.countryName = ""
        selectedCountryContainer.visibility = View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)

    }
}