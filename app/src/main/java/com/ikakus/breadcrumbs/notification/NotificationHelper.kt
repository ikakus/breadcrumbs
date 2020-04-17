package com.ikakus.breadcrumbs.notification

import android.R
import android.app.Notification
import android.app.NotificationManager
import android.content.Context

fun getNotification(context: Context, title: String): Notification {
    val builder: Notification.Builder = Notification.Builder(context)
    builder.setContentTitle("Scheduled Notification")
    builder.setContentText(title)
    builder.setSmallIcon(R.drawable.ic_lock_idle_alarm)
    return builder.build()
}

fun show(context: Context) {
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    notificationManager.notify(111,
        getNotification(context, "lol")
    )
}