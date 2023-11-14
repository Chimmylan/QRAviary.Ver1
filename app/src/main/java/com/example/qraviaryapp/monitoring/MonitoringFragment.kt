package com.example.qraviaryapp.monitoring

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.mainactivities.HomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.Calendar
import kotlin.math.round
import kotlin.math.roundToInt


/**
 * A simple [Fragment] subclass.
 * Use the [MonitoringFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MonitoringFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var day: TextView
    private lateinit var cv_Settemp: CardView
    private lateinit var cv_IncubSetTemp: CardView
    private lateinit var temperature_text_view: TextView
    private lateinit var humidity_text_view: TextView
    private lateinit var temperature_incubator_text_view: TextView
    private lateinit var humidity_incubator_text_view: TextView
    private lateinit var incubatorTextView: TextView
    private lateinit var fanTextView: TextView
    private lateinit var fanTextView2: TextView
    private lateinit var dbase: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private val CHANNEL_ID = "MyNotificationChannel"
    private var lastTemp = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_monitoring, container, false)
        day = view.findViewById(R.id.day)
        incubatorTextView = view.findViewById(R.id.incubatorTextView)
        cv_Settemp = view.findViewById(R.id.cv_Settemp)
        cv_IncubSetTemp = view.findViewById(R.id.cv_IncubSetTemp)
        dbase = FirebaseDatabase.getInstance().reference
        mAuth = FirebaseAuth.getInstance()
//        cv_Settemp.setOnClickListener{
//            val intent = Intent(requireContext(), SetTempActivity::class.java)
//            startActivity(intent)
//        }
        val currentTime = Calendar.getInstance()
        val currentHour = currentTime.get(Calendar.HOUR_OF_DAY)
        val currentUserId = mAuth.currentUser?.uid
        val db = FirebaseDatabase.getInstance().getReference("Users")
            .child("ID: ${currentUserId.toString()}")

        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val roomDetails = dataSnapshot.child("Aviary")
                    .getValue(object : GenericTypeIndicator<HashMap<String, Any>>() {})

                val temperature = roomDetails?.get("temperature")
                val humidity = roomDetails?.get("humidity")

                val temperatureTextView = view.findViewById<TextView>(R.id.temperature_text_view)
                val humidityTextView = view.findViewById<TextView>(R.id.humidity_text_view)

                if (temperature is Number) {
                    // Check if it's a Long or Double and then convert to Double
                    val temperatureDouble =
                        if (temperature is Long) temperature.toDouble() else temperature.toDouble()
//                    temperatureTextView.text = "Temperature: ${temperatureDouble.roundToInt()} °C"

                    var maxTemperature: Float = 30.0F

                    if (temperatureDouble >= maxTemperature) {
                        fanTextView = view.findViewById(R.id.fan_text_view)
                        fanTextView2 = view.findViewById(R.id.fan_text_view2)
                        fanTextView.text = "It is too hot!"
                        fanTextView2.text = "Fan is on."
                    } else if (temperatureDouble < maxTemperature) {
                        fanTextView.text = "Normal Temperature."
                        fanTextView2.text = "Fan is off!"
                    } else {
                        fanTextView.text = "Sensor is not working!"
                        fanTextView2.text = "Fan is Unavailable!"
                    }

//                    notification
                    val roundedTemp = temperatureDouble.roundToInt()
                    val tempDifference = roundedTemp - lastTemp
                    val tempThreshold = 0.5

                    if(temperatureDouble != null && tempDifference >= tempThreshold){

                        sendTempNotification("High", temperatureDouble)
                        lastTemp = temperatureDouble
                    }

//                    if(temperatureDouble != null && temperatureDouble > maxTemperature){
//                        sendTempNotification("High", temperatureDouble)
//                    }

                }


                if (humidity is Number) {
                    // Check if it's a Long or Double and then convert to Double
                    val humidityDouble =
                        if (humidity is Long) humidity.toDouble() else humidity.toDouble()
//                    humidityTextView.text = "Humidity: ${humidityDouble.roundToInt()} %"
                }

                val progressText = view.findViewById<TextView>(R.id.text_view_progress)
                val progressBarTemp = view.findViewById<ProgressBar>(R.id.progress_bar)

                val progressText2 = view.findViewById<TextView>(R.id.text_view_progress2)
                val progressBarHumid = view.findViewById<ProgressBar>(R.id.progress_bar2)

                if (temperature is Number) {
                    progressBarTemp.progress = temperature.toDouble().roundToInt()
                }

                if (humidity is Number) {
                    progressBarHumid.progress = humidity.toDouble().roundToInt()
                }

                progressText.text = "$temperature °C"
                progressText2.text = "$humidity %"
//Incubator
                val incubatorDetails = dataSnapshot.child("Incubator")
                    .getValue(object : GenericTypeIndicator<HashMap<String, Any>>() {})

                val temperatureIncubator = incubatorDetails?.get("temperature")
                val humidityIncubator = incubatorDetails?.get("humidity")

                val temperatureIncubatorTextView =
                    view.findViewById<TextView>(R.id.temperature_incubator_text_view)
                val humidityIncubatorTextView =
                    view.findViewById<TextView>(R.id.humidity_incubator_text_view)
                val incubatorTextView = view.findViewById<TextView>(R.id.incubatorTextView)

                if (temperatureIncubator is Number) {
                    // Check if it's a Long or Double and then convert to Double
                    incubatorTextView.text = "Incubator is Working"

                    val temperatureIncubatorDouble =
                        if (temperatureIncubator is Long) temperatureIncubator.toDouble() else temperatureIncubator.toDouble()
                    temperatureIncubatorTextView.text =
                        "Temperature: $temperatureIncubatorDouble °C"
                }

                if (humidityIncubator is Number) {
                    // Check if it's a Long or Double and then convert to Double
                    val humidityIncubatorDouble =
                        if (humidityIncubator is Long) humidityIncubator.toDouble() else humidityIncubator.toDouble()
                    humidityIncubatorTextView.text = "Humidity: $humidityIncubatorDouble %"
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        // Set the greeting based on the time of day
        val greeting = when {
            currentHour in 6..11 -> "Good morning!"
            currentHour in 12..17 -> "Good afternoon!"
            else -> "Good evening!"
        }

        day.text = greeting
//        cv_IncubSetTemp.setOnClickListener{
//            val intent = Intent(requireContext(), IncubatorActivity::class.java)
//            startActivity(intent)
//        }


        createNotificationChannel()

        return view
    }

    private fun sendTempNotification(alertType: String, temperatureDouble: Double) {
        val tempHot = 30.0
        val tempNormal = 29.0

        when {
            temperatureDouble >= tempHot -> {
                sendNotification("High", temperatureDouble, "It is too hot! Fan is on.")
            }
            temperatureDouble < tempNormal -> {
                sendNotification("Normal", temperatureDouble, "Normal Temperature, Fan is off.")
            }
        }
    }

    private fun sendNotification(alertType: String, temperatureDouble: Double, notificationMessage: String) {
        val intent = Intent(requireContext(), MonitoringFragment::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pendingIntent = PendingIntent.getActivity(requireContext(), 0,intent,PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_icon)
            .setContentTitle("Temperature Alert")
            .setContentText("Temperature is $temperatureDouble°C. $notificationMessage")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        // Show the notification
        val notificationManager = NotificationManagerCompat.from(requireContext())
        val notificationId = System.currentTimeMillis().toInt()
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        notificationManager.notify(notificationId, builder.build())
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "My Notification Channel"
            val descriptionText = "Channel for my notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
//            channel.description = description
//            channel.enableLights(true)
//            channel.lightColor = Color.BLUE
//            channel.enableVibration(true)
//            val notificationManager = requireContext().getSystemService(NotificationManager::class.java)
//            notificationManager.createNotificationChannel(channel)
            val notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
             }
        }
    }
