package com.loconav.locodriver.Trips.tripDetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.loconav.locodriver.Constants.FRAGMENT_TAG.Companion.TRIP_DETAIL_FRAGMENT
import com.loconav.locodriver.R
import com.loconav.locodriver.base.BaseFragmentActivity
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : BaseFragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        inflateReplaceFragment(
            TripDetailFragment.getInstance(intent.data.toString()),
            false,
            TRIP_DETAIL_FRAGMENT
        )
        call_fab.setOnClickListener {
            val phoneIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+"9650793733"))
            startActivity(phoneIntent)
        }
    }

    override val frameId: Int
        get() = R.id.fragment_container
}