package com.example.tourpin2

import CountrySearchDialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.example.tourpin2.dialog.CitySearchDialog
import com.example.tourpin2.model.CityItem
import com.example.tourpin2.model.CountryItem

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

interface OnCountrySelectedListener {
    fun onCountrySelected(country: CountryItem)
}

interface OnCitySelectedListener {
    fun onCitySelected(city: CityItem)
}


class Search : Fragment(), OnCountrySelectedListener, OnCitySelectedListener {
    private var param1: String? = null
    private var param2: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnAddCont = view.findViewById<LinearLayout>(R.id.btn_add_country)
        btnAddCont.setOnClickListener {
            val dialog = CountrySearchDialog()
            dialog.setListener(this) // Установка слушателя
            dialog.show(parentFragmentManager, "CountrySearchDialog")
        }

        val btnAddCity = view.findViewById<LinearLayout>(R.id.btn_add_city)
        btnAddCity.setOnClickListener {
            val dialog = CitySearchDialog()
            dialog.setListener(this) // Установка слушателя
            dialog.show(parentFragmentManager, "CitySearchDialog")
        }
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
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onCitySelected(city: CityItem) {
        val btnAddCity = view?.findViewById<LinearLayout>(R.id.btn_add_city)
        val cityTextView = btnAddCity?.findViewById<TextView>(R.id.city)
        val deleteImageView = btnAddCity?.findViewById<ImageView>(R.id.deleteCity)

        cityTextView?.text = city.name
        deleteImageView?.visibility = View.VISIBLE
        cityTextView?.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                android.R.color.white
            )
        )

        btnAddCity?.let {
            ViewCompat.setBackgroundTintList(
                it,
                ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.act))
            )
        }

        deleteImageView?.setOnClickListener {
            resetCity()
        }


    }

    private fun resetCity() {
        val btnAddCity = view?.findViewById<LinearLayout>(R.id.btn_add_city)
        val cityTextView = btnAddCity?.findViewById<TextView>(R.id.city)
        val deleteImageView = btnAddCity?.findViewById<ImageView>(R.id.deleteCity)
        cityTextView?.text = "Выбрать город"
        cityTextView?.setTextColor(ContextCompat.getColor(requireContext(), R.color.unact))
        btnAddCity?.let {
            ViewCompat.setBackgroundTintList(
                it,
                ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.back))
            )
        }
        deleteImageView?.visibility = View.GONE

    }

    override fun onCountrySelected(country: CountryItem) {
        val btnAddCont = view?.findViewById<LinearLayout>(R.id.btn_add_country)
        val countryTextView = btnAddCont?.findViewById<TextView>(R.id.country)
        val deleteImageView = btnAddCont?.findViewById<ImageView>(R.id.deleteCountry)

        countryTextView?.text = country.name
        deleteImageView?.visibility = View.VISIBLE
        countryTextView?.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                android.R.color.white
            )
        )
        btnAddCont?.let {
            ViewCompat.setBackgroundTintList(
                it,
                ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.act))
            )
        }

        deleteImageView?.setOnClickListener {
            resetCountry()
        }
    }

    private fun resetCountry() {
        val btnAddCont = view?.findViewById<LinearLayout>(R.id.btn_add_country)
        val countryTextView = btnAddCont?.findViewById<TextView>(R.id.country)
        val deleteImageView = btnAddCont?.findViewById<ImageView>(R.id.deleteCountry)
        countryTextView?.text = "Выбрать направление"
        countryTextView?.setTextColor(ContextCompat.getColor(requireContext(), R.color.unact))
        btnAddCont?.let {
            ViewCompat.setBackgroundTintList(
                it,
                ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.back))
            )
        }
        deleteImageView?.visibility = View.GONE
    }

}
