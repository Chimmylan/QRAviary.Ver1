package com.example.qraviaryapp.activities.detailedactivities

import android.content.ContentValues.TAG
import android.graphics.drawable.ColorDrawable
import android.media.Image
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.qraviaryapp.R
import com.example.qraviaryapp.adapter.FragmentAdapter
import com.example.qraviaryapp.fragments.DetailedFragment.BirdBabiesFragment
import com.example.qraviaryapp.fragments.DetailedFragment.BirdBasicFragment
import com.example.qraviaryapp.fragments.DetailedFragment.BirdGalleryFragment
import com.example.qraviaryapp.fragments.DetailedFragment.BirdOriginFragment
import com.example.qraviaryapp.fragments.DetailedFragment.BirdPairingFragment
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


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
    private lateinit var databaseReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.statusBarColor = ContextCompat.getColor(this, R.color.bottom_nav_background)
//        }
        setContentView(R.layout.activity_birds_detailed)

        toolbar= findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val bundle = intent.extras

        val newBundle = Bundle()
        database = FirebaseDatabase.getInstance()
        databaseReference = database.getReference("Users")

        BirdId = bundle?.getString("BirdId").toString()
        BirdLegband = bundle?.getString("BirdLegband").toString()
        BirdKey = bundle?.getString("BirdKey").toString()
        FlightKey = bundle?.getString("FlightKey").toString()
        BirdImage = bundle?.getString("BirdImage").toString()
        BirdGender = bundle?.getString("BirdGender").toString()
        BirdStatus = bundle?.getString("BirdStatus").toString()
        BirdDateBirth = bundle?.getString("BirdDateBirth").toString()
        BirdSalePrice = bundle?.getString("BirdSalePrice").toString()
        BirdBuyer = bundle?.getString("BirdBuyer").toString()
        BirdDeathReason = bundle?.getString("BirdDeathReason").toString()
        BirdExchangeWith = bundle?.getString("BirdExchangeWith").toString()
        BirdLostDetails = bundle?.getString("BirdLostDetails").toString()
        BirdAvailCage = bundle?.getString("BirdAvailCage").toString()
        BirdForsaleCage = bundle?.getString("BirdForsaleCage").toString()
        BirdExchangeReason = bundle?.getString("BirdDeathReason").toString()
        BirdRequestedPrice = bundle?.getString("BirdRequestedPrice").toString()
        BirdComment = bundle?.getString("BirdComment").toString()
        BirdBuyPrice = bundle?.getString("BirdBuyPrice").toString()
        BirdBoughtOn = bundle?.getString("BirdBoughtOn").toString()
        BirdBoughtBreeder = bundle?.getString("BirdBoughtBreeder").toString()
        BirdBreeder = bundle?.getString("BirdBreeder").toString()
        BirdDeceaseDate= bundle?.getString("BirdDeceaseDate").toString()
        BirdSoldDate = bundle?.getString("BirdSoldDate").toString()
        BirdLostDate = bundle?.getString("BirdLostDate").toString()
        BirdExchangeDate = bundle?.getString("BirdExchangeDate").toString()
        BirdDonatedDate = bundle?.getString("BirdDonatedDate").toString()
        BirdDonatedContact = bundle?.getString("BirdDonatedContact").toString()
        BirdMutation1 = bundle?.getString("BirdMutation1").toString()
        BirdMutation2 = bundle?.getString("BirdMutation2").toString()
        BirdMutation3 = bundle?.getString("BirdMutation3").toString()
        BirdMutation4 = bundle?.getString("BirdMutation4").toString()
        BirdMutation5 = bundle?.getString("BirdMutation5").toString()
        BirdMutation6 = bundle?.getString("BirdMutation6").toString()
        BirdFather = bundle?.getString("BirdFather").toString()
        BirdFatherKey = bundle?.getString("BirdFatherKey").toString()
        BirdMother= bundle?.getString("BirdMother").toString()
        BirdMotherKey= bundle?.getString("BirdMotherKey").toString()
        Log.d(TAG, "BIRDKEY NEW $BirdKey")

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
//            val view = findViewById<View>(R.id.view)
            val collapsingToolbar = findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar)
//            val appBarLayout = findViewById<AppBarLayout>(R.id.app_bar_layout)
//
//            val params = collapsingToolbar.layoutParams as AppBarLayout.LayoutParams
//            params.scrollFlags = 0 // Remove the scroll flag
//            collapsingToolbar.layoutParams = params
            Glide.with(this)
                .load(R.drawable.nobirdimage)
                .placeholder(R.drawable.nobirdimage)
                .error(R.drawable.nobirdimage)
                .into(ImageView)
////            view.visibility = GONE

//            val toolbar = findViewById<Toolbar>(R.id.toolbar)
//            val appBarLayout = findViewById<AppBarLayout>(R.id.app_bar_layout)
//
//            // Set the height of the AppBarLayout to match the Toolbar's height
//
//            val toolbarLayoutParams = toolbar.layoutParams
//            val appBarLayoutParams = view.layoutParams
//            appBarLayoutParams.height = toolbarLayoutParams.height
//            view.layoutParams = appBarLayoutParams
//            ImageView.visibility = GONE

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
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            // Set the white back button for night mode
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_white)
        } else {
            // Set the black back button for non-night mode
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_black)
        }

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


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.birddetailed_option, menu)



        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_sell -> {
                // Handle the Remove button click here
                // Implement the logic to remove the item or perform any action you need.
                true
            }
            R.id.menu_edit -> {
                // Handle the Remove button click here
                // Implement the logic to remove the item or perform any action you need.
                true
            }
            R.id.menu_share -> {
                // Handle the Remove button click here
                // Implement the logic to remove the item or perform any action you need.
                true
            }
            R.id.menu_remove -> {
                databaseReference.child(BirdKey).removeValue()
                finish() // Close the activity or navigate back to the previous screen
                return true

            }
            android.R.id.home -> {
                onBackPressed() // Call this to navigate back to the previous fragment
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
