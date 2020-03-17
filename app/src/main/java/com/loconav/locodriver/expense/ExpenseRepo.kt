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

    val expenseTypeDataWrapper = DataWrapper<ExpenseType>()
    val expenseTypeApiResponse = MutableLiveData<DataWrapper<ExpenseType>>()
    val expenseListDataWrapper = DataWrapper<List<Expense>>()
    val expenseListApiResponse = MutableLiveData<DataWrapper<List<Expense>>>()
    val expenseDataWrapper = DataWrapper<Expense>()
    val expenseApiResponse = MutableLiveData<DataWrapper<Expense>>()

    fun getExpenseType(): LiveData<DataWrapper<ExpenseType>>? {
        httpApiService.getExpenseType().enqueue(object : RetrofitCallback<ExpenseType>() {
            override fun handleSuccess(call: Call<ExpenseType>, response: Response<ExpenseType>) {
                response.body()?.let {
                    expenseTypeDataWrapper.data = it
                    sharedPreferenceUtil.saveJsonData(EXPENSE_TYPE, it)
                    expenseTypeApiResponse.postValue(expenseTypeDataWrapper)
                }
            }

            override fun handleFailure(call: Call<ExpenseType>, t: Throwable) {
                expenseTypeDataWrapper.throwable = t
                expenseTypeApiResponse.postValue(expenseTypeDataWrapper)
            }
        })
        return expenseTypeApiResponse
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
        httpApiService.getExpenseList(page)
            .enqueue(object : RetrofitCallback<List<Expense>>() {
                override fun handleSuccess(
                    call: Call<List<Expense>>,
                    response: Response<List<Expense>>
                ) {
                    response.body()?.let {
                        expenseListDataWrapper.data = response.body()
                        for (item in expenseListDataWrapper.data!!) {
                            item.isUpdated = true
                            GlobalScope.launch {
                                Dispatchers.Default
                                expenseDao.insertAll(item)
                            }
                        }
                    }
                    expenseListApiResponse.postValue(expenseListDataWrapper)
                }

                override fun handleFailure(call: Call<List<Expense>>, t: Throwable) {
                    expenseListDataWrapper.throwable = t
                    expenseListApiResponse.postValue(expenseListDataWrapper)
                }
            })
        return expenseListApiResponse
    }

    fun getExpense(expenseId: Long): LiveData<DataWrapper<Expense>> {
        httpApiService.getExpense(expenseId)
            .enqueue(object : RetrofitCallback<Expense>() {
                override fun handleSuccess(
                    call: Call<Expense>,
                    response: Response<Expense>
                ) {
                    response.body()?.let {
                        expenseDataWrapper.data = it
                        it.isUpdated = true
                        GlobalScope.launch {
                            Dispatchers.Default
                            expenseDao.updateExpense(it)
                        }
                    }
                }

                override fun handleFailure(call: Call<Expense>, t: Throwable) {
                    expenseDataWrapper.throwable = t
                    expenseApiResponse.postValue(expenseDataWrapper)
                }
            })
        return expenseApiResponse
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