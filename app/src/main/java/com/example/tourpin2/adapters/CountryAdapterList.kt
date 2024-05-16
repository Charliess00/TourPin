import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.tourpin2.R

class CountryAdapterList(context: Context, countriesWithFlags: List<Pair<String, Int>>) :
    ArrayAdapter<Pair<String, Int>>(context, R.layout.list_item_country, countriesWithFlags) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        Log.d("CustomAdapter", "getView called for position: " + position);
        val view = convertView?: LayoutInflater.from(context).inflate(R.layout.list_item_country, parent, false)
        val flagImageView = view.findViewById<ImageView>(R.id.flagImageView)
        val countryNameTextView = view.findViewById<TextView>(R.id.txt)

        val countryWithFlag = getItem(position)?: return view
        flagImageView.setImageResource(countryWithFlag.second)
        countryNameTextView.text = countryWithFlag.first

        return view
    }
}
