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


class DialogLanguageAdapter : RecyclerView.Adapter<DialogLanguageAdapter.DialogLanguageViewHolder>(), KoinComponent {

    val context : Context by inject()

    var defaultSelectedLanguage: Int = LanguageType.English.num


    val languages = context.resources.getStringArray(R.array.local_language_array);

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DialogLanguageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_language_dialog, parent, false)


        when (LocaleHelper.getLanguage(parent.context)) {
            languages[0] -> defaultSelectedLanguage = LanguageType.English.num
            languages[1] -> defaultSelectedLanguage = LanguageType.Hindi.num
        }
        return DialogLanguageViewHolder(view)
    }

    override fun onBindViewHolder(holder: DialogLanguageViewHolder, position: Int) {
        setData(holder, position)
    }

    private fun setData(holder: DialogLanguageViewHolder, position: Int) {
        if(position == 0){
            holder.languageTextView.text = LanguageType.English.name
        } else if(position == 1) {
            holder.languageTextView.text = LanguageType.Hindi.name
        }


        if (defaultSelectedLanguage == position)
            holder.tick.visibility = VISIBLE
        else
            holder.tick.visibility = GONE
        holder.itemview.setOnClickListener { v -> changeLanguage(holder, position) }
    }

    private fun changeLanguage(holder: DialogLanguageViewHolder, position: Int) {
        holder.tick.visibility = VISIBLE
        val selectedLanguage = holder.itemview.context.resources.getStringArray(R.array.local_language_array)[position]
        LocaleHelper.changeLanguage(holder.itemview.context, selectedLanguage)
        EventBus.getDefault().post(LanguageEventBus(LanguageEventBus.ON_LANGUAGE_CHANGED_FROM_PROFILE))
    }


    override fun getItemCount(): Int {
        return languages.size
    }


    class DialogLanguageViewHolder(var itemview: View) : RecyclerView.ViewHolder(itemview) {
        val tick = itemview.iv_tick
        val languageTextView = itemView.text_language
    }




}
