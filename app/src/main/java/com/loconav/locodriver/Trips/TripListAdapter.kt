package com.loconav.locodriver.Trips

import android.util.TimeUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.loconav.locodriver.R
import com.loconav.locodriver.Trips.model.TripData
import com.loconav.locodriver.Trips.model.TripDataResponse
import kotlinx.android.synthetic.main.item_trips_card.view.*

class TripListAdapter(val tripData: List<TripData>) :
    androidx.recyclerview.widget.RecyclerView.Adapter<TripListAdapter.TripListAdapterViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripListAdapterViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_trips_card, parent, false)
        return TripListAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tripData.size
    }

    override fun onBindViewHolder(holder: TripListAdapterViewHolder, position: Int) {
        holder.setData(tripData[position])
        holder.enableActiveTripView(position)
    }

    class TripListAdapterViewHolder(itemView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        fun setData(tripData: TripData) {
            itemView.vehicle_number_tv.text = tripData.vehicleNumber
            itemView.source_city_tv.text =
                tripData.tripSource?.polygonDetail?.checkPointCity?.cityName
            itemView.start_time_value_tv.text = tripData.tripShouldStartTs.toString()
            itemView.destination_city_tv.text =
                tripData.tripDestination?.polygonDetail?.checkPointCity?.cityName
            itemView.end_time_value_tv.text = tripData.tripDestination?.exitEta.toString()
        }

        fun enableActiveTripView(position: Int) {
            /**
             * trip object will have a flag for active status and actionable button
             * till then added a hardcoded flag.
             */
            val flag = true
            if (flag && position == 0) {
                itemView.active_tag_tv.visibility = View.VISIBLE
                itemView.button_start_trip.visibility = View.VISIBLE
            } else {
                itemView.active_tag_tv.visibility = View.INVISIBLE
                itemView.button_start_trip.visibility = View.GONE
            }
        }
    }
}