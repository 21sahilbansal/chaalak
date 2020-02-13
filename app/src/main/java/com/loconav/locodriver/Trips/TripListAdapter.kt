package com.loconav.locodriver.Trips

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.loconav.locodriver.R
import com.loconav.locodriver.Trips.model.TripData
import com.loconav.locodriver.Trips.model.TripDataResponse

class TripListAdapter(val tripData: List<TripData>) :
    androidx.recyclerview.widget.RecyclerView.Adapter<TripListAdapter.TripListAdapterViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripListAdapterViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_trips_card, parent, false)
        return TripListAdapterViewHolder(view, tripData)
    }

    override fun getItemCount(): Int {
        return tripData.size
    }

    override fun onBindViewHolder(holder: TripListAdapterViewHolder, position: Int) {
        holder.enableActiveTripView(position)
    }

    class TripListAdapterViewHolder(itemView: View, tripData: List<TripData>) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        fun enableActiveTripView(position: Int) {
            val activeTag = itemView.findViewById<TextView>(R.id.active_tag_tv)
            val startTripButton = itemView.findViewById<Button>(R.id.button_start_trip)
            //added for position just to check UI need to check on active tag in data list
            if (position == 0) {
                activeTag.visibility = View.VISIBLE
                startTripButton.visibility = View.VISIBLE
            } else {
                activeTag.visibility = View.INVISIBLE
                startTripButton.visibility = View.GONE
            }
        }
    }

}