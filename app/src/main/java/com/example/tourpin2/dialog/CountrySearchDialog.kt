import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ListView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.tourpin2.OnCountrySelectedListener
import com.example.tourpin2.R
import com.example.tourpin2.model.CountryItem
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CountrySearchDialog : BottomSheetDialogFragment() {

    private var listener: OnCountrySelectedListener? = null

    fun setListener(listener: OnCountrySelectedListener) {
        this.listener = listener
    }

    private lateinit var countryList: ListView


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
        return inflater.inflate(R.layout.dialog_country_search, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        countryList = view.findViewById(R.id.countryListView)

        val btnBack = view.findViewById<ImageButton>(R.id.btn_back)
        btnBack.setOnClickListener {
            dismiss()
        }

        val countries = listOf(
            "Австралия", "Австрия", "Великобритания", "Германия", "Греция", "Египет",
            "Индия", "Испания", "Италия", "Канада", "Китай",
            "Мексика", "ОАЭ", "Россия", "США", "Таиланд",
            "Турция", "Франция", "Япония"
        )

        val countryFlagsMap = mapOf(
            "Россия" to R.drawable.ic_flag_russia,
            "Египет" to R.drawable.ic_flag_egypt,
            "Турция" to R.drawable.ic_flag_turkey,
            "ОАЭ" to R.drawable.ic_flag_uae,
            "Франция" to R.drawable.ic_flag_france,
            "Испания" to R.drawable.ic_flag_spain,
            "США" to R.drawable.ic_flag_usa,
            "Китай" to R.drawable.ic_flag_china,
            "Италия" to R.drawable.ic_flag_italy,
            "Мексика" to R.drawable.ic_flag_mexico,
            "Великобритания" to R.drawable.ic_flag_uk,
            "Таиланд" to R.drawable.ic_flag_thailand,
            "Германия" to R.drawable.ic_flag_germany,
            "Австрия" to R.drawable.ic_flag_austria,
            "Греция" to R.drawable.ic_flag_greece,
            "Австралия" to R.drawable.ic_flag_australia,
            "Канада" to R.drawable.ic_flag_canada,
            "Индия" to R.drawable.ic_flag_india,
            "Япония" to R.drawable.ic_flag_japan,
            "Малайзия" to R.drawable.ic_flag_malaysia
        )

        val adapter = CountryAdapterList(requireContext(), countries.map { it to countryFlagsMap[it]!! })
        countryList.adapter = adapter

        countryList.setOnItemClickListener { _, _, position, _ ->
            val selectedCountryName = countries[position]
            val selectedCountryFlag = countryFlagsMap[selectedCountryName]!!
            val selectedCountry = CountryItem(position + 1, selectedCountryFlag, selectedCountryName)
            Log.d("CountrySelected", "Selected country: $selectedCountry")
            listener?.onCountrySelected(selectedCountry)
            dismiss()
        }

    }
}