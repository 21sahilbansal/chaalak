package com.loconav.locodriver.Trips.tripDetail

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.loconav.locodriver.R
import com.loconav.locodriver.Trips.TripActionConfirmationDialog
import com.loconav.locodriver.Trips.TripStateGeneratorUtil
import com.loconav.locodriver.Trips.model.TripData
import com.loconav.locodriver.base.BaseFragment
import kotlinx.android.synthetic.main.trip_detail_fragment.*

class TripDetailFragment : BaseFragment() {
    var tripDetailFragmentViewModel: TripDetailFragmentViewModel? = null
    var actionBar: ActionBar? = null

    override fun onViewInflated(view: View, savedInstanceState: Bundle?) {
        tripDetailFragmentViewModel =
            ViewModelProviders.of(this).get(TripDetailFragmentViewModel::class.java)
        setHasOptionsMenu(true)
        setActionBar()
        tripDetailFragmentViewModel?.getTrip(arguments?.get(TRIP_ID).toString())?.observe(this,
            Observer {
                //                TripStateGeneratorUtil.setStateList(it)
                setData(it)
                setTripActions(it)
            })
        setClickListener()
    }

    private fun setClickListener() {
//        driver_cta_button.setOnClickListener {
////            val confirmationDialog = TripActionConfirmationDialog()
////            confirmationDialog.show(childFragmentManager, "trip_action_confirmation_dialog")
//        }
    }

    private fun setData(trip: TripData) {
        trip_detail_veh_number_tv.text = trip.vehicleNumber
//        driver_cta_button.text = TripStateGeneratorUtil.getCurrentState()
//        driver_cta_button.text = trip.driverCta?.ctaName
        initTripDetailAdapter(trip_detail_rec, trip)
    }

    private fun setTripActions(trip: TripData) {
        val activeTag = actionBar?.customView?.findViewById<TextView>(R.id.active_tag_tv)
        val upComingTripTag = actionBar?.customView?.findViewById<TextView>(R.id.upcoming_tag_tv)

        when {
            trip.activeState == false -> {
                driver_cta_button_ll.visibility = View.GONE
                activeTag?.visibility = View.GONE
                upComingTripTag?.visibility = View.VISIBLE
            }
            trip.actionableFlag == false -> {
                driver_cta_button_ll.visibility = View.GONE
            }
            else -> {
                driver_cta_button_ll.visibility = View.VISIBLE
                activeTag?.visibility = View.VISIBLE
                upComingTripTag?.visibility = View.GONE
            }
        }
    }

    private fun setActionBar() {
        actionBar = (activity as AppCompatActivity).supportActionBar as ActionBar
        actionBar?.let {
            it.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            it.setCustomView(R.layout.item_trip_detail_action_bar)
            val actionBarView = it.customView
            val title = actionBarView.findViewById<TextView>(R.id.trip_detail_action_bar_title)
            val backArrow = actionBarView.findViewById<ImageView>(R.id.trip_detail_back_arrow)
            title.text = arguments?.get(TRIP_ID).toString()
            backArrow.setOnClickListener {
                activity?.onBackPressed()
            }
        }
    }

    private fun initTripDetailAdapter(view: RecyclerView, trip: TripData) {
        if (trip.checkPointsList.isNullOrEmpty()) {
            return
        }
        val tripDetailListAdapter = TripDetailListAdapter(trip.checkPointsList!!)
        val layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        view.layoutManager = layoutManager
        view.adapter = tripDetailListAdapter
    }

    override fun getLayoutId(): Int {
        return R.layout.trip_detail_fragment
    }

    companion object {
        fun getInstance(tripId: String): TripDetailFragment {
            val tripDetaiFragment = TripDetailFragment()
            val bundle = Bundle()
            bundle.putString(TRIP_ID, tripId)
            tripDetaiFragment.arguments = bundle
            return tripDetaiFragment
        }

        const val TRIP_ID = "trip_unique_id"
    }
}