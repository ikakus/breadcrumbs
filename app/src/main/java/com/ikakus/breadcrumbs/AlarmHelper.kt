package com.ikakus.breadcrumbs

import android.R
import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import java.util.*


class AlarmHelper(val context: Context) {

    fun setAlaram(calendar: Calendar) {
        val notification = getNotification("lolol")
        scheduleNotification(notification, calendar)
    }

    private fun scheduleNotification(notification: Notification, calendar: Calendar) {
        val notificationIntent = Intent(context, NotificationPublisher::class.java)
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1)
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmMgr.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )

        Toast.makeText(context,"Reminder set", Toast.LENGTH_SHORT).show()
    }

    private fun getNotification(content: String): Notification {
        val builder: Notification.Builder = Notification.Builder(context)
        builder.setContentTitle("Scheduled Notification")
        builder.setContentText(content)
        builder.setSmallIcon(R.drawable.ic_lock_idle_alarm)
        return builder.build()
    }

}