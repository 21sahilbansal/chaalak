package com.loconav.locodriver.Trips.tripDetail

import android.os.Bundle
import com.loconav.locodriver.Constants.ExpenseConstants.Companion.EXPENSE_TITLE
import com.loconav.locodriver.Constants.ExpenseConstants.Companion.FAKE_EXPENSE
import com.loconav.locodriver.Constants.ExpenseConstants.Companion.SOURCE
import com.loconav.locodriver.Constants.ExpenseConstants.Companion.SOURCE_EXPENSE
import com.loconav.locodriver.Constants.FRAGMENT_TAG.Companion.EXPENSE_DETAIL_FRAGMENT
import com.loconav.locodriver.Constants.FRAGMENT_TAG.Companion.TRIP_DETAIL_FRAGMENT
import com.loconav.locodriver.Constants.TripConstants.Companion.SOURCE_TRIP
import com.loconav.locodriver.R
import com.loconav.locodriver.base.BaseFragmentActivity
import com.loconav.locodriver.expense.expenseDetail.ExpenseDetailFragment

class DetailActivity : BaseFragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        intent.extras?.let {
            when (it.getString(SOURCE)) {
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
                                it.toString(),
                                intent.extras?.getString(EXPENSE_TITLE),
                                intent.extras?.getBoolean(FAKE_EXPENSE)

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