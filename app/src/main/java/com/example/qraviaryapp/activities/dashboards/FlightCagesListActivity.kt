package com.example.qraviaryapp.activities.dashboards

import CageData
import ClickListener
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qraviaryapp.R
import com.example.qraviaryapp.adapter.CageListAdapter
import com.example.qraviaryapp.adapter.FlightCageListAdapter
import com.example.qraviaryapp.adapter.NurseryCageListAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FlightCagesListActivity : AppCompatActivity(), ClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: DatabaseReference
    private lateinit var dataList: ArrayList<CageData>
    private lateinit var adapter: FlightCageListAdapter
    private lateinit var fabBtn: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.bottom_nav_background)
        }
        setContentView(R.layout.activity_cages_list)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    this,
                    R.color.new_appbar_color
                )
            )
        )
        val abcolortitle = resources.getColor(R.color.appbar)
        supportActionBar?.title = HtmlCompat.fromHtml(
            "<font color='$abcolortitle'>Flight Cages</font>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        // Check if night mode is enabled
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            // Set the white back button for night mode
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_white)
        } else {
            // Set the black back button for non-night mode
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_black)
        }

        recyclerView = findViewById(R.id.cageRecyclerView)
        val gridLayoutManager = GridLayoutManager(this, 1)
        recyclerView.layoutManager = gridLayoutManager
        dataList = ArrayList()
        adapter = FlightCageListAdapter(this, dataList, this)
        recyclerView.adapter = adapter

        mAuth = FirebaseAuth.getInstance()

        fabBtn = findViewById(R.id.fabCage)

        fabBtn.setOnClickListener {
            showCageAddDialog()
        }

        lifecycleScope.launch {
            try {
                val data = getDataFromDataBase()
                dataList.clear()
                dataList.addAll(data)
                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error retrieving data: ${e.message}")
            }
        }
    }

//    override fun onResume() {
//        super.onResume()
//        lifecycleScope.launch {
//            try {
//                val data = getDataFromDataBase()
//                dataList.clear()
//                dataList.addAll(data)
//                adapter.notifyDataSetChanged()
//            } catch (e: Exception) {
//                Log.e(ContentValues.TAG, "Error retrieving data: ${e.message}")
//            }
//        }
//    }

    private fun showCageAddDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.cage_add_showlayout, null)
        val editText = dialogLayout.findViewById<EditText>(R.id.etAddCage)
        val currentUserId = mAuth.currentUser?.uid
        val db = FirebaseDatabase.getInstance().reference.child("Users")
            .child("ID: ${currentUserId.toString()}").child("Cages")
            .child("Flight Cages")
        val newCageRef = db.push()


        builder.setTitle("Add Cage")
        builder.setPositiveButton("Add", null)

        builder.setNegativeButton("Cancel") { dialog, which ->
            Toast.makeText(this@FlightCagesListActivity, "Cancel", Toast.LENGTH_SHORT).show()
        }
        builder.setView(dialogLayout)

        val alertDialog = builder.create()

        alertDialog.setOnShowListener{

            val addButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            addButton.setOnClickListener {
                val cageName = editText.text.toString()
                if (TextUtils.isEmpty(cageName)) {
                    editText.error = "Enter cage name"
                    Toast.makeText(
                        this@FlightCagesListActivity,
                        "Please enter cage name before adding",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val data: Map<String, Any?> = hashMapOf(
                        "Cage" to cageName
                    )
                    newCageRef.updateChildren(data)
                    val newCage = CageData()
                    newCage.cage = cageName
                    dataList.add(newCage)
                    adapter.notifyItemInserted(dataList.size - 1)
                    dataList.sortBy { it.cage }
                    lifecycleScope.launch {
                        try {
                            val data = getDataFromDataBase()
                            dataList.clear()
                            dataList.addAll(data)
                            adapter.notifyDataSetChanged()
                        } catch (e: Exception) {
                            Log.e(ContentValues.TAG, "Error retrieving data: ${e.message}")
                        }
                    }


                    alertDialog.dismiss()
                }
            }

        }

        alertDialog.show()

    }



    private suspend fun getDataFromDataBase(): List<CageData> = withContext(Dispatchers.IO) {

        val currentUserId = mAuth.currentUser?.uid
        val db = FirebaseDatabase.getInstance().reference.child("Users")
            .child("ID: ${currentUserId.toString()}").child("Cages").child("Flight Cages")
        val dataList = ArrayList<CageData>()
        val snapshot = db.get().await()
        for (itemSnapshot in snapshot.children) {
            val data = itemSnapshot.getValue(CageData::class.java)
            if (data != null) {
                val key = itemSnapshot.key.toString()
                data.cageId = key
                val cageName = itemSnapshot.child("Cage").value
                val cageNameValue = cageName.toString()
                data.cage = cageNameValue

                if (Looper.myLooper() != Looper.getMainLooper()) {
                    Log.d(ContentValues.TAG, "Code is running on a background thread")
                } else {
                    Log.d(ContentValues.TAG, "Code is running on the main thread")
                    //
                }
                dataList.add(data)
            }

        }
        dataList.sortBy { it.cage }
        dataList
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
        val intent = Intent()
        intent.putExtra("selectedCageId", nameValue)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onClick(nameValue: String, id: String) {
        TODO("Not yet implemented")
    }

    override fun onClick(nameValue: String, id: String, mutation: String) {
        TODO("Not yet implemented")
    }
}