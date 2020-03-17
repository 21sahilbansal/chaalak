package com.loconav.locodriver.expense.expenseList

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.loconav.locodriver.R
import com.loconav.locodriver.base.BaseFragment
import com.loconav.locodriver.expense.addExpense.AddExpenseActivity
import com.loconav.locodriver.expense.model.Expense
import kotlinx.android.synthetic.main.fragment_enter_otp.progressBar
import kotlinx.android.synthetic.main.fragment_expense_list.*

class ExpenseListFragment : BaseFragment() {
    private var expenseListViewModel: ExpenseListViewModel? = null
    var page: Int = 1
    private var isLastPage: Boolean? = null

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
            val intent = Intent(context, AddExpenseActivity::class.java)
            startActivity(intent)
        }

    }

    private fun initRequest(page: Int) {
        expenseListViewModel?.getFetchExpenseList(page)?.observe(this, Observer {
            it.data?.let {
                isLastPage = it.isNullOrEmpty()
                progressBar.visibility = View.GONE
            }
            it.throwable?.let { error ->
                progressBar.visibility = View.GONE
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
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
        if (isLastPage == false) {
            progressBar.visibility = View.VISIBLE
            initRequest(page++)
        }
    }

    private fun initAdapter(view: RecyclerView, expenseList: List<Expense>) {
        val expenseListAdapter =
            ExpenseListAdapter(expenseList)
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