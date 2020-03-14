package com.loconav.locodriver.user.attendence

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.loconav.locodriver.R
import com.loconav.locodriver.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_attendance.*

class AttendanceFragment:BaseFragment() {
    private var attendanceFragmentViewModel:AttendanceFragmentViewModel?=null
    override fun onViewInflated(view: View, savedInstanceState: Bundle?) {
        attendanceFragmentViewModel=ViewModelProviders.of(this).get(AttendanceFragmentViewModel::class.java)
        setHasOptionsMenu(true)
        val actionBar = (activity as AppCompatActivity).supportActionBar as ActionBar
        actionBar.let {
            it.title = getString(R.string.attendance_title)
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }
        initRequest()
        setAttananceAdapter()
    }

    private fun initRequest(){
        progressBar.visibility = View.VISIBLE
        attendanceFragmentViewModel?.getUserAttendance()?.observe(this, Observer {
            it.data?.let {
                attendanceFragmentViewModel?.getStoredAttandance()
                progressBar.visibility = View.GONE
            } ?: kotlin.run {
                progressBar.visibility = View.GONE
                it.throwable?.let { error ->
                    Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun setAttananceAdapter(){
        attendanceFragmentViewModel?.attendanceList?.observe(this, Observer {
            initAttendanceAdapter(it)
        })
        attendanceFragmentViewModel?.getStoredAttandance()
    }

    private fun initAttendanceAdapter(attendanceList:List<Attendance>){
        val attendanceListAdapter = AttendanceListAdapter(attendanceList)
        val layoutManager = LinearLayoutManager(rec_attendance.context, LinearLayoutManager.VERTICAL, false)
        rec_attendance.layoutManager = layoutManager
        rec_attendance.adapter = attendanceListAdapter
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_attendance
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId ) {
            android.R.id.home -> {
                activity?.onBackPressed()
                return true
            }

            R.id.refresh -> {
                initRequest()
                return true
            }
        }
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val menuInflater: MenuInflater = inflater
        menuInflater.inflate(R.menu.menu_attendance, menu)
    }


    companion object{
        fun getinstance():AttendanceFragment{
            return AttendanceFragment()
        }
    }
}