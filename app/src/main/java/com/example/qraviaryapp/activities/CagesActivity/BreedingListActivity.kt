package com.example.qraviaryapp.activities.CagesActivity

import PairData
import android.content.ContentValues
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.CagesActivity.CagesAdapter.BreedingListAdapter
import com.example.qraviaryapp.activities.CagesActivity.CagesAdapter.BreedingListPreviousAdapter
import com.example.qraviaryapp.activities.EditActivities.EditBirdActivity
import com.example.qraviaryapp.activities.detailedactivities.QRCodeActivity
import com.example.qraviaryapp.activities.detailedactivities.SellActivity
import com.example.qraviaryapp.adapter.PairListAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class BreedingListActivity : AppCompatActivity() {
    private lateinit var CageKey: String
    private lateinit var CageName: String
    private lateinit var CageQR: String
    private lateinit var mAuth: FirebaseAuth
    private lateinit var dbase: DatabaseReference
    private lateinit var dataList: ArrayList<PairData>
    private lateinit var dataList1: ArrayList<PairData>
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerView1: RecyclerView
    private lateinit var adapter: BreedingListAdapter
    private lateinit var adapter1: BreedingListPreviousAdapter
    private lateinit var fab: FloatingActionButton
    private lateinit var current: TextView
    private lateinit var previous: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.statusbar)
        }
        setContentView(R.layout.activity_breeding_list)

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

        CageName = intent?.getStringExtra("CageName").toString()
        CageKey = intent?.getStringExtra("CageKey").toString()
        CageQR = intent?.getStringExtra("CageQR").toString()

        val abcolortitle = resources.getColor(R.color.appbar)
        supportActionBar?.title = HtmlCompat.fromHtml(
            "<font color='$abcolortitle'>B$CageName</font>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        // Check if night mode is enabled
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_white)
        mAuth = FirebaseAuth.getInstance()
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView1 = findViewById(R.id.recyclerView1)
        val gridLayoutManager = GridLayoutManager(this, 1)
        val gridLayoutManager1 = GridLayoutManager(this, 1)

        recyclerView.layoutManager = gridLayoutManager
        recyclerView1.layoutManager = gridLayoutManager1

        dataList = ArrayList()
        dataList1 = ArrayList()
        adapter = BreedingListAdapter(this, dataList)
        adapter1 = BreedingListPreviousAdapter(this, dataList1)
        recyclerView.adapter = adapter

        current = findViewById(R.id.tvCurrent)
        previous = findViewById(R.id.tvPrevious)



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
        lifecycleScope.launch{
            try {
                val data = getDataFromPreviousDatabase()
                dataList1.clear()
                dataList1.addAll(data)
                adapter.notifyDataSetChanged()
                if (dataList1.isEmpty()) {
                    previous.visibility = View.GONE
                } else {
                    previous.visibility = View.VISIBLE
                }
            }catch (e:Exception){
                Log.e(ContentValues.TAG, "Error retrieving data: ${e.message}")
            }
        }
    }
    private suspend fun getDataFromDatabase(): List<PairData> = withContext(Dispatchers.IO) {

        val currentUserId = mAuth.currentUser?.uid
        val db = FirebaseDatabase.getInstance().reference.child("Users")
            .child("ID: ${currentUserId.toString()}").child("Cages")
            .child("Breeding Cages").child(CageKey).child("Pair Birds")

        val qrRef = FirebaseDatabase.getInstance().reference.child("Users")
            .child("ID: ${currentUserId.toString()}").child("Cages")
            .child("Breeding Cages").child(CageKey)

        val qrSnapshot = qrRef.get().await()
        val dataList = ArrayList<PairData>()
        val snapshot = db.get().await()

        CageQR = qrSnapshot.child("QR").value.toString()

        for (itemSnapshot in snapshot.children) {
            val data = itemSnapshot.getValue(PairData::class.java)
            if (data != null) {
                val key = itemSnapshot.key.toString()
                val cageName = itemSnapshot.child("Cage").value.toString()
                val male = itemSnapshot.child("Male").value.toString()
                val female = itemSnapshot.child("Female").value.toString()
                val maleMutation = itemSnapshot.child("Male Mutation").value.toString()
                val femaleMutation = itemSnapshot.child("Female Mutation").value.toString()
                val beginningDate = itemSnapshot.child("Beginning").value.toString()
                val pairMaleKey = itemSnapshot.child("Male Bird Key").value.toString()
                val pairFemaleKey = itemSnapshot.child("Female Bird Key").value.toString()
                val separateDate = itemSnapshot.child("Separate Date").value.toString()

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

                if (Looper.myLooper() != Looper.getMainLooper()) {
                    Log.d(ContentValues.TAG, "Code is running on a background thread")
                } else {
                    Log.d(ContentValues.TAG, "Code is running on the main thread")
                }

                dataList.add(data)
            }
        }
        dataList.sortBy { it.pairDateBeg }
        dataList
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.qr_option, menu)



        return true
    }

    private suspend fun getDataFromPreviousDatabase(): List<PairData> = withContext(Dispatchers.IO) {
        val currentUserId = mAuth.currentUser?.uid
        val db = FirebaseDatabase.getInstance().reference.child("Users")
            .child("ID: ${currentUserId.toString()}").child("Pairs")


        val dataList = ArrayList<PairData>()
        val snapshot = db.get().await()
        for (itemSnapshot in snapshot.children) {
            val data = itemSnapshot.getValue(PairData::class.java)
            if (data != null) {
                if (itemSnapshot.child("Separate Date").exists()) {
                    val key = itemSnapshot.key.toString()
                    val cageName = itemSnapshot.child("Cage").value.toString()
                    val cageKeyFemale = itemSnapshot.child("CageKeyFemale").value.toString()
                    val cageKeyMale = itemSnapshot.child("CageKeyMale").value.toString()
                    val cageBirdFemale = itemSnapshot.child("CageKeyFlightFemaleValue").value.toString()
                    val cageBirdMale = itemSnapshot.child("CageKeyFlightMaleValue").value.toString()
                    val male = itemSnapshot.child("Male").value.toString()
                    val female = itemSnapshot.child("Female").value.toString()
                    val maleMutation = itemSnapshot.child("Male Mutation").value.toString()
                    val femaleMutation = itemSnapshot.child("Female Mutation").value.toString()
                    val beginningDate = itemSnapshot.child("Beginning").value.toString()
                    val pairMaleKey = itemSnapshot.child("Male Bird Key").value.toString()
                    val pairFemaleKey = itemSnapshot.child("Female Bird Key").value.toString()
                    val separateDate = itemSnapshot.child("Separate Date").value.toString()

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
                    data.paircagebirdFemale =cageBirdFemale
                    data.paircagebirdMale = cageBirdMale
                    data.paircagekeyFemale = cageKeyFemale
                    data.paircagekeyMale = cageKeyMale

                    if (Looper.myLooper() != Looper.getMainLooper()) {
                        Log.d(ContentValues.TAG, "Code is running on a background thread")
                    } else {
                        Log.d(ContentValues.TAG, "Code is running on the main thread")
                    }

                    dataList.add(data)
                } else {

                }
            }

        }

        dataList.sortBy { it.pairDateBeg }
        dataList
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_qr -> {
                val i = Intent(this, QRCodeActivity::class.java)
                i.putExtra("CageQR", CageQR)
                startActivity(i)
                true
            }
            R.id.menu_delete -> {

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