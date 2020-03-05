package com.loconav.locodriver.expense

import android.net.Uri
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.loconav.locodriver.R
import com.loconav.locodriver.expense.ImageSelectionEvent.Companion.DISABLE_ADD_IMAGE
import com.loconav.locodriver.expense.ImageSelectionEvent.Companion.ENABLE_ADD_IMAGE
import com.loconav.locodriver.expense.model.Expense
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_expense_document.view.*
import okhttp3.internal.notify
import okhttp3.internal.notifyAll
import org.greenrobot.eventbus.EventBus
import org.koin.dsl.module.applicationContext
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class ExpenseDocumentAdapter(val list: ArrayList<String>, private val editable: Boolean) :
    RecyclerView.Adapter<ExpenseDocumentAdapter.ExpenseDocumentViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExpenseDocumentViewHolder {

        if (itemCount > 4) {
            EventBus.getDefault().post(ImageSelectionEvent(DISABLE_ADD_IMAGE))
        }
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expense_document, parent, false)
        return ExpenseDocumentViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(
        holder: ExpenseDocumentViewHolder,
        position: Int
    ) {
        holder.setImage(list[position], editable)
        holder.itemView.close_image_ll.setOnClickListener {
            list.removeAt(position)
            notifyDataSetChanged()
            if (itemCount < 5) {
                EventBus.getDefault().post(ImageSelectionEvent(ENABLE_ADD_IMAGE))
            }
        }
    }

    class ExpenseDocumentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        KoinComponent {
        private val picasso: Picasso by inject()
        fun setImage(imageUri: String, editable: Boolean) {
            if (editable) {
                itemView.close_image_ll.visibility = View.VISIBLE
            }
            picasso.load(Uri.parse(imageUri)).error(R.drawable.ic_user_placeholder).fit()
                .into(itemView.document_image)
        }
    }
}