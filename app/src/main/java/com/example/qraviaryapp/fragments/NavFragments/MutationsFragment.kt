package com.example.qraviaryapp.fragments.NavFragments
import MutationData
import android.content.ContentValues
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.qraviaryapp.R
import com.example.qraviaryapp.adapter.HomeGenesAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MutationsFragment : Fragment() {

    private lateinit var dataList: ArrayList<MutationData>
    private lateinit var recyclerView: RecyclerView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: DatabaseReference
    private lateinit var adapter: HomeGenesAdapter
    private lateinit var fab: FloatingActionButton
    private lateinit var snackbar: Snackbar
    private lateinit var connectivityManager: ConnectivityManager
    private var isNetworkAvailable = true
    private lateinit var totalBirds: TextView
    private var mutationCount = 0
    private lateinit var swipeToRefresh: SwipeRefreshLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mutations, container, false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            requireActivity().window.statusBarColor =
                ContextCompat.getColor(requireContext(), R.color.bottom_nav_background)
        }
        swipeToRefresh = view.findViewById(R.id.swipeToRefresh)
        totalBirds = view.findViewById(R.id.tvBirdCount)
        mAuth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().reference
        recyclerView = view.findViewById(R.id.recyclerView)
        fab= view.findViewById(R.id.fab)
        val gridLayoutManager = GridLayoutManager(requireContext(), 1)
        recyclerView.layoutManager = gridLayoutManager
        dataList = ArrayList()
        adapter = HomeGenesAdapter(requireContext(), dataList)
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
            showAddMutationsDialog()
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

                    val data = getDataFromDataBase()
                    dataList.clear()
                    dataList.addAll(data)
                    swipeToRefresh.isRefreshing = false
                    adapter.notifyDataSetChanged()
                } catch (e: Exception) {
                    Log.e(ContentValues.TAG, "Error reloading data: ${e.message}")
                }

            }
        }
    }
    private fun showSnackbar(message: String) {
        snackbar.setText(message)
        snackbar.show()
    }
    private suspend fun getDataFromDataBase(): List<MutationData> =
        withContext(Dispatchers.IO){

            val currentUserId = mAuth.currentUser?.uid
            val db = FirebaseDatabase.getInstance().reference.child("Users")
                .child("ID: ${currentUserId.toString()}").child("Mutations")
            val dataList = ArrayList<MutationData>()
            val snapshot = db.get().await()
            for (itemSnapshot in snapshot.children){
                val data = itemSnapshot.getValue(MutationData::class.java)
                if (data != null){
                    val key = itemSnapshot.key.toString()
                    data.mutationsId = key
                    val mutationName = itemSnapshot.child("Mutation").value
                    val mutationNameValue = mutationName.toString()
                    data.mutations = mutationNameValue
                    mutationCount++
                    data.mutationCount = mutationCount.toString()
                    if (Looper.myLooper() != Looper.getMainLooper()) {
                        Log.d(ContentValues.TAG, "Code is running on a background thread")
                    } else {
                        Log.d(ContentValues.TAG, "Code is running on the main thread")
                        //
                    }
                    dataList.add(data)
                }
            }
            totalBirds.text = "Total Mutations: $mutationCount"
            dataList.sortBy { it.mutations }
            dataList
        }

    fun showAddMutationsDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())

        // Inflate the custom layout
        val inflater = LayoutInflater.from(requireContext())
        val dialogView = inflater.inflate(R.layout.dialog_mutation, null)

        alertDialogBuilder.setView(dialogView)

        // Set the title for the AlertDialog
        alertDialogBuilder.setTitle("Add Mutations")

        val alertDialog = alertDialogBuilder.create()

        // Find your custom EditText fields in the custom layout
        val mutationNameEditText = dialogView.findViewById<EditText>(R.id.mutationName)
        val incubationEditText = dialogView.findViewById<EditText>(R.id.incubation)
        val maturedEditText = dialogView.findViewById<EditText>(R.id.matured)

        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)
        val btnSave = dialogView.findViewById<Button>(R.id.btnSave)

        btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        btnSave.setOnClickListener {
            val mutationNameValue = mutationNameEditText.text.toString()
            val incubationValue = incubationEditText.text.toString()
            val maturedValue = maturedEditText.text.toString()

            if (TextUtils.isEmpty(mutationNameValue)) {
                mutationNameEditText.error = "Enter a name"
            } else {
                // Handle saving the data as you did before
                val currentUserId = mAuth.currentUser?.uid
                val newDb = FirebaseDatabase.getInstance().reference.child("Users")
                    .child("ID: ${currentUserId.toString()}").child("Mutations")
                val newMutationRef = newDb.push()

                val data: Map<String, Any?> = hashMapOf(
                    "Mutation" to mutationNameValue,
                    "Incubating Days" to incubationValue,
                    "Maturing Days" to maturedValue
                )

                newMutationRef.updateChildren(data)
                val newMutation = MutationData()
                newMutation.mutations = mutationNameValue

                dataList.add(newMutation)
                adapter.notifyItemInserted(dataList.size - 1)
                dataList.sortBy { it.mutations }
                adapter.notifyDataSetChanged()
                alertDialog.dismiss()
            }

        }

        alertDialog.show()
    }



    // Move the rest of your code here, including the functions and onOptionsItemSelected
    // Note: Replace "this" with "requireActivity()" where needed
}