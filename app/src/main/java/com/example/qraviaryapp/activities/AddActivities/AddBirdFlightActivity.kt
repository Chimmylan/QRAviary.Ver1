package com.example.qraviaryapp.activities.AddActivities

import BirdData
import BirdDataListener
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.dashboards.BirdListActivity
import com.example.qraviaryapp.activities.mainactivities.NavHomeActivity
import com.example.qraviaryapp.adapter.FragmentAdapter
import com.example.qraviaryapp.fragments.AddFragment.AddGalleryFragment
import com.example.qraviaryapp.fragments.AddFragment.BasicFlightFragment
import com.example.qraviaryapp.fragments.AddFragment.BasicFragment
import com.example.qraviaryapp.fragments.AddFragment.OriginFragment
import com.example.qraviaryapp.fragments.NavFragments.BirdsFragment
import com.example.qraviaryapp.fragments.NavFragments.GalleryFragment
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class AddBirdFlightActivity : AppCompatActivity(), BirdDataListener {
    private lateinit var viewPager: ViewPager
    private lateinit var tablayout: TabLayout
    private lateinit var spinner: Spinner

    private var storageRef = FirebaseStorage.getInstance().reference
    private var mAuth = FirebaseAuth.getInstance()
    private var dbase = FirebaseDatabase.getInstance().reference
    private val BasicFragment: BasicFragment = BasicFragment()
    val fragmentAdapter = FragmentAdapter(supportFragmentManager)


    private var birdIdentifier: String? = null

    private lateinit var basicFragment: BasicFlightFragment
    private lateinit var originFragment: OriginFragment
    private lateinit var galleryFragment: AddGalleryFragment

    private var qrBundle = Bundle()

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
            "<font color='$abcolortitle'>Add Flight Bird</font>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_white)


        viewPager = findViewById(R.id.viewPager)
        tablayout = findViewById(R.id.tablayout)
        viewPager.offscreenPageLimit = 3


        val basicFragmentDeferred = BasicFlightFragment()
        val originFragmentDeferred = OriginFragment()
        val galleryFragmentDeferred = AddGalleryFragment()

        basicFragment = basicFragmentDeferred
        originFragment = originFragmentDeferred
        galleryFragment = galleryFragmentDeferred

        fragmentAdapter.addFragment(basicFragment, "Basic")
        fragmentAdapter.addFragment(originFragment, "Origin")
        fragmentAdapter.addFragment(galleryFragment, "Gallery")

        viewPager.adapter = fragmentAdapter
        tablayout.setupWithViewPager(viewPager)

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add_bird, menu)

        val saveMenuItem = menu.findItem(R.id.action_save)

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
            R.id.menu_qr -> {
                val requestCode = 0
                val intent = Intent(this, AddBirdScanActivity::class.java)
                startActivityForResult(intent, requestCode)
                true
            }
            R.id.action_save -> {

                val basicFragment = fragmentAdapter.getItem(0) as BasicFlightFragment
                val originFragment = fragmentAdapter.getItem(1) as OriginFragment
                val galleryFragment = fragmentAdapter.getItem(2) as AddGalleryFragment

                lifecycleScope.launch {
                    try {
                        var birdId = ""
                        var newBundle: Bundle = Bundle()
                        var flightId = ""
                        var soldId = ""
                        var cagebirdkey = ""
                        var cagekeyvalue = ""
                        basicFragment.birdDataGetters { receivedBirdId, receivedFlightId, receivedNewBundle, receivesoldId, receivecagebirdkey, receivecagekeyvalue ->
                            birdId = receivedBirdId
                            flightId = receivedFlightId
                            newBundle = receivedNewBundle
                            soldId = receivesoldId

                            cagebirdkey = receivecagebirdkey
                            cagekeyvalue = receivecagekeyvalue
                            originFragment.addFlightOrigin(birdId, flightId, newBundle, soldId)
                            { callBackMotherKey, callBackFatherKey, descendantfatherkey, descendantmotherkey, purchaseId, originBundle ->
                                galleryFragment.FlightuploadImageToStorage(
                                    birdId,
                                    flightId,
                                    newBundle,
                                    callBackMotherKey,
                                    callBackFatherKey,
                                    descendantfatherkey,
                                    descendantmotherkey,
                                    cagebirdkey,
                                    cagekeyvalue,
                                    soldId,
                                    purchaseId
                                )

                                flightToDetailedScanner(newBundle,originBundle)

                            }


                            onBackPressed()
                            finish()

                        }


                    } catch (e: NullPointerException) {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0){
            if (resultCode == RESULT_OK){
                birdIdentifier = data?.getStringExtra("BirdIdentifier").toString()
                qrBundle.putString("BirdIdentifier", birdIdentifier)
                Log.d(TAG,"ID:: $birdIdentifier")
                basicFragment.arguments = qrBundle

            }
        }
    }


    fun flightToDetailedScanner(basicFragmentBundle: Bundle, originFragmentBundle: Bundle) {

        val birdKey = basicFragmentBundle.getString("BirdKey")//
        val nurseryKey = basicFragmentBundle.getString("NurseryKey")
        val flightKey = basicFragmentBundle.getString("FlightKey")
        val birdLegband = basicFragmentBundle.getString("BirdLegband")
        val birdId = basicFragmentBundle.getString("BirdIdentifier")
        val birdImg = basicFragmentBundle.getString("BirdImage")
        val birdGender = basicFragmentBundle.getString("BirdGender")
        val birdStatus = basicFragmentBundle.getString("BirdStatus")
        val birdDateBirth = basicFragmentBundle.getString("BirdDateBirth")
        val birdSalePrice = basicFragmentBundle.getString("BirdSalePrice")
        val birdBuyer = basicFragmentBundle.getString("BirdBuyer")
        val birdDeatherReason = basicFragmentBundle.getString("BirdDeathReason")
        val birdExchangeReason = basicFragmentBundle.getString("BirdExchangeReason")
        val birdExchangeWith = basicFragmentBundle.getString("BirdExchangeWith")
        val birdLostDetails = basicFragmentBundle.getString("BirdLostDetails")
        val birdAvailCage = basicFragmentBundle.getString("BirdAvailCage")
        val birdForSaleCage = basicFragmentBundle.getString("BirdForsaleCage")
        val birdRequestedPrice = basicFragmentBundle.getString("BirdRequestedPrice")
        val birdBuyPrice = basicFragmentBundle.getString("BirdBuyPrice")
        val birdBoughtOn = basicFragmentBundle.getString("BirdBoughtOn")
        val birdBoughtBreeder = basicFragmentBundle.getString("BirdBoughtBreeder")
        val birdMutation1 = basicFragmentBundle.getString("BirdMutation1Name")
        val birdMutation2 = basicFragmentBundle.getString("BirdMutation2Name")
        val birdMutation3 = basicFragmentBundle.getString("BirdMutation3Name")
        val birdMutation4 = basicFragmentBundle.getString("BirdMutation4Name")
        val birdMutation5 = basicFragmentBundle.getString("BirdMutation5Name")
        val birdMutation6 = basicFragmentBundle.getString("BirdMutation6Name")
        val birdSoldDate = basicFragmentBundle.getString("BirdSoldDate")
        val birdDeceasedDate = basicFragmentBundle.getString("BirdDeceaseDate")
        val birdExchangeDate = basicFragmentBundle.getString("BirdExchangeDate")
        val birdLostDate = basicFragmentBundle.getString("BirdLostDate")
        val birdDonatedDate = basicFragmentBundle.getString("BirdDonatedDate")
        val birdDonatedContact = basicFragmentBundle.getString("BirdDonatedContact")


        val birdFather = originFragmentBundle.getString("BirdFather")
        val birdFatherKey = originFragmentBundle.getString("BirdFatherKey")
        val birdMother = originFragmentBundle.getString("BirdMother")
        val birdMotherKey = originFragmentBundle.getString("BirdMotherKey")


        //References
        //Bird
        //NurseryCage
        //NurseryBirds

        Log.d(TAG, birdFather.toString())
        Log.d(TAG, birdMotherKey.toString())

        val userId = mAuth.currentUser?.uid
        val userBird = dbase.child("Users").child("ID: $userId")
            .child("Birds").child(birdKey.toString())
        val NurseryBird = dbase.child("Users").child("ID: $userId")
            .child("Nursery Birds").child(nurseryKey.toString())


        val jsonData = JSONObject()

        jsonData.put("BirdDetailedQR", true)
        jsonData.put("BirdKey", birdKey)//
        jsonData.put("FlightKey", flightKey)
        jsonData.put("BirdLegband", birdLegband)
        jsonData.put("BirdId", birdId)
        jsonData.put("BirdImage", birdImg)
        jsonData.put("BirdGender", birdGender)
        jsonData.put("BirdStatus", birdStatus)
        jsonData.put("BirdDateBirth", birdDateBirth)
        jsonData.put("BirdSalePrice", birdSalePrice)
        jsonData.put("BirdBuyer", birdBuyer)
        jsonData.put("BirdDeathReason", birdDeatherReason)
        jsonData.put("BirdExchangeReason", birdExchangeReason)
        jsonData.put("BirdExchangeWith", birdExchangeWith)
        jsonData.put("BirdLostDetails", birdLostDetails)
        jsonData.put("BirdAvailCage", birdAvailCage)
        jsonData.put("BirdForsaleCage", birdForSaleCage)
        jsonData.put("BirdRequestedPrice", birdRequestedPrice)
        jsonData.put("BirdBuyPrice", birdBuyPrice)
        jsonData.put("BirdBoughtOn", birdBoughtOn)
        jsonData.put("BirdBoughtBreeder", birdBoughtBreeder)
        jsonData.put("BirdMutation1", birdMutation1)
        jsonData.put("BirdMutation2", birdMutation2)
        jsonData.put("BirdMutation3", birdMutation3)
        jsonData.put("BirdMutation4", birdMutation4)
        jsonData.put("BirdMutation5", birdMutation5)
        jsonData.put("BirdMutation6", birdMutation6)
        jsonData.put("BirdSoldDate", birdSoldDate)
        jsonData.put("BirdDeceaseDate", birdDeceasedDate)
        jsonData.put("BirdExchangeDate", birdExchangeDate)
        jsonData.put("BirdLostDate", birdLostDate)
        jsonData.put("BirdDonatedDate", birdDonatedDate)
        jsonData.put("BirdDonatedContact", birdDonatedContact)
        jsonData.put("BirdFather", birdFather)
        jsonData.put("BirdFatherKey", birdFatherKey)
        jsonData.put("BirdMother", birdMother)
        jsonData.put("BirdMotherKey", birdMotherKey)


        qrAdd(jsonData, NurseryBird, userBird)
//        val i = Intent(this, BirdsDetailedActivity::class.java)
    }

    private fun generateQRCodeUri(bundleCageData: String): Uri? {
        val multiFormatWriter = MultiFormatWriter()
        val bitMatrix = multiFormatWriter.encode(bundleCageData, BarcodeFormat.QR_CODE, 400, 400)
        val barcodeEncoder = BarcodeEncoder()
        val bitmap = barcodeEncoder.createBitmap(bitMatrix)

        // Create a file to store the QR code image
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File.createTempFile("QRCode", ".png", storageDir)

        try {
            val stream = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        // Convert the file URI to a string and return
        return Uri.fromFile(imageFile)
    }

    fun qrAdd(bundle: JSONObject, nurseryKey: DatabaseReference, birdKey: DatabaseReference) {


        val imageUri = generateQRCodeUri(bundle.toString())

        val imageRef = storageRef.child("images/${System.currentTimeMillis()}.jpg")
        val uploadTask = imageUri?.let { it1 -> imageRef.putFile(it1) }

        uploadTask?.addOnSuccessListener { task ->
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                val imageUrl = uri.toString()

                val dataQR: Map<String, Any?> = hashMapOf(
                    "QR" to imageUrl
                )
                nurseryKey.updateChildren(dataQR)
                birdKey.updateChildren(dataQR)
            }
        }
    }


    /* override fun onBackPressed() {
         // Create an intent to navigate back to BirdListActivity
         val intent = Intent(this, BirdListActivity::class.java)
         startActivity(intent)
         finish() // Finish the current activity (AddBirdActivity)
     }*/

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
