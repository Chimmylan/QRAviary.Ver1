package com.example.qraviaryapp.activities.detailedactivities

import EggData
import PairData
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.CheckBox
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qraviaryapp.R
import com.example.qraviaryapp.adapter.ClutchesListAdapter
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PairsDetailedActivity : AppCompatActivity() {

    private lateinit var tvDate: TextView
    private lateinit var tvMutations: TextView
    private lateinit var btnMale: MaterialButton
    private lateinit var btnFemale: MaterialButton
    private lateinit var fab: FloatingActionButton
    private lateinit var mAuth: FirebaseAuth

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ClutchesListAdapter
    private lateinit var dataList: ArrayList<EggData>

    private lateinit var pairKey: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.bottom_nav_background)
        }
        setContentView(R.layout.detailed_activity_pairs)

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
            "<font color='$abcolortitle'>Clutch</font>",
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

        val bundle = intent.extras

        mAuth = FirebaseAuth.getInstance()
        dataList = ArrayList()
        val gridLayoutManager = GridLayoutManager(this, 1)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = gridLayoutManager

        adapter = ClutchesListAdapter(this, dataList)
        recyclerView.adapter = adapter

        tvDate = findViewById(R.id.tvfromdate)
        tvMutations = findViewById(R.id.tvmutations)
        btnMale = findViewById(R.id.btnmaleid)
        btnFemale = findViewById(R.id.btnfemaleid)
        fab = findViewById(R.id.fab)


        fab.setOnClickListener {
            showEggDialog()
        }


        //Bundle from PairListActivity
        if (bundle != null) {
            val maleId = bundle.getString("MaleID")
            val femaleId = bundle.getString("FemaleID")
            val beginningDate = bundle.getString("BeginningDate")
            val maleGender = bundle.getString("MaleGender")
            val femaleGender = bundle.getString("FemaleGender")
            pairKey = bundle.getString("PairKey").toString()
            tvDate.text = beginningDate.toString()
            tvMutations.text = "${maleGender.toString()} x ${femaleGender.toString()}"
            btnFemale.text = femaleId.toString()
            btnMale.text = maleId.toString()
        }


        //Coroutine
        lifecycleScope.launch {
            try {
                val data = getDataFromDatabase()
                dataList.addAll(data)
                adapter.notifyDataSetChanged()
            } catch (e: java.lang.Exception) {
                Log.e(ContentValues.TAG, "Error retrieving data: ${e.message}")
            }
        }


        invalidateOptionsMenu()
    }

    //Gets data from database and display it
    private suspend fun getDataFromDatabase(): List<EggData> = withContext(Dispatchers.IO) {
        val currentUserId = mAuth.currentUser?.uid
        val db = FirebaseDatabase.getInstance().reference.child("Users")
            .child("ID: ${currentUserId.toString()}").child("Pairs")
            .child(pairKey).child("Clutches")
        val dataList = ArrayList<EggData>()
        val snapshot = db.get().await()
        for (clutchSnapshot in snapshot.children) {
            val data = clutchSnapshot.getValue(EggData::class.java)
            val key = clutchSnapshot.key.toString()
            var incubatingCount = 0
            var laidCount = 0
            var eggsCount = 0
            if (data != null) {
                for (eggSnapshot in clutchSnapshot.children) {
                    val eggData = eggSnapshot.getValue(EggData::class.java)

                    val eggStatus = eggSnapshot.child("Status").value.toString()
                    val eggDate = eggSnapshot.child("Date").value.toString()
                    eggsCount++
                    if (eggStatus == "Incubating") {

                        incubatingCount++
                        Log.d(TAG, incubatingCount.toString())
                        data.pairKey = pairKey
                        data.eggKey = key
                        data.eggCount = eggsCount.toString()
                        data.eggIncubating = incubatingCount.toString()
                        data.eggIncubationStartDate = eggDate
                    }
                    if (eggStatus == "Laid") {

                        laidCount++
                        Log.d(TAG, laidCount.toString())
                        data.pairKey = pairKey
                        data.eggKey = key
                        data.eggCount = eggsCount.toString()
                        data.eggLaid = laidCount.toString()
                        data.eggLaidStartDate = eggDate

                    }

                }
            }
            if (data != null) {
                dataList.add(data)
            }
        }
        dataList.sortBy { it.eggIncubationStartDate }
        dataList
    }

    private fun showEggDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.showlayout_add_egg, null)
        val numberPicker = dialogLayout.findViewById<NumberPicker>(R.id.numberPicker)
        val checkBox = dialogLayout.findViewById<CheckBox>(R.id.checkbox)
        val currentUserId = mAuth.currentUser?.uid
        val db = FirebaseDatabase.getInstance().reference.child("Users")
            .child("ID: ${currentUserId.toString()}").child("Pairs")
            .child(pairKey).child("Clutches")

        val newClutchRef = db.push()

        numberPicker.minValue = 0
        numberPicker.maxValue = 10

        builder.setTitle("Add Eggs")
        builder.setPositiveButton("Save", null)

        builder.setNegativeButton("Cancel") { dialog, which ->
            Toast.makeText(this@PairsDetailedActivity, "Cancel", Toast.LENGTH_SHORT).show()
        }
        builder.setView(dialogLayout)

        val alertDialog = builder.create()

        alertDialog.setOnShowListener {
            val addButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            var eggValue: Int
            var defaultStatus = "Incubating"
            var currentDate = LocalDate.now()
            var eggCount = 0
            var incubatingCount = 0
            var laidCount = 0
            val formatter = DateTimeFormatter.ofPattern("MM/dd/yy")
            val formattedDate = currentDate.format(formatter)
            addButton.setOnClickListener {
                val newEggs = EggData()
                eggValue = numberPicker.value
                if (checkBox.isChecked) {


                    Toast.makeText(
                        this@PairsDetailedActivity,
                        "Checked $eggValue",
                        Toast.LENGTH_SHORT
                    ).show()


                    for (i in 0 until eggValue) {
                        val clutches = newClutchRef.push()
                        eggCount++
                        incubatingCount++
                        val data: Map<String, Any?> = hashMapOf(
                            "Status" to defaultStatus,
                            "Date" to formattedDate
                        )


                        newEggs.eggCount = eggCount.toString()
                        newEggs.eggIncubating = incubatingCount.toString()
                        newEggs.eggIncubationStartDate = formattedDate

                        clutches.updateChildren(data)

                    }
                    dataList.add(newEggs)
                    adapter.notifyDataSetChanged()

                } else if (!checkBox.isChecked) {
                    Toast.makeText(
                        this@PairsDetailedActivity,
                        "notChecked $eggValue",
                        Toast.LENGTH_SHORT
                    ).show()

                    defaultStatus = "Laid"
                    for (i in 0 until eggValue) {
                        val clutches = newClutchRef.push()
                        eggCount++
                        laidCount++
                        val data: Map<String, Any?> = hashMapOf(
                            "Status" to defaultStatus,
                            "Date" to formattedDate
                        )


                        newEggs.eggCount = eggCount.toString()
                        newEggs.eggLaid = laidCount.toString()
                        newEggs.eggLaidStartDate = formattedDate

                        clutches.updateChildren(data)

                    }
                    dataList.add(newEggs)
                    adapter.notifyDataSetChanged()
                }

                alertDialog.dismiss()
            }

        }

        alertDialog.show()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)



        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_edit -> {
                // Handle the Edit button click here
                // Implement the logic to edit the item or perform any action you need.
                true
            }
            R.id.menu_remove -> {
                // Handle the Remove button click here
                // Implement the logic to remove the item or perform any action you need.
                true
            }
            android.R.id.home -> {
                onBackPressed() // Call this to navigate back to the previous fragment
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
