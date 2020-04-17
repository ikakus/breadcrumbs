package com.ikakus.breadcrumbs.reminder

import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.ikakus.breadcrumbs.reminder.notification.NotificationPublisher
import com.ikakus.breadcrumbs.reminder.notification.getNotification
import java.util.*


class AlarmHelper(val context: Context) {

    fun setAlaram(calendar: Calendar) {
        val notification =
            getNotification(context, "lolol")
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

        Toast.makeText(context, "Reminder set", Toast.LENGTH_SHORT).show()
    }
}