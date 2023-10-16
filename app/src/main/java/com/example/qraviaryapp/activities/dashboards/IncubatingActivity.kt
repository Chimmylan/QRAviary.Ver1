package com.example.qraviaryapp.activities.dashboards

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
import com.example.qraviaryapp.adapter.ClutchesListAdapter
import com.example.qraviaryapp.adapter.DetailedAdapter.EggAdapter
import com.example.qraviaryapp.adapter.DetailedAdapter.SoldAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class IncubatingActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: DatabaseReference
    private lateinit var dataList: ArrayList<EggData>
    private lateinit var adapter: EggAdapter
    private var flightKey: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.statusbar)
        }
        setContentView(R.layout.activity_incubating)
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
            "<font color='$abcolortitle'>Incubating</font>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_white)

        mAuth = FirebaseAuth.getInstance()
        dataList = ArrayList()
        val gridLayoutManager = GridLayoutManager(this, 1)
        recyclerView = findViewById(R.id.RecyclerView)
        recyclerView.layoutManager = gridLayoutManager

//        adapter = EggAdapter(this, dataList)
        recyclerView.adapter = adapter

        lifecycleScope.launch {
            try {
                val data = getDataFromDatabase()
                dataList.clear()
                dataList.addAll(data)
                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error retrieving data: ${e.message}")
            }
        }
    }
    private suspend fun getDataFromDatabase(): List<EggData> = withContext(Dispatchers.IO) {
        val currentUserId = mAuth.currentUser?.uid
        val db = FirebaseDatabase.getInstance().reference.child("Users")
            .child("ID: ${currentUserId.toString()}").child("Pairs")

        val dataList = ArrayList<EggData>()
        val snapshot = db.get().await()

        for (itemsnapshot in snapshot.children) {

            val clutches = itemsnapshot.child("Clutches")
            for (clutchSnapshot in clutches.children) {
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

                            data.eggKey = key
                            data.eggCount = eggsCount.toString()
                            data.eggIncubating = incubatingCount.toString()
                            data.eggIncubationStartDate = eggDate
                        }
                        if (eggStatus == "Laid") {

                            laidCount++
                            Log.d(TAG, laidCount.toString())

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
        }
        dataList.sortBy { it.eggIncubationStartDate }
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