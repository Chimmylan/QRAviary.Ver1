package com.example.qraviaryapp.fragments.NavFragments

import PairData
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.AddActivities.AddPairActivity
import com.example.qraviaryapp.adapter.PairListAdapter
import com.example.qraviaryapp.adapter.PreviousPairAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class PairsFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var dbase: DatabaseReference
    private lateinit var dataList: ArrayList<PairData>
    private lateinit var dataList1: ArrayList<PairData>
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerView1: RecyclerView
    private lateinit var adapter1: PreviousPairAdapter
    private lateinit var adapter: PairListAdapter
    private lateinit var fab: FloatingActionButton
    private lateinit var snackbar: Snackbar
    private lateinit var connectivityManager: ConnectivityManager
    private var isNetworkAvailable = true
    private lateinit var current: TextView
    private lateinit var previous: TextView
    private var femalegallery: String? = null
    private var malegallery: String? = null
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var swipeToRefresh: SwipeRefreshLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pairs, container, false)
        fab = view.findViewById(R.id.fab)
        mAuth = FirebaseAuth.getInstance()
        swipeToRefresh = view.findViewById(R.id.swipeToRefresh)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView1 = view.findViewById(R.id.recyclerView1)
        val gridLayoutManager = GridLayoutManager(requireContext(), 1)
        recyclerView.layoutManager = gridLayoutManager

        val gridLayoutManager1 = GridLayoutManager(requireContext(), 1)
        recyclerView1.layoutManager = gridLayoutManager1
        loadingProgressBar = view.findViewById(R.id.loadingProgressBar)
        dataList1 = ArrayList()
        adapter1 = PreviousPairAdapter(requireContext(), dataList1)
        recyclerView1.adapter = adapter1
        dataList = ArrayList()
        adapter = PairListAdapter(requireContext(), dataList)
        recyclerView.adapter = adapter
        current = view.findViewById(R.id.tvCurrent)
        previous = view.findViewById(R.id.tvPrevious)
        lifecycleScope.launch {
            try {
                val data = getDataFromDatabase()
                dataList.clear()
                dataList.addAll(data)
                adapter.notifyDataSetChanged()
                if (dataList.isEmpty()) {
                    current.visibility = View.GONE
                } else {
                    current.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error retrieving data: ${e.message}")
            }
        }
        lifecycleScope.launch {
            try {
                val data = getDataFromDatabasePrevious()
                dataList1.clear()
                dataList1.addAll(data)
                adapter1.notifyDataSetChanged()
                if (dataList1.isEmpty()) {
                    previous.visibility = View.GONE
                } else {
                    previous.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error retrieving data: ${e.message}")
            }
        }
        fab.setOnClickListener {
            startActivity(Intent(requireContext(), AddPairActivity::class.java))
        }
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
                    adapter.notifyDataSetChanged()
                    swipeToRefresh.isRefreshing = false
                    if (dataList.isEmpty()) {
                        current.visibility = View.GONE
                    } else {
                        current.visibility = View.VISIBLE
                    }
                } catch (e: Exception) {
                    Log.e(ContentValues.TAG, "Error retrieving data: ${e.message}")
                }
            }
            lifecycleScope.launch {
                try {
                    val data = getDataFromDatabasePrevious()
                    dataList1.clear()
                    dataList1.addAll(data)
                    adapter1.notifyDataSetChanged()
                    swipeToRefresh.isRefreshing = false
                    if (dataList1.isEmpty()) {
                        previous.visibility = View.GONE
                    } else {
                        previous.visibility = View.VISIBLE
                    }
                } catch (e: Exception) {
                    Log.e(ContentValues.TAG, "Error retrieving data: ${e.message}")
                }
            }
        }
    }

    private fun showSnackbar(message: String) {
        snackbar.setText(message)
        snackbar.show()
    }

    private suspend fun getDataFromDatabase(): List<PairData> = withContext(Dispatchers.IO) {

        val currentUserId = mAuth.currentUser?.uid
        val db = FirebaseDatabase.getInstance().reference.child("Users")
            .child("ID: ${currentUserId.toString()}").child("Pairs")
        val dataList = ArrayList<PairData>()
        val snapshot = db.get().await()
        for (itemSnapshot in snapshot.children) {
            val data = itemSnapshot.getValue(PairData::class.java)
            if (data != null) {
                if (itemSnapshot.child("Separate Date").exists()) {

                } else {
                    femalegallery = itemSnapshot.child("Female Gallery").value.toString()
                    malegallery = itemSnapshot.child("Male Gallery").value.toString()
                    val key = itemSnapshot.key.toString()
                    val pairsId = itemSnapshot.child("Pair ID").value.toString()
                    val cageName = itemSnapshot.child("Cage").value.toString()
                    val cageKeyFemale = itemSnapshot.child("CageKeyFemale").value.toString()
                    val cageKeyMale = itemSnapshot.child("CageKeyMale").value.toString()
                    val cageBirdFemale = itemSnapshot.child("CageKeyFlightFemaleValue").value.toString()
                    val cageBirdMale = itemSnapshot.child("CageKeyFlightMaleValue").value.toString()
                    val male = itemSnapshot.child("Male").value.toString()
                    val female = itemSnapshot.child("Female").value.toString()
                    val maleMutation = itemSnapshot.child("Male Mutation").value.toString()
                    val femaleMutation = itemSnapshot.child("Female Mutation").value.toString()
                    val beginningDate = itemSnapshot.child("Beginning").value.toString()
                    val pairMaleKey = itemSnapshot.child("Male Bird Key").value.toString()
                    val pairFemaleKey = itemSnapshot.child("Female Bird Key").value.toString()
                    val separateDate = itemSnapshot.child("Separate Date").value.toString()
                    val pairMaleFlightKey = itemSnapshot.child("Male Flight Key").value.toString()
                    val pairFemaleFlightKey = itemSnapshot.child("Female Flight Key").value.toString()

                    data.pairId = pairsId
                    data.pairfemaleimg = femalegallery
                    data.pairmaleimg = malegallery
                    data.pairFlightMaleKey = pairMaleFlightKey
                    data.pairFlightFemaleKey = pairFemaleFlightKey
                    data.pairMaleKey = pairMaleKey
                    data.pairFemaleKey = pairFemaleKey
                    data.pairKey = key
                    data.pairFemale = female
                    data.pairMale = male
                    data.pairCage = cageName
                    data.pairMaleMutation = maleMutation
                    data.pairFemaleMutation = femaleMutation
                    data.pairDateBeg = beginningDate
                    data.pairDateSep = separateDate
                    data.paircagebirdFemale =cageBirdFemale
                    data.paircagebirdMale = cageBirdMale
                    data.paircagekeyFemale = cageKeyFemale
                    data.paircagekeyMale = cageKeyMale
                    if (Looper.myLooper() != Looper.getMainLooper()) {
                        Log.d(ContentValues.TAG, "Code is running on a background thread")
                    } else {
                        Log.d(ContentValues.TAG, "Code is running on the main thread")
                    }

                    dataList.add(data)

                }

            }
        }
        dataList.sortBy { it.pairDateBeg }
        dataList
    }

    private suspend fun getDataFromDatabasePrevious(): List<PairData> =
        withContext(Dispatchers.IO) {

            val currentUserId = mAuth.currentUser?.uid
            val db = FirebaseDatabase.getInstance().reference.child("Users")
                .child("ID: ${currentUserId.toString()}").child("Pairs")
            val dataList = ArrayList<PairData>()
            val snapshot = db.get().await()
            for (itemSnapshot in snapshot.children) {
                val data = itemSnapshot.getValue(PairData::class.java)
                if (data != null) {

                    if (itemSnapshot.child("Separate Date").exists()) {
                        femalegallery = itemSnapshot.child("Female Gallery").value.toString()
                        malegallery = itemSnapshot.child("Male Gallery").value.toString()
                        val key = itemSnapshot.key.toString()
                        val pairsId = itemSnapshot.child("Pair ID").value.toString()
                        val cageName = itemSnapshot.child("Cage").value.toString()
                        val cageKeyFemale = itemSnapshot.child("CageKeyFemale").value.toString()
                        val cageKeyMale = itemSnapshot.child("CageKeyMale").value.toString()
                        val cageBirdFemale = itemSnapshot.child("CageKeyFlightFemaleValue").value.toString()
                        val cageBirdMale = itemSnapshot.child("CageKeyFlightMaleValue").value.toString()
                        val male = itemSnapshot.child("Male").value.toString()
                        val female = itemSnapshot.child("Female").value.toString()
                        val maleMutation = itemSnapshot.child("Male Mutation").value.toString()
                        val femaleMutation = itemSnapshot.child("Female Mutation").value.toString()
                        val beginningDate = itemSnapshot.child("Beginning").value.toString()
                        val pairMaleKey = itemSnapshot.child("Male Bird Key").value.toString()
                        val pairFemaleKey = itemSnapshot.child("Female Bird Key").value.toString()
                        val separateDate = itemSnapshot.child("Separate Date").value.toString()

                        data.pairfemaleimg = femalegallery
                        data.pairmaleimg = malegallery
                        data.pairId = pairsId
                        data.pairMaleKey = pairMaleKey
                        data.pairFemaleKey = pairFemaleKey
                        data.pairKey = key
                        data.pairFemale = female
                        data.pairMale = male
                        data.pairCage = cageName
                        data.pairMaleMutation = maleMutation
                        data.pairFemaleMutation = femaleMutation
                        data.pairDateBeg = beginningDate
                        data.pairDateSep = separateDate
                        data.paircagebirdFemale =cageBirdFemale
                        data.paircagebirdMale = cageBirdMale
                        data.paircagekeyFemale = cageKeyFemale
                        data.paircagekeyMale = cageKeyMale

                        if (Looper.myLooper() != Looper.getMainLooper()) {
                            Log.d(ContentValues.TAG, "Code is running on a background thread")
                        } else {
                            Log.d(ContentValues.TAG, "Code is running on the main thread")
                        }

                        dataList.add(data)
                    } else {
                       
                    }
                }

            }

            dataList.sortBy { it.pairDateBeg }
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
                if (!dataList.isEmpty()) {
                    current.visibility = View.VISIBLE
                } else {
                    current.visibility = View.GONE
                }
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error reloading data: ${e.message}")
            }
            finally {
                loadingProgressBar.visibility = View.GONE
            }
        }
        lifecycleScope.launch {
            try {
                val data = getDataFromDatabasePrevious()
                dataList1.clear()
                dataList1.addAll(data)
                adapter1.notifyDataSetChanged()
                if (!dataList1.isEmpty()) {
                    previous.visibility = View.VISIBLE
                } else {
                    previous.visibility = View.GONE
                }
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error reloading data: ${e.message}")
            }
            finally {
                loadingProgressBar.visibility = View.GONE
            }
        }
    }
}
