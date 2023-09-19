package com.example.qraviaryapp.activities.detailedactivities

import EggData
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qraviaryapp.R
import com.example.qraviaryapp.adapter.EggClutchesListAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ClutchesDetailedActivity : AppCompatActivity() {


    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: DatabaseReference
    private lateinit var adapter: EggClutchesListAdapter
    private lateinit var dataList: ArrayList<EggData>
    private lateinit var recyclerView: RecyclerView

    private lateinit var eggKey: String
    private lateinit var pairKey: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.bottom_nav_background)
        }
        setContentView(R.layout.detailed_activity_clutches)

        supportActionoBar()

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().reference
        dataList = ArrayList()
        adapter = EggClutchesListAdapter(this, dataList)
        recyclerView = findViewById(R.id.recyclerView)
        val gridLayoutManager = GridLayoutManager(this, 1)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = adapter

        val bundle = intent.extras

        if (bundle != null){
            eggKey = bundle.getString("EggKey").toString()
            pairKey = bundle.getString("PairKey").toString()
        }

        lifecycleScope.launch{
            try {
                val data = getDataFromDatabase()
                dataList.addAll(data)
                adapter.notifyDataSetChanged()
            }catch (e: java.lang.Exception){
                Log.e(ContentValues.TAG, "Error retrieving data: ${e.message}")
            }
        }

    }


    private suspend fun getDataFromDatabase(): List<EggData> = withContext(Dispatchers.IO){

        val currenUserId = mAuth.currentUser?.uid
        db = FirebaseDatabase.getInstance().reference.child("Users")
            .child("ID: ${currenUserId.toString()}").child("Pairs")
            .child(pairKey).child("Clutches").child(eggKey)
        val dataList = ArrayList<EggData>()
        val snapshot = db.get().await()
        Log.d(TAG,"Egg key: $eggKey")
        Log.d(TAG,"Pair key: $pairKey")
        for (eggSnapshot in snapshot.children){
            val data = eggSnapshot.getValue(EggData::class.java)
            if (data != null){

                val statusValue = eggSnapshot.child("Status").value.toString()
                val dateValue = eggSnapshot.child("Date").value.toString()

                if (statusValue == "Incubating"){
                    data.eggIncubating = statusValue
                    data.eggIncubationStartDate = dateValue
                }
                if (statusValue == "Laid"){
                    data.eggLaid = statusValue
                    data.eggLaidStartDate = dateValue
                }

                data.eggStatus = statusValue
                data.eggDate = dateValue

                dataList.add(data)
            }
        }

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

    fun supportActionoBar() {
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
    }

}