package com.loconav.locodriver.user.attendence

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.loconav.locodriver.Constants.RegexConstants.Companion.DATE_FORMAT_WITH_FULL_MONTH
import com.loconav.locodriver.R
import kotlinx.android.synthetic.main.item_attendance.view.*

class AttendanceListAdapter(val list: List<Attendance>) :
    androidx.recyclerview.widget.RecyclerView.Adapter<AttendanceListAdapter.AttendanceListAdapterViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AttendanceListAdapterViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_attendance, parent, false)
        return AttendanceListAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: AttendanceListAdapterViewHolder, position: Int) {
        holder.setData(list[position])
    }

    class AttendanceListAdapterViewHolder(itemView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        private val PRESENT = "Present"
        private val ABSENT = "Absent"
        private val OFF = "On Leave"
        fun setData(attendance: Attendance) {
            setAttendanceDate(attendance.attendanceDate)
            setAttendanceStatus(attendance.attendanceStatus)
            setAttendanceReason(attendance.attendanceReason)
        }

        private fun setAttendanceStatusBackground(drawable: Int) {
            itemView.attendance_status_tv.background =
                ContextCompat.getDrawable(itemView.context, drawable)
        }

        private fun setAttendanceReason(attendanceReason: String?) {
            attendanceReason?.let {
                itemView.attendance_reason.visibility = View.VISIBLE
                itemView.attendance_reason.text = it
            } ?: kotlin.run {
                itemView.attendance_reason.visibility = View.GONE
            }
        }

        private fun setAttendanceDate(attendanceDate: Long?) {
            attendanceDate?.let {
                itemView.attendance_date_tv.text =
                    com.loconav.locodriver.util.TimeUtils.getDateTimeFromEpoch(
                        it,
                        DATE_FORMAT_WITH_FULL_MONTH
                    )
            } ?: kotlin.run {
                itemView.attendance_date_tv.text = itemView.context.getString(R.string.unknown_Date)
            }
        }

        private fun setAttendanceStatus(attendanceStatus: String?) {
            attendanceStatus?.let {
                itemView.attendance_status_tv.text = it
                when (it) {
                    PRESENT -> {
                        setAttendanceStatusBackground(R.drawable.bg_active_trip_tag)
                    }
                    ABSENT -> {
                        setAttendanceStatusBackground(R.drawable.bg_pale_red_round_4dp)
                    }
                    OFF -> {
                        setAttendanceStatusBackground(R.drawable.bg_pale_yellow_round_4dp)
                    }
                }
            } ?: run {
                itemView.attendance_status_tv.text =
                    itemView.context.getString(R.string.attendance_not_updated)
            }
        }
    }
    // Todo : Make a BaseRecylerViewHolder

}