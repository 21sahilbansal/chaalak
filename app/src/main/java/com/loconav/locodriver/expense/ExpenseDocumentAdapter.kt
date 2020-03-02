package com.loconav.locodriver.expense

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.loconav.locodriver.R
import kotlinx.android.synthetic.main.item_expense_document.view.*

class ExpenseDocumentAdapter(private val expense: Expense) :
    androidx.recyclerview.widget.RecyclerView.Adapter<ExpenseDocumentAdapter.ExpenseDocumentViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ):ExpenseDocumentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expense_document, parent, false)
        return ExpenseDocumentViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(
        holder:ExpenseDocumentViewHolder,
        position: Int
    ) {
        holder.setImage()
    }

    class ExpenseDocumentViewHolder(itemView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun setImage(){
            itemView.document_image.setImageResource(R.drawable.ic_user_placeholder)
        }
    }
}