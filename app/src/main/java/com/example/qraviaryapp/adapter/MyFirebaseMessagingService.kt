package com.example.qraviaryapp.adapter

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.qraviaryapp.R
import com.example.qraviaryapp.fragments.NavFragments.IncubatingFragment
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val channelId = "notification_channel"
const val channelName = "com.example.qraviaryapp.adapter"

class MyFirebaseMessagingService : FirebaseMessagingService() {


    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        if (remoteMessage.notification != null) {

            showNotification(
                remoteMessage.notification!!.title!!,
                remoteMessage.notification!!.body!!
            )
        }
    }

    
    private fun getCustomDesign(
        title: String,
        message: String
    ): RemoteViews {
        val remoteViews = RemoteViews("com.example.qraviaryapp", R.layout.notification)
        remoteViews.setTextViewText(R.id.notification_title, title)
        remoteViews.setTextViewText(R.id.notification_description, message)
        remoteViews.setImageViewResource(
            R.id.notification_icon,
            R.drawable.logoqraviary
        )

        return remoteViews
    }


    fun showNotification(
        title: String,
        message: String
    ) {
        val intent = Intent(this, IncubatingFragment::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Create a Builder object using NotificationCompat
        // class. This will allow control over all the flags
        var builder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.logoqraviary)
                .setAutoCancel(true)
                .setVibrate(
                    longArrayOf(
                        1000, 1000, 1000,
                        1000, 1000
                    )
                )
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)


        builder = builder.setContent(getCustomDesign(title, message))

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager?.createNotificationChannel(notificationChannel)

        }
        notificationManager?.notify(0, builder.build())
    }

    override fun onNewToken(token: String) {
        Log.d("FCM_TOKEN", token)

    }

}



