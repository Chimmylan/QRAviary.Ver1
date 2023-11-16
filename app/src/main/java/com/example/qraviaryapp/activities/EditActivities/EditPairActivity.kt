package com.example.qraviaryapp.activities.EditActivities

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.dashboards.BreedingCagesListActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EditPairActivity : AppCompatActivity() {
    private lateinit var cage: MaterialButton
    private var cageKeyValue: String? = null
    private var pairKey: String? = null
    private var pairCageKey: String? = null
    private var cagePairKey: String? = null
    private var pairCage: String? = null
    private var etcageValue: String? = null
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.statusbar)
        }
        setContentView(R.layout.activity_edit_pair)
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
            "<font color='$abcolortitle'>Move to Another Pair Cage</font>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        // Check if night mode is enabled
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_white)
        cage = findViewById(R.id.etPairCage)
        mAuth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().reference
        val bundle = intent.extras

        // Now you can access data from the bundle


        pairKey = bundle?.getString("PairKey")
        pairCageKey = bundle?.getString("CageKey")
        cagePairKey = bundle?.getString("CagePairKey")
        pairCage = bundle?.getString("PairCage")

        cage.text = pairCage
        cageKeyValue = pairCageKey
        cage.setOnClickListener {
            val requestCode = 1
            val i = Intent(this, BreedingCagesListActivity::class.java)
            startActivityForResult(i, requestCode)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                etcageValue = data?.getStringExtra("CageName").toString()
                cageKeyValue = data?.getStringExtra("CageKey").toString()
                cage.text = etcageValue
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add_bird, menu)
        val menuqr = menu.findItem(R.id.menu_qr)
        val saveMenuItem = menu.findItem(R.id.action_save)
        menuqr.isVisible = false
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
                val userId = mAuth.currentUser?.uid.toString()
                val newcageReference =
                    db.child("Users").child("ID: $userId").child("Cages").child("Breeding Cages")
                        .child(pairCageKey.toString()).child("Pair Birds")
                        .child(cagePairKey.toString())
                newcageReference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        val data = snapshot.value

                        val newcage =
                            db.child("Users").child("ID: $userId").child("Cages")
                                .child("Breeding Cages")
                                .child(cageKeyValue.toString()).child("Pair Birds")
                                .child(cagePairKey.toString())
                        val pairRef = db.child("Users").child("ID: $userId").child("Pairs").child(pairKey.toString())

                        pairRef.child("Cage Key").setValue(cageKeyValue)
                        pairRef.child("Cage").setValue(etcageValue)

                        newcage.setValue(data)

                        newcage.child("Cage Key").setValue(cageKeyValue)
                        newcage.child("Cage").setValue(etcageValue)

                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })

                val cageReference =
                    db.child("Users").child("ID: $userId").child("Cages").child("Breeding Cages")
                        .child(pairCageKey.toString()).child("Pair Birds")
                        .child(cagePairKey.toString()).removeValue()



                onBackPressed()
                finish()
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