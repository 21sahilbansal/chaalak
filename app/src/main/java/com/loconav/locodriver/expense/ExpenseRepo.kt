package com.loconav.locodriver.expenses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.loconav.locodriver.Constants.ExpenseConstants.Companion.EXPENSE_TYPE
import com.loconav.locodriver.R
import com.loconav.locodriver.application.LocoDriverApplication
import com.loconav.locodriver.base.DataWrapper
import com.loconav.locodriver.db.room.AppDatabase
import com.loconav.locodriver.db.sharedPF.SharedPreferenceUtil
import com.loconav.locodriver.expense.model.*
import com.loconav.locodriver.network.HttpApiService
import com.loconav.locodriver.network.NetworkUtil
import com.loconav.locodriver.network.RetrofitCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import retrofit2.Call
import retrofit2.Response

object ExpenseRepo : KoinComponent {
    private val httpApiService: HttpApiService by inject()
    private val db: AppDatabase by inject()
    private val sharedPreferenceUtil: SharedPreferenceUtil by inject()
    val expenseDao = db.expenseDao()


    fun getExpenseType(): LiveData<DataWrapper<ExpenseType>>? {
        val dataWrapper = DataWrapper<ExpenseType>()
        val apiResponse = MutableLiveData<DataWrapper<ExpenseType>>()
        httpApiService.getExpenseType().enqueue(object : RetrofitCallback<ExpenseType>() {
            override fun handleSuccess(call: Call<ExpenseType>, response: Response<ExpenseType>) {
                response.body()?.let {
                    dataWrapper.data = it
                    sharedPreferenceUtil.saveJsonData(EXPENSE_TYPE, it)
                    apiResponse.postValue(dataWrapper)
                }
            }

            override fun handleFailure(call: Call<ExpenseType>, t: Throwable) {
                dataWrapper.throwable = t
                apiResponse.postValue(dataWrapper)
            }
        })
        return apiResponse
    }

    fun uploadExpense(addExpenseRequestBody: AddExpenseRequestBody) {
        if (addExpenseRequestBody.expenseType == null && addExpenseRequestBody.amount == null && addExpenseRequestBody.expenseDate == null) return
        val expenseType = setUpMultipartRequest(addExpenseRequestBody.expenseType!!)
        val expenseAmount = setUpMultipartRequest(addExpenseRequestBody.amount!!.toString())
        val expenseDate = setUpMultipartRequest(addExpenseRequestBody.expenseDate!!.toString())
        val expenseDocument = Document(expenseDocList = addExpenseRequestBody.imageList)
        val expenseFake = Expense(
            expenseType = addExpenseRequestBody.expenseType,
            amount = addExpenseRequestBody.amount,
            expenseDate = addExpenseRequestBody.expenseDate,
            verificationStatus = LocoDriverApplication.instance.applicationContext.getString(R.string.verification_pending_expense_status),
            documents = expenseDocument
        )
        GlobalScope.launch {
            Dispatchers.Default
            expenseDao.insertAll(expenseFake)
        }
        uploadToServer(
            expenseType,
            expenseAmount,
            expenseDate,
            addExpenseRequestBody.multipartList,
            expenseFake
        )
    }

    fun setUpMultipartRequest(expenseRequestParam: String): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), expenseRequestParam)
    }

    fun uploadToServer(
        expenseType: RequestBody,
        expenseAmount: RequestBody,
        expenseDate: RequestBody,
        list: List<MultipartBody.Part>?,
        fakeExpense: Expense
    ) {
        if (NetworkUtil.isUserOnline) {
            GlobalScope.launch {
                Dispatchers.Default
                val expenseObject = httpApiService.uploadExpense(
                    expenseType, expenseAmount, expenseDate,
                    list
                ).execute()
                expenseObject.body()?.expense?.let {
                    expenseDao.deleteSingleExpense(fakeExpense.autoId!!)
                    insertExpense(it)
                }
            }

        }
    }

    private fun insertExpense(expense: Expense) {
        GlobalScope.launch {
            Dispatchers.Default
            expenseDao.insertAll(expense)
        }
    }

    fun getExpenseList(page: Int): LiveData<DataWrapper<List<Expense>>>? {
        val dataWrapper = DataWrapper<List<Expense>>()
        val apiResponse = MutableLiveData<DataWrapper<List<Expense>>>()
        httpApiService.getExpenseList(page)
            .enqueue(object : RetrofitCallback<List<Expense>>() {
                override fun handleSuccess(
                    call: Call<List<Expense>>,
                    response: Response<List<Expense>>
                ) {
                    response.body()?.let {
                        dataWrapper.data = response.body()
                        for (item in dataWrapper.data!!) {
                            item.isUpdated = true
                            GlobalScope.launch {
                                Dispatchers.Default
                                expenseDao.insertAll(item)
                            }
                        }
                    }
                    apiResponse.postValue(dataWrapper)
                }

                override fun handleFailure(call: Call<List<Expense>>, t: Throwable) {
                    dataWrapper.throwable = t
                    apiResponse.postValue(dataWrapper)
                }
            })
        return apiResponse
    }

    fun getExpense(expenseId: Long): LiveData<DataWrapper<Expense>> {
        val dataWrapper = DataWrapper<Expense>()
        val apiResponse = MutableLiveData<DataWrapper<Expense>>()
        httpApiService.getExpense(expenseId)
            .enqueue(object : RetrofitCallback<Expense>() {
                override fun handleSuccess(
                    call: Call<Expense>,
                    response: Response<Expense>
                ) {
                    response.body()?.let {
                        dataWrapper.data = it
                        it.isUpdated = true
                        GlobalScope.launch {
                            Dispatchers.Default
                            expenseDao.updateExpense(it)
                        }
                    }
                }

                override fun handleFailure(call: Call<Expense>, t: Throwable) {
                    dataWrapper.throwable = t
                    apiResponse.postValue(dataWrapper)
                }
            })
        return apiResponse
    }

    fun getunSyncedExpenseListFromDb(): List<Expense> {
        var list = listOf<Expense>()
        GlobalScope.launch {
            Dispatchers.Default
            list = expenseDao.findUnsyncedExpenseList()
        }
        return list
    }

    fun getExpenseListFromDb(): LiveData<List<Expense>> {
        return expenseDao.getAllExpense()
    }

    fun getExpenseFromDb(expenseAutoId: Long): LiveData<Expense> {
        return expenseDao.findByExpenseId(expenseAutoId)
    }

    fun getExpenseId(autoId: Long): Long? {
        var expenseId: Long? = null
        GlobalScope.launch {
            Dispatchers.Default
            expenseId = expenseDao.getExpenseIDFromAutoId(autoId)
        }
        return expenseId
    }

    fun updateExpenseFromNotification(expense: Expense) {
        val expenseList = expenseDao.getExpenses()
        if (!expenseList.isNullOrEmpty() && expenseList.contains(expense)) {
            expenseDao.updateExpense(expense)
        } else {
            expenseDao.insertAll(expense)
        }
    }
}