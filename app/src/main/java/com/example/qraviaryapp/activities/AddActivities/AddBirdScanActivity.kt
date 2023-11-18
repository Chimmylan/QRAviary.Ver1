package com.example.qraviaryapp.activities.AddActivities

import android.app.Activity
import android.content.ContentValues
import android.content.ContentValues.TAG
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
import org.json.JSONObject
import java.io.IOException

class AddBirdScanActivity : AppCompatActivity() {
    private lateinit var codeScanner: CodeScanner
    private lateinit var generate: MaterialButton
    private lateinit var uploadqr: MaterialButton
    private val GALLERY_REQUEST_CODE = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.statusbar)
        }
        setContentView(R.layout.activity_add_bird_scan)
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
            "<font color='$abcolortitle'>Scan Generate Bird QR</font>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        // Check if night mode is enabled
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_white)


        generate = findViewById(R.id.GenerateQR)
        uploadqr = findViewById(R.id.UploadQR)
        generate.setOnClickListener {
            startActivity(Intent(this, GenerateQrActivity::class.java))
        }
//
        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)

        val activity = this
        setupPermission()
        codeScanner = CodeScanner(activity, scannerView)







        codeScanner.apply {

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.SINGLE
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                val jsonData = JSONObject(it.text)
                if (jsonData.has("AddBirdQR")) {
                    Log.d(TAG,"ADDSCANQR")
                    val status = jsonData.getString("Status")
                    val i = Intent()
                    i.putExtra("BirdIdentifier", jsonData.getString("Identifier"))
                    i.putExtra("BirdLegband", jsonData.getString("LegBand"))
                    i.putExtra("BirdGender", jsonData.getString("Gender"))
                    Log.d(TAG,"Check")


                    if (jsonData.has("Mutation1")) {
                        i.putExtra("BirdMutation1", jsonData.optString("Mutation1", ""))
                    }
                    if (jsonData.has("Mutation2")) {
                        i.putExtra("BirdMutation2", jsonData.optString("Mutation2", ""))
                    }
                    if (jsonData.has("Mutation3")) {
                        i.putExtra("BirdMutation3", jsonData.optString("Mutation3", ""))
                    }
                    if (jsonData.has("Mutation4")) {
                        i.putExtra("BirdMutation4", jsonData.optString("Mutation4", ""))
                    }
                    if (jsonData.has("Mutation5")) {
                        i.putExtra("BirdMutation5", jsonData.optString("Mutation5", ""))
                    }
                    if (jsonData.has("Mutation6")) {
                        i.putExtra("BirdMutation6", jsonData.optString("Mutation6", ""))
                    }

                    val mutationMap1 = jsonData.optJSONObject("Mutation1Map")
                    if (mutationMap1 != null){
                        val mutation1MapString = mutationMap1.toString()
                        i.putExtra("BirdMutationMap1", mutation1MapString)
                    }
                    val mutationMap2 = jsonData.optJSONObject("Mutation2Map")
                    if (mutationMap2 != null){
                        val mutation2MapString = mutationMap2.toString()
                        i.putExtra("BirdMutationMap2", mutation2MapString)
                    }
                    val mutationMap3 = jsonData.optJSONObject("Mutation3Map")
                    if (mutationMap3 != null){
                        val mutation3MapString = mutationMap3.toString()
                        i.putExtra("BirdMutationMap3", mutation3MapString)
                    }
                    val mutationMap4 = jsonData.optJSONObject("Mutation4Map")
                    if (mutationMap4 != null){
                        val mutation4MapString = mutationMap4.toString()
                        i.putExtra("BirdMutationMap4", mutation4MapString)
                    }
                    val mutationMap5 = jsonData.optJSONObject("Mutation5Map")
                    if (mutationMap5 != null){
                        val mutation5MapString = mutationMap5.toString()
                        i.putExtra("BirdMutationMap5", mutation5MapString)
                    }
                    val mutationMap6 = jsonData.optJSONObject("Mutation6Map")
                    if (mutationMap6 != null){
                        val mutation6MapString = mutationMap6.toString()
                        i.putExtra("BirdMutationMap6", mutation6MapString)
                    }

                    i.putExtra("BirdBirthDate", jsonData.optString("BirthDate", null))
                    i.putExtra("BirdStatus", status)
                    i.putExtra("BirdFatherId", jsonData.optString("FatherId", null))
                    i.putExtra("BirdFatherKey", jsonData.optString("FatherKey", null))
                    i.putExtra("BirdFatherBirdKey", jsonData.optString("FatherBirdKey", null))
                    i.putExtra("BirdMotherId", jsonData.optString("MotherId", null))
                    i.putExtra("BirdMotherKey", jsonData.optString("MotherKey", null))
                    i.putExtra("BirdMotherBirdKey", jsonData.optString("MotherBirdKey", null))

                    i.putExtra("BirdCageName", jsonData.optString("CageName", null))
                    i.putExtra("BirdCageKey", jsonData.optString("CageKey", null))
                    when (status) {
                        "Available" -> {
                            i.putExtra("BirdAvailableCage", jsonData.optString("AvailableCage", null))
                        }
                        "For Sale" -> {
                            i.putExtra("BirdForSaleCage", jsonData.optString("ForSaleCage", null))
                            i.putExtra("BirdForSalePrice", jsonData.optString("ForSalePrice", null))
                        }
                        "Sold" -> {
                            i.putExtra("BirdSoldDate", jsonData.optString("SoldDate", null))
                            i.putExtra("BirdSoldPrice", jsonData.optString("SoldPrice", null))
                            i.putExtra("BirdSoldContact", jsonData.optString("SoldContact", null))
                        }
                        "Deceased" -> {
                            i.putExtra("BirdDeceasedDate", jsonData.optString("DeceasedDate", null))
                            i.putExtra("BirdDeceasedReason", jsonData.optString("DeceasedReason", null))
                        }
                        "Exchanged" -> {
                            i.putExtra("BirdExchangeDate", jsonData.optString("ExchangedDate", null))
                            i.putExtra("BirdExchangeReason", jsonData.optString("ExchangedReason", null))
                            i.putExtra("BirdExchangeContact", jsonData.optString("ExchangedContact", null))
                        }
                        "Lost" -> {
                            i.putExtra("BirdLostDate", jsonData.optString("LostDate", null))
                            i.putExtra("BirdLostDetails", jsonData.optString("LostDetails", null))
                        }
                        "Donated" -> {
                            i.putExtra("BirdDonatedDate", jsonData.optString("DonatedDate", null))
                            i.putExtra("BirdDonatedContact", jsonData.optString("DonatedContact", null))
                        }
                        "Other" -> {
                            i.putExtra("BirdOtherComment", jsonData.optString("OtherComment", null))
                        }
                        else -> {
                            // Handle the case when 'status' is null or not recognized
                        }
                    }

                    val provenance = jsonData.optString("Provenance", null)
                    Log.d(TAG, "SCAN PROVENANCE" + provenance)
                    if (provenance == "Bought") {
                        i.putExtra("BirdBreederContact", jsonData.optString("BreederContact", null))
                        i.putExtra("BirdBreederBuyPrice", jsonData.optString("BreederBuyPrice", null))
                        i.putExtra("BirdBreederBuyDate", jsonData.optString("BreederBuyDate", null))
                    } else if (provenance == "Other") {
                        i.putExtra("BirdOtherOrigin", jsonData.optString("OtherOrigin", null))
                    }

                    i.putExtra("BirdProvenance", provenance)


                    activity.setResult(Activity.RESULT_OK, i)
                    activity.finish()
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