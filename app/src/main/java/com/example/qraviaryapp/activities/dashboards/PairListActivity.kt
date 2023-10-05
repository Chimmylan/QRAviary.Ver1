package com.example.qraviaryapp.activities.dashboards

import BirdData
import PairData
import android.content.AbstractThreadedSyncAdapter
import android.content.ContentValues
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.AddActivities.AddBirdActivity
import com.example.qraviaryapp.activities.AddActivities.AddPairActivity
import com.example.qraviaryapp.adapter.PairListAdapter
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class PairListActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var dbase: DatabaseReference
    private lateinit var dataList: ArrayList<PairData>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PairListAdapter
    private lateinit var fab: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.statusbar)
        }
        setContentView(R.layout.activity_pair_list)
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
            "<font color='$abcolortitle'>Pairs</font>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        // Check if night mode is enabled
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_white)

        mAuth = FirebaseAuth.getInstance()
        recyclerView = findViewById(R.id.recyclerView)
        val gridLayoutManager = GridLayoutManager(this, 1)
        recyclerView.layoutManager = gridLayoutManager
        dataList = ArrayList()
        adapter = PairListAdapter(this, dataList)
        recyclerView.adapter = adapter




        lifecycleScope.launch{
            try {
                val data = getDataFromDatabase()
                dataList.addAll(data)
                adapter.notifyDataSetChanged()
            }catch (e:Exception){
                Log.e(ContentValues.TAG, "Error retrieving data: ${e.message}")
            }
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
            if (data != null){
                val key = itemSnapshot.key.toString()
//                val FemaleIdentifier = itemSnapshot.child("FemaleIdentifier").value.toString()
//                val MaleIdentifier = itemSnapshot.child("MaleIdentifier").value.toString()
                val cageName = itemSnapshot.child("Cage").value.toString()
                val male = itemSnapshot.child("Male").value.toString()
                val female = itemSnapshot.child("Female").value.toString()
                val maleMutation = itemSnapshot.child("Male Mutation").value.toString()
                val femaleMutation = itemSnapshot.child("Female Mutation").value.toString()
                val beginningDate = itemSnapshot.child("Beginning").value.toString()


                data.pairKey = key
                data.pairFemale = female
                data.pairMale = male
                data.pairCage = cageName
                data.pairMaleMutation = maleMutation
                data.pairFemaleMutation = femaleMutation
                data.pairDateBeg = beginningDate


                if (Looper.myLooper() != Looper.getMainLooper()) {
                    Log.d(ContentValues.TAG, "Code is running on a background thread")
                } else {
                    Log.d(ContentValues.TAG, "Code is running on the main thread")
                    //
                }

                dataList.add(data)

            }
        }
        dataList.sortBy { it.pairDateBeg }
        dataList
    }

    fun fab(view: View) {
        startActivity(Intent(this@PairListActivity, AddPairActivity::class.java))
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