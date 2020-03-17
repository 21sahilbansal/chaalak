package com.loconav.locodriver.Trips.tripList

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.loconav.locodriver.Constants
import com.loconav.locodriver.R
import com.loconav.locodriver.Trips.model.TripData
import com.loconav.locodriver.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_trips.*

class TripsFragment : BaseFragment() {
    private var tripsListViewModel: TripsListViewModel? = null

    override fun onViewInflated(view: View, savedInstanceState: Bundle?) {
        tripsListViewModel = ViewModelProviders.of(this).get(TripsListViewModel::class.java)
        progressBar.visibility = View.VISIBLE
        tripsListViewModel?.getTripList()?.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()) {
                progressBar.visibility = View.GONE
                no_trip_layout.visibility = View.VISIBLE
                no_trip_text.visibility = View.VISIBLE
            } else {
                no_trip_layout.visibility = View.GONE
                no_trip_text.visibility = View.GONE
                initAdapter(list_recycler_view, it)
                progressBar.visibility = View.GONE
            }
        })
        tripsListViewModel?.fetchTripListData()?.observe(viewLifecycleOwner, Observer {
            Log.i("transformation done", it.toString())
        })

        call_fab.setOnClickListener {
            val phoneIntent = Intent(
                Intent.ACTION_DIAL, Uri.parse(
                    String.format(
                        "%s%s",
                        Constants.TripConstants.INTENT_ACTION_DIAL_TEXT,
                        Constants.TripConstants.CONTACT_PHONE_NUMBER
                    )
                )
            )
            startActivity(phoneIntent)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_trips
    }

    companion object {
        fun getInstance(): TripsFragment {
            return TripsFragment()
        }
    }

    override fun onResume() {
        super.onResume()
        tripsListViewModel?.fetchTripListData()
    }

    private fun initAdapter(view: RecyclerView, tripData: List<TripData>) {
        val tripListAdapter = TripListAdapter(tripData)
        val layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        view.layoutManager = layoutManager
        view.adapter = tripListAdapter
    }
}