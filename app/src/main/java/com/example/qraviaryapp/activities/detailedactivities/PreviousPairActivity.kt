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
import androidx.viewpager.widget.ViewPager
import com.example.qraviaryapp.R

import com.example.qraviaryapp.adapter.ClutchesListAdapter
import com.example.qraviaryapp.adapter.FragmentAdapter
import com.example.qraviaryapp.fragments.Pairs.ClutchesFragment
import com.example.qraviaryapp.fragments.Pairs.DescendantsFragment
import com.example.qraviaryapp.fragments.Pairs.PreviousClutchesFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
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

class PreviousPairActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager
    private lateinit var tablayout: TabLayout
    val fragmentAdapter = FragmentAdapter(supportFragmentManager)
    private lateinit var tvDate: TextView
    private lateinit var tvMutations: TextView
    private lateinit var btnMale: MaterialButton
    private lateinit var btnFemale: MaterialButton
    private lateinit var fab: FloatingActionButton
    private lateinit var mAuth: FirebaseAuth

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ClutchesListAdapter
    private lateinit var dataList: ArrayList<EggData>
    private lateinit var pairId: String
    private lateinit var pairKey: String
    private lateinit var pairMaleKey: String
    private lateinit var pairFlightMaleKey: String
    private lateinit var pairFemaleKey: String
    private lateinit var pairFlightFemaleKey: String
    private lateinit var pairMale: String
    private lateinit var pairFemale: String
    private lateinit var pairCageKeyMale: String
    private lateinit var pairCageKeyFemale: String
    private lateinit var pairCageBirdMale: String
    private lateinit var pairCageBirdFemale: String
    private lateinit var currentUserId: String
    //    private lateinit var pairfemaleimg: String
//    private lateinit var pairmaleimg: String
//    private lateinit var totalclutch: TextView
    private var clutchCount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.statusbar)
        }
        setContentView(R.layout.activity_previous_pair)

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
//        totalclutch =findViewById(R.id.tvBirdCount)

        // Check if night mode is enabled
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_white)

        val bundle = intent.extras

        mAuth = FirebaseAuth.getInstance()
        dataList = ArrayList()
        val gridLayoutManager = GridLayoutManager(this, 1)
//        recyclerView = findViewById(R.id.recyclerView)
//        recyclerView.layoutManager = gridLayoutManager

//        adapter = ClutchesListAdapter(this, dataList)
//        recyclerView.adapter = adapter

        tvDate = findViewById(R.id.tvfromdate)
        tvMutations = findViewById(R.id.tvmutations)
        btnMale = findViewById(R.id.btnmaleid)
        btnFemale = findViewById(R.id.btnfemaleid)

        viewPager = findViewById(R.id.viewPager)
        tablayout = findViewById(R.id.tablayout)



        val newBundle = Bundle()
        //Bundle from PairListActivity
        if (bundle != null) {
//            pairfemaleimg = bundle.getString("PairFemaleImg").toString()
//            pairmaleimg = bundle.getString("PairMaleImg").toString()
            pairId = bundle.getString("PairId").toString()
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
            pairCageBirdFemale = bundle.getString("CageBirdFemale").toString()
            pairCageBirdMale = bundle.getString("CageBirdFemale").toString()
            pairCageKeyFemale = bundle.getString("CageKeyFemale").toString()
            pairCageKeyMale = bundle.getString("CageKeyMale").toString()

            newBundle.putString("PairId", pairId)
            newBundle.putString("MaleID", pairMale)
            newBundle.putString("FemaleID", pairFemale)
            newBundle.putString("BeginningDate", beginningDate)
            newBundle.putString("SeparateDate", separateDate)
            newBundle.putString("MaleGender", maleGender)
            newBundle.putString("FemaleGender", femaleGender)
            newBundle.putString("PairFlightFemaleKey", pairFlightFemaleKey)
            newBundle.putString("PairFlightMaleKey", pairFlightMaleKey)
            newBundle.putString("PairMaleKey", pairMaleKey)
            newBundle.putString("PairFemaleKey", pairFemaleKey)
            newBundle.putString("PairKey", pairKey)
            newBundle.putString("CageBirdFemale", pairCageBirdFemale)
            newBundle.putString("CageBirdMale", pairCageBirdMale)
            newBundle.putString("CageKeyFemale", pairCageKeyFemale)
            newBundle.putString("CageKeyMale", pairCageKeyMale)
            val clutchesFragment = PreviousClutchesFragment() // Create an instance of ClutchesFragment
            val descendantsFragment = DescendantsFragment() // Create an instance of DescendantsFragment
            clutchesFragment.arguments = newBundle
            fragmentAdapter.addFragment(clutchesFragment, "CLUTCHES")
            fragmentAdapter.addFragment(descendantsFragment, "DESCENDANTS")

            viewPager.adapter = fragmentAdapter
            tablayout.setupWithViewPager(viewPager)

            currentUserId = mAuth.currentUser?.uid.toString()
//            val db = FirebaseDatabase.getInstance().reference.child("Users")
//                .child("ID: ${currentUserId.toString()}").child("Pairs")
//                .child(pairKey)

            val abcolortitle = resources.getColor(R.color.appbar)
            supportActionBar?.title = HtmlCompat.fromHtml(
                "<font color='$abcolortitle'>Pair</font>",
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            currentUserId = mAuth.currentUser?.uid.toString()
            val db = FirebaseDatabase.getInstance().reference.child("Users")
                .child("ID: ${currentUserId.toString()}").child("Pairs")
                .child(pairKey)
            db.addListenerForSingleValueEvent(object : ValueEventListener {
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



        invalidateOptionsMenu()
    }

    //Gets data from database and display it




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





}