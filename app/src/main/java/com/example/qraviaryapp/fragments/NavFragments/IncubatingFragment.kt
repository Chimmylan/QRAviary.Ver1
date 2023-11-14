package com.example.qraviaryapp.fragments.NavFragments

import EggData
import PairData
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.qraviaryapp.R
import com.example.qraviaryapp.adapter.DetailedAdapter.EggAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class IncubatingFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var token: FirebaseApp
    private lateinit var db: DatabaseReference
    private lateinit var dataList: ArrayList<EggData>
    private lateinit var adapter: EggAdapter
    private lateinit var snackbar: Snackbar
    private lateinit var totalBirds: TextView
    private lateinit var connectivityManager: ConnectivityManager
    private var isNetworkAvailable = true
    private var femalegallery: String? = null
    private var malegallery: String? = null
    private lateinit var swipeToRefresh: SwipeRefreshLayout
    private lateinit var loadingProgressBar: ProgressBar
    var total: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_incubating, container, false)


        loadingProgressBar = view.findViewById(R.id.loadingProgressBar)
        mAuth = FirebaseAuth.getInstance()
        totalBirds = view.findViewById(R.id.tvBirdCount)
        swipeToRefresh = view.findViewById(R.id.swipeToRefresh)
        dataList = ArrayList()
        val gridLayoutManager = GridLayoutManager(requireContext(), 1)
        recyclerView = view.findViewById(R.id.RecyclerView)
        recyclerView.layoutManager = gridLayoutManager

        adapter = EggAdapter(requireContext(), dataList)
        recyclerView.adapter = adapter


        loadDataFromDatabase()
        snackbar = Snackbar.make(view, "", Snackbar.LENGTH_LONG)
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

        refreshApp()
        return view
    }
    private fun refreshApp() {
        swipeToRefresh.setOnRefreshListener {
            lifecycleScope.launch {
                try {
                    val data = getDataFromDatabase()
                    dataList.clear()
                    dataList.addAll(data)
                    swipeToRefresh.isRefreshing = false
                    Toast.makeText(requireContext(), "Refreshed", Toast.LENGTH_SHORT).show()
                    adapter.notifyDataSetChanged()
                } catch (e: Exception) {
                    Log.e(ContentValues.TAG, "Error reloading data: ${e.message}")
                }

            }
            swipeToRefresh.isRefreshing = false
        }
    }
    private fun showSnackbar(message: String) {
        snackbar.setText(message)
        snackbar.show()
    }

    private fun loadDataFromDatabase() {
        lifecycleScope.launch {
            try {
                val data = getDataFromDatabase()
                dataList.clear()
                dataList.addAll(data)
                adapter.notifyDataSetChanged()
            }  catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error reloading data: ${e.message}")
            }
        }
    }

    suspend fun getDataFromDatabase(): List<EggData> = withContext(Dispatchers.IO) {
        val currentUserId = mAuth.currentUser?.uid
        val db = FirebaseDatabase.getInstance().reference.child("Users")
            .child("ID: ${currentUserId.toString()}").child("Pairs")

        val dataList = ArrayList<EggData>()
        val snapshot = db.get().await()

        for (itemsnapshot in snapshot.children) {
            val data = itemsnapshot.getValue(EggData::class.java)
            val newData = itemsnapshot.getValue(PairData::class.java)
            femalegallery = itemsnapshot.child("Female Gallery").value.toString()
            malegallery = itemsnapshot.child("Male Gallery").value.toString()
            val key = itemsnapshot.key.toString()
            val pairsId = itemsnapshot.child("Pair ID").value.toString()
            val cageName = itemsnapshot.child("Cage").value.toString()
            val cageKeyFemale = itemsnapshot.child("CageKeyFemale").value.toString()
            val cageKeyMale = itemsnapshot.child("CageKeyMale").value.toString()
            val cageBirdFemale = itemsnapshot.child("CageKeyFlightFemaleValue").value.toString()
            val cageBirdMale = itemsnapshot.child("CageKeyFlightMaleValue").value.toString()
            val male = itemsnapshot.child("Male").value.toString()
            val female = itemsnapshot.child("Female").value.toString()
            val maleMutation = itemsnapshot.child("Male Mutation").value.toString()
            val femaleMutation = itemsnapshot.child("Female Mutation").value.toString()
            val beginningDate = itemsnapshot.child("Beginning").value.toString()
            val pairMaleKey = itemsnapshot.child("Male Bird Key").value.toString()
            val pairFemaleKey = itemsnapshot.child("Female Bird Key").value.toString()
            val separateDate = itemsnapshot.child("Separate Date").value.toString()
            val pairMaleFlightKey = itemsnapshot.child("Male Flight Key").value.toString()
            val pairFemaleFlightKey = itemsnapshot.child("Female Flight Key").value.toString()


            data?.pairKey = key
            data?.pairFlightFemaleKey = pairFemaleFlightKey
            data?.pairFlightMaleKey = pairMaleFlightKey
            data?.pairBirdMaleKey = pairMaleKey
            data?.pairBirdFemaleKey = pairFemaleKey
            data?.pairMaleId = male
            data?.pairFemaleId = female
            data?.eggcagebirdFemale = cageBirdFemale
            data?.eggcagebirdMale = cageBirdMale
            data?.eggcagekeyMale = cageKeyMale
            data?.eggcagekeyFemale = cageKeyFemale
            var clutchCount = 0
            val clutches = itemsnapshot.child("Clutches")
            for (clutchSnapshot in clutches.children) {
                val data = EggData()  // Create a new EggData object for each clutch

                val key = clutchSnapshot.key.toString()
                var eggsCount = 0

                if (data != null) {
                    for (eggSnapshot in clutchSnapshot.children) {
                        val eggStatus = eggSnapshot.child("Status").value.toString()
                        val eggDate = eggSnapshot.child("Date").value.toString()


                        if (eggStatus == "Incubating") {
                            eggsCount++
                            data.eggKey = key
                            data.eggCount = eggsCount.toString()
                            data.eggIncubating = eggsCount.toString()
                            data.eggIncubationStartDate = eggDate
                        }
                    }
                    // Only add data to the list if it has Incubating eggs
                    if (data.eggIncubating != null) {
                        dataList.add(data)
                    }
                }
            }
        }



        var totalEggCount = 0

        for (dataItem in dataList) {
            totalEggCount += dataItem.eggCount?.toIntOrNull() ?: 0
        }

        // Store the total egg count in SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("totalEggCount", totalEggCount)
        editor.apply()
        total = dataList.count()
        if(total>1){
            totalBirds.text = total.toString() + " Clutches"
        }
        else{
            totalBirds.text = total.toString() + " Clutch"
        }


        dataList.sortBy { it.eggIncubationStartDate }
        dataList
    }


    override fun onResume() {
        super.onResume()

        // Call a function to reload data from the database and update the RecyclerView
        reloadDataFromDatabase()

    }

    private fun reloadDataFromDatabase() {
        loadingProgressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {

                val data = getDataFromDatabase()
                dataList.clear()
                dataList.addAll(data)

                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error reloading data: ${e.message}")
            } finally {

                loadingProgressBar.visibility = View.GONE
            }
        }}

    // Add any other methods you need

}