package com.loconav.locodriver.expense.addExpense

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.loconav.locodriver.Constants.FRAGMENT_TAG.Companion.IMAGE_CHOOSER_DIALOG
import com.loconav.locodriver.R
import com.loconav.locodriver.base.BaseFragment
import com.loconav.locodriver.db.sharedPF.SharedPreferenceUtil
import com.loconav.locodriver.expense.ExpenseDocumentAdapter
import com.loconav.locodriver.expense.ImageSelectionEvent
import com.loconav.locodriver.expense.ImageSourceChooserDialog
import kotlinx.android.synthetic.main.add_expense_fragment.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class AddExpenseFragment : BaseFragment(), KoinComponent {

    val sharedPreferenceUtil: SharedPreferenceUtil by inject()
    var addExpenseViewModel: AddExpenseViewModel? = null
    var imageUriList:ArrayList<String> = ArrayList()
    override fun onViewInflated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        val actionBar = (activity as AppCompatActivity).supportActionBar as ActionBar
        actionBar.let {
            it.title = getString(R.string.add_expense_toolbar_title)
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }
        addExpenseViewModel = ViewModelProviders.of(this).get(AddExpenseViewModel::class.java)
        progress_bar.visibility = View.VISIBLE
        addExpenseViewModel?.getExpenseType()?.observe(this, Observer {
            it.data?.let {
                progress_bar.visibility = View.GONE
            } ?: kotlin.run {
                progress_bar.visibility = View.GONE
                it.throwable?.let { error ->
                    Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
        setSpinnerAdapter()
        setClickListener()
    }

    private fun initImageAdapter(){
        val expenseListAdapter = ExpenseDocumentAdapter(imageUriList,true)
        val layoutManager = GridLayoutManager(rec_doc_image.context, 3, GridLayoutManager.VERTICAL,false)
        rec_doc_image.visibility=View.VISIBLE
        rec_doc_image.layoutManager = layoutManager
        rec_doc_image.adapter = expenseListAdapter
    }

    private fun setClickListener(){
        add_image_layout.setOnClickListener {
            ImageSourceChooserDialog().show(childFragmentManager, IMAGE_CHOOSER_DIALOG)
        }
    }

    private val expenseTypeSpinnerItemSelectListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(p0: AdapterView<*>?) {}

        override fun onItemSelected(p0: AdapterView<*>?, view: View, position: Int, p3: Long) {
            if (position == 0) {
                setStyleForSpinnerListItem(view)
            }
        }
    }
    private val expenseDateSpinnerItemSelectListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(p0: AdapterView<*>?) {}

        override fun onItemSelected(p0: AdapterView<*>?, view: View, position: Int, p3: Long) {
            if (position == 0) {
                setStyleForSpinnerListItem(view)
            }
        }
    }
    private val expenseMonthSpinnerItemSelectListener =
        object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(p0: AdapterView<*>?, view: View, position: Int, p3: Long) {
                if (position == 0) {
                    setStyleForSpinnerListItem(view)
                } else {
                    addExpenseViewModel?.updateDateList(position)
                }
            }
        }
    private val expenseYearSpinnerItemSelectListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(p0: AdapterView<*>?) {
        }

        override fun onItemSelected(p0: AdapterView<*>?, view: View, position: Int, p3: Long) {
            if (position == 0) {
                setStyleForSpinnerListItem(view)
            }
        }
    }

    private fun setSpinnerAdapter() {
        setExpenseTypeSpinnerAdapter()
        setDateSpinner()
    }

    private fun setExpenseTypeSpinnerAdapter() {
        addExpenseViewModel?.expenseTypeList?.observe(this, Observer {
            setAdapterForSpinner(spinner_expense_type, it)
        })
        addExpenseViewModel?.getExpenseTypeList()
        spinner_expense_type.onItemSelectedListener =
            expenseTypeSpinnerItemSelectListener
    }

    private fun setDateSpinner() {
        addExpenseViewModel?.getDateSpinnerList()
        setDaySpinner()
        setMonthSpinner()
        setYearSpinner()

    }

    private fun setDaySpinner() {
        addExpenseViewModel?.dateLiveList?.observe(this, Observer {
            setAdapterForSpinner(spinner_expense_date, it)
        })
        spinner_expense_date.onItemSelectedListener =
            expenseDateSpinnerItemSelectListener
    }

    private fun setMonthSpinner() {
        addExpenseViewModel?.monthLiveList?.observe(this, Observer {
            setAdapterForSpinner(spinner_expense_month, it)
        })
        spinner_expense_month.onItemSelectedListener =
            expenseMonthSpinnerItemSelectListener
    }

    private fun setYearSpinner() {
        addExpenseViewModel?.yearLiveList?.observe(this, Observer {
            setAdapterForSpinner(spinner_expense_year, it)
        })
        spinner_expense_year.onItemSelectedListener =
            expenseYearSpinnerItemSelectListener
    }

    private fun setAdapterForSpinner(view: Spinner, list: List<String>) {
        val listAdapter =
            ArrayAdapter<String>(context!!, android.R.layout.simple_spinner_item, list)
        listAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown)
        view.adapter = listAdapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            activity?.onBackPressed()
            return true
        }
        return false
    }

    private fun setStyleForSpinnerListItem(view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            (view as TextView).setTextAppearance(R.style.BodyLight)
        } else {
            (view as TextView).setTextAppearance(view.context, R.style.BodyLight)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.add_expense_fragment
    }

    companion object {
        fun getInstance(): AddExpenseFragment {
            return AddExpenseFragment()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onImageCaptureEventRecieved(imageSelectionEvent: ImageSelectionEvent){
        when(imageSelectionEvent.message){
            ImageSelectionEvent.IMAGE_SELECTED -> {
                val imageUri = imageSelectionEvent.`object` as Uri
                imageUriList.add(imageUri.toString())
                initImageAdapter()
            }
            ImageSelectionEvent.DISABLE_ADD_IMAGE -> {
                disableAddImage()
            }
            ImageSelectionEvent.ENABLE_ADD_IMAGE -> {
                enableAddImage()
            }
        }
    }
    private fun disableAddImage(){
        add_image_layout.isClickable = false

    }
    private fun enableAddImage(){
        add_image_layout.isClickable = true

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this)
        }
    }
}