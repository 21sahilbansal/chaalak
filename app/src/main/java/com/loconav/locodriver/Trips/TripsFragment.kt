package com.loconav.locodriver.Trips

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TripsFragment : BaseFragment() {
    var tripsListViewModel: TripsListViewModel? = null

    override fun onViewInflated(view: View, savedInstanceState: Bundle?) {
        tripsListViewModel = ViewModelProviders.of(this).get(TripsListViewModel::class.java)
        tripsListViewModel?.tripListDataResponse?.observe(this, Observer { dataWrapper ->
            val tripsList = dataWrapper.data?.tripDataList
            Log.i("trip_list",tripsList?.size.toString())
            tripsList?.let {
                initAdapter(list_recycler_view, it)
            }
        })
        GlobalScope.launch(Dispatchers.Main) {
            tripsListViewModel?.getTripsList()
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

    private fun initAdapter(view: RecyclerView, tripData: List<TripData>) {
        val tripListAdapter = TripListAdapter(tripData)
        val layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        view.layoutManager = layoutManager
        view.adapter = tripListAdapter
    }
}