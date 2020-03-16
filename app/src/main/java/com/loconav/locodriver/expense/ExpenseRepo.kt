package com.loconav.locodriver.expenses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.loconav.locodriver.base.DataWrapper
import com.loconav.locodriver.db.room.AppDatabase
import com.loconav.locodriver.db.sharedPF.SharedPreferenceUtil
import com.loconav.locodriver.expense.model.*
import com.loconav.locodriver.network.HttpApiService
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

class ExpenseRepo : KoinComponent {
    private val httpApiService: HttpApiService by inject()
    private val db: AppDatabase by inject()
    private val sharedPreferenceUtil: SharedPreferenceUtil by inject()
    val expenseDao = db.expenseDao()


    fun getExpenseType(): MutableLiveData<DataWrapper<ExpenseType>>? {
        val dataWrapper = DataWrapper<ExpenseType>()
        val apiResponse = MutableLiveData<DataWrapper<ExpenseType>>()
        httpApiService.getExpenseType().enqueue(object : RetrofitCallback<ExpenseType>() {
            override fun handleSuccess(call: Call<ExpenseType>, response: Response<ExpenseType>) {
                response.body()?.let {
                    dataWrapper.data = it
                    sharedPreferenceUtil.saveData("expense_type", it)
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

    fun uploadExpense(addExpenseRequestBody: AddExpenseRequestBody): MutableLiveData<DataWrapper<UploadExpenseResponse>>? {
        val dataWrapper = DataWrapper<UploadExpenseResponse>()
        val apiResponse = MutableLiveData<DataWrapper<UploadExpenseResponse>>()
        if (addExpenseRequestBody.expenseType == null && addExpenseRequestBody.amount == null && addExpenseRequestBody.expenseDate == null) return null
        val expenseType =
            RequestBody.create(MediaType.parse("text/plain"), addExpenseRequestBody.expenseType!!)
        val expenseAmount = RequestBody.create(
            MediaType.parse("text/plain"),
            addExpenseRequestBody.amount!!.toString()
        )
        val expenseDate = RequestBody.create(
            MediaType.parse("text/plain"),
            addExpenseRequestBody.expenseDate!!.toString()
        )
        /**
         * Uncomment when there is UUID for fake expense to Update
         */
//        val expenseDocument=Document(expenseDocList = addExpenseRequestBody.imageList)
//        val expenseFake=Expense(expenseType = addExpenseRequestBody.expenseType,amount = addExpenseRequestBody.amount,expenseDate = addExpenseRequestBody.expenseDate,documents = expenseDocument)
//        GlobalScope.launch {
//            Dispatchers.Default
//            expenseDao.insertAll(expenseFake)
//        }

        httpApiService.uploadExpense(
            expenseType, expenseAmount, expenseDate,
            addExpenseRequestBody.multipartList
        )
            .enqueue(object : RetrofitCallback<UploadExpenseResponse>() {
                override fun handleSuccess(
                    call: Call<UploadExpenseResponse>,
                    response: Response<UploadExpenseResponse>
                ) {
                    response.body()?.let {
                        dataWrapper.data = it
                        it.expense?.let {
                            GlobalScope.launch {
                                Dispatchers.Default
//                                expenseDao.updateExpense(it) // Uncomment when there is UUID on fake expense to sync with server
                                expenseDao.insertAll(it)
                            }
                        }
                    }
                    apiResponse.postValue(dataWrapper)
                }

                override fun handleFailure(call: Call<UploadExpenseResponse>, t: Throwable) {
                    dataWrapper.throwable = t
                    apiResponse.postValue(dataWrapper)
                }
            })
        return apiResponse
    }

    fun getExpenseList(page: Int): MutableLiveData<DataWrapper<List<Expense>>>? {
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
                            GlobalScope.launch {
                                Dispatchers.Default
                                expenseDao.insertAll(item)
                            }
                        }
                    }
                }

                override fun handleFailure(call: Call<List<Expense>>, t: Throwable) {
                    dataWrapper.throwable = t
//                    apiResponse.postValue(dataWrapper)
                }
            })
        return apiResponse
    }

    fun getExpense(expenseId: Long): MutableLiveData<DataWrapper<Expense>> {
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
                        GlobalScope.launch {
                            Dispatchers.Default
                            expenseDao.updateExpense(dataWrapper.data!!)
                        }
                    }

//                    apiResponse.postValue(dataWrapper)
                }

                override fun handleFailure(call: Call<Expense>, t: Throwable) {
                    dataWrapper.throwable = t
                    apiResponse.postValue(dataWrapper)
                }
            })
        return apiResponse
    }


    fun getExpenseListFromDb(): LiveData<List<Expense>> {
        return expenseDao.getAllExpense()
    }

    fun getExpenseFromDb(expenseId: Long): LiveData<Expense> {
        return expenseDao.findByExpenseId(expenseId)
    }
}