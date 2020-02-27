package com.loconav.locodriver.expense

import android.os.Bundle
import android.view.View
import com.loconav.locodriver.R
import com.loconav.locodriver.base.BaseFragment

class ExpenseListFragment : BaseFragment() {
    override fun onViewInflated(view: View, savedInstanceState: Bundle?) {

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_expense_list
    }

    companion object {
        fun getInstance(): ExpenseListFragment {
            return ExpenseListFragment()
        }
    }

}