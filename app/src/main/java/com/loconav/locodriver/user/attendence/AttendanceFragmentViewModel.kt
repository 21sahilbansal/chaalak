package com.loconav.locodriver.user.attendence

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.loconav.locodriver.Constants.SharedPreferences.Companion.ATTENDANCE_LIST
import com.loconav.locodriver.base.DataWrapper
import com.loconav.locodriver.db.sharedPF.SharedPreferenceUtil
import com.loconav.locodriver.user.UserHttpService
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class AttendanceFragmentViewModel : ViewModel(), KoinComponent {
    private val userHttpService: UserHttpService by inject()
    private val gson:Gson by inject()
    val sharedPreferenceUtil: SharedPreferenceUtil by inject()
    var attendanceList = MutableLiveData<List<Attendance>>()

    fun getUserAttendance(): LiveData<DataWrapper<AttendanceResponse>>? {
        return userHttpService.getAttendance()
    }

    fun getStoredAttendance() {
        val expenseList = ArrayList<Attendance>()
        val json = sharedPreferenceUtil.getData(ATTENDANCE_LIST, "")
        val attendance = gson.fromJson(json, AttendanceResponse::class.java)
        attendance?.let {
            if (!it.attendanceList.isNullOrEmpty()) {
                for (item in it.attendanceList!!) {
                    expenseList.add(item)
                }
            }
            attendanceList.postValue(expenseList)
        }
    }
}