package com.loconav.locodriver.Trips.tripList

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.loconav.locodriver.Constants
import com.loconav.locodriver.R
import com.loconav.locodriver.Trips.TripStateGeneratorUtil
import com.loconav.locodriver.Trips.tripDetail.DetailActivity
import com.loconav.locodriver.Trips.model.TripData
import com.loconav.locodriver.db.sharedPF.SharedPreferenceUtil
import kotlinx.android.synthetic.main.item_trips_card.view.*
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class TripListAdapter(val tripData: List<TripData>) :
    androidx.recyclerview.widget.RecyclerView.Adapter<TripListAdapter.TripListAdapterViewHolder>(),KoinComponent {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripListAdapterViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_trips_card, parent, false)
        return TripListAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tripData.size
    }

    override fun onBindViewHolder(holder: TripListAdapterViewHolder, position: Int) {
//        holder.setTripState(tripData[position])
        holder.setTripActiveState(tripData[position], position)
        holder.setData(tripData[position])
        holder.enableActiveTripView(tripData[position])
        holder.setClickListeners(tripData[position])

    }

    class TripListAdapterViewHolder(itemView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView),KoinComponent {
        val sharedPreferenceUtil:SharedPreferenceUtil by inject()

        fun setData(tripData: TripData) {
            val viewContext = itemView.context

            tripData.vehicleNumber?.let {
                itemView.vehicle_number_tv.text = it
            } ?: run {
                itemView.vehicle_number_tv.text =
                    viewContext.getString(R.string.unknown_vehicle_number)
            }

            tripData.tripSource?.polygonDetail?.checkPointCity?.cityName?.let {
                itemView.source_city_tv.text = it
            } ?: run {
                itemView.source_city_tv.text = viewContext.getString(R.string.unknown_source_city)
            }

            tripData.tripShouldStartTs?.let {
                itemView.start_time_value_tv.text =
                    com.loconav.locodriver.util.TimeUtils.getDateTimeFromEpoch(it)
            } ?: run {
                itemView.start_time_value_tv.text =
                    viewContext.getString(R.string.unknown_start_time)
            }

            tripData.tripDestination?.polygonDetail?.checkPointCity?.cityName?.let {
                itemView.destination_city_tv.text = it
            } ?: run {
                itemView.destination_city_tv.text =
                    viewContext.getString(R.string.unknown_dest_text)
            }

            tripData.tripDestination?.exitEta?.let {
                itemView.end_time_value_tv.text =
                    com.loconav.locodriver.util.TimeUtils.getDateTimeFromEpoch(it)
            } ?: run {
                itemView.end_time_value_tv.text =
                    viewContext.getString(R.string.unknown_dest_end_time)
            }

//            tripData.driverCta?.ctaName?.let {
//                itemView.button_start_trip.text = TripStateGeneratorUtil.getCurrentState()
//            }
        }

        fun setClickListeners(tripData: TripData){
            val holderOnClickListener=View.OnClickListener {
                val intent = Intent(
                    itemView.context,
                    DetailActivity::class.java
                )
                intent.data = Uri.parse(tripData.tripUniqueId)
                itemView.context.startActivity(intent)
            }

            itemView.setOnClickListener(holderOnClickListener)
            itemView.button_start_trip.setOnClickListener(holderOnClickListener)
        }

        fun enableActiveTripView(tripData: TripData) {

            when {
                tripData.activeState == false -> {
                    itemView.active_tag_tv.visibility = View.GONE
                    itemView.button_start_trip.visibility = View.GONE
                }
                tripData.actionableFlag == false ->
                    itemView.button_start_trip.visibility = View.GONE
                else -> {
                    itemView.active_tag_tv.visibility = View.VISIBLE
                    itemView.button_start_trip.visibility = View.VISIBLE
                }
            }
        }
//        fun setTripState(tripData: TripData){
//            TripStateGeneratorUtil.setStateList(tripData)
//            tripData.currentState=TripStateGeneratorUtil.getCurrentState()
////            if(tripData.driverCta?.ctaName == Constants.SharedPreferences.DRIVER_CTA_LABEL_TRIP_START
////                ||tripData.driverCta?.ctaName == Constants.SharedPreferences.DRIVER_CTA_LABEL_TRIP_END
////                ||tripData.driverCta?.ctaName == Constants.SharedPreferences.DRIVER_CTA_LABEL_SOURCE_ENTRY
////                ||tripData.driverCta?.ctaName == Constants.SharedPreferences.DRIVER_CTA_LABEL_SOURCE_EXIT
////                ||tripData.driverCta?.ctaName == Constants.SharedPreferences.DRIVER_CTA_LABEL_DESTINATION_ENTRY
////                ||tripData.driverCta?.ctaName == Constants.SharedPreferences.DRIVER_CTA_LABEL_DESTINATION_EXIT )
////            tripData.currentCptId=tripData.driverCta.
//        }

        fun setTripActiveState(tripData: TripData, position: Int) {
            tripData.activeState = position == 0
        }
    }
}