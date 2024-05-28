package com.example.tourpin2

import CountrySearchDialog
import Orders
import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.example.tourpin2.dialog.*
import com.example.tourpin2.model.CityItem
import com.example.tourpin2.model.CountryItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

interface Listiner {
    fun onCountrySelected(country: CountryItem)
    fun onCitySelected(city: CityItem)
    fun onPersonSelected(personCount: Int)
    fun onDataSelected(date: String, startData: Int, endDate: Int)
}


class Search : Fragment(), Listiner {
    private var param1: String? = null
    private var param2: String? = null
    private var person: Int = 1
    lateinit var order: Orders
    var auth = FirebaseAuth.getInstance()
    private val cUser = auth.currentUser
    private lateinit var loading: LoadingDialog


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loading = LoadingDialog(requireActivity())

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

        val btnAddPerson = view.findViewById<LinearLayout>(R.id.btn_add_person)
        btnAddPerson.setOnClickListener {
            val dialog = PersonCountDialog(person)
            dialog.setListener(this) // Установка слушателя
            dialog.show(parentFragmentManager, "PersonCountDialog")
        }

        val btnAddData = view.findViewById<LinearLayout>(R.id.btn_add_data)
        btnAddData.setOnClickListener {
            val dialog = DataCountDialog()
            dialog.setListener(this) // Установка слушателя
            dialog.show(parentFragmentManager, "DataCountDialog")
        }

        val btnOrder = view.findViewById<Button>(R.id.button)
        btnOrder.setOnClickListener {
            loading.start()
            if (validateFields()) {
                order.uid = cUser?.uid.toString()
                order.tourists_count = extractNumberFromText(order.tourists_count)

                sendToFirebase(order)


            } else {
                loading.dismiss()
            }
        }
    }

    private fun toOrder(fragment: Fragment) {
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            loading.end()
            fragmentTransaction.replace(R.id.frame_layout, fragment)
            fragmentTransaction.commit()
    }


    private fun extractNumberFromText(text: String): String {
        val numberPattern = """\d""".toRegex()
        val matchResult = numberPattern.find(text)
        return matchResult?.value.toString()
    }

    private fun sendToFirebase(order: Orders) {
        val myRef = FirebaseDatabase.getInstance().getReference("Order")
        val orderRef = myRef.push()
        orderRef.setValue(order)
            .addOnSuccessListener {
                Log.d("Order", "TRUE push")
                toOrder(Order())

            }
            .addOnFailureListener {
                Log.d("Order", "False push")
                loading.error()
            }
    }

    private fun validateFields(): Boolean {
        var isValid = true
        val nightInt =
            extractNumbersFromText(view?.findViewById<TextView>(R.id.desData)?.text.toString())
        val colorError = view?.context?.let { ContextCompat.getColor(it, R.color.error) }
        val colorErrorList =
            view?.context?.let { ContextCompat.getColorStateList(it, R.color.error) }

        order = Orders(
            country = view?.findViewById<TextView>(R.id.country)?.text.toString(),
            city = view?.findViewById<TextView>(R.id.city)?.text.toString(),
            data = view?.findViewById<TextView>(R.id.data)?.text.toString(),
            nightFirst = nightInt.first.toString(),
            nightSecond = nightInt.second.toString(),
            tourists_count = view?.findViewById<TextView>(R.id.person)?.text.toString(),
            uid = ""
        )

        if (order.country == "Выбрать направление") {
            view?.findViewById<TextView>(R.id.h2Country)?.setTextColor(colorError!!)
            isValid = false
        }
        if (order.city == "Выбрать город") {
            view?.findViewById<TextView>(R.id.h2City)?.setTextColor(colorError!!)
            isValid = false
        }
        if (order.data == "Дата") {
            view?.findViewById<TextView>(R.id.data)?.setTextColor(colorError!!)
            val drawable = view?.findViewById<ImageView>(R.id.ic_data)?.drawable
            DrawableCompat.setTintList(drawable!!, colorErrorList!!)
            isValid = false
        }
        if (nightInt.first == 0 || nightInt.second == 0) {
            isValid = false
        }
        if (order.tourists_count.first().toString().toIntOrNull() == null) {
            view?.findViewById<TextView>(R.id.person)?.setTextColor(colorError!!)
            val drawable = view?.findViewById<ImageView>(R.id.ic_person)?.drawable
            DrawableCompat.setTintList(drawable!!, colorErrorList!!)
            isValid = false
        }

        return isValid
    }

    private fun extractNumbersFromText(text: String): Pair<Int, Int> {
        val numberPattern = """(\d+)""".toRegex()
        val matches = numberPattern.findAll(text).map { it.value.toInt() }.toList()

        if (matches.size >= 2) {
            return Pair(matches[0], matches[1])
        }

        if (matches.size == 1) {
            return Pair(matches[0], matches[0])
        }

        return Pair(0, 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onCitySelected(city: CityItem) {
        val btnAddCity = view?.findViewById<LinearLayout>(R.id.btn_add_city)
        val cityTextView = btnAddCity?.findViewById<TextView>(R.id.city)
        val cityH = view?.findViewById<TextView>(R.id.h2City)
        val deleteImageView = btnAddCity?.findViewById<ImageView>(R.id.deleteCity)

        val colorBlack = ContextCompat.getColor(requireContext(), R.color.black)

        cityTextView?.text = city.name
        cityH?.setTextColor(colorBlack)
        deleteImageView?.visibility = View.VISIBLE
        cityTextView?.setTextColor(
            ContextCompat.getColor(
                requireContext(), android.R.color.white
            )
        )

        btnAddCity?.let {
            ViewCompat.setBackgroundTintList(
                it, ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.act))
            )
        }

        deleteImageView?.setOnClickListener {
            resetCity()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onDataSelected(date: String, startData: Int, endDate: Int) {
        val dataText = view?.findViewById<TextView>(R.id.data)
        val desc = view?.findViewById<TextView>(R.id.desData)

        val colorBlack = ContextCompat.getColor(requireContext(), R.color.black)
        val colorBlackList = ContextCompat.getColorStateList(requireContext(), R.color.black)

        val drawable = view?.findViewById<ImageView>(R.id.ic_data)?.drawable
        DrawableCompat.setTintList(drawable!!, colorBlackList)

        view?.findViewById<TextView>(R.id.data)?.setTextColor(colorBlack)

        dataText?.text = date
        if (startData != endDate) {
            desc?.text = "от $startData до $endDate ноч."
        } else {
            desc?.text = when (endDate) {
                2, 3, 4 -> "$endDate ночи"
                else -> "$endDate ночей"
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onPersonSelected(personCount: Int) {
        val personText = view?.findViewById<TextView>(R.id.person)
        val desc = view?.findViewById<TextView>(R.id.desPerson)

        val colorBlack = ContextCompat.getColor(requireContext(), R.color.black)
        val colorBlackList = ContextCompat.getColorStateList(requireContext(), R.color.black)

        person = personCount
        personText?.setTextColor(colorBlack)

        val drawable = view?.findViewById<ImageView>(R.id.ic_person)?.drawable
        DrawableCompat.setTintList(drawable!!, colorBlackList!!)

        personText?.text = when (person) {
            1, 5, 6 -> "$person человек"
            else -> "$person человека"
        }
        desc?.text = when (person) {
            1 -> "$person взрослый"
            else -> "$person взрослых"
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
                it, ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.back))
            )
        }
        deleteImageView?.visibility = View.GONE
    }

    override fun onCountrySelected(country: CountryItem) {
        val btnAddCont = view?.findViewById<LinearLayout>(R.id.btn_add_country)
        val countryTextView = btnAddCont?.findViewById<TextView>(R.id.country)
        val countryH = view?.findViewById<TextView>(R.id.h2Country)
        val deleteImageView = btnAddCont?.findViewById<ImageView>(R.id.deleteCountry)

        val colorBlack = ContextCompat.getColor(requireContext(), R.color.black)

        countryTextView?.text = country.name
        countryH?.setTextColor(colorBlack)
        deleteImageView?.visibility = View.VISIBLE
        countryTextView?.setTextColor(
            ContextCompat.getColor(
                requireContext(), android.R.color.white
            )
        )
        btnAddCont?.let {
            ViewCompat.setBackgroundTintList(
                it, ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.act))
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
                it, ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.back))
            )
        }
        deleteImageView?.visibility = View.GONE
    }

}
