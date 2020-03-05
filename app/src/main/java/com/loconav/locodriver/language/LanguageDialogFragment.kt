package com.loconav.locodriver.language

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.loconav.locodriver.R
import com.loconav.locodriver.base.BaseDialogFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class LanguageDialogFragment : BaseDialogFragment() {

    override val layoutId: Int = R.layout.dialog_change_language

    override fun getScreenName(): String? {
        return this::class.java.simpleName
    }
    lateinit var recyclerView: RecyclerView
    private lateinit var dialogLanguageAdapter: DialogLanguageAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_change_language, container, false)
        recyclerView = view.findViewById(R.id.rv_language)
        recyclerView.layoutManager = LinearLayoutManager(this.activity)
        dialogLanguageAdapter = DialogLanguageAdapter()
        recyclerView.adapter = dialogLanguageAdapter
        val tvCancel = view.findViewById<TextView>(R.id.tv_cancel)
        tvCancel.setOnClickListener {
            dismiss()
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLanguageChangedFromDialog(event: LanguageEventBus) {
        when (event.message) {
            LanguageEventBus.ON_LANGUAGE_CHANGED_FROM_PROFILE -> dismiss()
        }
    }



}