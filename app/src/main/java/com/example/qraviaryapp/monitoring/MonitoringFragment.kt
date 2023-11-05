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


/**
 * A simple [Fragment] subclass.
 * Use the [MonitoringFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MonitoringFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var tv1: TextView
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

        cv_Settemp = view.findViewById(R.id.cv_Settemp)
        cv_IncubSetTemp = view.findViewById(R.id.cv_IncubSetTemp)
        cv_Settemp.setOnClickListener{
            val intent = Intent(requireContext(), SetTempActivity::class.java)
            startActivity(intent)
        }
        cv_IncubSetTemp.setOnClickListener{
            val intent = Intent(requireContext(), IncubatorActivity::class.java)
            startActivity(intent)
        }
        return view
    }


}