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
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.AddActivities.AddPairActivity
import com.example.qraviaryapp.adapter.PairListAdapter
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
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PairListAdapter
    private lateinit var fab: FloatingActionButton
    private lateinit var snackbar: Snackbar
    private lateinit var connectivityManager: ConnectivityManager
    private var isNetworkAvailable = true
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pairs, container, false)
        fab = view.findViewById(R.id.fab)
        mAuth = FirebaseAuth.getInstance()
        recyclerView = view.findViewById(R.id.recyclerView)
        val gridLayoutManager = GridLayoutManager(requireContext(), 1)
        recyclerView.layoutManager = gridLayoutManager
        dataList = ArrayList()
        adapter = PairListAdapter(requireContext(), dataList)
        recyclerView.adapter = adapter

        lifecycleScope.launch {
            try {
                val data = getDataFromDatabase()
                dataList.addAll(data)
                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error retrieving data: ${e.message}")
            }
        }
        fab.setOnClickListener{
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



        return view
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
                val key = itemSnapshot.key.toString()
                val cageName = itemSnapshot.child("Cage").value.toString()
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

                if (Looper.myLooper() != Looper.getMainLooper()) {
                    Log.d(ContentValues.TAG, "Code is running on a background thread")
                } else {
                    Log.d(ContentValues.TAG, "Code is running on the main thread")
                }

                dataList.add(data)
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
        lifecycleScope.launch {
            try {
                val data = getDataFromDatabase()
                dataList.clear()
                dataList.addAll(data)
                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error reloading data: ${e.message}")
            }
        }
    }
}
