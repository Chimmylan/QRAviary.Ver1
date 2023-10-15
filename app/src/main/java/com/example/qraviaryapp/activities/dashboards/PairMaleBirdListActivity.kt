package com.example.qraviaryapp.activities.dashboards

import BirdData
import ClickListener
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Debug
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qraviaryapp.R
import com.example.qraviaryapp.adapter.MaleBirdListAdapter
import com.example.qraviaryapp.adapter.PairMaleBirdListAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class PairMaleBirdListActivity : AppCompatActivity(), ClickListener {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: DatabaseReference
    private lateinit var adapter: PairMaleBirdListAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<BirdData>
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: Editor

    private var femaleMutation: String? = null
    private var femaleMutation2: String? = null
    private var femaleMutation3: String? = null
    private var femaleMutation4: String? = null
    private var femaleMutation5: String? = null
    private var femaleMutation6: String? = null
    private var mainPic: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.statusbar)
        }
        setContentView(R.layout.activity_male_bird_list)
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
            "<font color='$abcolortitle'>Male Flight Birds</font>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_white)

        sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()


        val isHybridization = sharedPreferences.getBoolean("isHybridization", false)

        femaleMutation = intent.getStringExtra("FemaleMutation")
        femaleMutation2= intent.getStringExtra("FemaleMutation2")
        femaleMutation3 = intent.getStringExtra("FemaleMutation3")
        femaleMutation4 = intent.getStringExtra("FemaleMutation4")
        femaleMutation5 = intent.getStringExtra("FemaleMutation5")
        femaleMutation6 = intent.getStringExtra("FemaleMutation6")
        Log.d(ContentValues.TAG, "Male Mutation Pair" + femaleMutation.toString())


        mAuth = FirebaseAuth.getInstance()
        recyclerView = findViewById(R.id.recyclerView)
        val gridLayoutManager = GridLayoutManager(this, 1)
        recyclerView.layoutManager = gridLayoutManager
        dataList = ArrayList()
        adapter = PairMaleBirdListAdapter(this, dataList, this)
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

    suspend fun getDataFromDatabase(): List<BirdData> = withContext(Dispatchers.IO) {
        val mutationArray = arrayOf(femaleMutation,femaleMutation2,femaleMutation3,femaleMutation4,femaleMutation5,femaleMutation6)
        val currentUserId = mAuth.currentUser?.uid
        val db = FirebaseDatabase.getInstance().reference.child("Users")
            .child("ID: ${currentUserId.toString()}").child("Flight Birds")
        val dataList = ArrayList<BirdData>()
        val snapshot = db.get().await()
        for (itemSnapshot in snapshot.children) {
            val data = itemSnapshot.getValue(BirdData::class.java)
            val gallery = itemSnapshot.child("Gallery")
            if (data != null) {

                val male = itemSnapshot.child("Gender").value.toString()
                val mutations = arrayOf(
                    itemSnapshot.child("Mutation1").child("Mutation Name").value.toString(),
                    itemSnapshot.child("Mutation2").child("Mutation Name").value.toString(),
                    itemSnapshot.child("Mutation3").child("Mutation Name").value.toString(),
                    itemSnapshot.child("Mutation4").child("Mutation Name").value.toString(),
                    itemSnapshot.child("Mutation5").child("Mutation Name").value.toString(),
                    itemSnapshot.child("Mutation6").child("Mutation Name").value.toString(),
                )
                Log.d(ContentValues.TAG, mutations.toString())
                //Run if mutation exist
                if (femaleMutation?.isNotEmpty() == true) {
                    if (male == "Male" && mutations.any { mutationArray.contains(it) }) {
                        mainPic = gallery.children.firstOrNull()?.value.toString()
                        val maleKey = itemSnapshot.key
                        val birdKey = itemSnapshot.child("Bird Key").value.toString()
                        val flightKey = itemSnapshot.child("Flight Key").value.toString()
                        val cagekeyvalue = itemSnapshot.child("CageKey").value.toString()
                        val cagebirdkeyvalue = itemSnapshot.child("Cage Bird Key").value.toString()
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

                        /*==++==*/
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
                        data.img = mainPic
                        data.birdKey = birdKey
                        data.flightKey = flightKey
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
                        data.cagekeymalevalue = cagekeyvalue
                        data.cagebirdmalekey = cagebirdkeyvalue

                        if (Looper.myLooper() != Looper.getMainLooper()) {
                            Log.d(ContentValues.TAG, "Code is running on a background thread")
                        } else {
                            Log.d(ContentValues.TAG, "Code is running on the main thread")
                            //
                        }
                        dataList.add(data)
                        Log.d(ContentValues.TAG, "Have Same Mutation")
                    }
                } else {
                    if (male == "Male") {
                        val maleKey = itemSnapshot.key
                        mainPic = gallery.children.firstOrNull()?.value.toString()
                        val birdKey = itemSnapshot.child("Bird Key").value.toString()
                        val flightKey = itemSnapshot.child("Flight Key").value.toString()
                        val cagekeyvalue = itemSnapshot.child("CageKey").value.toString()
                        val cagebirdkeyvalue = itemSnapshot.child("Cage Bird Key").value.toString()
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

                        /*==++==*/
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
                        data.img = mainPic
                        data.birdKey = birdKey
                        data.flightKey = flightKey
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
                        data.cagekeymalevalue = cagekeyvalue
                        data.cagebirdmalekey = cagebirdkeyvalue

                        if (Looper.myLooper() != Looper.getMainLooper()) {
                            Log.d(ContentValues.TAG, "Code is running on a background thread")
                        } else {
                            Log.d(ContentValues.TAG, "Code is running on the main thread")
                            //
                        }
                        dataList.add(data)
                        Log.d(ContentValues.TAG, "Dont Have Same Mutation")
                    }
                }


            }
        }

        dataList
    }

    override fun onClick(nameValue: String) {
        TODO("Not yet implemented")
    }

    override fun onClick(nameValue: String, id: String) {
        TODO("Not yet implemented")
    }

    override fun onClick(nameValue: String, id: String, mutation: String) {
        val intent = Intent()
        intent.putExtra("MaleGallery", mainPic)
        intent.putExtra("MaleBird", nameValue)
        intent.putExtra("MaleBirdId", id)
        intent.putExtra("MaleMutation", mutation)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}