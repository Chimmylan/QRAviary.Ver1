package com.example.qraviaryapp.monitoring

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.mainactivities.NavHomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
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

    private lateinit var cv_IncubSetTemp: LinearLayout
    private lateinit var temperature_incubator_text_view: TextView
    private lateinit var humidity_incubator_text_view: TextView
    private lateinit var incubatorTextView: TextView
    private lateinit var completedate: TextView
    private lateinit var fanTextView1: TextView
    private lateinit var fanTextView2: TextView
    private lateinit var dbase: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private val CHANNEL_ID = "MyNotificationChannel"
    private var lastTemp: Double = 0.0
    private var lastTempIncubator: Double = 0.0
    private lateinit var monitoringlayout: LinearLayout
    private lateinit var nodevicelayout: LinearLayout
    private lateinit var monitoringView: LinearLayout
    private lateinit var visitwebsite: TextView
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
        cv_IncubSetTemp = view.findViewById(R.id.cv_IncubSetTemp)
        dbase = FirebaseDatabase.getInstance().reference
        mAuth = FirebaseAuth.getInstance()
        nodevicelayout = view.findViewById(R.id.nodevice)
        monitoringlayout = view.findViewById(R.id.monitoring)
        completedate = view.findViewById(R.id.date)
        val currentTime = Calendar.getInstance()
        val currentHour = currentTime.get(Calendar.HOUR_OF_DAY)
        val currentUserId = mAuth.currentUser?.uid
        val db = FirebaseDatabase.getInstance().getReference("Users")
            .child("ID: ${currentUserId.toString()}")
        visitwebsite = view.findViewById(R.id.visitwebsite)

        visitwebsite.setOnClickListener {
            val websiteUrl = "https://qraviary.wixsite.com/my-site"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl))
            startActivity(intent)
        }
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val roomDetails = dataSnapshot.child("Aviary")
                    .getValue(object : GenericTypeIndicator<HashMap<String, Any>>() {})

                val temperature = roomDetails?.get("temperature")
                val humidity = roomDetails?.get("humidity")

                if (temperature == null) {

                    nodevicelayout.visibility = View.VISIBLE
                    monitoringlayout.visibility = View.GONE
                } else {

                    nodevicelayout.visibility = View.GONE
                    monitoringlayout.visibility = View.VISIBLE
                }
                if (temperature is Number) {
                    val temperatureDouble = temperature.toDouble()
                    var maxTemperature: Float = 30.0F
                    val roundedTemp = temperatureDouble.roundToInt()
                    val tempDifference = roundedTemp - lastTemp
                    val tempThreshold = 0.5

                    if (temperatureDouble >= maxTemperature) {
                        fanTextView2.visibility = View.VISIBLE
                        fanTextView1 = view.findViewById(R.id.fan_text_view)
                        fanTextView2 = view.findViewById(R.id.fan_text_view2)
                        fanTextView1.text = "It is too hot!"
                        fanTextView2.text = "Fan is on."

//                        if (temperatureDouble != null) {
//                            Log.d("TemperatureDebug", "Current Temperature: $temperatureDouble, Last Temperature: $lastTemp, Round of: $roundedTemp")
//                            if (roundedTemp > temperatureDouble) {
//                                Log.d("TemperatureDebug", "Temperature increased beyond the threshold. Sending notification.")
//                                sendTempNotification("High", temperatureDouble)
//                            } else if (temperatureDouble < roundedTemp - tempThreshold) {
//                                Log.d("TemperatureDebug", "Temperature decreased beyond the threshold. Sending notification.")
//                                sendTempNotification("Low", temperatureDouble)
//                            }
//                        }
//                        if (temperatureDouble != null) {
//                            when {
//                                temperatureDouble == 32.0 -> sendTempNotification("High", temperatureDouble)
//                                temperatureDouble == 31.0 -> sendTempNotification("High", temperatureDouble)
//                                temperatureDouble == 33.0 -> sendTempNotification("High", temperatureDouble)
//                            }
//
//                            // Update last temperature after checking
//                            lastTemp = temperatureDouble
//                        }
                        var notificationSent = false // Initialize the flag

                        if (temperatureDouble >= 30.00 && temperatureDouble % 1.0 == 0.0 && !notificationSent) {
                            // Send notification when the decimal part is exactly 00
                            sendTempNotification("High", temperatureDouble)
                        } else if (temperatureDouble <= 29.00) {
                            sendTempNotification("Normal", temperatureDouble)
                        }


                    } else if (temperatureDouble < maxTemperature) {
                        fanTextView1 = view.findViewById(R.id.fan_text_view)
                        fanTextView2 = view.findViewById(R.id.fan_text_view2)
                        fanTextView1.text = "Normal Temperature."
                        fanTextView2.text = "Fan is off!"

                        if (temperatureDouble != null && temperatureDouble <= 29.00) {
                            sendTempNotification("Normal", temperatureDouble)
                            lastTemp = temperatureDouble
                        }
                    } else {
                        fanTextView1.text = "Sensor is not working!"
                        fanTextView2.text = "Fan is Unavailable!"
                    }
                }


                if (humidity is Number) {
                    val humidityDouble =
                        if (humidity is Long) humidity.toDouble() else humidity.toDouble()
                }

                val progressText = view.findViewById<TextView>(R.id.text_view_progress)
//                val progressBarTemp = view.findViewById<ProgressBar>(R.id.progress_bar)

                val progressText2 = view.findViewById<TextView>(R.id.text_view_progress2)
//                val progressBarHumid = view.findViewById<ProgressBar>(R.id.progress_bar2)

//                if (temperature is Number) {
//                    progressBarTemp.progress = temperature.toDouble().roundToInt()
//                }
//
//                if (humidity is Number) {
//                    progressBarHumid.progress = humidity.toDouble().roundToInt()
//                }

                progressText.text = "$temperature °C"
                progressText2.text = "$humidity %"


// ---------------------------------------Incubator
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
                    val temperatureIncubatorDouble = temperatureIncubator.toDouble()
                    if (temperatureIncubator is Long) temperatureIncubator.toDouble() else temperatureIncubator.toDouble()
                    temperatureIncubatorTextView.text = "$temperatureIncubatorDouble °C"
                    val roundedTemp = temperatureIncubatorDouble.roundToInt()
                    val tempDifference = roundedTemp - lastTempIncubator
                    val tempThreshold = 1.0

                    if (temperatureIncubatorDouble != null && temperatureIncubatorDouble >= 36.9) {
                        sendTempNotificationIncubator("High", temperatureIncubatorDouble)

                        lastTempIncubator = temperatureIncubatorDouble
                    } else if (temperatureIncubatorDouble != null && temperatureIncubatorDouble == 35.00) {
                        sendTempNotificationIncubator("Normal", temperatureIncubatorDouble)
                    }
                }

                if (humidityIncubator is Number) {
                    // Check if it's a Long or Double and then convert to Double
                    val humidityIncubatorDouble =
                        if (humidityIncubator is Long) humidityIncubator.toDouble() else humidityIncubator.toDouble()
                    humidityIncubatorTextView.text = "$humidityIncubatorDouble %"
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        val completedate1 = Date()

        val dateFormat = SimpleDateFormat("EEEE dd MMMM yyyy", Locale.getDefault())


        val formattedDate = dateFormat.format(completedate1)

        completedate.text = formattedDate
        // Set the greeting based on the time of day
        val greeting = when {
            currentHour in 5..8 -> "Good early morning!"
            currentHour in 9..12 -> "Good morning!"
            currentHour in 12..17 -> "Good afternoon!"
            currentHour in 13..15 -> "Good afternoon!"
            currentHour in 16..17 -> "Good late afternoon!"
            currentHour in 18..19 -> "Good evening!"
            currentHour in 20..23 -> "Good night!"
            currentHour in 0..4 -> "Good night!"
            else -> "Still Sleeping!"
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
                sendNotificationTemperature(
                    "High",
                    temperatureDouble,
                    "Aviary is too hot! Fan is on."
                )
            }

            temperatureDouble < tempNormal -> {
                sendNotificationTemperature(
                    "Normal",
                    temperatureDouble,
                    "Aviary is  in normal temperature, Fan is off."
                )
            }

        }
    }

    private fun sendTempNotificationIncubator(
        alertType: String,
        temperatureDoubleIncubator: Double
    ) {
        val tempHot = 37.0
        val tempNormal = 35.0

        when {
            temperatureDoubleIncubator == tempHot -> {
                sendNotificationTemperature(
                    "High",
                    temperatureDoubleIncubator,
                    "Incubator, Light will turn off."
                )
            }

            temperatureDoubleIncubator == tempNormal -> {
                sendNotificationTemperature(
                    "Normal",
                    temperatureDoubleIncubator,
                    "Incubator, Light will turn on."
                )
            }

        }
    }

    private fun sendNotificationTemperature(
        context: String,
        temperatureDouble: Double,
        notificationMessage: String
    ) {

//        if (context == null) {
//            return
//        }
        if (isAdded) {
            val intent = Intent(requireContext(), NavHomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            val pendingIntent = PendingIntent.getActivity(
                requireContext(),
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val builder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setContentTitle("Temperature Alert")
                .setContentText("$temperatureDouble°C - $notificationMessage")
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

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "My Notification Channel"
            val descriptionText = "Channel for my notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            channel.enableLights(true)
            channel.lightColor = Color.BLUE
            channel.enableVibration(true)
            val notificationManager =
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}