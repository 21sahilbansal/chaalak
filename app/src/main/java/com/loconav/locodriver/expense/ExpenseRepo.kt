package com.loconav.locodriver.expenses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.loconav.locodriver.Constants.ExpenseConstants.Companion.EXPENSE_TYPE
import com.loconav.locodriver.R
import com.loconav.locodriver.application.LocoDriverApplication
import com.loconav.locodriver.base.DataWrapper
import com.loconav.locodriver.db.room.AppDatabase
import com.loconav.locodriver.db.sharedPF.SharedPreferenceUtil
import com.loconav.locodriver.expense.model.AddExpenseRequestBody
import com.loconav.locodriver.expense.model.Document
import com.loconav.locodriver.expense.model.Expense
import com.loconav.locodriver.expense.model.ExpenseType
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
import java.util.*

object ExpenseRepo : KoinComponent {
    private val httpApiService: HttpApiService by inject()
    private val db: AppDatabase by inject()
    private val gson: Gson by inject()
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
                    sharedPreferenceUtil.put(it, EXPENSE_TYPE)
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
        //TODO : div by 1000 to send this in secs to server (need to change once consistent unit is there)
        val expenseDate =
            setUpMultipartRequest(addExpenseRequestBody.expenseDate!!.div(1000).toString())
        val expenseDocument = Document(expenseDocList = addExpenseRequestBody.imageList)
        val json = sharedPreferenceUtil.getData(EXPENSE_TYPE, "")
        val list = gson.fromJson(json, ExpenseType::class.java)
        var fakeExpenseType: String? = null
        list?.let {
            if (!it.expenseType.isNullOrEmpty()) {
                for (item in it.expenseType) {
                    if (item.key == addExpenseRequestBody.expenseType!!) {
                        fakeExpenseType = item.value
                    }
                }
            }
        }
        val fakeId = UUID.randomUUID().toString()
        val expenseFake = Expense(
            expenseType = fakeExpenseType,
            amount = addExpenseRequestBody.amount,
            expenseDate = addExpenseRequestBody.expenseDate!!.div(1000),
            verificationStatus = LocoDriverApplication.instance.applicationContext.getString(R.string.verification_pending_expense_status),
            documents = expenseDocument,
            fake_id = fakeId
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
                    insertExpense(it)
                    fakeExpense.fake_id?.let {
                        expenseDao.delete(it)
                    }
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
        return expenseDao.findUnsyncedExpenseList()
    }

    fun getExpenseListFromDb(): LiveData<List<Expense>> {
        return expenseDao.getAllExpense()
    }

    fun getExpenseFromDb(expenseAutoId: String): LiveData<Expense> {
        return expenseDao.findByExpenseId(expenseAutoId)
    }

    fun getExpenseFromFakeId(fakeId : String):LiveData<Expense>{
        return expenseDao.findByFakeExpenseId(fakeId)

    }

    fun getExpenseId(fakeID: String): Long? {
        var expenseId: Long? = null
        GlobalScope.launch {
            Dispatchers.Default
            expenseId = expenseDao.getExpenseIDFromAutoId(fakeID)
        }
        return expenseId
    }

    fun updateExpenseFromNotification(expense: Expense?) {
        expense?.let {
            expenseDao.insertAll(it)
        }
    }

}