package com.loconav.locodriver.expense

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import com.loconav.locodriver.R
import com.loconav.locodriver.base.BaseFragment
import com.loconav.locodriver.db.sharedPF.SharedPreferenceUtil
import kotlinx.android.synthetic.main.add_expense_fragment.*
import kotlinx.android.synthetic.main.add_expense_fragment.view.*
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class AddExpenseFragment : BaseFragment(), KoinComponent {

    val sharedPreferenceUtil: SharedPreferenceUtil by inject()
    var addExpenseViewModel: AddExpenseViewModel? = null
    override fun onViewInflated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        val actionBar = (activity as AppCompatActivity).supportActionBar as ActionBar
        actionBar.let {
            it.title = getString(R.string.add_expense_toolbar_title)
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }
        addExpenseViewModel = ViewModelProviders.of(this).get(AddExpenseViewModel::class.java)
        addExpenseViewModel?.getExpenseType()?.observe(this, Observer {
            it.data?.let {
                setSpinnerAdapter()
            } ?: kotlin.run {
                it.throwable?.let { error ->
                    Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

    }
    private val expenseTypeSpinnerItemSelectListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(p0: AdapterView<*>?) {
        }

        override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, p3: Long) {
            if (position == 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    (view as TextView).setTextAppearance(R.style.BodyLight)
                } else {
                    (view as TextView).setTextAppearance(view.context, R.style.BodyLight)
                }
            }
        }
    }
    private val expensedateSpinnerItemSelectListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(p0: AdapterView<*>?) {
        }

        override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, p3: Long) {
            if (position == 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    (view as TextView).setTextAppearance(R.style.BodyLight)
                } else {
                    (view as TextView).setTextAppearance(view.context, R.style.BodyLight)
                }
            }
        }
    }
    private val expenseMonthSpinnerItemSelectListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(p0: AdapterView<*>?) {
        }

        override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, p3: Long) {
            if (position == 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    (view as TextView).setTextAppearance(R.style.BodyLight)
                } else {
                    (view as TextView).setTextAppearance(view.context, R.style.BodyLight)
                }
            } else{
                addExpenseViewModel?.updateDateList(position)
            }
        }
    }
    private val expenseYearSpinnerItemSelectListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(p0: AdapterView<*>?) {
        }

        override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, p3: Long) {
            if (position == 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    (view as TextView).setTextAppearance(R.style.BodyLight)
                } else {
                    (view as TextView).setTextAppearance(view.context, R.style.BodyLight)
                }
            }
        }
    }

    private fun setSpinnerAdapter() {
        setExpenseTypeSpinnerAdapter()
        setDateSpinner()
    }

    private fun setExpenseTypeSpinnerAdapter() {
        addExpenseViewModel?.expenseTypeList?.observe(this, Observer {
            val foodListAdapter =
                ArrayAdapter<String>(context!!, android.R.layout.simple_spinner_item, it)
            foodListAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown)
            spinner_expense_type.adapter = foodListAdapter
        })
        addExpenseViewModel?.getExpenseTypeList()
        spinner_expense_type.onItemSelectedListener =
            expenseTypeSpinnerItemSelectListener
    }

    private fun setDateSpinner(){
        addExpenseViewModel?.getDateSpinnerList()
        setDaySpinner()
        setMonthSpinner()
        setYearSpinner()

    }

    private fun setDaySpinner(){
        addExpenseViewModel?.dateLiveList?.observe(this, Observer {
            val dateListAdapter =
                ArrayAdapter<String>(context!!, android.R.layout.simple_spinner_item, it)
            dateListAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown)
            spinner_expense_date.adapter = dateListAdapter
        })
        spinner_expense_date.onItemSelectedListener =
            expensedateSpinnerItemSelectListener
    }
    private fun setMonthSpinner(){
        addExpenseViewModel?.monthLiveList?.observe(this, Observer {
            val monthListAdapter =
                ArrayAdapter<String>(context!!, android.R.layout.simple_spinner_item, it)
            monthListAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown)
            spinner_expense_month.adapter = monthListAdapter
        })
        spinner_expense_month.onItemSelectedListener =
            expenseMonthSpinnerItemSelectListener
    }
    private fun setYearSpinner(){
        addExpenseViewModel?.yearLiveList?.observe(this, Observer {
            val yearListAdapter =
                ArrayAdapter<String>(context!!, android.R.layout.simple_spinner_item, it)
            yearListAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown)
            spinner_expense_year.adapter = yearListAdapter
        })
       spinner_expense_year.onItemSelectedListener =
            expenseYearSpinnerItemSelectListener
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

    companion object {
        fun getInstance(): AddExpenseFragment {
            return AddExpenseFragment()
        }
    }
}