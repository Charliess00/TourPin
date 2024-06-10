package com.example.tourpin2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tourpin2.R
import com.example.tourpin2.`class`.Proposals
import com.squareup.picasso.Picasso


class ProposalsAdapter(
    private val proposalsList: MutableList<Proposals>,
    private val itemClickListener: OnProposalItemClickListener
) : RecyclerView.Adapter<ProposalsAdapter.ProposalViewHolder>() {

    interface OnProposalItemClickListener {
        fun onProposalItemClick(position: Int)
    }

    inner class ProposalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hotelName: TextView = itemView.findViewById(R.id.hotel_name)
        val country: TextView = itemView.findViewById(R.id.country)
        val tourCity: TextView = itemView.findViewById(R.id.tour_city)
        val startDate: TextView = itemView.findViewById(R.id.dataStart)
        val endDate: TextView = itemView.findViewById(R.id.dataEnd)
        val night: TextView = itemView.findViewById(R.id.night)
        val img: ImageView = itemView.findViewById(R.id.img)
        val price: TextView = itemView.findViewById(R.id.price)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProposalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_proposal, parent, false)
        return ProposalViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProposalViewHolder, position: Int) {
        val proposal = proposalsList[position]
        holder.hotelName.text = proposal.hotel_name
        holder.country.text = proposal.country
        holder.tourCity.text = proposal.city_tour
        holder.startDate.text = proposal.data_start
        holder.endDate.text = proposal.data_end
        holder.endDate.text = proposal.data_end
        holder.price.text = formatNumber(proposal.price)

        if (proposal.hotel_img != "") {
            Picasso.get().load(proposal.hotel_img).into(holder.img)
        }

        val night = proposal.night.toString()
        holder.night.text = "$night ноч."

        holder.itemView.setOnClickListener {
            itemClickListener.onProposalItemClick(position)
        }

    }

    fun formatNumber(number: Int): String {
        val numberAsString = number.toString()
        var formatted = ""
        for (i in numberAsString.indices step 3) {
            formatted += numberAsString.substring(i, minOf(i + 3, numberAsString.length)) + " "
        }

        return formatted + "₽"
    }


    override fun getItemCount(): Int {
        return proposalsList.size
    }
}
