package com.loconav.locodriver.expense.expenseList

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.loconav.locodriver.Constants.ExpenseConstants.Companion.REJECTED
import com.loconav.locodriver.Constants.ExpenseConstants.Companion.SOURCE_EXPENSE
import com.loconav.locodriver.Constants.ExpenseConstants.Companion.VERIFICATION_PENDING
import com.loconav.locodriver.Constants.ExpenseConstants.Companion.VERIFIED
import com.loconav.locodriver.Constants.RegexConstants.Companion.TIME_FORMAT_12_HOUR
import com.loconav.locodriver.R
import com.loconav.locodriver.Trips.tripDetail.DetailActivity
import com.loconav.locodriver.expense.model.Expense
import com.loconav.locodriver.util.TimeUtils
import kotlinx.android.synthetic.main.item_expense_list_card.view.*


class ExpenseListAdapter(private val expenseData: List<Expense>) :
    androidx.recyclerview.widget.RecyclerView.Adapter<ExpenseListAdapter.ExpenseListAdapterViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExpenseListAdapterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expense_list_card, parent, false)
        return ExpenseListAdapterViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return expenseData.size
    }

    override fun onBindViewHolder(holder: ExpenseListAdapterViewHolder, position: Int) {
        holder.setData(expenseData[position])
        holder.setClickListener(expenseData[position])
    }

    class ExpenseListAdapterViewHolder(itemView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun setClickListener(expense: Expense){
            val holderOnClickListener = View.OnClickListener {
                val intent = Intent(
                    itemView.context,
                    DetailActivity::class.java
                )
                val bundle= Bundle()
                bundle.putString("source",SOURCE_EXPENSE)
                bundle.putString("expense_title",expense.expenseType)
                intent.putExtras(bundle)
                intent.data = Uri.parse(expense.expenseId.toString())
                itemView.context.startActivity(intent)
            }

            itemView.setOnClickListener(holderOnClickListener)
        }
        fun setData(expense: Expense) {
            expense.expenseType?.let {
                itemView.trip_expense_type_text.text = it
            } ?: run {
                itemView.trip_expense_type_text.text =
                    itemView.context.getString(R.string.unknown_expense_type)
            }

            expense.tripUniqueId?.let {
                itemView.expense_trip_id.text = it
            }?:run{
                itemView.expense_trip_id.text = ""
            }

            expense.verificationStatus?.let {
                itemView.trip_expense_status_tv.text = it
                setStatusColor(itemView, it)
            } ?: run {
                itemView.trip_expense_status_tv.text =
                    itemView.context.getString(R.string.verification_pending_expense_status)
                itemView.trip_expense_status_tv.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.color_pending_brown
                    )
                )
            }

            expense.expenseDate?.let {
                itemView.trip_expense_date.text = String.format(
                    "%s,%s",
                    TimeUtils.getThFormatTime(it),
                    TimeUtils.getDateTimeFromEpoch(it, TIME_FORMAT_12_HOUR)
                )
            } ?: run {
                itemView.trip_expense_date.text =
                    itemView.context.getString(R.string.unknown_time_text)
            }

            expense.amount?.let {
                itemView.trip_expense_amount_tv.text =
                    String.format(itemView.context.getString(R.string.rupee), it)
            } ?: run {
                itemView.trip_expense_amount_tv.text =
                    itemView.context.getString(R.string.no_amount_present)
            }
        }

        private fun setStatusColor(view: View, status: String) {
            when (status) {
                VERIFICATION_PENDING -> {
                    view.trip_expense_status_tv.setTextColor(
                        ContextCompat.getColor(
                            view.context,
                            R.color.color_pending_brown
                        )
                    )
                }
                VERIFIED -> {
                    view.trip_expense_status_tv.setTextColor(
                        ContextCompat.getColor(
                            view.context,
                            R.color.color_approved_green
                        )
                    )
                }
                REJECTED -> {
                    view.trip_expense_status_tv.setTextColor(
                        ContextCompat.getColor(
                            view.context,
                            R.color.color_rejected_red
                        )
                    )
                }
            }
        }
    }
}