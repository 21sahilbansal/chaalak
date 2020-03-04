package com.loconav.locodriver.Trips.tripDetail

import android.os.Bundle
import com.loconav.locodriver.Constants.ExpenseConstants.Companion.SOURCE_EXPENSE
import com.loconav.locodriver.Constants.FRAGMENT_TAG.Companion.EXPENSE_DETAIL_FRAGMENT
import com.loconav.locodriver.Constants.FRAGMENT_TAG.Companion.TRIP_DETAIL_FRAGMENT
import com.loconav.locodriver.Constants.TripConstants.Companion.SOURCE_TRIP
import com.loconav.locodriver.R
import com.loconav.locodriver.base.BaseFragmentActivity
import com.loconav.locodriver.expense.ExpenseDetailFragment

class DetailActivity : BaseFragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        intent.extras?.let {
            when (it.getString("source")) {
                SOURCE_TRIP -> {
                    intent?.data?.let {
                        inflateReplaceFragment(
                            TripDetailFragment.getInstance(it.toString()),
                            false,
                            TRIP_DETAIL_FRAGMENT
                        )
                    }
                }
                SOURCE_EXPENSE -> {
                    intent?.data?.let {
                        inflateReplaceFragment(
                            ExpenseDetailFragment.getInstance(
                                it.toString().toLong(),
                                intent.extras?.getString("expense_title")
                            ),
                            false,
                            EXPENSE_DETAIL_FRAGMENT
                        )
                    }
                }
                else -> {
                }
            }
        }
    }

    override val frameId: Int
        get() = R.id.fragment_container
}