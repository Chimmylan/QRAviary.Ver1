package com.example.qraviaryapp.activities.detailedactivities

import android.content.ContentValues
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.dashboards.FlightCagesListActivity
import com.example.qraviaryapp.activities.dashboards.MutationsActivity
import com.example.qraviaryapp.activities.dashboards.NurseryCagesListActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlin.math.log

class MoveEggActivity : AppCompatActivity() {
    private lateinit var choosecage: MaterialButton
    private lateinit var cageNameValue: String
    private var cageKeyValue: String? = null
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: DatabaseReference

    private lateinit var eggKey: String
    private lateinit var individualEggKey: String
    private lateinit var pairKey: String
    private lateinit var pairFlightMaleKey: String
    private lateinit var pairFlightFemaleKey: String
    private lateinit var pairBirdMaleKey: String
    private lateinit var pairBirdFemaleKey: String
    private lateinit var pairMaleID: String
    private lateinit var pairFemaleID: String
    private lateinit var eggDate: String

    private lateinit var incubatingStartDate: String
    private lateinit var maturingStartDate: String

    private var currentUserId: String? = null

    private lateinit var etIdentifier: EditText

    private lateinit var addBtn: MaterialButton
    private lateinit var removeBtn: MaterialButton

    private lateinit var btnMutation1: MaterialButton
    private lateinit var btnMutation2: MaterialButton
    private lateinit var btnMutation3: MaterialButton
    private lateinit var btnMutation4: MaterialButton
    private lateinit var btnMutation5: MaterialButton
    private lateinit var btnMutation6: MaterialButton

    private var mutation1IncubatingDays = ""
    private var mutation1MaturingDays = ""
    private var mutation2IncubatingDays = ""
    private var mutation2MaturingDays = ""
    private var mutation3IncubatingDays = ""
    private var mutation3MaturingDays = ""
    private var mutation4IncubatingDays = ""
    private var mutation4MaturingDays = ""
    private var mutation5IncubatingDays = ""
    private var mutation5MaturingDays = ""
    private var mutation6IncubatingDays = ""
    private var mutation6MaturingDays = ""

    /*RadioGroup*/
    private lateinit var rgGender: RadioGroup

    /*RadioGroup*/
    private lateinit var rbFemale: RadioButton
    private lateinit var rbMale: RadioButton
    private lateinit var rbUnknown: RadioButton




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.bottom_nav_background)
        }
        setContentView(R.layout.activity_move_egg)
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
            "<font color='$abcolortitle'>Chick to Nursery Cage</font>",
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
        /*RadioGroup*/
        rgGender = findViewById(R.id.radioGroupGender)
        /*RadioButtons*/
        rbMale = findViewById(R.id.radioButtonMale)
        rbFemale = findViewById(R.id.radioButtonFemale)
        rbUnknown = findViewById(R.id.radioButtonUnknown)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().reference
        currentUserId = mAuth.currentUser?.uid
        choosecage = findViewById(R.id.btnnurserycage)

        etIdentifier = findViewById(R.id.etIdentifier)
        addBtn = findViewById(R.id.addBtn)
        removeBtn = findViewById(R.id.removeBtn)
        btnMutation1 = findViewById(R.id.mutationBtn1)
        btnMutation2 = findViewById(R.id.mutationBtn2)
        btnMutation3 = findViewById(R.id.mutationBtn3)
        btnMutation4 = findViewById(R.id.mutationBtn4)
        btnMutation5 = findViewById(R.id.mutationBtn5)
        btnMutation6 = findViewById(R.id.mutationBtn6)

        pairKey = intent.getStringExtra("PairKey").toString()
        individualEggKey = intent.getStringExtra("IndividualEggKey").toString()
        eggKey = intent.getStringExtra("EggKey").toString()
        maturingStartDate = intent.getStringExtra("MaturingStartDate").toString()
        incubatingStartDate = intent.getStringExtra("IncubatingStartDate").toString()
        pairFlightFemaleKey = intent.getStringExtra("PairFlightFemaleKey").toString()
        pairFlightMaleKey = intent.getStringExtra("PairFlightMaleKey").toString()
        pairBirdFemaleKey = intent.getStringExtra("PairFemaleKey").toString()
        pairBirdMaleKey = intent.getStringExtra("PairMaleKey").toString()
        pairMaleID = intent.getStringExtra("PairMaleID").toString()
        pairFemaleID = intent.getStringExtra("PairFemaleID").toString()
        eggDate = intent.getStringExtra("DateOfBirth").toString()


        rbUnknown.isChecked = true
        btnMutation1.setOnClickListener {
            val requestCode = 1 // You can use any integer as the request code
            val intent = Intent(this, MutationsActivity::class.java)
            startActivityForResult(intent, requestCode)

        }
        btnMutation2.setOnClickListener {
            val requestCode = 2 // You can use any integer as the request code
            val intent = Intent(this, MutationsActivity::class.java)
            startActivityForResult(intent, requestCode)

        }
        btnMutation3.setOnClickListener {
            val requestCode = 3 // You can use any integer as the request code
            val intent = Intent(this, MutationsActivity::class.java)
            startActivityForResult(intent, requestCode)

        }
        btnMutation4.setOnClickListener {
            val requestCode = 4 // You can use any integer as the request code
            val intent = Intent(this, MutationsActivity::class.java)
            startActivityForResult(intent, requestCode)

        }
        btnMutation5.setOnClickListener {
            val requestCode = 5 // You can use any integer as the request code
            val intent = Intent(this, MutationsActivity::class.java)
            startActivityForResult(intent, requestCode)

        }
        btnMutation6.setOnClickListener {
            val requestCode = 6 // You can use any integer as the request code
            val intent = Intent(this, MutationsActivity::class.java)
            startActivityForResult(intent, requestCode)

        }

        choosecage.setOnClickListener {
            val requestCode = 7
            val intent = Intent(this, NurseryCagesListActivity::class.java)
            startActivityForResult(intent, requestCode)


        }
        AddMutation()
        RemoveLastMutation()
    }
    private var spinnerCount = 0


    fun AddMutation() {

        btnMutation2.visibility = View.GONE
        btnMutation3.visibility = View.GONE
        btnMutation4.visibility = View.GONE
        btnMutation5.visibility = View.GONE
        btnMutation6.visibility = View.GONE

        addBtn.setOnClickListener {

            when (spinnerCount) {

                0 -> {
                    btnMutation2.visibility = View.VISIBLE
                    spinnerCount++
                }
                1 -> {
                    btnMutation3.visibility = View.VISIBLE
                    spinnerCount++
                }
                2 -> {
                    btnMutation4.visibility = View.VISIBLE
                    spinnerCount++
                }
                3 -> {
                    btnMutation5.visibility = View.VISIBLE
                    spinnerCount++
                }
                4 -> {
                    btnMutation6.visibility = View.VISIBLE
                    spinnerCount++
                }
                else -> {
                    Toast.makeText(this, "added $spinnerCount", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }
    }

    fun RemoveLastMutation() {
        removeBtn.setOnClickListener {

            when (spinnerCount) {
                0 -> {
                    Toast.makeText(this, spinnerCount.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
                1 -> {
                    spinnerCount--
                    btnMutation2.visibility = View.GONE
                    Toast.makeText(this, spinnerCount.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
                2 -> {
                    spinnerCount--
                    btnMutation3.visibility = View.GONE
                    Toast.makeText(this, spinnerCount.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
                3 -> {

                    spinnerCount--
                    btnMutation4.visibility = View.GONE
                    Toast.makeText(this, spinnerCount.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
                4 -> {

                    spinnerCount--
                    btnMutation5.visibility = View.GONE
                    Toast.makeText(this, spinnerCount.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                    spinnerCount--
                    btnMutation6.visibility = View.GONE

                    Toast.makeText(this, spinnerCount.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                val btnMutation1Value: String =
                    data?.getStringExtra("selectedMutationId").toString()
                mutation1MaturingDays =
                    data?.getStringExtra("selectedMutationMaturingDays").toString()
                mutation1IncubatingDays =
                    data?.getStringExtra("selectedMutationIncubatingDays").toString()

                btnMutation1.text = btnMutation1Value


            }
        }
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                val btnMutation2Value: String =
                    data?.getStringExtra("selectedMutationId").toString()
                mutation2MaturingDays =
                    data?.getStringExtra("selectedMutationMaturingDays").toString()
                mutation2IncubatingDays =
                    data?.getStringExtra("selectedMutationIncubatingDays").toString()
                btnMutation2.text = btnMutation2Value
            }
        }
        if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                val btnMutation3Value: String =
                    data?.getStringExtra("selectedMutationId").toString()
                mutation3MaturingDays =
                    data?.getStringExtra("selectedMutationMaturingDays").toString()
                mutation3IncubatingDays =
                    data?.getStringExtra("selectedMutationIncubatingDays").toString()
                btnMutation3.text = btnMutation3Value
            }
        }
        if (requestCode == 4) {
            if (resultCode == RESULT_OK) {
                val btnMutation4Value: String =
                    data?.getStringExtra("selectedMutationId").toString()
                mutation4MaturingDays =
                    data?.getStringExtra("selectedMutationMaturingDays").toString()
                mutation4IncubatingDays =
                    data?.getStringExtra("selectedMutationIncubatingDays").toString()
                btnMutation4.text = btnMutation4Value
            }
        }
        if (requestCode == 5) {
            if (resultCode == RESULT_OK) {
                val btnMutation5Value: String =
                    data?.getStringExtra("selectedMutationId").toString()
                mutation5MaturingDays =
                    data?.getStringExtra("selectedMutationMaturingDays").toString()
                mutation5IncubatingDays =
                    data?.getStringExtra("selectedMutationIncubatingDays").toString()
                btnMutation5.text = btnMutation5Value
            }
        }
        if (requestCode == 6) {
            if (resultCode == RESULT_OK) {
                val btnMutation6Value: String =
                    data?.getStringExtra("selectedMutationId").toString()
                mutation6MaturingDays =
                    data?.getStringExtra("selectedMutationMaturingDays").toString()
                mutation6IncubatingDays =
                    data?.getStringExtra("selectedMutationIncubatingDays").toString()
                btnMutation6.text = btnMutation6Value
            }
        }

        if (requestCode == 7) {
            if (resultCode == RESULT_OK) {
                cageNameValue = data?.getStringExtra("CageName").toString()
                cageKeyValue = data?.getStringExtra("CageKey").toString()
                Log.d(ContentValues.TAG, "cage name : $cageNameValue")
                Log.d(ContentValues.TAG, "cage Key : $cageKeyValue")
                choosecage.setText(cageNameValue)
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add_bird, menu)

        val saveMenuItem = menu.findItem(R.id.action_save)

        // Check if night mode is enabled
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            // Set the text color to white for night mode
            saveMenuItem.title = HtmlCompat.fromHtml("<font color='#FFFFFF'>Save</font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
        } else {
            // Set the text color to black for non-night mode
            saveMenuItem.title = HtmlCompat.fromHtml("<font color='#000000'>Save</font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
        }

        return true
    }

    fun saveNursery(){
        val birdRef = db.child("Users").child("ID: $currentUserId").child("Birds")
        val newPrefBird = birdRef.push()
        val birdKey = newPrefBird.key

        val selectedOption: Int = rgGender.checkedRadioButtonId
        val dataSelectedGen = findViewById<RadioButton>(selectedOption)!!



        //toDeleteRef
        val eggRef = db.child("Users").child("ID: $currentUserId").child("Pairs").child(pairKey)
            .child("Clutches").child(eggKey).child(individualEggKey)

        val descendantsfatherRef = db.child("Users").child("ID: $currentUserId").child("Flight Birds")
            .child(pairFlightMaleKey).child("Descendants").push()
        val descendantsmotherRef = db.child("Users").child("ID: $currentUserId").child("Flight Birds")
            .child(pairFlightFemaleKey).child("Descendants").push()
        //nurseryCageRef
        val nurseryCageRef = db.child("Users").child("ID: $currentUserId").child("Cages")
            .child("Nursery Cages").child(cageKeyValue.toString()).child("Birds").push()
        val descendantsbirdfatherRef = db.child("Users").child("ID: $currentUserId").child("Birds")
            .child(pairBirdMaleKey).child("Descendants").push()
        val descendantsbirdmotherRef = db.child("Users").child("ID: $currentUserId").child("Birds")
            .child(pairBirdFemaleKey).child("Descendants").push()
        val nurseryRef = db.child("Users").child("ID: $currentUserId").child("Nursery Birds")
            .push()

        val nurseryKey = nurseryRef.key

//        cute
        val mutation1 = mapOf(
            "Mutation Name" to btnMutation1.text,
            "Maturing Days" to mutation1MaturingDays,
            "Incubating Days" to mutation1IncubatingDays
        )
        val mutation2 = mapOf(
            "Mutation Name" to btnMutation2.text,
            "Maturing Days" to mutation2MaturingDays,
            "Incubating Days" to mutation2IncubatingDays
        )
        val mutation3 = mapOf(
            "Mutation Name" to btnMutation3.text,
            "Maturing Days" to mutation3MaturingDays,
            "Incubating Days" to mutation3IncubatingDays
        )
        val mutation4 = mapOf(
            "Mutation Name" to btnMutation4.text,
            "Maturing Days" to mutation4MaturingDays,
            "Incubating Days" to mutation4IncubatingDays
        )
        val mutation5 = mapOf(
            "Mutation Name" to btnMutation5.text,
            "Maturing Days" to mutation5MaturingDays,
            "Incubating Days" to mutation5IncubatingDays
        )
        val mutation6 = mapOf(
            "Mutation Name" to btnMutation6.text,
            "Maturing Days" to mutation6MaturingDays,
            "Incubating Days" to mutation6IncubatingDays
        )


        val parent = mapOf<String, Any?>(
            "BirdFatherKey" to pairBirdMaleKey,
            "BirdMotherKey" to pairBirdFemaleKey,
            "Father" to pairMaleID,
            "Mother" to pairFemaleID,
            "FatherKey" to pairFlightMaleKey,
            "MotherKey" to pairFlightFemaleKey
        )

        val data: Map<String, Any?> = hashMapOf(
            "Bird Key" to birdKey,
            "Parents" to parent,
            "Date of Birth" to eggDate,
            "Nursery Key" to nurseryKey,
            "Status" to "Available",
            "Identifier" to etIdentifier.text.toString(),
            "Mutation1" to mutation1,
            "Mutation2" to mutation2,
            "Mutation3" to mutation3,
            "Mutation4" to mutation4,
            "Mutation5" to mutation5,
            "Mutation6" to mutation6,
            "Gender" to dataSelectedGen.text.toString()
        )

        newPrefBird.updateChildren(data)
        nurseryCageRef.updateChildren(data)
        nurseryRef.updateChildren(data)
        descendantsmotherRef.updateChildren(data)
        descendantsfatherRef.updateChildren(data)
        descendantsbirdmotherRef.updateChildren(data)
        descendantsbirdfatherRef.updateChildren(data)
        eggRef.removeValue()
        Log.d(ContentValues.TAG, data.toString())



        onBackPressed()
        finish()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
               saveNursery()
                true
            }
            android.R.id.home -> {
                onBackPressed() // Call this to navigate back to the previous fragment
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}