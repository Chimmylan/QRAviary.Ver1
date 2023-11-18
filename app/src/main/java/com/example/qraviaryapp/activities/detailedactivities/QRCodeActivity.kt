package com.example.qraviaryapp.activities.detailedactivities


import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.example.qraviaryapp.R
import com.example.qraviaryapp.fragments.generateFragment.GenerateEggFragment
import com.google.android.material.button.MaterialButton
import java.io.IOException

class QRCodeActivity : AppCompatActivity() {
    private lateinit var qrcode: ImageView
    private lateinit var qrimageLayout: LinearLayout
    private val REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE = 1 // You can use any integer value you prefer
    private lateinit var birdqrdetail: LinearLayout
    private lateinit var cageqrdetail: LinearLayout
    private lateinit var pairqrdetail: LinearLayout
    private lateinit var clutchqrdetail: LinearLayout
    private lateinit var bird_id: TextView
    private lateinit var bird_legband: TextView
    private lateinit var bird_mutation: TextView
    private lateinit var bird_cage: TextView
    private lateinit var cage: TextView
    private lateinit var pair_id: TextView
    private lateinit var pairclutchid: TextView
    private lateinit var pair_cage: TextView
    private lateinit var pair_mutation: TextView
    private lateinit var pair_date: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.statusbar)
        }
        setContentView(R.layout.activity_qrcode)
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
        pairclutchid = findViewById(R.id.pairclucthid)
        qrimageLayout = findViewById(R.id.qrimagelayout)
        bird_id = findViewById(R.id.birdid)
        bird_legband = findViewById(R.id.birdlegband)
        bird_mutation = findViewById(R.id.birdmutation)
        bird_cage = findViewById(R.id.birdcage)
        cage = findViewById(R.id.cage)
        pair_id= findViewById(R.id.pairid)
        pair_cage = findViewById(R.id.paircage)
        pair_date = findViewById(R.id.pairdate)
        pair_mutation = findViewById(R.id.pairmutation)
        val abcolortitle = resources.getColor(R.color.appbar)

        supportActionBar?.title = HtmlCompat.fromHtml(
            "<font color='$abcolortitle'>QR code</font>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        // Check if night mode is enabled
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_white)
        birdqrdetail = findViewById(R.id.birdqrdetail)
        cageqrdetail = findViewById(R.id.cageqrdetail)
        pairqrdetail = findViewById(R.id.pairqrdetail)
        clutchqrdetail = findViewById(R.id.clutchqrdetail)

        qrcode = findViewById(R.id.qrcode)
        val pairmaleid = intent?.getStringExtra("PairMaleID")
        val pairfemaleid = intent?.getStringExtra("PairFemaleID")
        val qr = intent?.getStringExtra("CageQR")
        val birdid = intent?.getStringExtra("BirdId")
        val birdlegband = intent?.getStringExtra("Legband")
        val mutation1 = intent?.getStringExtra("Mutation1")
        val mutation2 = intent?.getStringExtra("Mutation2")
        val mutation3 = intent?.getStringExtra("Mutation3")
        val mutation4 = intent?.getStringExtra("Mutation4")
        val mutation5 = intent?.getStringExtra("Mutation5")
        val mutation6 = intent?.getStringExtra("Mutation6")
        val birdavailcage = intent?.getStringExtra("BirdAvailCage")
        val birdforsalecage = intent?.getStringExtra("BirdForSaleCage")
        val birdstatus = intent?.getStringExtra("BirdStatus")
        val nurserycage = intent?.getStringExtra("CageNursery")
        val breedingcage = intent?.getStringExtra("CageBreeding")
        val flightcage = intent?.getStringExtra("CageFlight")
        val pairid = intent?.getStringExtra("PairId")
        val paircage = intent?.getStringExtra("Cage")
        val pairmutation = intent?.getStringExtra("Mutation")
        val pairdate  = intent?.getStringExtra("Date")
        val begdate = intent?.getStringExtra("BegDate")

        if (!pairid.isNullOrEmpty()){
            pairqrdetail.visibility = View.VISIBLE

            if (pairid == "0"){
                pair_id.visibility = View.GONE
                pair_date.text ="Date: " + pairdate
                pair_mutation.text = "Mutations: " +pairmutation
                pair_cage.visibility = View.GONE
            }
            else{
                pair_id.text = "Pair Id: " + pairid
                pair_date.text ="Beginning: " + begdate
                pair_mutation.text = "Mutations: " +pairmutation
                pair_cage.text = "Cage: B"+ paircage

            }

        }

        if (!nurserycage.isNullOrEmpty()){
            cageqrdetail.visibility = View.VISIBLE
            cage.text = "N" + nurserycage
        }
        if (!breedingcage.isNullOrEmpty()){
            cageqrdetail.visibility = View.VISIBLE
            cage.text = "B" + breedingcage
        }
        if (!flightcage.isNullOrEmpty()){
            cageqrdetail.visibility = View.VISIBLE
            cage.text = "F" + flightcage
        }

        if(!pairmaleid.isNullOrEmpty()){
            clutchqrdetail.visibility = View.VISIBLE
            pairclutchid.text = "Pair: " + pairfemaleid + " x " + pairmaleid

        }


        if (!birdid.isNullOrEmpty()){
            birdqrdetail.visibility = View.VISIBLE
            bird_id.text = "Id: "+ birdid

            if (!birdlegband.isNullOrEmpty()){
                bird_legband.text = "Legband: "+ birdlegband
            }
            else{
                bird_legband.visibility = View.GONE
            }
            val nonNullMutations = listOf(
                mutation1,
                mutation2,
                mutation3,
                mutation4,
                mutation5,
                mutation6
            ).filter { !it.isNullOrBlank() }
            val NonNullMutations = mutableListOf<String>()
            for (mutation in nonNullMutations) {
                if (mutation != "null") {
                    NonNullMutations.add(mutation.toString())
                }
            }
            val CombinedMutations = if (NonNullMutations.isNotEmpty()) {
                NonNullMutations.joinToString(" / ")

            } else {
                "Mutation: None"
            }
            bird_mutation.text = "Mutation: "+ CombinedMutations

            if (birdstatus == "Available" || birdstatus == "For Sale") {
                val cageInfo = when {
                    birdstatus == "Available" -> birdavailcage
                    birdstatus == "For Sale" -> birdforsalecage
                    else -> ""
                }

                if (cageInfo.isNullOrEmpty()) {
                    bird_cage.visibility = View.GONE
                } else {
                    bird_cage.visibility = View.VISIBLE
                    bird_cage.text = "Cage: $cageInfo"
                }
            } else {
                bird_cage.visibility = View.GONE
            }


        }
        val qrCodeUrl = qr

        Glide.with(this)
            .load(qrCodeUrl)
            .into(qrcode)

//        val cardView = findViewById<CardView>(R.id.cardView)

        val saveButton = findViewById<MaterialButton>(R.id.saveButton)
        saveButton.setOnClickListener {
            // Check for permission to write to external storage



        }
        saveButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // Permission is already granted. You can proceed with saving the image.
                saveImage()
            } else {
                // Request the WRITE_EXTERNAL_STORAGE permission.
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    GenerateEggFragment.WRITE_EXTERNAL_STORAGE_REQUEST_CODE
                )
            }
        }

    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            GenerateEggFragment.WRITE_EXTERNAL_STORAGE_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, you can now save the image.
                    saveImage()
                } else {
                    // Permission denied, show a message or handle it accordingly.
                    Toast.makeText(this, "Permission denied. Image cannot be saved.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    fun captureLayoutAsBitmap(view: View): Bitmap {
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        val bitmap = Bitmap.createBitmap(view.drawingCache)
        view.isDrawingCacheEnabled = false // Release the cache
        return bitmap
    }
    fun saveImage() {
        val layoutBitmap = captureLayoutAsBitmap(qrimageLayout)
        val uniqueImageName = "qr_image_${System.currentTimeMillis()}.jpg"
        save(layoutBitmap, uniqueImageName)
    }

    fun save(bitmap: Bitmap?, imageName: String) {
        val mimeType = "image/jpeg"

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, imageName)
            put(MediaStore.Images.Media.MIME_TYPE, mimeType)
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }

        val resolver = this.contentResolver
        val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        try {
            if (imageUri != null) {
                resolver.openOutputStream(imageUri)?.use { outputStream ->
                    bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                }
            }

            Toast.makeText(this, "Image saved to gallery as $imageName", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            Toast.makeText(this, "Error: ${e.toString()}", Toast.LENGTH_SHORT).show()
        }
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