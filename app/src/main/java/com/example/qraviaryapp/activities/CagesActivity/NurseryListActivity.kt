package com.example.qraviaryapp.activities.CagesActivity

import BirdData
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.CagesActivity.CagesAdapter.NurseryListAdapter
import com.example.qraviaryapp.activities.detailedactivities.QRCodeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class NurseryListActivity : AppCompatActivity() {
    private lateinit var CageKey: String
    private lateinit var CageName: String
    private lateinit var recyclerView: RecyclerView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: DatabaseReference
    private lateinit var dataList: ArrayList<BirdData>
    private lateinit var adapter: NurseryListAdapter
    private lateinit var CageQR: String
    private lateinit var totalBirds: TextView
    private var birdCount = 0
    private lateinit var swipeToRefresh: SwipeRefreshLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.statusbar)
        }
        setContentView(R.layout.activity_nursery_list)

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
        totalBirds = findViewById(R.id.tvBirdCount)
        swipeToRefresh = findViewById(R.id.swipeToRefresh)
        val sharedPrefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val maturingValue = sharedPrefs.getString("maturingValue", "50") // Default to 50 if not set
        val maturingDays = maturingValue?.toIntOrNull() ?: 50



        recyclerView = findViewById(R.id.recyclerView_bird_list)
        val gridLayoutManager = GridLayoutManager(this,1)
        recyclerView.layoutManager = gridLayoutManager
        dataList = ArrayList()
        adapter = NurseryListAdapter(this,dataList, maturingDays)
        recyclerView.adapter = adapter

        CageName = intent?.getStringExtra("CageName").toString()
        CageKey = intent?.getStringExtra("CageKey").toString()
        val abcolortitle = resources.getColor(R.color.appbar)
        supportActionBar?.title = HtmlCompat.fromHtml(
            "<font color='$abcolortitle'>N$CageName</font>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        // Check if night mode is enabled
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_white)

        mAuth = FirebaseAuth.getInstance()
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
        refreshApp()
    }
    private fun refreshApp() {
        swipeToRefresh.setOnRefreshListener {
            lifecycleScope.launch {
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
            Toast.makeText(applicationContext, "Refreshed", Toast.LENGTH_SHORT).show()
        }
    }
    private suspend fun getDataFromDatabase(): List<BirdData> = withContext(Dispatchers.IO) {
        val currentUserId = mAuth.currentUser?.uid
        val db = FirebaseDatabase.getInstance().getReference("Users")
            .child("ID: ${currentUserId.toString()}").child("Cages")
            .child("Nursery Cages").child(CageKey).child("Birds")
        val qrRef= FirebaseDatabase.getInstance().getReference("Users")
            .child("ID: ${currentUserId.toString()}").child("Cages")
            .child("Nursery Cages").child(CageKey)
        val dataList = ArrayList<BirdData>()
        val snapshot = db.get().await()
        val qrSnapshot = qrRef.get().await()
        CageQR = qrSnapshot.child("QR").value.toString()
        for (itemSnapshot in snapshot.children) {

            val data = itemSnapshot.getValue(BirdData::class.java)
            val gallery = itemSnapshot.child("Gallery")
            if (data != null) {

                val mainPic = gallery.children.firstOrNull()?.value.toString()

                val adultingKey = itemSnapshot.key
                val nurserykey = itemSnapshot.child("Nursery Key").value.toString()
                val birdKey = itemSnapshot.child("Bird Key").value
                val LegbandValue = itemSnapshot.child("Legband").value
                val identifierValue = itemSnapshot.child("Identifier").value
                val genderValue = itemSnapshot.child("Gender").value
                val mutation1Value = if (itemSnapshot.hasChild("Mutation1")) {
                    itemSnapshot.child("Mutation1").child("Mutation Name").value.toString()
                } else {
                    ""
                }
                val mutation2Value = if (itemSnapshot.hasChild("Mutation2")) {
                    itemSnapshot.child("Mutation2").child("Mutation Name").value.toString()
                } else {
                    ""
                }
                val mutation3Value = if (itemSnapshot.hasChild("Mutation3")) {
                    itemSnapshot.child("Mutation3").child("Mutation Name").value.toString()
                } else {
                    ""
                }
                val mutation4Value = if (itemSnapshot.hasChild("Mutation4")) {
                    itemSnapshot.child("Mutation4").child("Mutation Name").value.toString()
                } else {
                    ""
                }
                val mutation5Value = if (itemSnapshot.hasChild("Mutation5")) {
                    itemSnapshot.child("Mutation5").child("Mutation Name").value.toString()
                } else {
                    ""
                }
                val mutation6Value = if (itemSnapshot.hasChild("Mutation6")) {
                    itemSnapshot.child("Mutation6").child("Mutation Name").value.toString()
                } else {
                    ""
                }
                val maturingDays = itemSnapshot.child("Maturing Days").value.toString()
                val dateOfBandingValue = itemSnapshot.child("Date of Banding").value
                val dateOfBirthValue = itemSnapshot.child("Date of Birth").value
                val statusValue = itemSnapshot.child("Status").value
                val availCageValue = itemSnapshot.child("Cage").value
                val forSaleCageValue = itemSnapshot.child("Cage").value
                val forSaleRequestedPriceValue = itemSnapshot.child("Requested Price").value
                val soldDateValue = itemSnapshot.child("Sold Date").value
                val soldPriceValue = itemSnapshot.child("Sale Price").value
                val soldContactValue = itemSnapshot.child("Sale Contact").value
                val deathDateValue = itemSnapshot.child("Death Date").value
                val deathReasonValue = itemSnapshot.child("Death Reason").value
                val exDateValue = itemSnapshot.child("Exchange Date").value
                val exReasonValue = itemSnapshot.child("Exchange Reason").value
                val exContactValue = itemSnapshot.child("Exchange Contact").value
                val lostDateValue = itemSnapshot.child("Lost Date").value
                val lostDetailsValue = itemSnapshot.child("Lost Details").value
                val donatedDateValue = itemSnapshot.child("Donated Date").value
                val donatedContactValue = itemSnapshot.child("Donated Contact").value
                val otherCommentsValue = itemSnapshot.child("Comments").value
                val buyPriceValue = itemSnapshot.child("Buy Price").value
                val boughtDateValue = itemSnapshot.child("Bought Date").value
                val breederContactValue = itemSnapshot.child("Breeder Contact").value
                val FatherValue = itemSnapshot.child("Parents").child("Father").value
                val MotherValue = itemSnapshot.child("Parents").child("Mother").value
                val fatherKeyValue = itemSnapshot.child("Parents").child("FatherKey").value
                val motherKeyValue = itemSnapshot.child("Parents").child("MotherKey").value

                val legband = LegbandValue.toString() ?: ""
                val identifier = identifierValue.toString() ?: ""
                val gender = genderValue.toString() ?: ""
                val dateOfBanding = dateOfBandingValue.toString() ?: ""
                val dateOfBirth = dateOfBirthValue.toString() ?: ""
                val status = statusValue.toString() ?: ""
                val availCage = availCageValue.toString() ?: ""
                val forSaleCage = forSaleCageValue.toString() ?: ""
                val forSaleRequestedPrice = forSaleRequestedPriceValue.toString() ?: ""
                val soldDate = soldDateValue.toString() ?: ""
                val soldPrice = soldPriceValue.toString() ?: ""
                val soldContact = soldContactValue.toString() ?: ""
                val deathDate = deathDateValue.toString() ?: ""
                val deathReason = deathReasonValue.toString() ?: ""
                val exDate = exDateValue.toString() ?: ""
                val exReason = exReasonValue.toString() ?: ""
                val exContact = exContactValue.toString() ?: ""
                val lostDate = lostDateValue.toString() ?: ""
                val lostDetails = lostDetailsValue.toString() ?: ""
                val donatedDate = donatedDateValue.toString() ?: ""
                val donatedContact = donatedContactValue.toString() ?: ""
                val otherComments = otherCommentsValue.toString() ?: ""
                val buyPrice = buyPriceValue.toString() ?: ""
                val boughtDate = boughtDateValue.toString() ?: ""
                val boughtBreeder = breederContactValue.toString() ?: ""
                val mother = MotherValue.toString() ?: ""
                val father = FatherValue.toString() ?: ""
                birdCount = snapshot.childrenCount.toInt()
                Log.d(TAG, maturingDays)
                data.maturingDays = maturingDays
                data.adultingKey = adultingKey
                data.cageKey = CageKey
                data.img = mainPic
                data.birdCount = birdCount.toString()
                data.birdKey = birdKey.toString()
                data.nurseryKey = nurserykey
                data.legband = legband
                data.identifier = identifier
                data.gender = gender
                data.dateOfBanding = dateOfBanding
                data.dateOfBirth = dateOfBirth
                data.status = status
                data.mutation1 = mutation1Value
                data.mutation2 = mutation2Value
                data.mutation3 = mutation3Value
                data.mutation4 = mutation4Value
                data.mutation5 = mutation5Value
                data.mutation6 = mutation6Value
                data.availCage = availCage
                data.forSaleCage = forSaleCage
                data.reqPrice = forSaleRequestedPrice
                data.soldDate = soldDate
                data.soldPrice = soldPrice
                data.saleContact = soldContact
                data.deathDate = deathDate
                data.deathReason = deathReason
                data.exDate = exDate
                data.exReason = exReason
                data.exContact = exContact
                data.lostDate = lostDate
                data.lostDetails = lostDetails
                data.donatedDate = donatedDate
                data.donatedContact = donatedContact
                data.otherComments = otherComments
                data.buyPrice = buyPrice
                data.boughtDate = boughtDate
                data.breederContact = boughtBreeder
                data.father = father
                data.mother = mother
                data.fatherKey = fatherKeyValue.toString()
                data.motherKey = motherKeyValue.toString()

                if (Looper.myLooper() != Looper.getMainLooper()) {
                    Log.d(ContentValues.TAG, "Code is running on a background thread")
                } else {
                    Log.d(ContentValues.TAG, "Code is running on the main thread")
                    //
                }
                dataList.add(data)


            }

        }

        if(dataList.count()>1){
            totalBirds.text = dataList.count().toString() + " Birds"
        }
        else{
            totalBirds.text = dataList.count().toString() + " Bird"
        }


        dataList

    }
    override fun onResume() {
        super.onResume()
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
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.cageoption, menu)



        return true
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