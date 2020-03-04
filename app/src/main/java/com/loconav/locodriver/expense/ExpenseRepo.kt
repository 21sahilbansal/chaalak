package com.loconav.locodriver.expenses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.loconav.locodriver.base.DataWrapper
import com.loconav.locodriver.db.room.AppDatabase
import com.loconav.locodriver.expense.Expense
import com.loconav.locodriver.network.HttpApiService
import com.loconav.locodriver.network.RetrofitCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import retrofit2.Call
import retrofit2.Response

class ExpenseRepo : KoinComponent {
    private val httpApiService: HttpApiService by inject()
    private val db: AppDatabase by inject()
    val expenseDao = db.expenseDao()

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