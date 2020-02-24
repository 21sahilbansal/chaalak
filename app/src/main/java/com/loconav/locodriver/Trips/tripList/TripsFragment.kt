package com.loconav.locodriver.Trips.tripList

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.loconav.locodriver.R
import com.loconav.locodriver.Trips.model.TripData
import com.loconav.locodriver.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_trips.*

class TripsFragment : BaseFragment() {
    var tripsListViewModel: TripsListViewModel? = null

    override fun onViewInflated(view: View, savedInstanceState: Bundle?) {
        tripsListViewModel = ViewModelProviders.of(this).get(TripsListViewModel::class.java)
        progressBar.visibility = View.VISIBLE
        tripsListViewModel?.getTripList()?.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()) {
                progressBar.visibility = View.GONE
                no_trip_layout.visibility =View.VISIBLE
                no_trip_text.visibility=View.VISIBLE
                //no trip view required
            } else {
                no_trip_layout.visibility =View.GONE
                no_trip_text.visibility=View.GONE
                initAdapter(list_recycler_view, it)
                progressBar.visibility = View.GONE
            }
        })
        tripsListViewModel?.getTransformedData()?.observe(viewLifecycleOwner, Observer {
            Log.i("transformation done", it.toString())
        })
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
        tripsListViewModel?.getTransformedData()
    }

    private fun initAdapter(view: RecyclerView, tripData: List<TripData>) {
        val tripListAdapter = TripListAdapter(tripData)
        val layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        view.layoutManager = layoutManager
        view.adapter = tripListAdapter
    }
}