package com.loconav.locodriver.Trips

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.loconav.locodriver.R
import com.loconav.locodriver.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_trips.*

class TripsFragment : BaseFragment() {
    override fun onViewInflated(view: View, savedInstanceState: Bundle?) {
        initAdapter(list_recycler_view)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_trips
    }

    companion object{
        fun getInstance():TripsFragment{
            return TripsFragment()
        }
    }
    private fun initAdapter(view: RecyclerView){
        val tripListAdapter=TripListAdapter()
        val layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        view.layoutManager = layoutManager
        view.adapter = tripListAdapter
    }
}