package com.loconav.locodriver.notification.notificationManager

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.loconav.locodriver.Constants
import com.loconav.locodriver.R
import com.loconav.locodriver.Trips.TripsRepo
import com.loconav.locodriver.application.LocoDriverApplication
import com.loconav.locodriver.expenses.ExpenseRepo
import com.loconav.locodriver.landing.LandingActivity
import com.loconav.locodriver.notification.model.LocoDrivePushNotification
import com.loconav.locodriver.notification.model.Meta
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.util.*


object LocoNotificationManager : KoinComponent {
    var locoDriverApplicationContext: Context = LocoDriverApplication.instance
    const val CHANNEL_ID = "locodrive_notification_channel_id"
    const val CHANNEL_NAME = "locodrive_notification_channel"
    const val CHANNEL_DESCRIPTION = "This channel is used to send locodrive notifications"
    val GROUPING_NOTIFICATION = "Notification are grouped"
    val gson: Gson by inject()
    const val BUNDLE_MESSAGE_ID = 100


    /**
     *
     * @return : notification manager, if channel will not be there, it will create a channel as well.
     */
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is new and not in the support library
    // Register the channel with the system; you can't change the importance
    // or other notification behaviors after this
    val notificationManager: NotificationManagerCompat
        get() {
            val mNotificationManager: NotificationManagerCompat =
                NotificationManagerCompat.from(LocoDriverApplication.instance)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
                channel.description = CHANNEL_DESCRIPTION
                mNotificationManager.createNotificationChannel(channel)
            }
            return mNotificationManager
        }
    val summaryNotificationBuilder = NotificationCompat.Builder(
        locoDriverApplicationContext,
        CHANNEL_ID
    )
        .setGroup(GROUPING_NOTIFICATION)
        .setGroupSummary(true)
        .setContentTitle("Locodrive")
        .setContentText("You have unread messages")
        .setSmallIcon(R.mipmap.ic_launcher)
        .setAutoCancel(true)


    private fun buildNotification(
        pendingIntent: PendingIntent,
        title: String? = "LocoDrive",
        message: String? = "Welcome To Locodrive",
        notificationId: Int
    ) {
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val largeIcon = BitmapFactory.decodeResource(
            locoDriverApplicationContext.resources,
            R.mipmap.ic_launcher_round
        )

        val notification = NotificationCompat.Builder(locoDriverApplicationContext, CHANNEL_ID)
            .setGroup(GROUPING_NOTIFICATION)
            .setLargeIcon(largeIcon)
            .setSmallIcon(notificationIcon)
            .setWhen(Date().time)
            .setContentTitle(title)
            .setContentIntent(pendingIntent)
            .setShowWhen(true)
            .setColorized(false)
            .setColor(ContextCompat.getColor(locoDriverApplicationContext, R.color.main_20))
            .setAutoCancel(true)
            .setTicker(message)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setSound(defaultSoundUri)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setDefaults(Notification.DEFAULT_SOUND)
            .build()
        notificationManager.notify(notificationId, notification)
        notificationManager.notify(BUNDLE_MESSAGE_ID, summaryNotificationBuilder.build())
    }

    private val notificationIcon: Int
        get() {
            return R.drawable.locodrive_logo_text
        }


    fun showPushNotification(locoDrivePushNotification: LocoDrivePushNotification) {
        val metaData: Meta = gson.fromJson(
            "{" + locoDrivePushNotification.meta?.asString?.substring(1, locoDrivePushNotification.meta?.asString!!.length - 1) + "}", Meta::class.java
        )
        when(metaData.type){
            Constants.NotificationConstants.NOTIFICATION_TYPE_IS_EXPENSE ->{
                metaData.expense?.let {ExpenseRepo.updateExpenseFromNotification(it)}
            }
            Constants.NotificationConstants.NOTIFICATION_TYPE_IS_TRIP ->{
                metaData.trip?.let {TripsRepo.updateTripFromNotification(it)}
            }
        }
        val broadcastIntent = Intent(locoDriverApplicationContext, LandingActivity::class.java)
        broadcastIntent.putExtra(Constants.NotificationConstants.NOTIFICATION_TYPE, metaData.type)
        val uniqueCode = System.currentTimeMillis().toInt()
        val pendingIntent = PendingIntent.getActivity(
            locoDriverApplicationContext, uniqueCode,
            broadcastIntent, PendingIntent.FLAG_CANCEL_CURRENT
        )
        /**
         * We can add  pending indent for bradcast receiver  for on click action
         */

        buildNotification(
            pendingIntent, locoDrivePushNotification.title, locoDrivePushNotification.desc,
            uniqueCode
        )
    }
}


