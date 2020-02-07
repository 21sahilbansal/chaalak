package com.loconav.locodriver.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import com.loconav.locodriver.SmsRetrieverEvent
import org.greenrobot.eventbus.EventBus
import java.util.regex.Pattern

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.action)) {
            val extras = intent.extras
            val status = extras!!.get(SmsRetriever.EXTRA_STATUS) as Status
            when (status.getStatusCode()) {
                CommonStatusCodes.SUCCESS -> {
                    val message = extras.get(SmsRetriever.EXTRA_SMS_MESSAGE) as String
                    val p = Pattern.compile("(\\d{4})")
                    val m = p.matcher(message) // get a matcher object
                    if (m.find())
                        EventBus.getDefault().post(SmsRetrieverEvent(SmsRetrieverEvent.READ_OTP , m.group(1)))

                }
                CommonStatusCodes.TIMEOUT -> {
//                    Do nothing here.
                }
            }
            // Extract one-time code from the message
            // Waiting for SMS timed out (5 minutes)
        }
    }
}