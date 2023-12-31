package com.example.qraviaryapp.activities.detailedactivities

import EggData
import android.content.ContentValues
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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

class PreviousClutchesActivity : AppCompatActivity() {


    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: DatabaseReference
    public lateinit var adapter: EggClutchesListAdapter
    private lateinit var dataList: ArrayList<EggData>
    private lateinit var recyclerView: RecyclerView



    private lateinit var eggKey: String
    private lateinit var pairKey: String
    private lateinit var pairFlightMaleKey: String
    private lateinit var pairFlightFemaleKey: String
    private lateinit var pairBirdMaleKey: String
    private lateinit var pairBirdFemaleKey: String
    private lateinit var pairMaleID: String
    private lateinit var pairFemaleID: String
    private lateinit var pairCageKeyMale: String
    private lateinit var pairCageKeyFemale: String
    private lateinit var pairCageBirdMale: String
    private lateinit var pairCageBirdFemale: String
    private lateinit var totalegg: TextView
    private var eggCount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.statusbar)
        }
        setContentView(R.layout.activity_previous_clutches)

        supportActionoBar()
        totalegg = findViewById(R.id.tvBirdCount)


        mAuth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().reference
        dataList = ArrayList()
        adapter = EggClutchesListAdapter(this, dataList)
        recyclerView = findViewById(R.id.recyclerView)
        val gridLayoutManager = GridLayoutManager(this, 1)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = adapter

        val bundle = intent.extras


        if (bundle != null) {
            eggKey = bundle.getString("EggKey").toString()
            pairKey = bundle.getString("PairKey").toString()
            pairFlightMaleKey = bundle.getString("PairFlightMaleKey").toString()
            pairFlightFemaleKey = bundle.getString("PairFlightFemaleKey").toString()
            pairBirdMaleKey = bundle.getString("PairMaleKey").toString()
            pairBirdFemaleKey = bundle.getString("PairFemaleKey").toString()
            pairFemaleID = bundle.getString("PairFemaleID").toString()
            pairMaleID = bundle.get("PairMaleID").toString()
            pairCageBirdFemale = bundle.getString("CageBirdFemale").toString()
            pairCageBirdMale = bundle.getString("CageBirdFemale").toString()
            pairCageKeyFemale = bundle.getString("CageKeyFemale").toString()
            pairCageKeyMale = bundle.getString("CageKeyMale").toString()

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

    }

    private suspend fun getDataFromDatabase(): List<EggData> = withContext(Dispatchers.IO) {

        val currenUserId = mAuth.currentUser?.uid
        db = FirebaseDatabase.getInstance().reference.child("Users")
            .child("ID: ${currenUserId.toString()}").child("Pairs")
            .child(pairKey).child("Clutches").child(eggKey)

        val dataList = ArrayList<EggData>()
        val snapshot = db.get().await()

        for (eggSnapshot in snapshot.children) {
            val data = eggSnapshot.getValue(EggData::class.java)
            if (data != null) {
                val individualEggKey = eggSnapshot.key.toString()


                val statusValue = eggSnapshot.child("Status").value.toString()
                val dateValue = eggSnapshot.child("Date").value.toString()
                val incubatingDateValue = eggSnapshot.child("Incubating Days").value.toString()
                val maturingDateValue = eggSnapshot.child("Maturing Days").value.toString()
                val birdkey = eggSnapshot.child("Bird Key").value.toString()
                val cage = eggSnapshot.child("Cage").value.toString()
                val Datebirth = eggSnapshot.child("Date of Birth").value.toString()
                val gender = eggSnapshot.child("Gender").value.toString()
                val Identifier = eggSnapshot.child("Identifier").value.toString()
                val statusAvailable = eggSnapshot.child("Status1").value.toString()
                val Legband = eggSnapshot.child("Legband").value.toString()
                val NurseryKey = eggSnapshot.child("Cage").value.toString()
                val mutation1Value = if (eggSnapshot.hasChild("Mutation1")) {
                    eggSnapshot.child("Mutation1").child("Mutation Name").value.toString()
                } else {
                    ""
                }
                val mutation2Value = if (eggSnapshot.hasChild("Mutation2")) {
                    eggSnapshot.child("Mutation2").child("Mutation Name").value.toString()
                } else {
                    ""
                }
                val mutation3Value = if (eggSnapshot.hasChild("Mutation3")) {
                    eggSnapshot.child("Mutation3").child("Mutation Name").value.toString()
                } else {
                    ""
                }
                val mutation4Value = if (eggSnapshot.hasChild("Mutation4")) {
                    eggSnapshot.child("Mutation4").child("Mutation Name").value.toString()
                } else {
                    ""
                }
                val mutation5Value = if (eggSnapshot.hasChild("Mutation5")) {
                    eggSnapshot.child("Mutation5").child("Mutation Name").value.toString()
                } else {
                    ""
                }
                val mutation6Value = if (eggSnapshot.hasChild("Mutation6")) {
                    eggSnapshot.child("Mutation6").child("Mutation Name").value.toString()
                } else {
                    ""
                }

                val FatherValue = eggSnapshot.child("Parents").child("Father").value.toString()
                val MotherValue = eggSnapshot.child("Parents").child("Mother").value.toString()
                val fatherKeyValue = eggSnapshot.child("Parents").child("FatherKey").value.toString()
                val motherKeyValue = eggSnapshot.child("Parents").child("MotherKey").value.toString()
                val fatherBirdKeyValue = eggSnapshot.child("Parents").child("BirdFatherKey").value.toString()
                val motherBirdKeyValue = eggSnapshot.child("Parents").child("BirdMotherKey").value.toString()


                if (statusValue == "Incubating") {
                    data.eggIncubating = statusValue
                    data.eggIncubationStartDate = dateValue
                }
                if (statusValue == "Laid") {
                    data.eggLaid = statusValue
                    data.eggLaidStartDate = dateValue
                }
                eggCount = snapshot.childrenCount.toInt()
                data.eggCount = eggCount.toString()
                data.pairKey = pairKey
                data.eggKey = eggKey
                data.individualEggKey = individualEggKey
                data.eggStatus = statusValue
                data.eggDate = dateValue
                data.eggIncubationStartDate = incubatingDateValue
                data.eggMaturingStartDate = maturingDateValue
                data.pairFlightMaleKey = pairFlightMaleKey
                data.pairFlightFemaleKey = pairFlightFemaleKey
                data.pairMaleId = pairMaleID
                data.pairFemaleId = pairFemaleID
                data.pairBirdFemaleKey = pairBirdFemaleKey
                data.pairBirdMaleKey = pairBirdMaleKey
                data.eggcagebirdMale = pairCageBirdMale
                data.eggcagebirdFemale = pairCageBirdFemale
                data.eggcagekeyMale = pairCageKeyMale
                data.eggcagekeyFemale = pairCageKeyFemale
                data.birdkey = birdkey
                data.datebirth = Datebirth
                data.legband = Legband
                data.identifier = Identifier
                data.cage = cage
                data.nurserykey = NurseryKey
                data.gender = gender
                data.mutation2 = mutation2Value
                data.mutation1 = mutation1Value
                data.mutation6 = mutation6Value
                data.mutation3 = mutation3Value
                data.mutation4 = mutation4Value
                data.mutation5 = mutation5Value
                data.father = FatherValue
                data.fatherkey = fatherKeyValue
                data.mother= MotherValue
                data.fatherbirdkey = fatherBirdKeyValue
                data.motherbirdkey = motherBirdKeyValue
                data.motherkey = motherKeyValue
                data.status1 = statusAvailable
                dataList.add(data)
            }
        }
        totalegg.text = "Total Eggs: $eggCount"
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
                    R.color.toolbarcolor
                )
            )
        )
        val abcolortitle = resources.getColor(R.color.appbar)
        supportActionBar?.title = HtmlCompat.fromHtml(
            "<font color='$abcolortitle'>Clutch</font>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        // Check if night mode is enabled
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_white)
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