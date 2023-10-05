package com.example.qraviaryapp.activities.CagesActivity

import PairData
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

class BreedingListActivity : AppCompatActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_breeding_list)

        mAuth = FirebaseAuth.getInstance()
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView1 = findViewById(R.id.recyclerView1)
        val gridLayoutManager = GridLayoutManager(this, 1)
        recyclerView.layoutManager = gridLayoutManager

        val gridLayoutManager1 = GridLayoutManager(this, 1)
        recyclerView1.layoutManager = gridLayoutManager1
        dataList1 = ArrayList()
        adapter1 = PreviousPairAdapter(this, dataList1)
        recyclerView1.adapter = adapter1
        dataList = ArrayList()
        adapter = PairListAdapter(this, dataList)
        recyclerView.adapter = adapter
        current = findViewById(R.id.tvCurrent)
        previous = findViewById(R.id.tvPrevious)

        fab = findViewById(R.id.fab)
        fab.setOnClickListener {
            startActivity(Intent(this, AddPairActivity::class.java))
        }

        snackbar = Snackbar.make(findViewById(android.R.id.content), "", Snackbar.LENGTH_LONG)
        connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

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
    }

    override fun onResume() {
        super.onResume()

        // Call a function to reload data from the database and update the RecyclerView
        reloadDataFromDatabase()
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
                    val key = itemSnapshot.key.toString()
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

                        val key = itemSnapshot.key.toString()
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

    private fun reloadDataFromDatabase() {
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
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            android.R.id.home -> {
                onBackPressed() // Call this to navigate back to the previous fragment
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}