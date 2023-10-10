package com.example.qraviaryapp.fragments.NavFragments

import ExpensesData
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.AddActivities.AddExpensesActivity
import com.example.qraviaryapp.adapter.DetailedAdapter.ExpensesAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ExpenseFragment : Fragment() {

    private lateinit var dataList: ArrayList<ExpensesData>
    private lateinit var recyclerView: RecyclerView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: DatabaseReference
    private lateinit var adapter: ExpensesAdapter
    private lateinit var fab: FloatingActionButton
    private lateinit var snackbar: Snackbar
    private lateinit var connectivityManager: ConnectivityManager
    private var isNetworkAvailable = true
    private lateinit var totalBirds: TextView
    private var expensesCount = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_expense, container, false)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().reference
        fab = view.findViewById(R.id.fab)
        recyclerView = view.findViewById(R.id.recyclerView)
        val gridLayoutManager = GridLayoutManager(requireContext(), 1)
        recyclerView.layoutManager = gridLayoutManager
        dataList = ArrayList()
        adapter = ExpensesAdapter(requireContext(), dataList)
        recyclerView.adapter = adapter

        lifecycleScope.launch {
            try {
                val data = getDataFromDataBase()
                dataList.addAll(data)

                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error retrieving data: ${e.message}")
            }
        }

        fab.setOnClickListener {
            val i = Intent(requireContext(), AddExpensesActivity::class.java)
            startActivity(i)
        }
        totalBirds = view.findViewById(R.id.tvBirdCount)
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
    private suspend fun getDataFromDataBase(): List<ExpensesData> =
        withContext(Dispatchers.IO){

            val currentUserId = mAuth.currentUser?.uid
            val db = FirebaseDatabase.getInstance().reference.child("Users")
                .child("ID: ${currentUserId.toString()}").child("Expenses")
            val dataList = ArrayList<ExpensesData>()
            val snapshot = db.get().await()
            for (itemSnapshot in snapshot.children){
                val data = itemSnapshot.getValue(ExpensesData::class.java)
                if (data != null){
                    val key = itemSnapshot.key.toString()
                    data.expensesId = key
                    val mutationName = itemSnapshot.child("Category").value
                    val PriceName = itemSnapshot.child("Amount").value
                    val date = itemSnapshot.child("Beginning").value
                    val comment = itemSnapshot.child("Comment").value
                    val mutationNameValue = mutationName.toString()
                    val priceNameValue = PriceName.toString()
                    val dateValue = date.toString()
                    val commentValue = comment.toString()
                    expensesCount = snapshot.childrenCount.toInt()
                    data.expensesCount = expensesCount.toString()
                    data.expenses = mutationNameValue
                    data.price = priceNameValue
                    data.expensesComment = commentValue
                    data.expensesDate = dateValue
                    if (Looper.myLooper() != Looper.getMainLooper()) {
                        Log.d(ContentValues.TAG, "Code is running on a background thread")
                    } else {
                        Log.d(ContentValues.TAG, "Code is running on the main thread")
                        //
                    }
                    dataList.add(data)
                }
            }
            totalBirds.text = "Total Expenses: $expensesCount"
            dataList.sortBy { it.expenses }
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
                val data = getDataFromDataBase()
                dataList.clear()
                dataList.addAll(data)
                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error reloading data: ${e.message}")
            }
        }
    }
    // Move the rest of your code here, including the functions and onOptionsItemSelected
    // Note: Replace "this" with "requireActivity()" where needed
}