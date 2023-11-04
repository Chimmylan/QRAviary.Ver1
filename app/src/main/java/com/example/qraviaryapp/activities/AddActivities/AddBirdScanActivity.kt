package com.example.qraviaryapp.activities.AddActivities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
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
import org.json.JSONObject

class AddBirdScanActivity : AppCompatActivity() {
    private lateinit var codeScanner: CodeScanner
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
                    val status = jsonData.getString("Status")
                    val i = Intent()
                    i.putExtra("BirdIdentifier", jsonData.getString("Identifier"))
                    i.putExtra("BirdLegband", jsonData.getString("LegBand"))
                    i.putExtra("BirdGender", jsonData.getString("Gender"))

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

                    if (jsonData.has("Mutation1Map")) {
                        i.putExtra("BirdMutationMap1", jsonData.optString("Mutation1Map", ""))
                    }
                    if (jsonData.has("Mutation2Map")) {
                        i.putExtra("BirdMutationMap2", jsonData.optString("Mutation2Map", ""))
                    }
                    if (jsonData.has("Mutation3Map")) {
                        i.putExtra("BirdMutationMap3", jsonData.optString("Mutation3Map", ""))
                    }
                    if (jsonData.has("Mutation4Map")) {
                        i.putExtra("BirdMutationMap4", jsonData.optString("Mutation4Map", ""))
                    }
                    if (jsonData.has("Mutation5Map")) {
                        i.putExtra("BirdMutationMap5", jsonData.optString("Mutation5Map", ""))
                    }
                    if (jsonData.has("Mutation6Map")) {
                        i.putExtra("BirdMutationMap6", jsonData.optString("Mutation6Map", ""))
                    }

                    i.putExtra("BirdBirthDate", jsonData.getString("BirthDate"))
                    i.putExtra("BirdStatus", jsonData.getString("Status"))
                    i.putExtra("BirdFatherId", jsonData.getString("FatherId"))
                    i.putExtra("BirdFatherKey", jsonData.getString("FatherKey"))
                    i.putExtra("BirdFatherBirdKey", jsonData.getString("FatherBirdKey"))
                    i.putExtra("BirdMotherId", jsonData.getString("MotherId"))
                    i.putExtra("BirdMotherKey", jsonData.getString("MotherKey"))
                    i.putExtra("BirdMotherBirdKey", jsonData.getString("MotherBirdKey"))
                    i.putExtra("BirdCageName", jsonData.getString("CageName"))
                    i.putExtra("BirdCageKey", jsonData.getString("CageKey"))
                    if (status == "Available") {
                        i.putExtra("BirdAvailableCage", jsonData.getString("AvailableCage"))
                    } else if (status == "For Sale") {
                        i.putExtra("BirdForSaleCage", jsonData.getString("ForSaleCage"))
                        i.putExtra("BirdForSalePrice", jsonData.getString("ForSalePrice"))
                    } else if (status == "Sold") {
                        i.putExtra("BirdSoldDate", jsonData.getString("SoldDate"))
                        i.putExtra("BirdSoldPrice", jsonData.getString("SoldPrice"))
                        i.putExtra("BirdSoldContact", jsonData.getString("SoldContact"))
                    } else if (status == "Deceased") {
                        i.putExtra("BirdDeceasedDate", jsonData.getString("DeceasedDate"))
                        i.putExtra("BirdDeceasedReason", jsonData.getString("DeceasedReason"))
                    } else if (status == "Exchanged") {
                        i.putExtra("BirdExchangeDate", jsonData.getString("ExchangedDate"))
                        i.putExtra("BirdExchangeReason", jsonData.getString("ExchangedReason"))
                        i.putExtra("BirdExchangeContact", jsonData.getString("ExchangedContact"))
                    } else if (status == "Lost") {
                        i.putExtra("BirdLostDate", jsonData.getString("LostDate"))
                        i.putExtra("BirdLostDetails", jsonData.getString("LostDetails"))
                    } else if (status == "Donated") {
                        i.putExtra("BirdDonatedDate", jsonData.getString("DonatedDate"))
                        i.putExtra("BirdDonatedContact", jsonData.getString("DonatedContact"))
                    } else if (status == "Other") {
                        i.putExtra("BirdOtherComment", jsonData.getString("OtherComment"))
                    }

                    activity.setResult(Activity.RESULT_OK, i)
                    activity.finish()
                }
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
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