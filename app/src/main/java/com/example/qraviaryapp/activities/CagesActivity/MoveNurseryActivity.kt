package com.example.qraviaryapp.activities.CagesActivity


import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.nfc.Tag
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
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
    private var age = ""
    private var birdkey = ""
    private var cage = ""
    private var datebirth = ""
    private var gender = ""
    private var id = ""
    private var legband = ""
    private var mutation1 = ""
    private var mutation2 = ""
    private var mutation3 = ""
    private var mutation4 = ""
    private var mutation5 = ""
    private var mutation6 = ""
    private var father= ""
    private var mother = ""
    private var status = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.bottom_nav_background)
        }
        setContentView(R.layout.activity_move_nursery)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    this,
                    R.color.new_appbar_color
                )
            )
        )
        mAuth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().reference
        choosecage = findViewById(R.id.btnflightcage)


        choosecage.setOnClickListener{
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
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            // Set the white back button for night mode
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_white)
        } else {
            // Set the black back button for non-night mode
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_black)
        }

        nurseryKey = intent.getStringExtra("Nursery Key")
        val userId = mAuth.currentUser?.uid.toString()
        val nurseryref =
            db.child("Users").child("ID: $userId").child("Nursery Birds").child(nurseryKey.toString())


        nurseryref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                age = snapshot.child("Age").value.toString()
                birdkey = snapshot.child("Bird Key").value.toString()
                cage = snapshot.child("Cage").value.toString()
                datebirth = snapshot.child("Date of Birth").value.toString()
                gender = snapshot.child("Gender").value.toString()
                id = snapshot.child("Identifier").value.toString()
                legband = snapshot.child("Legband").value.toString()
                mutation1 = snapshot.child("Mutation1").value.toString()
                mutation2 = snapshot.child("Mutation2").value.toString()
                mutation3 = snapshot.child("Mutation3").value.toString()
                mutation4 = snapshot.child("Mutation4").value.toString()
                mutation5 = snapshot.child("Mutation5").value.toString()
                mutation6 = snapshot.child("Mutation6").value.toString()
                father = snapshot.child("Father").value.toString()
                mother = snapshot.child("Mother").value.toString()
                status = snapshot.child("Status").value.toString()

                TODO("Not yet implemented")
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

    fun save(){
       Log.d(TAG,"$age")
        Log.d(TAG,"$birdkey")
        Log.d(TAG,"$cage")
        Log.d(TAG,"$datebirth")
        Log.d(TAG,"$gender")
        Log.d(TAG,"$id")
        Log.d(TAG,"$legband")
        Log.d(TAG,"$mutation1")
        Log.d(TAG,"$mutation2")
        Log.d(TAG,"$mutation3")
        Log.d(TAG,"$mutation4")
        Log.d(TAG,"$mutation5")
        Log.d(TAG,"$mutation6")
        Log.d(TAG,"$father")
        Log.d(TAG,"$mother")
        Log.d(TAG,"$status")



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