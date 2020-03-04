package com.loconav.locodriver.expense

import android.os.Bundle
import com.loconav.locodriver.Constants
import com.loconav.locodriver.R
import com.loconav.locodriver.base.BaseFragmentActivity

class AddExpenseActivity : BaseFragmentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)
        inflateReplaceFragment(
            AddExpenseFragment.getInstance(),
            false,
            Constants.FRAGMENT_TAG.ADD_EXPENSE_FRAGEMENT
        )
    }
    override val frameId: Int
        get() = R.id.fragment_container
}