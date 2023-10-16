package com.example.qraviaryapp.activities.dashboards

import CageData
import android.content.ContentValues
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qraviaryapp.R
import com.example.qraviaryapp.adapter.CageListAdapter2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class BreedingCagesList2Activity : AppCompatActivity(){

    private lateinit var recyclerView: RecyclerView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: DatabaseReference
    private lateinit var dataList: ArrayList<CageData>
    private lateinit var adapter: CageListAdapter2
    private lateinit var fabBtn: FloatingActionButton
    private lateinit var editText: EditText
    private lateinit var totalBirds: TextView
    private var cageCount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.statusbar)
        }
        setContentView(R.layout.activity_cages_list)

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
        val abcolortitle = resources.getColor(R.color.appbar)
        supportActionBar?.title = HtmlCompat.fromHtml(
            "<font color='$abcolortitle'>Breeding Cages</font>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        // Check if night mode is enabled
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_white)
        totalBirds = findViewById(R.id.tvBirdCount)
        recyclerView = findViewById(R.id.cageRecyclerView)
        val gridLayoutManager = GridLayoutManager(this, 1)
        recyclerView.layoutManager = gridLayoutManager
        dataList = ArrayList()
        adapter = CageListAdapter2(this, dataList)
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


        val currentUserId = mAuth.currentUser?.uid
        val db = FirebaseDatabase.getInstance().reference.child("Users")
            .child("ID: ${currentUserId.toString()}").child("Cages")
            .child("Breeding Cages")

        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.cage_add_showlayout, null)
        editText = dialogLayout.findViewById<EditText>(R.id.etAddCage)
        val btncustom = dialogLayout.findViewById<Button>(R.id.custom)
        val btnclose = dialogLayout.findViewById<Button>(R.id.close)

        // Initially, show the EditText and btncustom

        btncustom.visibility = View.VISIBLE

        btncustom.setOnClickListener {
            // When custom button is clicked, hide EditText and btncustom, and show btnclose
            editText.visibility = View.VISIBLE
            btncustom.visibility = View.GONE
            btnclose.visibility = View.VISIBLE
        }

        btnclose.setOnClickListener {
            // When close button is clicked, hide btnclose and show EditText and btncustom
            btnclose.visibility = View.GONE
            editText.visibility = View.GONE
            btncustom.visibility = View.VISIBLE
        }

        builder.setTitle("Add Cage")
        builder.setPositiveButton("Add", null)

        builder.setNegativeButton("Cancel") { dialog, which ->
            Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show()
        }
        builder.setView(dialogLayout)

        val alertDialog = builder.create()
        alertDialog.setOnShowListener {
            val addButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            addButton.setOnClickListener {
                val newCageNumber = editText.text.toString().trim()

                if (newCageNumber.isNotEmpty()) {
                    // User entered a custom cage name, use it
                    val data: Map<String, Any?> = hashMapOf(
                        "Cage" to newCageNumber.toInt()
                    )
                    db.push().updateChildren(data)

                    alertDialog.dismiss()

                    val newCage = CageData()
                    newCage.cage = newCageNumber
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
                }else{
                    // Find the highest numbered cage and increment it
                    db.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {


                            var maxCageNumber = 0

                            for (cageSnapshot in snapshot.children) {
                                val cageNumber =
                                    cageSnapshot.child("Cage").getValue(Int::class.java)
                                if (cageNumber != null && cageNumber > maxCageNumber) {
                                    maxCageNumber = cageNumber
                                }
                            }

                            // Increment the cage number
                            val newCageNumber = maxCageNumber + 1

                            // Create a new cage with the incremented number
                            val data: Map<String, Any?> = hashMapOf(
                                "Cage" to newCageNumber
                            )

                            db.push().updateChildren(data)

                            val newCage = CageData()
                            newCage.cage = newCageNumber.toString()
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

                        override fun onCancelled(error: DatabaseError) {
                            // Handle error
                        }
                    })
                }
            }
            val closeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            closeButton.setOnClickListener {
                alertDialog.dismiss()
            }
        }
        alertDialog.show()

    }



    private suspend fun getDataFromDataBase(): List<CageData> = withContext(Dispatchers.IO) {

        val currentUserId = mAuth.currentUser?.uid
        val db = FirebaseDatabase.getInstance().reference.child("Users")
            .child("ID: ${currentUserId.toString()}").child("Cages").child("Breeding Cages")
        val dataList = ArrayList<CageData>()
        val snapshot = db.get().await()
        for (itemSnapshot in snapshot.children) {
            val data = itemSnapshot.getValue(CageData::class.java)
            if (data != null) {


                val birds = itemSnapshot.child("Pair Birds")
                for (birdSnapshot in birds.children){

                    val pairCount = birds.childrenCount
                    val birdsCount = pairCount * 2
                    data.cagePairBirdCount = pairCount.toString()
                    data.cageBirdCount = birdsCount.toString()

                }
                val key = itemSnapshot.key.toString()

                val cageName = itemSnapshot.child("Cage").value
                val cageNameValue = cageName.toString()



                data.cage = cageNameValue
                data.cageId = key
                cageCount++
                data.cageCount = cageCount.toString()
                if (Looper.myLooper() != Looper.getMainLooper()) {
                    Log.d(ContentValues.TAG, "Code is running on a background thread")
                } else {
                    Log.d(ContentValues.TAG, "Code is running on the main thread")
                    //
                }
                dataList.add(data)
            }

        }
        totalBirds.text = "Total Cages: $cageCount"
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

}