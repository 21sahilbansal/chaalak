package com.loconav.locodriver.expense

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.loconav.locodriver.R
import com.loconav.locodriver.base.BaseFragment

class AddExpenseFragment:BaseFragment() {
    override fun onViewInflated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        val actionBar = (activity as AppCompatActivity).supportActionBar as ActionBar
        actionBar.let {
            it.title = getString(R.string.add_expense_toolbar_title)
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            activity?.onBackPressed()
            return true
        }
        return false
    }

    override fun getLayoutId(): Int {
        return R.layout.add_expense_fragment
    }

    companion object{
        fun getInstance():AddExpenseFragment{
            return AddExpenseFragment()
        }
    }
}