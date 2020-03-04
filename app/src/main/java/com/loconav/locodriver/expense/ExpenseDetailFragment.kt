package com.loconav.locodriver.expense

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.loconav.locodriver.Constants
import com.loconav.locodriver.R
import com.loconav.locodriver.base.BaseFragment
import com.loconav.locodriver.util.TimeUtils
import kotlinx.android.synthetic.main.fragment_enter_otp.progressBar
import kotlinx.android.synthetic.main.fragment_expense_detail.*

class ExpenseDetailFragment : BaseFragment() {
    private var expenseDetailViewModel: ExpenseDetailViewModel? = null
    override fun onViewInflated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        val actionBar = (activity as AppCompatActivity).supportActionBar as ActionBar
        actionBar.let {
            it.title = arguments?.getString(EXPENSE_TITLE)
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }
        expenseDetailViewModel = ViewModelProviders.of(this).get(ExpenseDetailViewModel::class.java)
        arguments?.getLong(EXPENSE_ID)?.let {
            expenseDetailViewModel?.getIndividualExpenseFromDb(it)?.observe(this, Observer {
                progressBar.visibility = View.GONE
                setData(it)
                initDocumentimageAdapter(expense_doc_rec,it)
            })
            progressBar.visibility = View.VISIBLE
            initRequest(it)
        }

    }

    private fun initRequest(expenseId: Long) {
        expenseDetailViewModel?.getIndividualExpense(expenseId)?.observe(this, Observer {
            it.throwable?.let { error ->
                progressBar.visibility = View.GONE
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        arguments?.getLong(EXPENSE_ID)?.let {
            initRequest(it)
        }

    }

    private fun initDocumentimageAdapter(view:RecyclerView,expense:Expense){
        val expenseListAdapter = ExpenseDocumentAdapter(expense)
        val layoutManager = GridLayoutManager(view.context, 2,GridLayoutManager.HORIZONTAL,false)
        view.layoutManager = layoutManager
        view.adapter = expenseListAdapter
    }

    private fun setData(expense: Expense) {
        expense.amount?.let {
            tv_trip_expense_amount.text = String.format(getString(R.string.rupee), it)
        } ?: run {
            tv_trip_expense_amount.text = getString(R.string.no_amount_present)
        }
        expense.expenseDate?.let {
            tv_expense_date.text = String.format(
                "%s,%s",
                TimeUtils.getThFormatTime(it),
                TimeUtils.getDateTimeFromEpoch(it, Constants.RegexConstants.TIME_FORMAT_12_HOUR)
            )
        } ?: run {
            tv_expense_date.text = getString(R.string.unknown_time_text)
        }
        expense.verificationStatus?.let {
            expense_status_tv.text = it
            setColor(expense_status_tv, it)
        } ?: run {
            expense_status_tv.text = getString(R.string.verification_pending_expense_status)
            expense_status_tv.setTextColor(
                ContextCompat.getColor(
                    expense_status_tv.context,
                    R.color.color_pending_brown
                )
            )
        }
    }

    private fun setColor(view: TextView, status: String) {
        when (status) {
            Constants.ExpenseConstants.VERIFICATION_PENDING -> {
                view.setTextColor(
                    ContextCompat.getColor(
                        view.context,
                        R.color.color_pending_brown
                    )
                )
            }
            Constants.ExpenseConstants.VERIFIED -> {
                view.setTextColor(
                    ContextCompat.getColor(
                        view.context,
                        R.color.color_approved_green
                    )
                )
            }
            Constants.ExpenseConstants.REJECTED -> {
                view.setTextColor(
                    ContextCompat.getColor(
                        view.context,
                        R.color.color_rejected_red
                    )
                )
            }
        }
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_expense_detail
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            activity?.onBackPressed()
            return true
        }
        return false
    }

    companion object {
        fun getInstance(expenseId: Long, expenseTitle: String?): ExpenseDetailFragment {
            val expenseDetaiFragment = ExpenseDetailFragment()
            val bundle = Bundle()
            bundle.putLong(EXPENSE_ID, expenseId)
            bundle.putString(EXPENSE_TITLE, expenseTitle)
            expenseDetaiFragment.arguments = bundle
            return expenseDetaiFragment
        }

        private const val EXPENSE_ID = "expense_id"
        private const val EXPENSE_TITLE = "expense_title"


    }
}