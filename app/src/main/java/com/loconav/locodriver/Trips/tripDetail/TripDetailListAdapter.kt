package com.loconav.locodriver.Trips.tripDetail

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.loconav.locodriver.Constants
import com.loconav.locodriver.R
import com.loconav.locodriver.Trips.model.CheckPointData
import com.loconav.locodriver.util.TimeUtils
import kotlinx.android.synthetic.main.item_trip_detail_checkpoints.view.*

class TripDetailListAdapter(private val checkpointList: List<CheckPointData>) :
    RecyclerView.Adapter<TripDetailListAdapter.TripDataAdapterViewHolder>() {
    override fun onBindViewHolder(holder: TripDataAdapterViewHolder, position: Int) {
        holder.setCheckpointData(checkpointList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripDataAdapterViewHolder {

        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_trip_detail_checkpoints, parent, false)

        return TripDataAdapterViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return checkpointList.size
    }

    class TripDataAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val viewContext = itemView.context
        fun setCheckpointData(checkpoint: CheckPointData) {
            itemView.start_location_address_tv.text = checkpoint.checkPointAddress
            setActivityTag(checkpoint)
            setCheckpointTimeText(checkpoint)

            when (checkpoint.checkPointIdentifier) {
                Constants.TripConstants.SOURCE_IDENTIFIER -> {
                    itemView.checkpoint_location_text.text =
                        viewContext.getString(R.string.start_location_text)
                    setItemData(R.drawable.ic_source, View.GONE, View.VISIBLE, R.style.BodyLarge)
                }
                Constants.TripConstants.CHECKPOINT_IDENTIFIER -> {
                    itemView.checkpoint_location_text.text =
                        String.format(
                            viewContext.getString(R.string.checkpoint_position_text),
                            checkpoint.checkPointPosition?.minus(1)
                        )
                    setItemData(
                        R.drawable.ic_checkpoint_dot,
                        View.VISIBLE,
                        View.VISIBLE,
                        R.style.BodyBoldLight
                    )
                }
                Constants.TripConstants.DESTINATION_IDENTIFIER -> {
                    itemView.checkpoint_location_text.text =
                        viewContext.getString(R.string.end_location_text)
                    setItemData(
                        R.drawable.ic_destination,
                        View.VISIBLE,
                        View.GONE,
                        R.style.BodyLarge
                    )
                }
            }

        }

        private fun setCheckpointTimeText(checkpoint: CheckPointData) {
            when {
                checkpoint.entryTs == null && checkpoint.exitTs == null -> {
                    itemView.start_time_tv.text =
                        viewContext.getString(R.string.eta_text_trip_detail)
                    setCheckpointTime(checkpoint.entryEta)
                }
                checkpoint.entryTs != null && checkpoint.exitTs == null -> {
                    itemView.start_time_tv.text =
                        viewContext.getString(R.string.start_time_text_trip_detail)
                    setCheckpointTime(checkpoint.entryTs)
                }

                checkpoint.exitTs != null -> {
                    itemView.start_time_tv.text =
                        viewContext.getString(R.string.end_Time_text_trip_details)
                    setCheckpointTime(checkpoint.exitTs)
                }

            }
        }

        private fun setCheckpointTime(checkpointTime: Long?) {
            checkpointTime?.let {
                itemView.start_time_value_tv.text = TimeUtils.getDateTimeFromEpoch(it,Constants.RegexConstants.DATE_TIME_FORMAT)
            } ?: run {
                itemView.start_time_value_tv.text =
                    viewContext.getString(R.string.unknown_time_text)
            }
        }

        private fun setActivityTag(checkpoint: CheckPointData) {

            checkpoint.chekPointActivity?.checkPointActivityName?.let {
                itemView.active_tag_tv.text =
                    checkpoint.chekPointActivity?.checkPointActivityName
            } ?: run {
                itemView.active_tag_tv.visibility = View.GONE
            }
        }

        private fun setItemData(
            checkpointImageResId: Int,
            trackLineAboveVisibility: Int,
            trackLineBelowVisibility: Int,
            textStyleResId: Int
        ) {
            itemView.source_icon.setImageResource(checkpointImageResId)
            itemView.vertical_solid_line_above.visibility = trackLineAboveVisibility
            itemView.vertical_solid_line_below.visibility = trackLineBelowVisibility
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                itemView.start_location_address_tv.setTextAppearance(textStyleResId)
            } else {
                itemView.start_location_address_tv.setTextAppearance(
                    itemView.context,
                    textStyleResId
                )
            }
        }

    }
}
