package com.loconav.locodriver.language

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.loconav.locodriver.R
import com.loconav.locodriver.db.sharedPF.SharedPreferenceUtil
import com.loconav.locodriver.util.LocaleHelper
import kotlinx.android.synthetic.main.item_language_dialog.view.*
import org.greenrobot.eventbus.EventBus
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject


class DialogLanguageAdapter :
    RecyclerView.Adapter<DialogLanguageAdapter.DialogLanguageViewHolder>(), KoinComponent {

    val context: Context by inject()

    private val languageArray: Array<LanguageDataClass> by inject()

    private var defaultSelectedLanguage: Int? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DialogLanguageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_language_dialog, parent, false)
        defaultSelectedLanguage = LocaleHelper.getLanguage(parent.context)
        return DialogLanguageViewHolder(view)
    }

    override fun onBindViewHolder(holder: DialogLanguageViewHolder, position: Int) {
        setData(holder, position)
    }

    private fun setData(holder: DialogLanguageViewHolder, position: Int) {
        holder.languageTextView.text = languageArray[position].longProperty

        if (defaultSelectedLanguage == position)
            holder.tick.visibility = VISIBLE
        else
            holder.tick.visibility = GONE
        holder.itemview.setOnClickListener { v -> changeLanguage(holder, position) }
    }

    private fun changeLanguage(holder: DialogLanguageViewHolder, position: Int) {
        holder.tick.visibility = VISIBLE
        val selectedLanguage = languageArray[position].shortProperty
        LocaleHelper.changeLanguage(holder.itemview.context, selectedLanguage)
        EventBus.getDefault()
            .post(LanguageEventBus(LanguageEventBus.ON_LANGUAGE_CHANGED_FROM_PROFILE))
    }


    override fun getItemCount(): Int {
        return languageArray.size
    }


    class DialogLanguageViewHolder(var itemview: View) : RecyclerView.ViewHolder(itemview) {
        val tick = itemview.iv_tick
        val languageTextView = itemView.text_language

    }


}
