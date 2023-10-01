package com.example.qraviaryapp.activities.AddActivities

import PairData
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.*
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.dashboards.*
import com.google.android.material.button.MaterialButton
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*

class AddPairActivity : AppCompatActivity() {


    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: DatabaseReference

    private lateinit var datePickerDialogBeginning: DatePickerDialog

    private lateinit var etCage: EditText
    private lateinit var etNest: EditText
    private lateinit var etComment: EditText

    private lateinit var btnBeginningDate: Button
    private lateinit var btnMale: MaterialButton
    private lateinit var btnFemale: MaterialButton
    private lateinit var switchMaterial: SwitchMaterial

    private var maleMutation: String? = null
    private var femaleMutation: String? = null

    private var beginningFormattedDate: String? = null
    private var btnMaleIdValue: String? = null
    private var btnFemaleIdValue: String? = null
    private var btnMaleValueKey: String? = null
    private var btnFemaleValueKey: String? = null

    private var cageKeyValue: String? = null

    private var parentAndChild = false

    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.bottom_nav_background)
        }
        setContentView(R.layout.activity_add_pair)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    this,
                    R.color.new_appbar_color
                )
            )
        )
        val abcolortitle = resources.getColor(R.color.appbar)
        supportActionBar?.title = HtmlCompat.fromHtml(
            "<font color='$abcolortitle'>Add Pair</font>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        // Check if night mode is enabled
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            // Set the white back button for night mode
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_white)
        } else {
            // Set the black back button for non-night mode
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_black)
        }

        sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putBoolean("Hybridization", false)
        editor.apply()

        switchMaterial = findViewById(R.id.switchHybridization)
        etCage = findViewById(R.id.etcage)
        etNest = findViewById(R.id.etnest)
        etComment = findViewById(R.id.etcomment)

        btnFemale = findViewById(R.id.btnFemale)
        btnMale = findViewById(R.id.btnMale)
        btnBeginningDate = findViewById(R.id.btndateband)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().reference

        var hybridizationCheck = switchMaterial.isChecked

        switchMaterial.setOnCheckedChangeListener { buttonView, isChecked ->

            hybridizationCheck = isChecked

            editor.putBoolean("Hybridization", isChecked)
            editor.apply()
        }
        initDatePickers()
        showDatePickerDialog(this, btnBeginningDate, datePickerDialogBeginning)

        btnMale.setOnClickListener {
            val requestCode = 1
            val i = Intent(this, PairMaleBirdListActivity::class.java)
            if (!hybridizationCheck) {
                if (femaleMutation?.isNotEmpty() == true) {
                    i.putExtra("FemaleMutation", femaleMutation)
                } else {
                    Log.d(ContentValues.TAG, "Empty Mutation")
                }
            } else {
                Log.d(ContentValues.TAG, "Hybridization is On")
            }

            startActivityForResult(i, requestCode)
        }
        btnFemale.setOnClickListener {
            val requestCode = 2
            val i = Intent(this, PairFemaleBirdListActivity::class.java)
            if (!hybridizationCheck) {
                if (maleMutation?.isNotEmpty() == true) {
                    i.putExtra("MaleMutation", maleMutation)
                } else {
                    Log.d(ContentValues.TAG, "Empty Mutation")
                }
            } else {
                Log.d(ContentValues.TAG, "Hybridization is On")
            }
            startActivityForResult(i, requestCode)
        }




        etCage.setOnClickListener {
            val requestCode = 3
            val i = Intent(this, BreedingCagesListActivity::class.java)
            startActivityForResult(i, requestCode)
        }

    }

    fun Checking(){


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

                                addPair()

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

    }

    fun addPair() {
        val cageValue = etCage.text.toString()
        val nestValue = etNest.text.toString()
        val commentValue = etComment.text.toString()

        val bird = PairData(
            pairCage = cageValue,
            nest = nestValue,
            pairComment = commentValue,
            pairDateBeg = beginningFormattedDate,
            pairFemale = btnFemale.text.toString(),
            pairMale = btnMale.text.toString(),
            pairFemaleGender = femaleMutation,
            pairMaleGender = maleMutation
        )

        val userId = mAuth.currentUser?.uid.toString()

        val cageReference =
            db.child("Users").child("ID: $userId").child("Cages").child("Breeding Cages")
                .child(cageKeyValue.toString()).child("Pair Birds").push()

        val userBird = db.child("Users").child("ID: $userId")
            .child("Pairs")

        val newMaleBirdPref =
            db.child("Users").child("ID: $userId").child("Birds").child(btnMaleValueKey.toString())
                .child("Pairs").push()
        val newFemaleBirdPref = db.child("Users").child("ID: $userId").child("Birds")
            .child(btnFemaleValueKey.toString())
            .child("Pairs").push()


        //Status Ref
        val newMaleBirdsPref =
            db.child("Users").child("ID: $userId").child("Birds").child(btnMaleValueKey.toString())
        val newFemaleBirdsPref = db.child("Users").child("ID: $userId").child("Birds")
            .child(btnFemaleValueKey.toString())

        //Checking parent ref
        val femaleParentRef = newFemaleBirdsPref.child("Parents")
        val maleParentRef = newMaleBirdsPref.child("Parents")


        val newPairPref = userBird.push()
        val pairId = newPairPref.key

        var validMale = false
        var validFemale = false

        if (btnMale.text.isEmpty()) {
            btnMale.error = "Please select a bird..."
        } else {
            validMale = true
        }
        if (btnFemale.text.isEmpty()) {
            btnFemale.error = "Please select a bird..."
        } else {
            validFemale = true
        }

        if (validMale && validFemale) {
            val data: Map<String, Any?> = hashMapOf(
                "Male" to bird.pairMale,
                "Male Mutation" to bird.pairMaleGender,
                "Female" to bird.pairFemale,
                "Female Mutation" to bird.pairFemaleGender,
                "Beginning" to bird.pairDateBeg,
                "Cage" to bird.pairCage,
                "Nest" to bird.nest,
                "Comment" to bird.pairComment,
                "MaleIdentifier" to btnMaleIdValue,
                "FemaleIdentifier" to btnFemaleIdValue,
                "Male Bird Key" to btnMaleValueKey,
                "Female Bird Key" to btnFemaleValueKey
            )

            val maleBirdPair: Map<String, Any?> = hashMapOf(
                "Identifier" to btnFemaleIdValue,
                "Bird Key" to btnFemaleValueKey
            )
            val femaleBirdPair: Map<String, Any?> = hashMapOf(
                "Identifier" to btnMaleIdValue,
                "Bird Key" to btnMaleValueKey
            )


            Log.d(ContentValues.TAG, parentAndChild.toString())
            if (parentAndChild) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Confirmation")

                builder.setMessage("They are Parent and Child.\nAre you sure you want to save?")
                builder.setPositiveButton("Yes") { dialogInterface: DialogInterface, _: Int ->
                    // Execute addPair() when the user clicks "Yes"

                    newMaleBirdsPref.child("Status").setValue("Paired")
                    newFemaleBirdsPref.child("Status").setValue("Paired")
                    cageReference.updateChildren(data)
                    newFemaleBirdPref.updateChildren(femaleBirdPair)
                    newMaleBirdPref.updateChildren(maleBirdPair)
                    newPairPref.updateChildren(data)

                    dialogInterface.dismiss()
                    onBackPressed()
                    finish()
                }
                builder.setNegativeButton("No") { dialogInterface: DialogInterface, _: Int ->
                    // Handle the case when the user clicks "No" (optional)
                    dialogInterface.dismiss()
                }
                val dialog = builder.create()
                dialog.show()
            } else {

                newMaleBirdsPref.child("Status").setValue("Paired")
                newFemaleBirdsPref.child("Status").setValue("Paired")
                cageReference.updateChildren(data)
                newFemaleBirdPref.updateChildren(femaleBirdPair)
                newMaleBirdPref.updateChildren(maleBirdPair)
                newPairPref.updateChildren(data)
                onBackPressed()
                finish()

            }


        }

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
                maleMutation = data?.getStringExtra("MaleMutation").toString()
                btnMale.text = btnMaleIdValue
            }
        }
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                btnFemaleValueKey = data?.getStringExtra("FemaleBirdKey").toString()
                btnFemaleIdValue = data?.getStringExtra("FemaleBirdId").toString()
                femaleMutation = data?.getStringExtra("FemaleBirdMutation").toString()
                btnFemale.text = btnFemaleIdValue
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
