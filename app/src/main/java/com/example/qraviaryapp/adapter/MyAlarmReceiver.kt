package com.example.qraviaryapp.adapter

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.detailedactivities.ClutchesDetailedActivity

class MyAlarmReceiver : BroadcastReceiver() {
    private lateinit var pairmale: String
    private lateinit var pairfemale: String
    private lateinit var clutchkey: String
    private lateinit var pairkey: String
    private lateinit var eggkey: String
    private lateinit var pairflightfemalekey: String
    private lateinit var pairflightmalekey: String
    private lateinit var pairmalekey: String
    private lateinit var pairfemalekey: String
    private lateinit var cagekeymale: String
    private lateinit var cagekeyfemale: String
    private lateinit var cagebirdfemale: String
    private lateinit var cagebirdmale: String
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null) {
            val eggIndex = intent.getIntExtra("egg_index", -1)
            pairmale = intent.getStringExtra("pairmale").toString()
            pairfemale = intent.getStringExtra("pairfemale").toString()
            clutchkey = intent.getStringExtra("clutchkey").toString()
            pairkey = intent.getStringExtra("pairkey").toString()
            eggkey= intent.getStringExtra("eggkey").toString()
            pairflightmalekey= intent.getStringExtra("pairflightmalekey").toString()
            pairflightfemalekey= intent.getStringExtra("pairflightfemalekey").toString()
            pairmalekey= intent.getStringExtra("pairmalekey").toString()
            pairfemalekey= intent.getStringExtra("pairfemalekey").toString()
            cagekeyfemale= intent.getStringExtra("cagekeyfemale").toString()
            cagekeymale= intent.getStringExtra("cagekeymale").toString()
            cagebirdmale= intent.getStringExtra("cagebirdmale").toString()
            cagebirdfemale= intent.getStringExtra("cagebirdfemale").toString()

            if (eggIndex != -1) {
                showNotification(context, "$eggIndex of Pair $pairmale and $pairfemale", "Egg is Hatched Today!", eggIndex)
            }
        }
    }

    private fun showNotification(context: Context?, title: String, message: String, notificationId: Int) {
        val channelId = "MyAlarmChannel"
        val bundle = Bundle()
        bundle.putString("PairKey", pairkey)
        bundle.putString("EggKey", eggkey)
        bundle.putString("PairFlightMaleKey", pairflightmalekey)
        bundle.putString("PairFlightFemaleKey", pairflightfemalekey)
        bundle.putString("PairMaleKey", pairmalekey)
        bundle.putString("PairFemaleKey", pairfemalekey)
        bundle.putString("PairMaleID",  pairmale)
        bundle.putString("PairFemaleID",pairfemale)
        bundle.putString("CageKeyFemale",cagekeyfemale)
        bundle.putString("CageKeyMale", cagekeymale)
        bundle.putString("CageBirdFemale",cagebirdfemale)
        bundle.putString("CageBirdMale", cagebirdmale)
        val intent = Intent(context, ClutchesDetailedActivity::class.java)
        intent.putExtras(bundle)

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        if (context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.POST_NOTIFICATIONS) } == PackageManager.PERMISSION_GRANTED) {
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

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
