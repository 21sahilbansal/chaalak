package com.loconav.locodriver.expense

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.loconav.locodriver.Constants
import com.loconav.locodriver.R
import com.loconav.locodriver.Trips.tripList.TripsListViewModel
import com.loconav.locodriver.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_enter_otp.*
import kotlinx.android.synthetic.main.fragment_enter_otp.progressBar
import kotlinx.android.synthetic.main.fragment_expense_list.*
import kotlinx.coroutines.GlobalScope
import okhttp3.Dispatcher

class ExpenseListFragment : BaseFragment() {
    var expenseListViewModel: ExpenseListViewModel? = null
    var page: Int = 1

    override fun onViewInflated(view: View, savedInstanceState: Bundle?) {
        expenseListViewModel = ViewModelProviders.of(this).get(ExpenseListViewModel::class.java)
        expenseListViewModel?.getExpenseListFromDb()?.observe(this, Observer {
            if (it.isNullOrEmpty()) {
                progressBar.visibility = View.GONE
                no_expense_layout.visibility = View.VISIBLE
                no_expense_text.visibility = View.VISIBLE
                no_expense_sub_text.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
                no_expense_layout.visibility = View.GONE
                no_expense_text.visibility = View.GONE
                no_expense_sub_text.visibility = View.GONE
                initAdapter(list_recycler_view, it)
            }
        })
        progressBar.visibility = View.VISIBLE
        initRequest(page)
        add_expense_fab.setOnClickListener {
            //            val phoneIntent = Intent(
//                Intent.ACTION_DIAL, Uri.parse(
//                    String.format(
//                        "%s%s",
//                        Constants.TripConstants.INTENT_ACTION_DIAL_TEXT,
//                        Constants.TripConstants.CONTACT_PHONE_NUMBER
//                    )
//                )
//            )
//            startActivity(phoneIntent)
        }

    }

    private fun initRequest(page: Int) {
        expenseListViewModel?.getFetchExpenseList(page)?.observe(this, Observer {
            it.throwable?.let { error ->
                progressBar.visibility = View.GONE
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun attachScrollListener(layoutManager: LinearLayoutManager) {
        list_recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                    loadMoreItems()
                }
            }
        })
    }

    private fun loadMoreItems() {
        initRequest(page++)
    }

    private fun initAdapter(view: RecyclerView, expenseList: List<Expense>) {
        val expenseListAdapter = ExpenseListAdapter(expenseList)
        val layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        view.layoutManager = layoutManager
        view.adapter = expenseListAdapter
        attachScrollListener(layoutManager)

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