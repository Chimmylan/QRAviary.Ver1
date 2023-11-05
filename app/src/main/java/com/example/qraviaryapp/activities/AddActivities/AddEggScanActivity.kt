package com.example.qraviaryapp.activities.AddActivities

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
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
import com.example.qraviaryapp.adapter.MyAlarmReceiver
import com.example.qraviaryapp.fragments.CAMERA_REQUEST_CODE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class AddEggScanActivity : AppCompatActivity() {
    private lateinit var codeScanner: CodeScanner
    private val storageRef = FirebaseStorage.getInstance().reference

    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: DatabaseReference


    private var eggKey: String? = null
    private var pairKey: String? = null

    private lateinit var hatchingDateTime: LocalDateTime
    private val formatter1 = DateTimeFormatter.ofPattern("MMM d yyyy hh:mm a", Locale.US)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.statusbar)
        }
        setContentView(R.layout.activity_add_egg_scan)
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
            "<font color='$abcolortitle'>Scan Egg QR</font>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        // Check if night mode is enabled
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_white)
        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)

        val activity = this
        setupPermission()
        codeScanner = CodeScanner(activity, scannerView)

        eggKey = intent.getStringExtra("EggKey")
//        individualEggKey = intent.getStringExtra("IndividualEggKey")
        pairKey = intent.getStringExtra("PairKey")



        db = FirebaseDatabase.getInstance().reference
        mAuth = FirebaseAuth.getInstance()

        val currentUserId = mAuth.currentUser?.uid


        codeScanner.apply {

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.SINGLE
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {

                val jsonData = JSONObject(it.text)
                if (jsonData.has("AddEggQR")){

                    val eggRef =
                        db.child("Users").child("ID: $currentUserId").child("Pairs").child(pairKey.toString())
                            .child("Clutches")
                            .child(eggKey.toString()).push()

                    val status = jsonData.get("EggStatus").toString()
                    val date = jsonData.get("EggDate").toString()
                    val incubatingDays = jsonData.get("IncubatingDays").toString()
                    val maturingDays = jsonData.get("MaturingDays").toString()

                    var currentDate = LocalDate.now()
                    val formatter = DateTimeFormatter.ofPattern("MMM d yyyy", Locale.US)

                    val formattedDate = currentDate.format(formatter)

                    val currentDateTime = LocalDateTime.now()
                    hatchingDateTime = currentDateTime.plusDays(incubatingDays.toLong())

                    val eggRandomID = kotlin.random.Random.nextInt()

                    if (status == "Incubating") {
                        eggRef.child("Date").setValue(date)
                        val data: Map<String, Any?> = hashMapOf(
                            "Incubating Days" to incubatingDays,
                            "Maturing Days" to maturingDays,
                            "Estimated Hatching Date" to hatchingDateTime.format(formatter1),
                            "Alarm ID" to eggRandomID,
                            "Status" to status
                        )
                        //TODO: Current date else statement
                        eggRef.updateChildren(data)

                    } else if (status == "Hatched") {
                        eggRef.child("Date").setValue(date)
                        val data: Map<String, Any?> = hashMapOf(
                            "Incubating Days" to incubatingDays,
                            "Maturing Days" to maturingDays,
                            "Status" to status
                        )
                        //TODO: Current date else statement
                        eggRef.updateChildren(data)

                    } else {

                        val data: Map<String, Any?> = hashMapOf(
                            "Incubating Days" to incubatingDays,
                            "Maturing Days" to maturingDays,
                            "Date" to formattedDate,
                            "Status" to status
                        )
                        eggRef.updateChildren(data)
                    }

                    val bundleData = JSONObject()
                    bundleData.put("EggClutchQR", true)
                    bundleData.put("IncubatingStartDate",incubatingDays)
                    bundleData.put("MaturingStartDate",  maturingDays)
                    bundleData.put("EggKey", eggKey)
                    bundleData.put("IndividualEggKey", eggRef.key)
                    bundleData.put("PairKey", pairKey)
                    qrAdd(bundleData, eggRef)


                    setAlarmForEgg(this@AddEggScanActivity, hatchingDateTime.format(formatter1), eggRandomID)




                }
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }

    }

    private fun generateQRCodeUri(bundleCageData: String): Uri? {
        val multiFormatWriter = MultiFormatWriter()
        val bitMatrix = multiFormatWriter.encode(bundleCageData, BarcodeFormat.QR_CODE, 400, 400)
        val barcodeEncoder = BarcodeEncoder()
        val bitmap = barcodeEncoder.createBitmap(bitMatrix)

        // Create a file to store the QR code image
        val storageDir =  getExternalFilesDir(Environment.DIRECTORY_PICTURES)
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

    fun qrAdd(bundle: JSONObject, pushKey: DatabaseReference){


        val imageUri = generateQRCodeUri(bundle.toString())

        val imageRef = storageRef.child("images/${System.currentTimeMillis()}.jpg")
        val uploadTask = imageUri?.let { it1 -> imageRef.putFile(it1) }

        uploadTask?.addOnSuccessListener { task ->
            imageRef.downloadUrl.addOnSuccessListener{ uri->
                val imageUrl = uri.toString()

                val dataQR: Map<String, Any?> = hashMapOf(
                    "QR" to imageUrl
                )
                pushKey.updateChildren(dataQR)
            }
        }
    }

    fun setAlarmForEgg(context: Context, estimatedHatchDate: String, eggIndex: Int) {

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, MyAlarmReceiver::class.java)
        intent.putExtra("egg_index", eggIndex) // Pass the index of the egg

        intent.putExtra("pairkey", pairKey)


        intent.putExtra("estimatedHatchDate", hatchingDateTime.format(formatter1))
        val pendingIntent = PendingIntent.getBroadcast(context, eggIndex, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val hatchDateTime = LocalDateTime.parse(estimatedHatchDate, formatter1)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = hatchDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

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