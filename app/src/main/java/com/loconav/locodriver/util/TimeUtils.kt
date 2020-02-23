package com.loconav.locodriver.util

import androidx.core.util.Preconditions.checkArgument
import java.text.SimpleDateFormat
import java.util.*

class TimeUtils {
    companion object {
        fun getThFormatTime(time: Long): String {
            var finalString: String
            val date = Date(time)
            val n = date.date
            val suffix: String

            checkArgument(n in 1..31, "illegal day o2f month: $n")
            suffix = if (n in 11..19) {
                "th"
            } else {
                when (n % 10) {
                    1 -> "st"
                    2 -> "nd"
                    3 -> "rd"
                    else -> "th"
                }
            }
            finalString = n.toString() + suffix
            val format = SimpleDateFormat(" MMM yyyy")

            finalString += format.format(Date(time))
            return finalString
        }

        fun getDateTimeFromEpoch(epoch: Long): String {
            val expiry = Date(epoch)
            val format = SimpleDateFormat("dd MMMM yyyy HH:mm:ss")
            return format.format(expiry)
        }

        fun getDateFromEpoch(epoch: Long):String {
            val expiry = Date(epoch)
            val format = SimpleDateFormat("dd/MM/yyyy")
            return format.format(expiry)
        }
    }


}