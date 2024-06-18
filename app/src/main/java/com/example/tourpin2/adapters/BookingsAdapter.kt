package com.example.tourpin2.adapters

import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.tourpin2.R
import com.example.tourpin2.`class`.BookingTour
import com.example.tourpin2.`class`.Proposals
import com.squareup.picasso.Picasso


class BookingsAdapter(
    private val bookingList: MutableList<BookingTour>,
) : RecyclerView.Adapter<BookingsAdapter.BookingViewHolder>() {

    inner class BookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hotelName: TextView = itemView.findViewById(R.id.hotel_name)
        val country: TextView = itemView.findViewById(R.id.country)
        val tourCity: TextView = itemView.findViewById(R.id.tour_city)
        val startDate: TextView = itemView.findViewById(R.id.dataStart)
        val endDate: TextView = itemView.findViewById(R.id.dataEnd)
        val night: TextView = itemView.findViewById(R.id.night)
        val img: ImageView = itemView.findViewById(R.id.img)
        val price: TextView = itemView.findViewById(R.id.price)
        val bg: FrameLayout = itemView.findViewById(R.id.button)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_proposal, parent, false)
        return BookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {

        val booking = bookingList[position]
        holder.hotelName.text = booking.hotel_name
        holder.country.text = booking.country
        holder.tourCity.text = booking.city_tour
        holder.startDate.text = booking.data_start
        holder.endDate.text = booking.data_end
        holder.endDate.text = booking.data_end

        holder.price.text = "ИСПОЛНЕН"

        holder.price.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.act))
        holder.price.typeface = ResourcesCompat.getFont(holder.itemView.context, R.font.montserrat_semibold)
        holder.price.letterSpacing = 0.05f
        holder.bg.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.context, R.color.back))

        if (booking.hotel_img != "") {
            Picasso.get().load(booking.hotel_img).into(holder.img)
        }

        val night = booking.night.toString()
        holder.night.text = "$night ноч."
            }

    override fun getItemCount(): Int {
        return bookingList.size
    }
}
