package com.example.qraviaryapp.activities.detailedactivities

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.text.HtmlCompat
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.EditActivities.EditBirdActivity
import com.example.qraviaryapp.activities.EditActivities.EditBirdFlightActivity
import com.example.qraviaryapp.adapter.FragmentAdapter
import com.example.qraviaryapp.fragments.DetailedFragment.BirdBabiesFragment
import com.example.qraviaryapp.fragments.DetailedFragment.BirdBasicFragment
import com.example.qraviaryapp.fragments.DetailedFragment.BirdGalleryFragment
import com.example.qraviaryapp.fragments.DetailedFragment.BirdOriginFragment
import com.example.qraviaryapp.fragments.DetailedFragment.BirdPairingFragment
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class BirdsDetailedActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager
    private lateinit var tablayout: TabLayout
    val fragmentAdapter = FragmentAdapter(supportFragmentManager)
    private lateinit var toolbar: Toolbar
    private lateinit var ImageView: ImageView
    private lateinit var BirdKey: String
    private lateinit var FlightKey: String
    private lateinit var NurseryKey: String
    private lateinit var BirdId: String
    private lateinit var BirdLegband: String
    private lateinit var BirdImage: String
    private lateinit var BirdGender: String
    private lateinit var BirdStatus: String
    private lateinit var BirdDateBirth: String
    private lateinit var BirdSalePrice: String
    private lateinit var BirdBuyer: String
    private lateinit var BirdDeathReason: String
    private lateinit var BirdExchangeReason: String
    private lateinit var BirdExchangeWith: String
    private lateinit var BirdLostDetails: String
    private lateinit var BirdAvailCage: String
    private lateinit var BirdForsaleCage: String
    private lateinit var BirdRequestedPrice: String
    private lateinit var BirdComment: String
    private lateinit var BirdBuyPrice: String
    private lateinit var BirdSoldId: String
    private lateinit var BirdBoughtOn: String
    private lateinit var BirdBoughtBreeder: String
    private lateinit var BirdBreeder: String
    private lateinit var BirdDeceaseDate: String
    private lateinit var BirdLostDate: String
    private lateinit var BirdSoldDate: String
    private lateinit var BirdExchangeDate: String
    private lateinit var BirdDonatedDate: String
    private lateinit var BirdDonatedContact: String
    private lateinit var BirdMutation1: String
    private lateinit var BirdMutation2: String
    private lateinit var BirdMutation3: String
    private lateinit var BirdMutation4: String
    private lateinit var BirdMutation5: String
    private lateinit var BirdMutation6: String
    private lateinit var BirdIncubatingDays1: String
    private lateinit var BirdIncubatingDays2: String
    private lateinit var BirdIncubatingDays3: String
    private lateinit var BirdIncubatingDays4: String
    private lateinit var BirdIncubatingDays5: String
    private lateinit var BirdIncubatingDays6: String
    private lateinit var BirdMaturingDays1: String
    private lateinit var BirdMaturingDays2: String
    private lateinit var BirdMaturingDays3: String
    private lateinit var BirdMaturingDays4: String
    private lateinit var BirdMaturingDays5: String
    private lateinit var BirdMaturingDays6: String
    private lateinit var BirdFather: String
    private lateinit var BirdFatherKey: String
    private lateinit var BirdMother: String
    private lateinit var BirdMotherKey: String
    private var Clutch: Boolean? = null
    private lateinit var mAuth: FirebaseAuth
    private var flightType: String? = null
    private var nurseryType: String? = null

    //cages
    private var fromFlightAdapter: Boolean = false
    private var fromNurseryAdapter: Boolean = false
    private lateinit var cageKeyValue: String
    private lateinit var CageQR: String
    private lateinit var SoldId: String
    private lateinit var PurchaseId: String
    private lateinit var CageKey: String
    private lateinit var CageBirdKey: String
    private lateinit var databaseReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var currentUser: String
    private lateinit var Auth: FirebaseAuth
    val newBundle = Bundle()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.statusBarColor = ContextCompat.getColor(this, R.color.bottom_nav_background)
//        }
        setContentView(R.layout.activity_birds_detailed)
        Auth = FirebaseAuth.getInstance()
        currentUser = Auth.currentUser?.uid.toString()
        toolbar = findViewById(R.id.toolbar)
        supportActionBar?.elevation = 0f
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val bundle = intent.extras
        database = FirebaseDatabase.getInstance()
        databaseReference = database.getReference("Users")


        mAuth = FirebaseAuth.getInstance()
        BirdSoldId= bundle?.getString("SoldId").toString()
        Clutch = bundle?.getBoolean("Clutch")
        BirdId = bundle?.getString("BirdId").toString()//
        BirdLegband = bundle?.getString("BirdLegband").toString()//
        BirdKey = bundle?.getString("BirdKey").toString()
        NurseryKey = bundle?.getString("NurseryKey").toString()
        FlightKey = bundle?.getString("FlightKey").toString()
        BirdImage = bundle?.getString("BirdImage").toString()//
        BirdGender = bundle?.getString("BirdGender").toString()//
        BirdStatus = bundle?.getString("BirdStatus").toString()//
        BirdDateBirth = bundle?.getString("BirdDateBirth").toString()//
        BirdSalePrice = bundle?.getString("BirdSalePrice").toString()//
        BirdBuyer = bundle?.getString("BirdBuyer").toString()//
        BirdDeathReason = bundle?.getString("BirdDeathReason").toString()//
        BirdExchangeWith = bundle?.getString("BirdExchangeWith").toString()//
        BirdLostDetails = bundle?.getString("BirdLostDetails").toString()//
        BirdAvailCage = bundle?.getString("BirdAvailCage").toString()//
        BirdForsaleCage = bundle?.getString("BirdForsaleCage").toString()//
        BirdExchangeReason = bundle?.getString("BirdExchangeReason").toString()//
        BirdRequestedPrice = bundle?.getString("BirdRequestedPrice").toString()//
        BirdComment = bundle?.getString("BirdComment").toString()//
        BirdBuyPrice = bundle?.getString("BirdBuyPrice").toString()//
        BirdBoughtOn = bundle?.getString("BirdBoughtOn").toString()//
        BirdBoughtBreeder = bundle?.getString("BirdBoughtBreeder").toString()//
        BirdBreeder = bundle?.getString("BirdBreeder").toString()//
        BirdDeceaseDate = bundle?.getString("BirdDeceaseDate").toString()//
        BirdSoldDate = bundle?.getString("BirdSoldDate").toString()//
        BirdLostDate = bundle?.getString("BirdLostDate").toString()//
        BirdExchangeDate = bundle?.getString("BirdExchangeDate").toString()//
        BirdDonatedDate = bundle?.getString("BirdDonatedDate").toString()//
        BirdDonatedContact = bundle?.getString("BirdDonatedContact").toString()//
        BirdMutation1 = bundle?.getString("BirdMutation1").toString()//
        BirdMutation2 = bundle?.getString("BirdMutation2").toString()//
        BirdMutation3 = bundle?.getString("BirdMutation3").toString()//
        BirdMutation4 = bundle?.getString("BirdMutation4").toString()//
        BirdMutation5 = bundle?.getString("BirdMutation5").toString()//
        BirdMutation6 = bundle?.getString("BirdMutation6").toString()//
        BirdMaturingDays1 = bundle?.getString("BirdMaturingDays1").toString()//
        BirdMaturingDays2 = bundle?.getString("BirdMaturingDays2").toString()//
        BirdMaturingDays3 = bundle?.getString("BirdMaturingDays3").toString()//
        BirdMaturingDays4 = bundle?.getString("BirdMaturingDays4").toString()//
        BirdMaturingDays5 = bundle?.getString("BirdMaturingDays5").toString()//
        BirdMaturingDays6 = bundle?.getString("BirdMaturingDays6").toString()//
        BirdIncubatingDays1 = bundle?.getString("BirdIncubatingDays1").toString()//
        BirdIncubatingDays2 = bundle?.getString("BirdIncubatingDays2").toString()//
        BirdIncubatingDays3 = bundle?.getString("BirdIncubatingDays3").toString()//
        BirdIncubatingDays4 = bundle?.getString("BirdIncubatingDays4").toString()//
        BirdIncubatingDays5 = bundle?.getString("BirdIncubatingDays5").toString()//
        BirdIncubatingDays6 = bundle?.getString("BirdIncubatingDays6").toString()//
        BirdFather = bundle?.getString("BirdFather").toString()//
        BirdFatherKey = bundle?.getString("BirdFatherKey").toString()//
        val BirdFatherBirdKey = bundle?.getString("BirdFatherBirdKey")
        BirdMother = bundle?.getString("BirdMother").toString()//
        BirdMotherKey = bundle?.getString("BirdMotherKey").toString()//
        val BirdMotherBirdKey = bundle?.getString("BirdMotherBirdKey")

        if (bundle != null) {
            fromFlightAdapter = bundle.getBoolean("fromFlightListAdapter", false)
            fromNurseryAdapter = bundle.getBoolean("fromNurseryListAdapter", false)
        }
        cageKeyValue = bundle?.getString("CageKeyValue").toString()//
        flightType = bundle?.getString("FlightType")
        nurseryType = bundle?.getString("NurseryType")
        val otOhter = bundle?.getString("otOther")
        val cageBirdKey = bundle?.getString("CageBirdKey")


        Log.d(TAG, "BIRDKEY NEW $otOhter" + cageKeyValue)

        Clutch?.let { newBundle.putBoolean("Clutch", it) }
        newBundle.putString("CageBirdKey", cageBirdKey)
        newBundle.putString("BirdOtherOrigin", otOhter)
        newBundle.putString("NurseryKey", NurseryKey)
        newBundle.putString("SoldId", BirdSoldId)
        newBundle.putString("CageKey", cageKeyValue)
        newBundle.putString("BirdKey", BirdKey)
        newBundle.putString("FlightKey", FlightKey)
        newBundle.putString("BirdId", BirdId)
        newBundle.putString("BirdLegband", BirdLegband)
        newBundle.putString("BirdImage", BirdImage)
        newBundle.putString("BirdGender", BirdGender)
        newBundle.putString("BirdStatus", BirdStatus)
        newBundle.putString("BirdDateBirth", BirdDateBirth)
        newBundle.putString("BirdSalePrice", BirdSalePrice)
        newBundle.putString("BirdBuyer", BirdBuyer)
        newBundle.putString("BirdDeathReason", BirdDeathReason)
        newBundle.putString("BirdExchangeReason", BirdExchangeReason)
        newBundle.putString("BirdExchangeWith", BirdExchangeWith)
        newBundle.putString("BirdLostDetails", BirdLostDetails)
        newBundle.putString("BirdAvailCage", BirdAvailCage)
        newBundle.putString("BirdForsaleCage", BirdForsaleCage)
        newBundle.putString("BirdRequestedPrice", BirdRequestedPrice)
        newBundle.putString("BirdComment", BirdComment)
        newBundle.putString("BirdBuyPrice", BirdBuyPrice)
        newBundle.putString("BirdBoughtOn", BirdBoughtOn)
        newBundle.putString("BirdBoughtBreeder", BirdBoughtBreeder)
        newBundle.putString("BirdBreeder", BirdBreeder)
        newBundle.putString("BirdDeceaseDate", BirdDeceaseDate)
        newBundle.putString("BirdSoldDate", BirdSoldDate)
        newBundle.putString("BirdLostDate", BirdLostDate)
        newBundle.putString("BirdExchangeDate", BirdExchangeDate)
        newBundle.putString("BirdDonatedDate", BirdDonatedDate)
        newBundle.putString("BirdDonatedContact", BirdDonatedContact)
        newBundle.putString("BirdMutation1", BirdMutation1)
        newBundle.putString("BirdMutation2", BirdMutation2)
        newBundle.putString("BirdMutation3", BirdMutation3)
        newBundle.putString("BirdMutation4", BirdMutation4)
        newBundle.putString("BirdMutation5", BirdMutation5)
        newBundle.putString("BirdMutation6", BirdMutation6)
        newBundle.putString("BirdIncubatingDays1", BirdIncubatingDays1)
        newBundle.putString("BirdIncubatingDays2", BirdIncubatingDays2)
        newBundle.putString("BirdIncubatingDays3", BirdIncubatingDays3)
        newBundle.putString("BirdIncubatingDays4", BirdIncubatingDays4)
        newBundle.putString("BirdIncubatingDays5", BirdIncubatingDays5)
        newBundle.putString("BirdIncubatingDays6", BirdIncubatingDays6)
        newBundle.putString("BirdMaturingDays1", BirdMaturingDays1)
        newBundle.putString("BirdMaturingDays2", BirdMaturingDays2)
        newBundle.putString("BirdMaturingDays3", BirdMaturingDays3)
        newBundle.putString("BirdMaturingDays4", BirdMaturingDays4)
        newBundle.putString("BirdMaturingDays5", BirdMaturingDays5)
        newBundle.putString("BirdMaturingDays6", BirdMaturingDays6)


        newBundle.putString("BirdFather", BirdFather)
        newBundle.putString("BirdFatherKey", BirdFatherKey)
        newBundle.putString("BirdFatherBirdKey", BirdFatherBirdKey)
        newBundle.putString("BirdMother", BirdMother)
        newBundle.putString("BirdMotherKey", BirdMotherKey)
        newBundle.putString("BirdMotherBirdKey", BirdMotherBirdKey)
        newBundle.putBoolean("fromFlightListAdapter", fromFlightAdapter)
        newBundle.putBoolean("fromNurseryListAdapter", fromNurseryAdapter)
        newBundle.putString("NurseryType", nurseryType)
        newBundle.putString("FlightType", flightType)
        val birdGalleryFragment = BirdGalleryFragment()
        val birdBasicFragment = BirdBasicFragment()
        val birdOriginFragment = BirdOriginFragment()
        val birdBabiesFragment = BirdBabiesFragment()
        val birdPairingFragment = BirdPairingFragment()
        birdGalleryFragment.arguments = newBundle
        birdBasicFragment.arguments = newBundle
        birdOriginFragment.arguments = newBundle
        birdBabiesFragment.arguments = newBundle
        birdPairingFragment.arguments = newBundle
        ImageView = findViewById(R.id.imageView)

        if (BirdImage.isEmpty() || BirdImage == "null") {
            Glide.with(this)
                .load(R.drawable.nobirdimage)
                .placeholder(R.drawable.nobirdimage)
                .error(R.drawable.nobirdimage)
                .into(ImageView)

        } else {
            Glide.with(this)
                .load(BirdImage)
                .placeholder(R.drawable.nobirdimage)
                .error(R.drawable.nobirdimage)
                .into(ImageView)
        }

        val abcolortitle = resources.getColor(R.color.appbar)
        supportActionBar?.title = HtmlCompat.fromHtml(
            "<font color='$abcolortitle'>$BirdId</font>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        // Check if night mode is enabled
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_white)

        viewPager = findViewById(R.id.viewPager)
        tablayout = findViewById(R.id.tablayout)


        Log.d(TAG, "NewFatherKey: $BirdFatherKey")
        Log.d(TAG, "NurseryKEY: $NurseryKey")

        fragmentAdapter.addFragment(birdBasicFragment, "BASIC")
        fragmentAdapter.addFragment(birdOriginFragment, "RELATIONSHIPS")
        fragmentAdapter.addFragment(birdBabiesFragment, "CHILDS")
        fragmentAdapter.addFragment(birdPairingFragment, "PAIRINGS")
        fragmentAdapter.addFragment(birdGalleryFragment, "GALLERY")
        viewPager.adapter = fragmentAdapter
        tablayout.setupWithViewPager(viewPager)
        val currentUserId = mAuth.currentUser?.uid
        val qrRef = FirebaseDatabase.getInstance().getReference("Users")
            .child("ID: ${currentUserId.toString()}")
            .child("Birds").child(BirdKey)
        qrRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                CageQR = dataSnapshot.child("QR").value.toString()
                CageKey = dataSnapshot.child("CageKey").value.toString()
                CageBirdKey = dataSnapshot.child("Cage Bird Key").value.toString()
                SoldId = dataSnapshot.child("Sold Id").value.toString()
                PurchaseId = dataSnapshot.child("Purchase Id").value.toString()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors or onCancelled event
            }
        })


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.birddetailed_option, menu)



        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
//            R.id.menu_sell -> {
//                val i = Intent(this, SellActivity::class.java)
//                startActivity(i)
//                true
//            }

            R.id.menu_qr -> {
                val i = Intent(this, QRCodeActivity::class.java)
                i.putExtra("BirdId", BirdId)
                i.putExtra("Legband", BirdLegband)
                i.putExtra("Mutation1", BirdMutation1)
                i.putExtra("Mutation2", BirdMutation2)
                i.putExtra("Mutation3", BirdMutation3)
                i.putExtra("Mutation4", BirdMutation4)
                i.putExtra("Mutation5", BirdMutation5)
                i.putExtra("Mutation6", BirdMutation6)
                i.putExtra("BirdAvailCage", BirdAvailCage)
                i.putExtra("BirdForSaleCage", BirdForsaleCage)
                i.putExtra("BirdStatus", BirdStatus)
                i.putExtra("CageQR", CageQR)
                startActivity(i)
                true
            }

            R.id.menu_edit -> {

                if (flightType != "null") {

                    val flightRef = databaseReference.child("ID: $currentUser").child("Flight Birds")
                        .child(FlightKey)
                    flightRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val isPaired = dataSnapshot.child("Status").value.toString()

                            if (isPaired == "Paired") {
                                showPairedFlightBirdDialog1()
                            } else {
                                val i = Intent(this@BirdsDetailedActivity, EditBirdFlightActivity::class.java)
                                Log.d(TAG, flightType.toString())

                                i.putExtras(newBundle)
                                startActivity(i)
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Handle errors or onCancelled event
                        }
                    })

                } else {




                    val i = Intent(this, EditBirdActivity::class.java)
                    Log.d(TAG, nurseryType.toString())

                    i.putExtras(newBundle)
                    startActivity(i)
                }




                true
            }

//            R.id.menu_share -> {
//                // Handle the Remove button click here
//                // Implement the logic to remove the item or perform any action you need.
//                true
//            }

            R.id.menu_remove -> {
                val flightRef = databaseReference.child("ID: $currentUser").child("Flight Birds")
                    .child(FlightKey)

                flightRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val isPaired = dataSnapshot.child("Status").value.toString()
                        val descendantsRef = dataSnapshot.child("Descendants")

                        if (isPaired == "Paired") {
                            showPairedFlightBirdDialog()
                        } else if (descendantsRef.hasChildren()) {

                            showDescendantsDialog()
                        } else {
                            showDeleteConfirmation()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle errors or onCancelled event
                    }
                })



                return true

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
        builder.setMessage("Are you sure you want to delete this bird?")
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


        databaseReference.child("ID: $currentUser").child("Birds").child(BirdKey).removeValue()
        databaseReference.child("ID: $currentUser").child("Flight Birds").child(FlightKey)
            .removeValue()
        databaseReference.child("ID: $currentUser").child("Nursery Birds").child(NurseryKey)
            .removeValue()
        databaseReference.child("ID: $currentUser").child("Cages").child("Nursery Cages")
            .child(CageKey).child("Birds").child(CageBirdKey).removeValue()
        databaseReference.child("ID: $currentUser").child("Cages").child("Flight Cages")
            .child(CageKey).child("Birds").child(CageBirdKey).removeValue()
        databaseReference.child("ID: $currentUser").child("Sold Items").child(SoldId).removeValue()
        databaseReference.child("ID: $currentUser").child("Purchase Items").child(PurchaseId)
            .removeValue()

    }

    fun showPairedFlightBirdDialog() {
        // Show a dialog to inform the user that the flight bird is paired and cannot be deleted
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Cannot Delete")
        alertDialog.setMessage("This flight bird is paired and cannot be deleted.")
        alertDialog.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialog.show()
    }

    fun showPairedFlightBirdDialog1() {
        // Show a dialog to inform the user that the flight bird is paired and cannot be deleted
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Cannot Edit")
        alertDialog.setMessage("This flight bird is currently paired")
        alertDialog.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialog.show()
    }
    fun showDescendantsDialog() {
        // Show a dialog to inform the user that the flight bird has descendants and cannot be deleted
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Cannot Delete")
        alertDialog.setMessage("This flight bird has descendants and cannot be deleted.")
        alertDialog.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialog.show()
    }

}
