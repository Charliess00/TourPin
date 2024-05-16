import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.tourpin2.R

class CityAdapterList(context: Context, cities: List<String>) : ArrayAdapter<String>(context, R.layout.list_item_city, cities) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView?: LayoutInflater.from(context).inflate(R.layout.list_item_city, parent, false)
        val cityImageView = view.findViewById<ImageView>(R.id.ic)
        val cityNameTextView = view.findViewById<TextView>(R.id.txt)

        val city = getItem(position)?: return view
        cityImageView.setImageResource(R.drawable.point_map)
        cityNameTextView.text = city

        return view
    }
}
