package com.example.qraviaryapp.monitoring

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.qraviaryapp.R
import java.util.Calendar


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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_monitoring, container, false)
        day = view.findViewById(R.id.day)
        cv_Settemp = view.findViewById(R.id.cv_Settemp)
        cv_IncubSetTemp = view.findViewById(R.id.cv_IncubSetTemp)
//        cv_Settemp.setOnClickListener{
//            val intent = Intent(requireContext(), SetTempActivity::class.java)
//            startActivity(intent)
//        }
        val currentTime = Calendar.getInstance()
        val currentHour = currentTime.get(Calendar.HOUR_OF_DAY)

        // Set the greeting based on the time of day
        val greeting = when {
            currentHour in 6..11 -> "Good morning!"
            currentHour in 12..17 -> "Good afternoon!"
            else -> "Good evening!"
        }

        day.text = greeting
        cv_IncubSetTemp.setOnClickListener{
            val intent = Intent(requireContext(), IncubatorActivity::class.java)
            startActivity(intent)
        }
        return view
    }


}