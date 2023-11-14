package com.example.qraviaryapp.activities.detailedactivities

import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.qraviaryapp.R
import com.example.qraviaryapp.fragments.CAMERA_REQUEST_CODE
import com.google.android.material.button.MaterialButton
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.NotFoundException
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class MoveEggScannerActivity : AppCompatActivity() {
    private lateinit var codeScanner: CodeScanner
    private lateinit var generate: MaterialButton
    private lateinit var uploadqr: MaterialButton
    private val GALLERY_REQUEST_CODE = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.statusbar)
        }
        setContentView(R.layout.activity_move_egg_scanner)
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
            "<font color='$abcolortitle'>Scan Nursery Cage</font>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        // Check if night mode is enabled
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_white)
        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)
        uploadqr = findViewById(R.id.UploadQR)
        val activity = this
        setupPermission()
        codeScanner = CodeScanner(activity, scannerView)







        codeScanner.apply {

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {

                try {
                    val jsonData = JSONObject(it.text)
                    if (jsonData.has("CageKey")) {
                        if (jsonData.getString("CageType") == "Nursery") {
                            val key = jsonData.getString("CageKey")
                            val cageNumber = jsonData.getString("CageNumber")


                            val incubatingstartdate = intent.getStringExtra("IncubatingStartDate")
                            val maturingstartdate = intent.getStringExtra("MaturingStartDate")
                            val eggkey = intent.getStringExtra("EggKey")
                            val individualeggkey = intent.getStringExtra("IndividualEggKey")
                            val pairkey = intent.getStringExtra("PairKey")
                            val pairflightmalekey = intent.getStringExtra("PairFlightMaleKey")
                            val pairflightfemalekey = intent.getStringExtra("PairFlightFemaleKey")
                            val pairmalekey = intent.getStringExtra("PairMaleKey")
                            val pairfemalekey = intent.getStringExtra("PairFemaleKey")
                            val pairmaleid = intent.getStringExtra("PairMaleID")
                            val pairfemaleid = intent.getStringExtra("PairFemaleID")
                            val dateofbirth = intent.getStringExtra("DateOfBirth")
                            val cagekeyfemale = intent.getStringExtra("CageKeyFemale")
                            val cagekeymale = intent.getStringExtra("CageKeyMale")
                            val cagebirdfemale = intent.getStringExtra("CageBirdFemale")
                            val cagebirdmale = intent.getStringExtra("CageBirdMale")

                            val intent = Intent(this@MoveEggScannerActivity, MoveEggActivity::class.java)
                            intent.putExtra("CageKey", key)
                            intent.putExtra("CageName", cageNumber)
                            intent.putExtra("IncubatingStartDate",incubatingstartdate)
                            intent.putExtra("MaturingStartDate", maturingstartdate)
                            intent.putExtra("EggKey", eggkey)
                            intent.putExtra("IndividualEggKey", individualeggkey)
                            intent.putExtra("PairKey", pairkey)
                            intent.putExtra("PairFlightMaleKey", pairflightmalekey)
                            intent.putExtra("PairFlightFemaleKey", pairflightfemalekey)
                            intent.putExtra("PairMaleKey", pairmalekey)
                            intent.putExtra("PairFemaleKey", pairfemalekey)
                            intent.putExtra("PairMaleID", pairmaleid)
                            intent.putExtra("PairFemaleID", pairfemaleid)
                            intent.putExtra("DateOfBirth", dateofbirth)
                            intent.putExtra("CageKeyFemale",cagekeyfemale)
                            intent.putExtra("CageKeyMale",cagekeymale)
                            intent.putExtra("CageBirdFemale",cagebirdfemale)
                            intent.putExtra("CageBirdMale",cagebirdmale)


                            startActivity(intent)
                        }
                    }

                    //if has cagekey
                    // means we are scanning the cages
                } catch (e: JSONException) {

                }


            }
        }
        uploadqr.setOnClickListener {
            openGalleryForImage()
        }
        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }

    }
    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    private fun decodeQrCodeFromBitmap(bitmap: Bitmap) {
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

        val source = RGBLuminanceSource(width, height, pixels)
        val binaryBitmap = BinaryBitmap(HybridBinarizer(source))

        try {
            val result = MultiFormatReader().decode(binaryBitmap)
            // Handle the result, e.g., call your existing decodeCallback
            codeScanner.decodeCallback?.onDecoded(result)
        } catch (e: NotFoundException) {
            // Handle exception if QR code is not found in the image
            Log.e(ContentValues.TAG, "QR code not found in the image")
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                GALLERY_REQUEST_CODE -> {
                    data?.data?.let { uri ->
                        try {
                            val inputStream = this.contentResolver.openInputStream(uri)
                            val bitmap = BitmapFactory.decodeStream(inputStream)
                            decodeQrCodeFromBitmap(bitmap)
                        } catch (e: IOException) {
                            Log.e(ContentValues.TAG, "Error loading image from gallery: ${e.message}")
                        }
                    }
                }
            }
        }
    }
    fun moveEgg(){


    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun setupPermission() {
        val permission =
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }

    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        this,
                        "You ned camera permission to be able to use this qr",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    //Successful
                }
            }
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