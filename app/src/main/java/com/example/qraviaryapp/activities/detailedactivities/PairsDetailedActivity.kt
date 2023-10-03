package com.example.qraviaryapp.activities.detailedactivities

import EggData
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

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
    private lateinit var pairMaleKey: String
    private lateinit var pairFlightMaleKey: String
    private lateinit var pairFemaleKey: String
    private lateinit var pairFlightFemaleKey: String
    private lateinit var pairMale: String
    private lateinit var pairFemale: String

    private lateinit var currentUserId: String
    private lateinit var totalclutch: TextView
    private var clutchCount = 0
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
        totalclutch =findViewById(R.id.tvBirdCount)
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
            pairMale = bundle.getString("MaleID").toString()
            pairFemale = bundle.getString("FemaleID").toString()
            val beginningDate = bundle.getString("BeginningDate")
            val separateDate = bundle.getString("SeparateDate")
            val maleGender = bundle.getString("MaleGender")
            val femaleGender = bundle.getString("FemaleGender")
            pairFlightFemaleKey = bundle.getString("PairFlightFemaleKey").toString()
            pairFlightMaleKey = bundle.getString("PairFlightMaleKey").toString()
            pairFemaleKey = bundle.getString("PairFemaleKey").toString()
            pairMaleKey = bundle.getString("PairMaleKey").toString()
            pairKey = bundle.getString("PairKey").toString()

            currentUserId = mAuth.currentUser?.uid.toString()
            val db = FirebaseDatabase.getInstance().reference.child("Users")
                .child("ID: ${currentUserId.toString()}").child("Pairs")
                .child(pairKey)


            db.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.child("Separate Date").exists()){
                        tvDate.text = "${beginningDate.toString()} - ${separateDate.toString()}"
                    }else
                    {
                        tvDate.text = beginningDate.toString()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })



            tvMutations.text = "${maleGender.toString()} x ${femaleGender.toString()}"
            btnFemale.text = pairFemale.toString()
            btnMale.text = pairMale.toString()
        }


        //Coroutine
        lifecycleScope.launch {
            try {
                val data = getDataFromDatabase()
                dataList.clear()
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
            var hatchedCount = 0
            var notFertilizedCount = 0
            var brokenCount = 0
            var abandonCount = 0
            var deadInShellCount = 0
            var deadBeforeMovingToNurseryCount = 0
            var eggsCount = 0

            if (data != null) {
                for (eggSnapshot in clutchSnapshot.children) {
                    val eggData = eggSnapshot.getValue(EggData::class.java)

                    val eggStatus = eggSnapshot.child("Status").value.toString()
                    val eggDate = eggSnapshot.child("Date").value.toString()
                    eggsCount++

                    clutchCount = snapshot.childrenCount.toInt()
                    data.clutchCount = clutchCount.toString()
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
                    if (eggStatus == "Hatched") {

                        hatchedCount++
                        Log.d(TAG, laidCount.toString())
                        data.pairKey = pairKey
                        data.eggKey = key
                        data.eggCount = eggsCount.toString()
                        data.eggHatched = hatchedCount.toString()
                        data.eggLaidStartDate = eggDate

                    }
                    if (eggStatus == "Not Fertilized") {

                        notFertilizedCount++
                        Log.d(TAG, notFertilizedCount.toString())
                        data.pairKey = pairKey
                        data.eggKey = key
                        data.eggCount = eggsCount.toString()
                        data.eggNotFertilized = notFertilizedCount.toString()
                        data.eggLaidStartDate = eggDate

                    }
                    if (eggStatus == "Broken") {

                        brokenCount++
                        Log.d(TAG, brokenCount.toString())
                        data.pairKey = pairKey
                        data.eggKey = key
                        data.eggCount = eggsCount.toString()
                        data.eggBroken = brokenCount.toString()
                        data.eggLaidStartDate = eggDate

                    }
                    if (eggStatus == "Abandon") {

                        abandonCount++
                        Log.d(TAG, abandonCount.toString())
                        data.pairKey = pairKey
                        data.eggKey = key
                        data.eggCount = eggsCount.toString()
                        data.eggAbandon = abandonCount.toString()
                        data.eggLaidStartDate = eggDate

                    }
                    if (eggStatus == "Dead in Shell") {

                        deadInShellCount++
                        Log.d(TAG, laidCount.toString())
                        data.pairKey = pairKey
                        data.eggKey = key
                        data.eggCount = eggsCount.toString()
                        data.eggDeadInShell = deadInShellCount.toString()
                        data.eggLaidStartDate = eggDate

                    }
                    if (eggStatus == "Dead Before Moving To Nursery") {

                        deadBeforeMovingToNurseryCount++
                        Log.d(TAG, laidCount.toString())
                        data.pairKey = pairKey
                        data.eggKey = key
                        data.eggCount = eggsCount.toString()
                        data.eggDeadBeforeMovingToNursery = deadBeforeMovingToNurseryCount.toString()
                        data.eggLaidStartDate = eggDate

                    }


                    data.pairFlightMaleKey = pairFlightMaleKey
                    data.pairFlightFemaleKey = pairFlightFemaleKey
                    data.pairBirdFemaleKey = pairFemaleKey
                    data.pairBirdMaleKey = pairMaleKey
                    data.pairFemaleId = pairFemale
                    data.pairMaleId = pairMale

                }
            }

            if (data != null) {
                dataList.add(data)
            }

        }
        totalclutch.text = "Total Clutch: $clutchCount"
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

        checkBox.isChecked = true


        val sharedPrefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val maturingValue = sharedPrefs.getString("maturingValue", "50") // Default to 50 if not set
        val maturingDays = maturingValue?.toIntOrNull() ?: 50

        val incubatingValue = sharedPrefs.getString("incubatingValue", "21")
        val incubatingDays = incubatingValue?.toIntOrNull() ?: 21



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
            val formatter = DateTimeFormatter.ofPattern("MMM d yyyy", Locale.US)
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
                            "Date" to formattedDate,
                            "Incubating Days" to incubatingDays,
                            "Maturing Days" to maturingDays
                        )

                        newEggs.eggCount = eggCount.toString()
                        newEggs.eggIncubating = incubatingCount.toString()
                        newEggs.eggIncubationStartDate = formattedDate

                        clutches.updateChildren(data)

                    }
                    dataList.add(newEggs)


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
                            "Date" to formattedDate,
                            "Incubating Days" to incubatingDays,
                            "Maturing Days" to maturingDays
                        )

                        newEggs.eggCount = eggCount.toString()
                        newEggs.eggLaid = laidCount.toString()
                        newEggs.eggLaidStartDate = formattedDate

                        clutches.updateChildren(data)

                    }
                    dataList.add(newEggs)

                }
                lifecycleScope.launch {
                    try {
                        val data = getDataFromDatabase()
                        dataList.clear()
                        dataList.addAll(data)
                        adapter.notifyDataSetChanged()
                    } catch (e: java.lang.Exception) {
                        Log.e(ContentValues.TAG, "Error retrieving data: ${e.message}")
                    }
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

                true
            }

            R.id.menu_seperate ->{
                showSeparateConfirmation()
                true
            }

            R.id.menu_remove -> {
                showDeleteConfirmation()
                true
            }

            android.R.id.home -> {
                onBackPressed() // Call this to navigate back to the previous fragment
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun showDeleteConfirmation() {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Delete")
        builder.setMessage("Are you sure you want to delete this pair?")
        builder.setPositiveButton("Yes") { _, _ ->
            delete()
            onBackPressed()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog: android.app.AlertDialog = builder.create()
        dialog.show()
    }
    fun delete() {
        val currentUserId = mAuth.currentUser?.uid
        val database = FirebaseDatabase.getInstance().reference.child("Users").child("ID: $currentUserId").child("Pairs")
            .child(pairKey).removeValue()
        }
    private fun showSeparateConfirmation() {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Separate")
        builder.setMessage("Are you sure you want to separate this pair?")
        builder.setPositiveButton("Yes") { _, _ ->
            separate()
            onBackPressed()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog: android.app.AlertDialog = builder.create()
        dialog.show()
    }

    fun separate(){
        val pairMaleRef = FirebaseDatabase.getInstance().reference.child("Users").child("ID: $currentUserId").child("Birds")
            .child(pairMaleKey).child("Status").setValue("Available")
        val pairFemaleRef = FirebaseDatabase.getInstance().reference.child("Users").child("ID: $currentUserId").child("Birds")
            .child(pairFemaleKey).child("Status").setValue("Available")

        val milliSecons = System.currentTimeMillis()
        val DateFormat = SimpleDateFormat("MMM dd yyyy", Locale.US)
        val date = Date(milliSecons)
        val formattedDate = DateFormat.format(date)

        val database = FirebaseDatabase.getInstance().reference.child("Users").child("ID: $currentUserId").child("Pairs")
            .child(pairKey).child("Separate Date").setValue(formattedDate)

    }


    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            try {
                val data = getDataFromDatabase()
                dataList.clear()
                dataList.addAll(data)
                adapter.notifyDataSetChanged()
            } catch (e: java.lang.Exception) {
                Log.e(ContentValues.TAG, "Error retrieving data: ${e.message}")
            }
        }
    }


}
