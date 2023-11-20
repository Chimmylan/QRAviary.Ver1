package com.example.qraviaryapp.activities.detailedactivities

import android.content.ContentValues
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.dashboards.NurseryCagesListActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MoveEggCageActivity : AppCompatActivity() {
    private lateinit var cage: MaterialButton
    private var cageKeyValue: String? = null
    private var etcageValue: String? = null
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: DatabaseReference
    private lateinit var cagescan: CardView
    private var incubatingStartDate: String? = null
    private var maturingStartDate: String? = null
    private var eggKey: String? = null
    private var individualEggKey: String? = null
    private var pairKey: String? = null
    private var pairFlightMaleKey: String? = null
    private var pairFlightFemaleKey: String? = null
    private var pairMaleKey: String? = null
    private var pairFemaleKey: String? = null
    private var pairMaleID: String? = null
    private var pairFemaleID: String? = null
    private var dateOfBirth: String? = null
    private var cageKeyFemale: String? = null
    private var cageKeyMale: String? = null
    private var cageBirdFemale: String? = null
    private var cageBirdMale: String? = null
    private var clutch: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.statusbar)
        }
        setContentView(R.layout.activity_move_egg_cage)
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
        cage = findViewById(R.id.etPairCage)
        mAuth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().reference
        cage.setOnClickListener {
            val requestCode = 1 // You can use any integer as the request code
            val intent = Intent(this, NurseryCagesListActivity::class.java)
            startActivityForResult(intent, requestCode)
        }
        val abcolortitle = resources.getColor(R.color.appbar)
        supportActionBar?.title = HtmlCompat.fromHtml(
            "<font color='$abcolortitle'>Choose Nursery Cage</font>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        // Check if night mode is enabled
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_white)
        cagescan = findViewById(R.id.cagescan)

        val i = intent.extras
        incubatingStartDate = i?.getString("IncubatingStartDate")
        maturingStartDate = i?.getString("MaturingStartDate")
        eggKey = i?.getString("EggKey")
        individualEggKey = i?.getString("IndividualEggKey")
        pairKey = i?.getString("PairKey")
        pairFlightMaleKey = i?.getString("PairFlightMaleKey")
        pairFlightFemaleKey = i?.getString("PairFlightFemaleKey")
        pairMaleKey = i?.getString("PairMaleKey")
        pairFemaleKey = i?.getString("PairFemaleKey")
        pairMaleID = i?.getString("PairMaleID")
        pairFemaleID = i?.getString("PairFemaleID")
        dateOfBirth = i?.getString("DateOfBirth")
        cageKeyFemale = i?.getString("CageKeyFemale")
        cageKeyMale = i?.getString("CageKeyMale")
        cageBirdFemale = i?.getString("CageBirdFemale")
        cageBirdMale = i?.getString("CageBirdMale")
        clutch = i?.getString("Clutch")

        cagescan.setOnClickListener {
            val moveEggCageIntent = Intent(this, MoveEggScannerActivity::class.java)
            moveEggCageIntent.putExtra("IncubatingStartDate", incubatingStartDate)
            moveEggCageIntent.putExtra("MaturingStartDate", maturingStartDate)
            moveEggCageIntent.putExtra("EggKey", eggKey)
            moveEggCageIntent.putExtra("IndividualEggKey", individualEggKey)
            moveEggCageIntent.putExtra("PairKey", pairKey)
            moveEggCageIntent.putExtra("PairFlightMaleKey", pairFlightMaleKey)
            moveEggCageIntent.putExtra("PairFlightFemaleKey", pairFlightFemaleKey)
            moveEggCageIntent.putExtra("PairMaleKey", pairMaleKey)
            moveEggCageIntent.putExtra("PairFemaleKey", pairFemaleKey)
            moveEggCageIntent.putExtra("PairMaleID", pairMaleID)
            moveEggCageIntent.putExtra("PairFemaleID", pairFemaleID)
            moveEggCageIntent.putExtra("DateOfBirth", dateOfBirth)
            moveEggCageIntent.putExtra("CageKeyFemale", cageKeyFemale)
            moveEggCageIntent.putExtra("CageKeyMale", cageKeyMale)
            moveEggCageIntent.putExtra("CageBirdFemale", cageBirdFemale)
            moveEggCageIntent.putExtra("CageBirdMale", cageBirdMale)
            moveEggCageIntent.putExtra("Clutch", clutch)
            this.startActivity(moveEggCageIntent)
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

        val saveMenuItem = menu.findItem(R.id.action_save)
        val menuqr = menu.findItem(R.id.menu_qr)
        menuqr.isVisible = false
        // Check if night mode is enabled
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            // Set the text color to white for night mode
            saveMenuItem.title = HtmlCompat.fromHtml(
                "<font color='#FFFFFF'>Next</font>",
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        } else {
            // Set the text color to black for non-night mode
            saveMenuItem.title = HtmlCompat.fromHtml(
                "<font color='#000000'>Next</font>",
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                val intent = Intent(this, MoveEggActivity::class.java)
                intent.putExtra("Clutch", false)
                intent.putExtra("CageKey", cageKeyValue)
                intent.putExtra("CageName", etcageValue)
                intent.putExtra("IncubatingStartDate", incubatingStartDate)
                intent.putExtra("MaturingStartDate", maturingStartDate)
                intent.putExtra("EggKey", eggKey)
                intent.putExtra("IndividualEggKey", individualEggKey)
                intent.putExtra("PairKey", pairKey)
                intent.putExtra("PairFlightMaleKey", pairFlightMaleKey)
                intent.putExtra("PairFlightFemaleKey", pairFlightFemaleKey)
                intent.putExtra("PairMaleKey", pairMaleKey)
                intent.putExtra("PairFemaleKey", pairFemaleKey)
                intent.putExtra("PairMaleID", pairMaleID)
                intent.putExtra("PairFemaleID", pairFemaleID)
                intent.putExtra("DateOfBirth", dateOfBirth)
                intent.putExtra("CageKeyFemale", cageKeyFemale)
                intent.putExtra("CageKeyMale", cageKeyMale)
                intent.putExtra("CageBirdFemale", cageBirdFemale)
                intent.putExtra("CageBirdMale", cageBirdMale)


                startActivity(intent)
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