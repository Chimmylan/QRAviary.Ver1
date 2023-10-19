package com.example.qraviaryapp.adapter
import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.os.Build
import androidx.core.content.ContextCompat
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.detailedactivities.PairsDetailedActivity
import com.example.qraviaryapp.activities.mainactivities.NavHomeActivity

class MyAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null) {
            val eggIndex = intent.getIntExtra("egg_index", -1)
            if (eggIndex != -1) {

                showNotification(context, "$eggIndex", "Egg is ready to hatch!", eggIndex)

            }
        }


    }

    private fun showNotification(context: Context?, title: String, message: String, notificationId: Int) {
        val channelId = "MyAlarmChannel"

        val intent = Intent(context, NavHomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        if (context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.POST_NOTIFICATIONS) } == PackageManager.PERMISSION_GRANTED) {
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            val builder = NotificationCompat.Builder(context!!, channelId)
                .setSmallIcon(R.drawable.logoqraviary)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            val notificationManager = NotificationManagerCompat.from(context)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(channelId, "Alarm Channel", NotificationManager.IMPORTANCE_HIGH)
                notificationManager.createNotificationChannel(channel)
            }

            notificationManager.notify(notificationId, builder.build())
        } else {
            // Handle the case where the POST_NOTIFICATIONS permission is not granted
            // You can request the permission or take an appropriate action
        }
    }

}
