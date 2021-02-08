package com.jadovan.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.jadovan.ui.DetailActivity
import com.jadovan.R

private const val NOTIFICATION_ID = 0
private const val REQUEST_CODE = 0

fun NotificationManager.sendNotification(downloadName: String, downloadStatus: String, applicationContext: Context) {

    val detailIntent = Intent(applicationContext, DetailActivity::class.java)
    detailIntent.putExtra("downloadName", downloadName)
    detailIntent.putExtra("downloadStatus", downloadStatus)

    val detailPendingIntent = PendingIntent.getActivity(
            applicationContext,
            REQUEST_CODE,
            detailIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
    )

    val builder = NotificationCompat.Builder(
            applicationContext,
            applicationContext.getString(R.string.notification_channel_id)
    )
            .setSmallIcon(R.drawable.ic_assistant_black_24dp)
            .setContentTitle(applicationContext.getString(R.string.notification_title))
            .setContentText(applicationContext.getString(R.string.notification_description))
            .setContentIntent(detailPendingIntent)
            .addAction(
                    R.drawable.ic_assistant_black_24dp,
                    applicationContext.getString(R.string.notification_button),
                    detailPendingIntent
            )
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setAutoCancel(true)

    notify(NOTIFICATION_ID, builder.build())
}

fun NotificationManager.cancelNotifications() {
    cancelAll()
}