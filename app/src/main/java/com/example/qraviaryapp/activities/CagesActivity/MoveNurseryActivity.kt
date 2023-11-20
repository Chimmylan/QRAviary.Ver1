package com.example.qraviaryapp.activities.CagesActivity


import android.content.ContentValues
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.dashboards.FlightCagesListActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MoveNurseryActivity : AppCompatActivity() {
    private lateinit var choosecage: MaterialButton
    private lateinit var cageNameValue: String
    private var cageKeyValue: String? = null
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: DatabaseReference
    private var nurseryKey: String? = null
    private var cageKey: String? = null
    private var nurseryBirdKey: String? = null
    private var age = ""
    private var birdkey = ""
    private var cage = ""
    private var datebirth = ""
    private var gender = ""
    private var id = ""
    private var legband = ""
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.statusbar)
        }
        setContentView(R.layout.activity_move_nursery)

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
        mAuth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().reference
        choosecage = findViewById(R.id.btnflightcage)


        choosecage.setOnClickListener {
            val requestCode = 7
            val intent = Intent(this, FlightCagesListActivity::class.java)
            startActivityForResult(intent, requestCode)

        }


        val abcolortitle = resources.getColor(R.color.appbar)
        supportActionBar?.title = HtmlCompat.fromHtml(
            "<font color='$abcolortitle'>Move to Flight Cage</font>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        // Check if night mode is enabled
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_white)
        nurseryKey = intent.getStringExtra("Nursery Key")
        cageKey = intent.getStringExtra("CageKeyValue")
        nurseryBirdKey = intent.getStringExtra("BirdKey")
        userId = mAuth.currentUser?.uid.toString()
        val nurseryref =
            db.child("Users").child("ID: $userId").child("Nursery Birds")
                .child(nurseryKey.toString())


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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
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

    fun save() {

        if (TextUtils.isEmpty(choosecage.text)){
            choosecage.error = "Cage must not be Empty"
        }
        else {
            val newFlightRef =
                db.child("Users").child("ID: $userId").child("Flight Birds").push()

            val birdPref = db.child("Users").child("ID: $userId").child("Birds").child(birdkey)
            val flightCageRef = db.child("Users").child("ID: $userId").child("Cages")
                .child("Flight Cages").child(cageKeyValue.toString()).child("Birds").push()

            val nurseryref =
                db.child("Users").child("ID: $userId").child("Nursery Birds")
                    .child(nurseryKey.toString())
            val NurseryCageRef = db.child("Users").child("ID: $userId").child("Cages")
                .child("Nursery Cages").child(cageKey.toString()).child("Birds")
                .child(nurseryBirdKey.toString())
            nurseryref.removeValue()
            NurseryCageRef.removeValue()

            val key = newFlightRef.key

            newFlightRef.setValue(dataToCopy)
            val updateData = hashMapOf<String, Any?>("Flight Key" to key)



            birdPref.updateChildren(updateData)
            newFlightRef.updateChildren(updateData)
            flightCageRef.setValue(dataToCopy)
            flightCageRef.updateChildren(updateData)
            onBackPressed()
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {

                save()
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