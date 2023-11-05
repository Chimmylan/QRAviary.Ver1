package com.example.qraviaryapp.fragments.NavFragments

import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.dashboards.BreedingCagesList2Activity
import com.example.qraviaryapp.activities.dashboards.BreedingCagesListActivity
import com.example.qraviaryapp.activities.dashboards.FlightCagesList2Activity
import com.example.qraviaryapp.activities.dashboards.FlightCagesListActivity
import com.example.qraviaryapp.activities.dashboards.NurseryCagesList2Activity
import com.example.qraviaryapp.activities.dashboards.NurseryCagesListActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CagesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CagesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var mAuth: FirebaseAuth
    private lateinit var btnBreeding: MaterialButton
    private lateinit var btnNursery: MaterialButton
    private lateinit var btnFlight: MaterialButton
    private lateinit var snackbar: Snackbar
    private lateinit var connectivityManager: ConnectivityManager
    private var isNetworkAvailable = true
    private lateinit var swipeToRefresh: SwipeRefreshLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       val view = inflater.inflate(R.layout.fragment_cages, container, false)

        mAuth = FirebaseAuth.getInstance()
        btnBreeding = view.findViewById(R.id.btnBreeding)
        btnNursery = view.findViewById(R.id.btnNursery)
        btnFlight = view.findViewById(R.id.btnFlight)

        // Set up click listeners for buttons
        btnBreeding.setOnClickListener {
            val i = Intent(requireContext(), BreedingCagesList2Activity::class.java)
            startActivity(i)
        }
        btnNursery.setOnClickListener {
            val i = Intent(requireContext(), NurseryCagesList2Activity::class.java)
            startActivity(i)
        }
        btnFlight.setOnClickListener {
            val i = Intent(requireContext(), FlightCagesList2Activity::class.java)
            startActivity(i)
        }
        /*snackbar = Snackbar.make(rootView, "", Snackbar.LENGTH_LONG)
        connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // Set up NetworkCallback to detect network changes
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                if (!isNetworkAvailable) {
                    // Network was restored from offline, show Snackbar
                    showSnackbar("Your Internet connection was restored")
                }
                isNetworkAvailable = true
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                // Network is offline, show Snackbar
                showSnackbar("You are currently offline")
                isNetworkAvailable = false
            }
        }

        // Register the NetworkCallback
        connectivityManager.registerDefaultNetworkCallback(networkCallback)

*/

        return view
    }
    /*private fun showSnackbar(message: String) {
        snackbar.setText(message)
        snackbar.show()
    }*/

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CagesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CagesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}