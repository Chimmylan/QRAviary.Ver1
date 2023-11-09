package com.example.qraviaryapp.activities.AddActivities

import CageData
import ClickListener
import PairData
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.util.Log
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
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.qraviaryapp.R
import com.example.qraviaryapp.adapter.CageListAdapter
import com.example.qraviaryapp.adapter.FosterAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class GiveFosterActivity : AppCompatActivity(), ClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: DatabaseReference
    private lateinit var dataList: ArrayList<PairData>
    private lateinit var adapter: FosterAdapter
    private lateinit var swipeToRefresh: SwipeRefreshLayout
    private lateinit var totalBirds: TextView
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var current: TextView
    private lateinit var CageKey: String
    private lateinit var PairKey: String
    private lateinit var ClutchKey: String
    private lateinit var CageName: String
    private lateinit var CageQR: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.statusbar)
        }
        setContentView(R.layout.activity_give_foster)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 4f
        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    this,
                    R.color.toolbarcolor
                )
            )
        )
        loadingProgressBar = findViewById(R.id.loadingProgressBar)
        totalBirds = findViewById(R.id.tvBirdCount)
        val abcolortitle = resources.getColor(R.color.appbar)
        supportActionBar?.title = HtmlCompat.fromHtml(
            "<font color='$abcolortitle'>Give to Foster Pair</font>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        // Check if night mode is enabled
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_white)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_white)
        swipeToRefresh = findViewById(R.id.swipeToRefresh)
        recyclerView = findViewById(R.id.cageRecyclerView)
        val gridLayoutManager = GridLayoutManager(this, 1)
        recyclerView.layoutManager = gridLayoutManager
        dataList = ArrayList()
        adapter = FosterAdapter(this, dataList, this)
        recyclerView.adapter = adapter
        current = findViewById(R.id.tvCurrent)
        CageName = intent?.getStringExtra("CageName").toString()
        CageKey = intent?.getStringExtra("CageKey").toString()
        PairKey = intent?.getStringExtra("PairKey").toString()
        ClutchKey = intent?.getStringExtra("ClutchKey").toString()
        CageQR = intent?.getStringExtra("CageQR").toString()
        mAuth = FirebaseAuth.getInstance()
        lifecycleScope.launch{
            try {
                val data = getDataFromDatabase()
                dataList.clear()
                dataList.addAll(data)
                adapter.notifyDataSetChanged()
                if (dataList.isEmpty()) {
                    current.visibility = View.GONE
                } else {
                    current.visibility = View.VISIBLE
                }
            }catch (e:Exception){
                Log.e(ContentValues.TAG, "Error retrieving data: ${e.message}")
            }
        }
        refreshApp()
    }
    private fun refreshApp() {
        swipeToRefresh.setOnRefreshListener {
            lifecycleScope.launch(Dispatchers.Main) {
                try {
                    val data = getDataFromDatabase()
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

    private suspend fun getDataFromDatabase(): List<PairData> = withContext(Dispatchers.IO) {

        val currentUserId = mAuth.currentUser?.uid
        val db = FirebaseDatabase.getInstance().reference.child("Users")
            .child("ID: ${currentUserId.toString()}").child("Pairs")


        val dataList = ArrayList<PairData>()
        val snapshot = db.get().await()

        for (itemSnapshot in snapshot.children) {
            val data = itemSnapshot.getValue(PairData::class.java)
            if (data != null) {
                if (itemSnapshot.key != PairKey){
                    if (itemSnapshot.child("Separate Date").exists()) {

                    }else{
                        val key = itemSnapshot.key.toString()
                        val pairsId = itemSnapshot.child("Pair ID").value.toString()
                        val cageName = itemSnapshot.child("Cage").value.toString()
                        val male = itemSnapshot.child("Male").value.toString()
                        val female = itemSnapshot.child("Female").value.toString()
                        val maleMutation = itemSnapshot.child("Male Mutation").value.toString()
                        val femaleMutation = itemSnapshot.child("Female Mutation").value.toString()
                        val beginningDate = itemSnapshot.child("Beginning").value.toString()
                        val pairMaleKey = itemSnapshot.child("Male Bird Key").value.toString()
                        val pairFemaleKey = itemSnapshot.child("Female Bird Key").value.toString()
                        val separateDate = itemSnapshot.child("Separate Date").value.toString()

                        data.pairClutchKey = ClutchKey
                        data.parentPairKey = PairKey
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
                        data.pairId = pairsId

                        if (Looper.myLooper() != Looper.getMainLooper()) {
                            Log.d(ContentValues.TAG, "Code is running on a background thread")
                        } else {
                            Log.d(ContentValues.TAG, "Code is running on the main thread")
                        }

                        dataList.add(data)

                    }
                }


            }
        }
        dataList.sortBy { it.pairDateBeg }
//        totalBirds.text = dataList.count()
        dataList
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

                val data = getDataFromDatabase()
                dataList.clear()
                dataList.addAll(data)

                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error reloading data: ${e.message}")
            } finally {

                loadingProgressBar.visibility = View.GONE
            }
        }}

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
//        val intent = Intent()
//        intent.putExtra("selectedCageId", nameValue)
//        setResult(Activity.RESULT_OK, intent)
//        finish()
    }
    override fun onClick(nameValue: String, id: String) {
        TODO("Not yet implemented")
    }

    override fun onClick(nameValue: String, id: String, mutation: String) {
        TODO("Not yet implemented")
    }
}