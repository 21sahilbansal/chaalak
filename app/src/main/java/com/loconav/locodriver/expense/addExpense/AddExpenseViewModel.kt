package com.loconav.locodriver.expense.addExpense

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.loconav.locodriver.Constants.TripConstants.Companion.monthMap
import com.loconav.locodriver.base.DataWrapper
import com.loconav.locodriver.db.sharedPF.SharedPreferenceUtil
import com.loconav.locodriver.expense.model.ExpenseType
import com.loconav.locodriver.expenses.ExpenseRepo
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.util.*
import kotlin.collections.ArrayList
import java.text.SimpleDateFormat
import com.loconav.locodriver.expense.model.AddExpenseRequestBody
import com.loconav.locodriver.expense.model.UploadExpenseResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import android.net.Uri
import android.os.Build
import com.loconav.locodriver.application.LocoDriverApplication
import com.loconav.locodriver.util.FileUtil
import okhttp3.MediaType
import java.io.File


class AddExpenseViewModel : ViewModel(), KoinComponent {
    val sharedPreferenceUtil: SharedPreferenceUtil by inject()
    private val expenseRepo = ExpenseRepo()
    val date = Calendar.getInstance()
    private val currentDate = date.get(Calendar.DATE)
    private val currentMonth = date.get(Calendar.MONTH)
    private val currentYear = date.get(Calendar.YEAR)
    var expenseTypeList: MutableLiveData<List<String>> = MutableLiveData()
    var dateLiveList: MutableLiveData<List<String>> = MutableLiveData()
    var monthLiveList: MutableLiveData<List<String>> = MutableLiveData()
    var yearLiveList: MutableLiveData<List<String>> = MutableLiveData()

    fun getExpenseType(): LiveData<DataWrapper<ExpenseType>>? {
        return expenseRepo.getExpenseType()
    }

    fun getExpenseTypeList() {
        val gson = Gson()
        val expenseList = ArrayList<String>()
        expenseList.add("Please Select")
        val json = sharedPreferenceUtil.getData("expense_type", "")
        val list = gson.fromJson(json, ExpenseType::class.java)
        list?.let {
            if (!it.expenseType.isNullOrEmpty()) {
                for (item in it.expenseType) {
                    expenseList.add(item.value)
                }
            }
            expenseTypeList.postValue(expenseList)
        }

    }

    fun getTypeOfExpense(typeTemplate: String): String {
        val gson = Gson()
        val json = sharedPreferenceUtil.getData("expense_type", "")
        val list = gson.fromJson(json, ExpenseType::class.java)
        list.expenseType?.let {
            for (entry in it.entries) {
                if (entry.value == typeTemplate) {
                    return entry.key
                }
            }
        }
        return ""
    }

    fun getDateSpinnerList() {
        val maxDate = 31
        val maxMonthindex = 12
        val maxYearIndex = currentYear - 2020

        generateDateList(maxDate)
        generateMonthList()
        generateYearList(maxYearIndex)
    }

    private fun generateYearList(maxYearIndex: Int) {
        val yearList = ArrayList<String>()
        yearList.add("Year")
        if (maxYearIndex == 0) {
            yearList.add(currentYear.toString())
        } else {
            for (i in maxYearIndex downTo 1) {
                yearList.add((currentYear - maxYearIndex).toString())
            }
        }
        yearLiveList.postValue(yearList)
    }

    private fun generateMonthList() {
        val monthList = ArrayList<String>()
        monthList.add("Month")
        for (i in 0..currentMonth) {
            monthList.add(monthMap[i].monthName)
        }
        monthLiveList.postValue(monthList)

    }

    private fun generateDateList(maxDate: Int) {
        val dateList = ArrayList<String>()
        dateList.add("Date")
        for (i in 0 until maxDate) {
            dateList.add((i + 1).toString())
        }
        dateLiveList.postValue(dateList)
    }

    fun updateDateList(position: Int) {
        var maxDate = 31
        if (position == 2) {
            maxDate = 28
        } else if (position == currentMonth + 1) {
            maxDate = currentDate
        }
        generateDateList(maxDate)
    }

    fun isAmountValid(amount: Editable?): Boolean {
        return if (amount.isNullOrEmpty()) {
            false
        } else amount.trim().toString().toInt() in 100000 downTo -1
    }

    fun getEpochFromExpenseDate(date: String, month: String, year: String): Long {
        val strDate = String.format("%s/%s/%s", date, month, year)
        return SimpleDateFormat("dd/MMMM/yyyy").parse(strDate).time
    }

    fun getMultipartFromUri(listImageUri: List<String>?): List<MultipartBody.Part> {
        val parts = ArrayList<MultipartBody.Part>()
        if (!listImageUri.isNullOrEmpty()) {
            for (item in listImageUri) {
                val uri = Uri.parse(item)
                val imageFile =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        FileUtil.getFile(LocoDriverApplication.instance.applicationContext, uri) ?: break
                    } else {
                        File(item)
                    }
                val extension = imageFile.absolutePath.substring(imageFile.absolutePath.lastIndexOf(".") + 1)

                val mediaType = MediaType.parse("image/${extension}")
                val requestImageFile = RequestBody.create(mediaType,imageFile)
                parts.add(
                    MultipartBody.Part.createFormData(
                        "uploads_attributes[images][]",
                        imageFile.name,
                        requestImageFile
                    )
                )
            }
        }
        return parts
    }

    fun uploadExpence(addExpenseRequestBody: AddExpenseRequestBody): LiveData<DataWrapper<UploadExpenseResponse>>? {
        return expenseRepo.uploadExpense(addExpenseRequestBody)
    }
}

data class MonthProperty(
    val monthIndex: Int,
    val monthName: String
)
