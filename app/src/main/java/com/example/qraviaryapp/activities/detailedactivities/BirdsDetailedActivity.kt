package com.example.qraviaryapp.activities.detailedactivities

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.text.HtmlCompat
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.EditActivities.EditBirdActivity
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
    private lateinit var BirdFather: String
    private lateinit var BirdFatherKey: String
    private lateinit var BirdMother: String
    private lateinit var BirdMotherKey: String
    private lateinit var mAuth: FirebaseAuth
    //cages
    private var fromFlightAdapter: Boolean = false
    private var fromNurseryAdapter: Boolean = false
    private lateinit var cageKeyValue: String
    private lateinit var CageQR: String


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
        toolbar= findViewById(R.id.toolbar)
        supportActionBar?.elevation = 0f
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val bundle = intent.extras
        database = FirebaseDatabase.getInstance()
        databaseReference = database.getReference("Users")


        mAuth = FirebaseAuth.getInstance()

        BirdId = bundle?.getString("BirdId").toString()//
        BirdLegband = bundle?.getString("BirdLegband").toString()//
        BirdKey = bundle?.getString("BirdKey").toString()
        val nureseryKey = bundle?.getString("NurseryKey")//
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
        BirdDeceaseDate= bundle?.getString("BirdDeceaseDate").toString()//
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
        BirdFather = bundle?.getString("BirdFather").toString()//
        BirdFatherKey = bundle?.getString("BirdFatherKey").toString()//
        BirdMother= bundle?.getString("BirdMother").toString()//
        BirdMotherKey= bundle?.getString("BirdMotherKey").toString()//
        if (bundle != null) {
            fromFlightAdapter = bundle.getBoolean("fromFlightListAdapter", false)
            fromNurseryAdapter = bundle.getBoolean("fromNurseryListAdapter", false)
        }
        cageKeyValue = bundle?.getString("CageKeyValue").toString()//

        Log.d(TAG, "BIRDKEY NEW $BirdKey")

        newBundle.putString("NurseryKey", nureseryKey)
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
        newBundle.putString("BirdFather", BirdFather)
        newBundle.putString("BirdFatherKey", BirdFatherKey)
        newBundle.putString("BirdMother", BirdMother)
        newBundle.putString("BirdMotherKey", BirdMotherKey)
        newBundle.putBoolean("fromFlightListAdapter", fromFlightAdapter)
        newBundle.putBoolean("fromNurseryListAdapter", fromNurseryAdapter)
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


        fragmentAdapter.addFragment(birdBasicFragment, "BASIC")
        fragmentAdapter.addFragment(birdOriginFragment, "RELATIONSHIPS")
        fragmentAdapter.addFragment(birdBabiesFragment, "DESCENDANTS")
        fragmentAdapter.addFragment(birdPairingFragment, "PAIRINGS")
        fragmentAdapter.addFragment(birdGalleryFragment, "GALLERY")
        viewPager.adapter = fragmentAdapter
        tablayout.setupWithViewPager(viewPager)
        val currentUserId = mAuth.currentUser?.uid
        val qrRef = FirebaseDatabase.getInstance().getReference("Users").child("ID: ${currentUserId.toString()}")
            .child("Birds").child(BirdKey)
        qrRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                CageQR = dataSnapshot.child("QR").value.toString()
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
            R.id.menu_sell -> {
                val i = Intent(this, SellActivity::class.java)
                startActivity(i)
                true
            }
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
                i.putExtra("BirdStatus",BirdStatus)
                i.putExtra("CageQR", CageQR)
                startActivity(i)
                true
            }
            R.id.menu_edit -> {
                val i = Intent(this, EditBirdActivity::class.java)
                i.putExtras(newBundle)
                startActivity(i)
                true
            }
            R.id.menu_share -> {
                // Handle the Remove button click here
                // Implement the logic to remove the item or perform any action you need.
                true
            }
            R.id.menu_remove -> {
                showDeleteConfirmation()

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
        databaseReference.child("ID: $currentUser").child("Flight Birds").child(FlightKey).removeValue()
        databaseReference.child("ID: $currentUser").child("Nursery Birds").child(FlightKey).removeValue()
    }
}
