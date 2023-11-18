package com.example.qraviaryapp.activities.detailedactivities

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.NotFoundException
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import org.json.JSONException
import org.json.JSONObject

class MoveNurseryScannerActivity : AppCompatActivity() {

    private lateinit var choosecage: MaterialButton
    private lateinit var cageNameValue: String
    private var cageKeyValue: String? = null
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: DatabaseReference
    private var nurseryKey: String? = null
    private var cageKey: String? = null
    private var nurseryBirdKey: String? = null
    private lateinit var uploadqr: MaterialButton
    private var age = ""
    private var birdkey = ""
    private var cage = ""
    private var datebirth = ""
    private var gender = ""
    private var id = ""
    private var legband = ""
    private val GALLERY_REQUEST_CODE = 2
    private lateinit var mutation1: Map<String, String>
    private lateinit var mutation2: Map<String, String>
    private lateinit var mutation3: Map<String, String>
    private lateinit var mutation4: Map<String, String>
    private lateinit var mutation5: Map<String, String>
    private lateinit var mutation6: Map<String, String>
    private var father = ""
    private var mother = ""
    private var status = ""
    private lateinit var dataToCopy: Any
    var userId = ""

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
        uploadqr = findViewById(R.id.UploadQR)
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


        mAuth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().reference






        // Check if night mode is enabled
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_white)
        nurseryKey = intent.getStringExtra("Nursery Key")
        cageKey = intent.getStringExtra("CageKeyValue")
        nurseryBirdKey = intent.getStringExtra("BirdKey")
        Log.d(TAG,"keeey ${nurseryBirdKey.toString()}")
        userId = mAuth.currentUser?.uid.toString()
        val nurseryref =
            db.child("Users").child("ID: $userId").child("Nursery Birds")
                .child(nurseryKey.toString())

        nurseryref.child("Nursery Key").removeValue()
        nurseryref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {


                dataToCopy = snapshot.value!!

                age = snapshot.child("Age").value.toString()
                birdkey = snapshot.child("Bird Key").value.toString()
                cage = snapshot.child("Cage").value.toString()
                datebirth = snapshot.child("Date of Birth").value.toString()
                gender = snapshot.child("Gender").value.toString()
                id = snapshot.child("Identifier").value.toString()
                legband = snapshot.child("Legband").value.toString()
                mutation1 = mapOf(
                    "Mutation Name" to snapshot.child("Mutation1")
                        .child("Mutation Name").value.toString(),
                    "Maturing Days" to snapshot.child("Mutation1")
                        .child("Maturing Days").value.toString(),
                    "Incubating Days" to snapshot.child("Mutation1")
                        .child("Incubating Days").value.toString()
                )
                mutation2 = mapOf(
                    "Mutation Name" to snapshot.child("Mutation2")
                        .child("Mutation Name").value.toString(),
                    "Maturing Days" to snapshot.child("Mutation2")
                        .child("Maturing Days").value.toString(),
                    "Incubating Days" to snapshot.child("Mutation2")
                        .child("Incubating Days").value.toString()
                )
                mutation3 = mapOf(
                    "Mutation Name" to snapshot.child("Mutation3")
                        .child("Mutation Name").value.toString(),
                    "Maturing Days" to snapshot.child("Mutation3")
                        .child("Maturing Days").value.toString(),
                    "Incubating Days" to snapshot.child("Mutation3")
                        .child("Incubating Days").value.toString()
                )
                mutation4 = mapOf(
                    "Mutation Name" to snapshot.child("Mutation4")
                        .child("Mutation Name").value.toString(),
                    "Maturing Days" to snapshot.child("Mutation4")
                        .child("Maturing Days").value.toString(),
                    "Incubating Days" to snapshot.child("Mutation4")
                        .child("Incubating Days").value.toString()
                )
                mutation5 = mapOf(
                    "Mutation Name" to snapshot.child("Mutation5")
                        .child("Mutation Name").value.toString(),
                    "Maturing Days" to snapshot.child("Mutation5")
                        .child("Maturing Days").value.toString(),
                    "Incubating Days" to snapshot.child("Mutation5")
                        .child("Incubating Days").value.toString()
                )
                mutation6 = mapOf(
                    "Mutation Name" to snapshot.child("Mutation6")
                        .child("Mutation Name").value.toString(),
                    "Maturing Days" to snapshot.child("Mutation6")
                        .child("Maturing Days").value.toString(),
                    "Incubating Days" to snapshot.child("Mutation6")
                        .child("Incubating Days").value.toString()
                )


                father = snapshot.child("Father").value.toString()
                mother = snapshot.child("Mother").value.toString()
                status = snapshot.child("Status").value.toString()


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })




        codeScanner.apply {

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {

                try {
                    val jsonData = JSONObject(it.text)
                    if (jsonData.has("CageKey")) {
                        if (jsonData.getString("CageType") == "Flight") {
                            val cageKeyValue = jsonData.getString("CageKey")
                            val cageNumber = jsonData.getString("CageNumber")


                            val newFlightRef =
                                db.child("Users").child("ID: $userId").child("Flight Birds").push()

                            val birdPref = db.child("Users").child("ID: $userId").child("Birds").child(birdkey)
                            val flightCageRef = db.child("Users").child("ID: $userId").child("Cages")
                                .child("Flight Cages").child(cageKeyValue.toString()).child("Birds").push()

                            val _nurseryref =
                                db.child("Users").child("ID: $userId").child("Nursery Birds")
                                    .child(nurseryKey.toString())
                            val NurseryCageRef = db.child("Users").child("ID: $userId").child("Cages")
                                .child("Nursery Cages").child(cageKey.toString()).child("Birds")
                                .child(nurseryBirdKey.toString())
                            _nurseryref.removeValue()
                            NurseryCageRef.removeValue()

                            val key = newFlightRef.key

                            newFlightRef.setValue(dataToCopy)
                            val updateData = hashMapOf<String, Any?>("Flight Key" to key)



                            birdPref.updateChildren(updateData)
                            newFlightRef.updateChildren(updateData)
                            flightCageRef.setValue(dataToCopy)
                            flightCageRef.updateChildren(updateData)
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

    fun save() {

        if (TextUtils.isEmpty(choosecage.text)){
            choosecage.error = "Cage must not be Empty"
        }
        else {

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