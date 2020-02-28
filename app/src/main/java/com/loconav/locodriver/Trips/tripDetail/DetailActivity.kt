package com.loconav.locodriver.Trips.tripDetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.loconav.locodriver.Constants
import com.loconav.locodriver.Constants.FRAGMENT_TAG.Companion.TRIP_DETAIL_FRAGMENT
import com.loconav.locodriver.R
import com.loconav.locodriver.base.BaseFragmentActivity
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : BaseFragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        intent?.data?.let {
            inflateReplaceFragment(
                TripDetailFragment.getInstance(it.toString()),
                false,
                TRIP_DETAIL_FRAGMENT
            )
        }
        call_fab.setOnClickListener {
            val phoneIntent = Intent(
                Intent.ACTION_DIAL,
                Uri.parse(
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

    override val frameId: Int
        get() = R.id.fragment_container
}