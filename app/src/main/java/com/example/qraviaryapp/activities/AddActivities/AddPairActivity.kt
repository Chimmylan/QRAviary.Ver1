package com.example.qraviaryapp.activities.AddActivities

import PairData
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.dashboards.BreedingCagesListActivity
import com.example.qraviaryapp.activities.dashboards.PairFemaleBirdListActivity
import com.example.qraviaryapp.activities.dashboards.PairMaleBirdListActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddPairActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: DatabaseReference

    private lateinit var datePickerDialogBeginning: DatePickerDialog

    private lateinit var etCage: MaterialButton
    private lateinit var etNest: EditText
    private lateinit var etComment: EditText

    private lateinit var btnBeginningDate: Button
    private lateinit var btnMale: MaterialButton
    private lateinit var btnFemale: MaterialButton
    private lateinit var switchMaterial: SwitchMaterial

    private var maleMutation: String? = null
    private var maleMutation2: String? = null
    private var maleMutation3: String? = null
    private var maleMutation4: String? = null
    private var maleMutation5: String? = null
    private var maleMutation6: String? = null
    private var femaleMutation: String? = null
    private var femaleMutation2: String? = null
    private var femaleMutation3: String? = null
    private var femaleMutation4: String? = null
    private var femaleMutation5: String? = null
    private var femaleMutation6: String? = null
    private var femaleStatus: String? = null
    private var maleStatus: String? = null

    private var beginningFormattedDate: String? = null
    private var btnMaleIdValue: String? = null
    private var btnFemaleIdValue: String? = null
    private var btnMaleValueKey: String? = null
    private var btnMaleFlightValueKey: String? = null
    private var btnFemaleValueKey: String? = null
    private var btnFemaleFlightValueKey: String? = null
    private var cageKeyFlightMaleValue: String? = null
    private var cageKeyFlightFemaleValue: String? = null
    private var CageBirdKeyMother: String? = null
    private var CageBirdKeyFather: String? = null
    private var cageKeyValue: String? = null
    private var femalegallery: String? = null
    private var malegallery: String? = null
    private var parentAndChild = false

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var  storageRef: StorageReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.statusbar)
        }
        setContentView(R.layout.activity_add_pair)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 4f
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
            "<font color='$abcolortitle'>Add Pair</font>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        // Check if night mode is enabled
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_white)

        sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putBoolean("Hybridization", false)
        editor.apply()

        switchMaterial = findViewById(R.id.switchHybridization)
        etCage = findViewById(R.id.etcage)
//        etNest = findViewById(R.id.etnest)
        etComment = findViewById(R.id.etcomment)
        storageRef = FirebaseStorage.getInstance().reference

        btnFemale = findViewById(R.id.btnFemale)
        btnMale = findViewById(R.id.btnMale)
        btnBeginningDate = findViewById(R.id.btndateband)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().reference

        var hybridizationCheck = switchMaterial.isChecked

        switchMaterial.setOnCheckedChangeListener { buttonView, isChecked ->

            hybridizationCheck = isChecked
            Log.d(ContentValues.TAG, "CHECKKI")
            editor.putBoolean("Hybridization", isChecked)
            editor.commit()
            editor.apply()
        }
        initDatePickers()
        showDatePickerDialog(this, btnBeginningDate, datePickerDialogBeginning)

        btnMale.setOnClickListener {
            val requestCode = 1
            val i = Intent(this, PairMaleBirdListActivity::class.java)
            if (!hybridizationCheck) {
                if (femaleMutation?.isNotEmpty() == true) {
                    i.putExtra("Hybridization", hybridizationCheck)
                    i.putExtra("FemaleMutation", femaleMutation)
                    i.putExtra("FemaleMutation2", femaleMutation2)
                    i.putExtra("FemaleMutation3", femaleMutation3)
                    i.putExtra("FemaleMutation4", femaleMutation4)
                    i.putExtra("FemaleMutation5", femaleMutation5)
                    i.putExtra("FemaleMutation6", femaleMutation6)
                    i.putExtra("FemaleStatus", femaleStatus)
                } else {
                    Log.d(ContentValues.TAG, "Empty Mutation")
                }
            } else {
                i.putExtra("Hybridization", hybridizationCheck)
                i.putExtra("FemaleStatus", femaleStatus)

                Log.d(ContentValues.TAG, "Hybridization is On")
            }

            startActivityForResult(i, requestCode)
//            if (maleStatus == "Paired") {
//                showPairedAlertDialog("Male")
//            }
        }
        btnFemale.setOnClickListener {
            val requestCode = 2
            val i = Intent(this, PairFemaleBirdListActivity::class.java)

            if (!hybridizationCheck) {
                if (maleMutation?.isNotEmpty() == true) {
                    i.putExtra("Hybridization", hybridizationCheck)
                    i.putExtra("MaleMutation", maleMutation)
                    i.putExtra("MaleMutation2", maleMutation2)
                    i.putExtra("MaleMutation3", maleMutation3)
                    i.putExtra("MaleMutation4", maleMutation4)
                    i.putExtra("MaleMutation5", maleMutation5)
                    i.putExtra("MaleMutation6", maleMutation6)
                    i.putExtra("MaleStatus", maleStatus)

                } else {
                    Log.d(ContentValues.TAG, "Empty Mutation")
                }
            } else {
                i.putExtra("Hybridization", hybridizationCheck)
                i.putExtra("MaleStatus", maleStatus)

                Log.d(ContentValues.TAG, "Hybridization is On")
            }
            startActivityForResult(i, requestCode)

//            if (femaleStatus == "Paired") {
//                showPairedAlertDialog("Female")
//            }
        }




        etCage.setOnClickListener {
            val requestCode = 3
            val i = Intent(this, BreedingCagesListActivity::class.java)
            startActivityForResult(i, requestCode)
        }
//        if (btnBeginningDate.text.isNullOrEmpty()) {
//            // If it's empty or null, set the current date in MMM d yyyy format
//            val cal = Calendar.getInstance()
//            val dateFormat = SimpleDateFormat("MMM d yyyy", Locale.getDefault())
//            val currentFormattedDate = dateFormat.format(cal.time)
//            btnBeginningDate.text = currentFormattedDate
//        }
    }
    private fun showParentChildAlertDialog(gender: String) {

    }
    var validMale = false
    var validFemale = false
    var validCage = false
    var validDate = false
    fun Checking() {


        if (btnMale.text.isNullOrEmpty()) {
            btnMale.error = "Please select a bird..."

            return
        } else {
            validMale = true
        }
        if (btnFemale.text.isNullOrEmpty()) {
            btnFemale.error = "Please select a bird..."
            return

        } else {
            validFemale = true
        }
        if (etCage.text.isNullOrEmpty()) {
            etCage.error = "Please select a cage..."
            return
        } else {
            validCage = true
        }
        if (btnBeginningDate.text.isNullOrEmpty()) {
            btnBeginningDate.error = "Please select a date..."
            return
        } else {
            validDate = true
        }


        val userId = mAuth.currentUser?.uid.toString()
        val newMaleBirdsPref =
            db.child("Users").child("ID: $userId").child("Birds").child(btnMaleValueKey.toString())
        val newFemaleBirdsPref = db.child("Users").child("ID: $userId").child("Birds")
            .child(btnFemaleValueKey.toString())

        //Checking parent ref
        val femaleParentRef = newFemaleBirdsPref.child("Parents")
        val maleParentRef = newMaleBirdsPref.child("Parents")

        var fatherKey = ""
        femaleParentRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    fatherKey = snapshot.child("BirdFatherKey").value.toString()
                    Log.d(ContentValues.TAG, "$fatherKey $btnMaleValueKey")
                    if (fatherKey == btnMaleValueKey) {
                        parentAndChild = true

                    }

                    var motherKey = ""
                    maleParentRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {

                                motherKey = snapshot.child("BirdMotherKey").value.toString()
                                Log.d(ContentValues.TAG, "$motherKey $btnFemaleValueKey")
                                if (motherKey == btnFemaleValueKey) {
                                    parentAndChild = true

                                }

                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


        if (validMale && validFemale && validCage && validDate){
            if (parentAndChild) {
                val builder = AlertDialog.Builder(this@AddPairActivity)
                builder.setTitle("Confirmation")

                builder.setMessage("They are Parent and Child.\nAre you sure you want to save?")
                builder.setPositiveButton("Yes") { dialogInterface: DialogInterface, _: Int ->
                    // Execute addPair() when the user clicks "Yes"
                    addPair()

                }
                builder.setNegativeButton("No") { dialogInterface: DialogInterface, _: Int ->

                    dialogInterface.dismiss()

                }
                val dialog = builder.create()
                dialog.show()
            } else {
                addPair()
            }
         }
    }

    fun addPair() {
        val cageValue = etCage.text.toString()
//        val nestValue = etNest.text.toString()
        val commentValue = etComment.text.toString()

        val nonNullMutations = listOf(
            femaleMutation,
            femaleMutation2,
            femaleMutation3,
            femaleMutation4,
            femaleMutation5,
            femaleMutation6
        ).filter { !it.isNullOrBlank() }
        val NonNullMutations = mutableListOf<String>()
        for (mutation in nonNullMutations) {
            if (mutation != "null") {
                NonNullMutations.add(mutation.toString())
            }
        }
        val femaleCombinedMutations = if (NonNullMutations.isNotEmpty()) {
            NonNullMutations.joinToString(" / ")

        } else {
            "Mutation: None"
        }
        val nonNullMutations1 = listOf(
            maleMutation,
            maleMutation2,
            maleMutation3,
            maleMutation4,
            maleMutation5,
            maleMutation6
        ).filter { !it.isNullOrBlank() }
        val NonNullMutations1 = mutableListOf<String>()
        for (mutation in nonNullMutations1) {
            if (mutation != "null") {
                NonNullMutations1.add(mutation.toString())
            }
        }
        val maleCombinedMutations = if (NonNullMutations1.isNotEmpty()) {
            NonNullMutations1.joinToString(" / ")

        } else {
            "Mutation: None"
        }
        val bird = PairData(
            pairCage = cageValue,
//            nest = nestValue,
            pairComment = commentValue,
            pairDateBeg = beginningFormattedDate,
            pairFemale = btnFemale.text.toString(),
            pairMale = btnMale.text.toString(),
            pairFemaleGender = femaleCombinedMutations,
            pairMaleGender = maleCombinedMutations
        )

        val userId = mAuth.currentUser?.uid.toString()

        var counter = 0
        val pairCountRef = db.child("Users").child("ID: $userId").child("Pairs")


        pairCountRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                counter = snapshot.childrenCount.toInt()

                val cageReference =
                    db.child("Users").child("ID: $userId").child("Cages").child("Breeding Cages")
                        .child(cageKeyValue.toString()).child("Pair Birds").push()

                val userBird = db.child("Users").child("ID: $userId")
                    .child("Pairs")

                val newMaleBirdPref =
                    db.child("Users").child("ID: $userId").child("Birds")
                        .child(btnMaleValueKey.toString())
                        .child("Pairs").child(btnFemaleValueKey.toString())
                val newFemaleBirdPref = db.child("Users").child("ID: $userId").child("Birds")
                    .child(btnFemaleValueKey.toString())
                    .child("Pairs").child(btnMaleValueKey.toString())


                //Status Ref
                val newMaleBirdsPref =
                    db.child("Users").child("ID: $userId").child("Birds")
                        .child(btnMaleValueKey.toString())
                val newMaleFlightBirdsPref =
                    db.child("Users").child("ID: $userId").child("Flight Birds")
                        .child(btnMaleFlightValueKey.toString())

                val newFemaleBirdsPref = db.child("Users").child("ID: $userId").child("Birds")
                    .child(btnFemaleValueKey.toString())
                val newFemaleFlightBirdsPref = db.child("Users").child("ID: $userId").child("Flight Birds")
                    .child(btnFemaleFlightValueKey.toString())

                //Checking parent ref
                val femaleParentRef = newFemaleBirdsPref.child("Parents")
                val maleParentRef = newMaleBirdsPref.child("Parents")


                val newPairPref = userBird.push() //pairkey




                userBird.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var highestPairKey = 0

                        for (childSnapshot in snapshot.children) {
                            val pairNumber =
                                childSnapshot.child("Pair ID").getValue(Int::class.java)
                            if (pairNumber != null && pairNumber > highestPairKey) {
                                highestPairKey = pairNumber
                                Log.d(
                                    ContentValues.TAG,
                                    "HigestPairKey " + highestPairKey.toString()
                                )
                            }else
                            {
                                Log.d(
                                    ContentValues.TAG,
                                    "HigestPairKey is null" + highestPairKey.toString()
                                )
                            }
                        }

                        val newPairNumber = highestPairKey + 1

                                val data: Map<String, Any?> = hashMapOf(
                                    "Pair Key" to newPairPref.key.toString(),
                                    "Pair ID" to newPairNumber,// idnumberPairID
                                    "Male" to bird.pairMale,//MaleId
                                    "Male Mutation" to bird.pairMaleGender,//MaleGender
                                    "Female" to bird.pairFemale,//FemalId
                                    "Female Mutation" to bird.pairFemaleGender,//FemaleGender
                                    "Beginning" to bird.pairDateBeg,//beginningDat
                                    "Cage" to bird.pairCage,//
                                    "Nest" to bird.nest,
                                    "Comment" to bird.pairComment,
                                    "MaleIdentifier" to btnMaleIdValue,
                                    "FemaleIdentifier" to btnFemaleIdValue,
                                    "Male Bird Key" to btnMaleValueKey,//pairmalekey
                                    "Female Bird Key" to btnFemaleValueKey,//pairfemalekey
                                    "Male Flight Key" to btnMaleFlightValueKey,// pairmaleflightkey
                                    "Female Flight Key" to btnFemaleFlightValueKey, //pairFemalefligtkey
                                    "CageKeyFlightFemaleValue" to cageKeyFlightFemaleValue, //cageke
                                    "CageKeyFlightMaleValue" to cageKeyFlightMaleValue, //cagekey
                                    "CageKeyFemale" to CageBirdKeyMother,//CageKeyFemale
                                    "CageKeyMale" to CageBirdKeyFather, //CageKeyMale
                                    "Female Gallery" to femalegallery, //FemaleIMG
                                    "Male Gallery" to malegallery, //MaleImg
                                    "Cage Key" to cageKeyValue,
                                    "Pair Cage Key" to cageReference.key.toString()
                                )

                                val maleBirdPair: Map<String, Any?> = hashMapOf(
                                    "Identifier" to btnFemaleIdValue,
                                    "Bird Key" to btnFemaleValueKey
                                )
                                val femaleBirdPair: Map<String, Any?> = hashMapOf(
                                    "Identifier" to btnMaleIdValue,
                                    "Bird Key" to btnMaleValueKey
                                )

                                val bundleData = JSONObject()

                                bundleData.put("PairQR", true)
                                bundleData.put("PairFemaleImg", femalegallery)
                                bundleData.put("PairMaleImg", malegallery)
                                bundleData.put("PairId", newPairNumber)
                                bundleData.put("PairMaleKey", btnMaleValueKey)
                                bundleData.put("PairFemaleKey", btnFemaleValueKey)
                                bundleData.put("PairFlightMaleKey", btnMaleFlightValueKey)
                                bundleData.put("PairFlightFemaleKey", btnFemaleFlightValueKey)
                                bundleData.put("PairKey", newPairPref.key)
                                bundleData.put("MaleID", bird.pairMale)
                                bundleData.put("FemaleID", bird.pairFemale)
                                bundleData.put("BeginningDate", bird.pairDateBeg)
                                bundleData.put("MaleGender", bird.pairMaleGender)
                                bundleData.put("FemaleGender", bird.pairFemaleGender)
                                bundleData.put("CageKeyFemale", CageBirdKeyMother)
                                bundleData.put("CageKeyMale", CageBirdKeyFather)
                                bundleData.put("CageBirdFemale", cageKeyFlightFemaleValue)
                                bundleData.put("CageBirdMale", cageKeyFlightMaleValue)
                                bundleData.put("CagePairKey", cageReference.key.toString())

                                newMaleBirdsPref.child("Status").setValue("Paired")
                                newFemaleBirdsPref.child("Status").setValue("Paired")
                                cageReference.updateChildren(data)
                                newFemaleBirdPref.updateChildren(femaleBirdPair)
                                newMaleBirdPref.updateChildren(maleBirdPair)
                                newPairPref.updateChildren(data)
                                qrAdd(bundleData, newPairPref)
                                onBackPressed()
                                finish()


                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add_bird, menu)
        val scan= menu.findItem(R.id.menu_qr)
        scan.isVisible = false
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
            R.id.action_save -> {


                Checking()



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

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                btnMaleValueKey = data?.getStringExtra("MaleBirdKey").toString()
                btnMaleIdValue = data?.getStringExtra("MaleBirdId").toString()
                btnMaleFlightValueKey = data?.getStringExtra("MaleFlightKey").toString()
                maleMutation = data?.getStringExtra("MaleMutation").toString()
                maleMutation2 = data?.getStringExtra("MaleMutation2").toString()
                maleMutation3 = data?.getStringExtra("MaleMutation3").toString()
                maleMutation4 = data?.getStringExtra("MaleMutation4").toString()
                maleMutation5 = data?.getStringExtra("MaleMutation5").toString()
                maleMutation6 = data?.getStringExtra("MaleMutation6").toString()
                cageKeyFlightMaleValue = data?.getStringExtra("CageKeyMale").toString()
                CageBirdKeyFather = data?.getStringExtra("CageBirdKeyMale").toString()
                btnMale.text = btnMaleIdValue
                malegallery = data?.getStringExtra("MaleGallery").toString()
                Log.d(TAG, "$malegallery malegallery")
            }
        }
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                btnFemaleValueKey = data?.getStringExtra("FemaleBirdKey").toString()
                btnFemaleIdValue = data?.getStringExtra("FemaleBirdId").toString()
                btnFemaleFlightValueKey = data?.getStringExtra("FemaleFlightKey").toString()
                femaleMutation = data?.getStringExtra("FemaleBirdMutation").toString()
                femaleMutation2 = data?.getStringExtra("FemaleBirdMutation2").toString()
                femaleMutation3 = data?.getStringExtra("FemaleBirdMutation3").toString()
                femaleMutation4 = data?.getStringExtra("FemaleBirdMutation4").toString()
                femaleMutation5 = data?.getStringExtra("FemaleBirdMutation5").toString()
                femaleMutation6 = data?.getStringExtra("FemaleBirdMutation6").toString()
                cageKeyFlightFemaleValue = data?.getStringExtra("CageKeyFemale").toString()
                CageBirdKeyMother = data?.getStringExtra("CageBirdKeyFemale").toString()
                btnFemale.text = btnFemaleIdValue
                femalegallery = data?.getStringExtra("FemaleGallery").toString()
                Log.d(TAG, "$femalegallery femalegallery")
            }
        }
        if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                val etcageValue = data?.getStringExtra("CageName").toString()
                cageKeyValue = data?.getStringExtra("CageKey").toString()
                etCage.setText(etcageValue)
            }

        }

    }

    private fun initDatePickers() {
        val dateSetListenerBeginning =
            DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                beginningFormattedDate = makeDateString(day, month + 1, year)
                btnBeginningDate.text = beginningFormattedDate
            }

        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        val style = AlertDialog.THEME_HOLO_LIGHT

        datePickerDialogBeginning =
            DatePickerDialog(
                this, style, dateSetListenerBeginning, year, month, day
            )

    }

    fun showDatePickerDialog(context: Context, button: Button, datePickerDialog: DatePickerDialog) {
        button.setOnClickListener {
            datePickerDialog.show()
        }
    }

    private fun makeDateString(day: Int, month: Int, year: Int): String {
        return getMonthFormat(month) + " " + day + " " + year
    }

    private fun getMonthFormat(month: Int): String {
        return when (month) {
            1 -> "JAN"
            2 -> "FEB"
            3 -> "MAR"
            4 -> "APR"
            5 -> "MAY"
            6 -> "JUN"
            7 -> "JUL"
            8 -> "AUG"
            9 -> "SEP"
            10 -> "OCT"
            11 -> "NOV"
            12 -> "DEC"
            else -> "JAN" // Default should never happen
        }
    }


    fun openDatebandPicker(view: View) {}
}
