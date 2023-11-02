package com.example.qraviaryapp.activities.detailedactivities

import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
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

class MoveEggScannerActivity : AppCompatActivity() {
    private lateinit var codeScanner: CodeScanner
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

        val activity = this
        setupPermission()
        codeScanner = CodeScanner(activity, scannerView)

        codeScanner.apply {

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {



            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
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


        val bundle = Bundle()
        bundle.putString("IncubatingStartDate", incubatingstartdate)
        bundle.putString("MaturingStartDate", maturingstartdate)
        bundle.putString("EggKey", eggkey)
        bundle.putString("IndividualEggKey", individualeggkey)
        bundle.putString("PairKey", pairkey)
        bundle.putString("PairFlightMaleKey", pairflightmalekey)
        bundle.putString("PairFlightFemaleKey", pairflightfemalekey)
        bundle.putString("PairMaleKey", pairmalekey)
        bundle.putString("PairFemaleKey", pairfemalekey)
        bundle.putString("PairMaleID", pairmaleid)
        bundle.putString("PairFemaleID", pairfemaleid)
        bundle.putString("DateOfBirth", dateofbirth)
        bundle.putString("CageKeyFemale", cagekeyfemale)
        bundle.putString("CageKeyMale", cagekeymale)
        bundle.putString("CageBirdFemale", cagebirdfemale)
        bundle.putString("CageBirdMale", cagebirdmale)


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