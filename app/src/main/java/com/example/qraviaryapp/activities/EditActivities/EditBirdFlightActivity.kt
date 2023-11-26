package com.example.qraviaryapp.activities.EditActivities

import BirdData
import BirdDataListener
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.example.qraviaryapp.R
import com.example.qraviaryapp.adapter.FragmentAdapter
import com.example.qraviaryapp.fragments.AddFragment.BasicFragment
import com.example.qraviaryapp.fragments.AddFragment.EditGalleryFragment
import com.example.qraviaryapp.fragments.AddFragment.EditOriginFragment
import com.example.qraviaryapp.fragments.EditFragment.EditBasicFlightFragment
import com.example.qraviaryapp.fragments.EditFragment.EditBasicFragment
import com.example.qraviaryapp.fragments.NavFragments.BirdsFragment
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch

class EditBirdFlightActivity : AppCompatActivity(), BirdDataListener {
    private lateinit var viewPager: ViewPager
    private lateinit var tablayout: TabLayout
    private lateinit var spinner: Spinner
    private val BasicFragment: BasicFragment = BasicFragment()
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
    private var fromFlightAdapter: Boolean = false
    private var fromNurseryAdapter: Boolean = false
    private lateinit var cageKeyValue: String
    private lateinit var pairkey: String


    private lateinit var databaseReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var currentUser: String
    private lateinit var Auth: FirebaseAuth
    val fragmentAdapter = FragmentAdapter(supportFragmentManager)
    val newBundle = Bundle()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.statusbar)
        }
        setContentView(R.layout.activity_add_bird)

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
            "<font color='$abcolortitle'>Edit</font>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_white)


        val bundle = intent.extras

        BirdId = bundle?.getString("BirdId").toString()
        BirdLegband = bundle?.getString("BirdLegband").toString()
        BirdKey = bundle?.getString("BirdKey").toString()
        pairkey = bundle?.getString("PairKey").toString()
        val nureseryKey = bundle?.getString("NurseryKey")
        FlightKey = bundle?.getString("FlightKey").toString()
        BirdImage = bundle?.getString("BirdImage").toString()
        BirdGender = bundle?.getString("BirdGender").toString()
        BirdStatus = bundle?.getString("BirdStatus").toString()
        BirdDateBirth = bundle?.getString("BirdDateBirth").toString()
        BirdSalePrice = bundle?.getString("BirdSalePrice").toString()
        BirdBuyer = bundle?.getString("BirdBuyer").toString()
        BirdDeathReason = bundle?.getString("BirdDeathReason").toString()
        BirdExchangeWith = bundle?.getString("BirdExchangeWith").toString()
        BirdExchangeReason = bundle?.getString("BirdExchangeReason").toString()
        BirdLostDetails = bundle?.getString("BirdLostDetails").toString()
        BirdAvailCage = bundle?.getString("BirdAvailCage").toString()
        BirdForsaleCage = bundle?.getString("BirdForsaleCage").toString()
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
        val BirdMotherBirdKey = bundle?.getString("BirdMotherBirdKey")
        val BirdFatherBirdKey = bundle?.getString("BirdFatherBirdKey")

        cageKeyValue = bundle?.getString("CageKey").toString()
        val otOther = bundle?.getString("BirdOtherOrigin").toString()
        val flightType = bundle?.getString("FlightType").toString()
        val nurseryType = bundle?.getString("NurseryType").toString()
        val clutch = bundle?.getBoolean("Clutch")
        val cageBirdKey =bundle?.getString("CageBirdKey")
        val soldid =bundle?.getString("SoldId")
        if (clutch != null) {
            newBundle.putBoolean("Clutch", clutch)
        }

        Log.d(TAG, BirdFatherBirdKey.toString())
        Log.d(TAG, BirdMotherBirdKey.toString())



        newBundle.putString("SoldId", soldid)
        newBundle.putString("CageBirdKey", cageBirdKey)
        newBundle.putString("BirdOtherOrigin", otOther)
        newBundle.putString("NurseryType", nurseryType)
        newBundle.putString("FlightType", flightType)
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
        newBundle.putString("BirdFatherBirdKey", BirdFatherBirdKey)
        newBundle.putString("BirdMother", BirdMother)
        newBundle.putString("BirdMotherKey", BirdMotherKey)
        newBundle.putString("BirdMotherBirdKey", BirdMotherBirdKey)
        newBundle.putBoolean("fromFlightListAdapter", fromFlightAdapter)
        newBundle.putBoolean("fromNurseryListAdapter", fromNurseryAdapter)



        Log.d(TAG, "Hello from EditBirdFlightActivity ")



        viewPager = findViewById(R.id.viewPager)
        tablayout = findViewById(R.id.tablayout)
        viewPager.offscreenPageLimit = 3





        val basicFragment = EditBasicFlightFragment()
        val originFragment = EditOriginFragment()
        val galleryFragment = EditGalleryFragment()
        basicFragment.arguments = newBundle
        originFragment.arguments = newBundle
        galleryFragment.arguments = newBundle
        fragmentAdapter.addFragment(basicFragment, "Basic")
        fragmentAdapter.addFragment(originFragment, "Origin")
        fragmentAdapter.addFragment(galleryFragment, "Gallery")

        viewPager.adapter = fragmentAdapter
        tablayout.setupWithViewPager(viewPager)

    }





    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add_bird, menu)

        val saveMenuItem = menu.findItem(R.id.action_save)
        val menuqr = menu.findItem(R.id.menu_qr)
        menuqr.isVisible = false
        // Check if night mode is enabled
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            // Set the text color to white for night mode
            saveMenuItem.title = HtmlCompat.fromHtml(
                "<font color='#FFFFFF'>Save</font>",
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        } else {
            // Set the text color to black for non-night mode
            saveMenuItem.title = HtmlCompat.fromHtml(
                "<font color='#000000'>Save</font>",
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        }



        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {


                val basicFragment = fragmentAdapter.getItem(0) as EditBasicFlightFragment
                val originFragment = fragmentAdapter.getItem(1) as EditOriginFragment
                val galleryFragment = fragmentAdapter.getItem(2) as EditGalleryFragment


                lifecycleScope.launch {
                    try {
                        var birdId = ""
                        var newBundle: Bundle = Bundle()
                        var nurseryId = ""
                        var soldId = ""
                        var cagebirdkey = ""
                        var cagekeyvalue = ""
                        var successBasic = false

                        basicFragment.birdDataGetters { receivedbirdId, NurseryId, receivednewBundle, receivesoldId, receivecagebirdkey, receivecagekeyvalue->
                            birdId = receivedbirdId
                            nurseryId = NurseryId
                            newBundle = receivednewBundle
                            soldId = receivesoldId
                            cagebirdkey = receivecagebirdkey
                            cagekeyvalue = receivecagekeyvalue
                            successBasic = true
                            originFragment.addFlightOrigin(birdId, nurseryId, newBundle, soldId,successBasic)
                            { callBackMotherKey, callBackFatherKey, descendantfatherkey, descendantmotherkey, purchaseId, newOriginalBundle->
                                galleryFragment.FlightuploadImageToStorage(birdId,
                                    nurseryId,
                                    newBundle,
                                    callBackMotherKey,
                                    callBackFatherKey,
                                    descendantfatherkey,
                                    descendantmotherkey,
                                    cagebirdkey,
                                    cagekeyvalue,
                                    soldId,
                                    purchaseId)
                            }

                            onBackPressed()
                            finish()

                        }



                        // Now that the background work is done, switch to the main thread



                    } catch (e: NullPointerException) {
                        // Handle the exception if needed
                        Toast.makeText(
                            applicationContext,
                            "Gender and Provenance in Origin tab must not be empty...",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }


                // Handle the Save button click here
                // Implement the logic to save the bird or perform any action you need.
                true
            }
            android.R.id.home -> {
                onBackPressed() // Call this to navigate back to the previous fragment
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    fun openDatePicker(view: View) {}
    fun openDatebandPicker(view: View) {}
    fun openDatebirthPicker(view: View) {}
    fun openSoldDatePicker(view: View) {}
    fun openDeathDatePicker(view: View) {}
    fun openExchangeDatePicker(view: View) {}
    fun openLostDatePicker(view: View) {}
    fun openDonatedDatePicker(view: View) {}
    fun openBoughtDatePicker(view: View) {}

    override fun onBirdDataSaved(id: String?) {
    }

    override fun onBirdDateSaved(birdData: BirdData) {
    }

}
