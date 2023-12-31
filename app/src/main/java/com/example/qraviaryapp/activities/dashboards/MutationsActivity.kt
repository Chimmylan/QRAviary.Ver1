package com.example.qraviaryapp.activities.dashboards

import ClickListener
import MutationData
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.qraviaryapp.R
import com.example.qraviaryapp.adapter.GenesAdapter
import com.example.qraviaryapp.adapter.HomeGenesAdapter
import com.example.qraviaryapp.adapter.StickyHeaderItemDecoration
import com.example.qraviaryapp.adapter.StickyHeaderItemDecoration1
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MutationsActivity : AppCompatActivity(), ClickListener {

    private lateinit var dataList: ArrayList<MutationData>
    private lateinit var recyclerView: RecyclerView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: DatabaseReference
    private lateinit var adapter: GenesAdapter
    private lateinit var totalBirds: TextView
    private lateinit var swipeToRefresh: SwipeRefreshLayout
    private lateinit var loadingProgressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.statusbar)
        }
        setContentView(R.layout.activity_mutations)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 0f
        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    this,
                    R.color.toolbarcolor
                )
            )
        )
        totalBirds = findViewById(R.id.tvBirdCount)
        val abcolortitle = resources.getColor(R.color.appbar)
        supportActionBar?.title = HtmlCompat.fromHtml(
            "<font color='$abcolortitle'>Mutations</font>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        // Check if night mode is enabled
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_white)
        swipeToRefresh = findViewById(R.id.swipeToRefresh)
        loadingProgressBar = findViewById(R.id.loadingProgressBar)
        mAuth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().reference
        recyclerView = findViewById(R.id.recyclerView)
//        val gridLayoutManager = GridLayoutManager(this,1)
//        recyclerView.layoutManager = gridLayoutManager
//        dataList = ArrayList()
//        adapter = GenesAdapter(this,dataList,this)
//        recyclerView.adapter = adapter

        dataList = ArrayList()
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = GenesAdapter(this, dataList,this)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(StickyHeaderItemDecoration1(adapter))

        lifecycleScope.launch {
            try {
                val data = getDataFromDataBase()
                dataList.addAll(data)

                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error retrieving data: ${e.message}")
            }
        }
        refreshApp()

    }
    private fun refreshApp() {
        swipeToRefresh.setOnRefreshListener {
            lifecycleScope.launch(Dispatchers.Main) {
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

            Toast.makeText(this, "Refreshed", Toast.LENGTH_SHORT).show()
        }

    }
    fun fab(view: View) {
        showAddMutationsDialog()
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
                    val mutationIncubatingDays = itemSnapshot.child("Incubating Days").value
                    val mutationMaturingDays = itemSnapshot.child("Maturing Days").value
                    val mutationNameValue = mutationName.toString()
                    data.mutations = mutationNameValue
                    data.mutationsIncubateDays = mutationIncubatingDays.toString()
                    data.mutationsMaturingDays = mutationMaturingDays.toString()

                    if (Looper.myLooper() != Looper.getMainLooper()) {
                        Log.d(ContentValues.TAG, "Code is running on a background thread")
                    } else {
                        Log.d(ContentValues.TAG, "Code is running on the main thread")
                        //
                    }
                    dataList.add(data)
                }
            }
            dataList.sortBy { it.mutations }
            if(dataList.count()>1){
                totalBirds.text = dataList.count().toString() + " Mutations"
            }
            else{
                totalBirds.text = dataList.count().toString() + " Mutation"
            }
            dataList
        }

    fun showAddMutationsDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_mutation, null)

        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(dialogView)
        alertDialogBuilder.setTitle("Add Mutations")

        val alertDialog = alertDialogBuilder.create()

        val currentUserId = mAuth.currentUser?.uid
        val newDb = FirebaseDatabase.getInstance().reference.child("Users")
            .child("ID: ${currentUserId.toString()}").child("Mutations")
        val newMutationRef = newDb.push()
        val mutationNameEditText = dialogView.findViewById<EditText>(R.id.mutationName)


        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)
        val btnSave = dialogView.findViewById<Button>(R.id.btnSave)
        val sharedPreferences = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        val incubatingValue = sharedPreferences.getString("incubatingValue", "21")
        val maturingValue = sharedPreferences.getString("maturingValue", "50")
        editor.apply()
        btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        btnSave.setOnClickListener {
            val mutationNameValue = mutationNameEditText.text.toString()


            if (TextUtils.isEmpty(mutationNameValue)) {
                mutationNameEditText.error = "Enter a name"
                return@setOnClickListener
            }
            val categoryExists = dataList.any { it.mutations.equals(mutationNameValue, ignoreCase = true) }


            if (categoryExists) {
                // Display an error message or handle the case where the category already exists
                mutationNameEditText.error = "Mutation Already Exist"
                return@setOnClickListener
            } else {
                // Handle saving the data as you did before
                val currentUserId = mAuth.currentUser?.uid
                val newDb = FirebaseDatabase.getInstance().reference.child("Users")
                    .child("ID: ${currentUserId.toString()}").child("Mutations")
                val newMutationRef = newDb.push()

                val data: Map<String, Any?> = hashMapOf(
                    "Mutation" to mutationNameValue.capitalize(),
                    "Incubating Days" to incubatingValue?.toInt(),
                    "Maturing Days" to maturingValue?.toInt()
                )

                newMutationRef.updateChildren(data)
                val newMutation = MutationData()
                newMutation.mutations = mutationNameValue.capitalize()

                dataList.add(newMutation)
                adapter.notifyItemInserted(dataList.size - 1)
                dataList.sortBy { it.mutations }
                adapter.notifyDataSetChanged()
                alertDialog.dismiss()
            }

        }

        alertDialog.show()
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

                val data = getDataFromDataBase()
                dataList.clear()
                dataList.addAll(data)

                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error reloading data: ${e.message}")
            } finally {

                loadingProgressBar.visibility = View.GONE
            }
        }}

    private fun getSortedGenesWithHeaders(sortedGenes: Array<String>): List<Pair<String, String>> {
        val genesWithHeaders = mutableListOf<Pair<String, String>>()
        var currentHeader = ""
        for (gene in sortedGenes) {
            val header = gene[0].toUpperCase().toString()
            if (header != currentHeader) {
                genesWithHeaders.add(
                    Pair(
                        header,
                        ""
                    )
                ) // Add a header with an empty gene to show only the header
                currentHeader = header
            }
            genesWithHeaders.add(Pair("", gene)) // Add an empty header with the gene
        }
        return genesWithHeaders
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

    override fun onClick(nameValue: String) {

    }

    override fun onClick(nameValue: String, id: String) {
        TODO("Not yet implemented")
    }

    override fun onClick(nameValue: String, id: String, mutation: String) {
        val intent = Intent()
        intent.putExtra("selectedMutationId", nameValue)
        intent.putExtra("selectedMutationMaturingDays", id)
        intent.putExtra("selectedMutationIncubatingDays", mutation)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }



}