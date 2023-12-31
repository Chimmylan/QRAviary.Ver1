package com.example.qraviaryapp.activities.detailedactivities

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.IntentCompat
import androidx.core.text.HtmlCompat
import com.example.qraviaryapp.R
import com.google.android.material.button.MaterialButton

class BirdFilterActivity : AppCompatActivity() {
    private lateinit var btnMutation1: MaterialButton
    private lateinit var btnMutation2: MaterialButton
    private lateinit var btnMutation3: MaterialButton
    private lateinit var btnMutation4: MaterialButton
    private lateinit var btnMutation5: MaterialButton
    private lateinit var btnMutation6: MaterialButton

    private lateinit var linearLayoutMutation1: LinearLayout
    private lateinit var linearLayoutMutation2: LinearLayout
    private lateinit var linearLayoutMutation3: LinearLayout
    private lateinit var linearLayoutMutation4: LinearLayout
    private lateinit var linearLayoutMutation5: LinearLayout
    private lateinit var linearLayoutMutation6: LinearLayout

    private lateinit var etIdentifier: EditText

    private lateinit var btnNurseryCge: MaterialButton
    private lateinit var btnFlightCage: MaterialButton

    private lateinit var rbGroup: RadioGroup
    private lateinit var oldest: RadioButton
    private lateinit var youngest: RadioButton
    private lateinit var id: RadioButton
    private lateinit var cbMale: CheckBox
    private lateinit var cbFemale: CheckBox
    private lateinit var cbUnknown: CheckBox
    private lateinit var cbAvail: CheckBox
    private lateinit var cbPaired: CheckBox
    private lateinit var cbForSale: CheckBox
    private lateinit var cbSold: CheckBox
    private lateinit var cbDeceased: CheckBox
    private lateinit var cbExchange: CheckBox
    private lateinit var cbLost: CheckBox
    private lateinit var cbDonated: CheckBox
    private lateinit var cbOther: CheckBox

    private lateinit var addBtn: Button
    private lateinit var removeBtn: Button
    private lateinit var categoryCheckboxes: List<CheckBox>
    private lateinit var genderCheckboxes: List<CheckBox>

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.statusbar)
        }
        setContentView(R.layout.activity_bird_filter)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 0f
        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    this, R.color.toolbarcolor
                )
            )
        )
        val abcolortitle = resources.getColor(R.color.appbar)
        supportActionBar?.title = HtmlCompat.fromHtml(
            "<font color='$abcolortitle'>Birds Filters </font>", HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        // Check if night mode is enabled
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_white)

        sharedPreferences = getSharedPreferences("BirdFilter", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

//        btnMutation1 = findViewById(R.id.btnmutation1)
//        btnMutation2 = findViewById(R.id.btnmutation2)
//        btnMutation3 = findViewById(R.id.btnmutation3)
//        btnMutation4 = findViewById(R.id.btnmutation4)
//        btnMutation5 = findViewById(R.id.btnmutation5)
//        btnMutation6 = findViewById(R.id.btnmutation6)

        //  etIdentifier = findViewById(R.id.etIdentifier)
//
//        btnFlightCage = findViewById(R.id.btnflightcage)
//        btnNurseryCge = findViewById(R.id.btnnurserycage)


        rbGroup = findViewById(R.id.rgGroup)
        youngest = findViewById(R.id.youngest)
        oldest = findViewById(R.id.oldest)
        id = findViewById(R.id.identifier)

        cbMale = findViewById(R.id.cbMale)
        cbFemale = findViewById(R.id.cbFemale)
        cbUnknown = findViewById(R.id.cbUnknown)

        cbAvail = findViewById(R.id.cbAvailable)
        cbPaired = findViewById(R.id.cbPaired)
        cbForSale = findViewById(R.id.cbForSale)
        cbSold = findViewById(R.id.cbSold)
        cbDeceased = findViewById(R.id.cbDeceased)
        cbExchange = findViewById(R.id.cbExchange)
        cbLost = findViewById(R.id.cbLost)
        cbDonated = findViewById(R.id.cbDonated)
        cbOther = findViewById(R.id.cbOther)


        youngest.isChecked = true
        cbMale.isChecked = true
        cbFemale.isChecked = true
        cbUnknown.isChecked = true
        cbAvail.isChecked = true
        cbPaired.isChecked = true
        cbForSale.isChecked = true
        cbSold.isChecked = false
        cbDeceased.isChecked = false
        cbExchange.isChecked = false
        cbLost.isChecked = false
        cbDonated.isChecked = false
        cbOther.isChecked = false

        genderCheckboxes = listOf(
            cbMale,
            cbFemale,
            cbUnknown,
        )

        categoryCheckboxes = listOf(
            cbAvail, cbPaired, cbForSale, cbSold, cbDeceased, cbExchange, cbLost, cbDonated, cbOther
        )


    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_filters, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ic_done -> {
                var selectedStatus: Set<String>? = null
                var selectedGender: Set<String>? = null
                var selectedSort: String? = null

                val i = Intent()
                // Handle the click event for your menu item here

                // Save category checkboxes state
                categoryCheckboxes.forEach { checkBox ->
                    val checkboxKey = "category_${checkBox.text}"
                    val isChecked = checkBox.isChecked
                    if (isChecked){
                        editor.putString(checkboxKey, checkBox.text.toString())
                    }
                }

// Save gender checkboxes state
                genderCheckboxes.forEach { checkBox ->
                    val checkboxKey = "gender_${checkBox.text}"
                    val isChecked = checkBox.isChecked
                    if (isChecked){
                        editor.putString(checkboxKey, checkBox.text.toString())
                    }
                }

// Apply changes to SharedPreferences
                editor.apply()


                // You can add your logic here.
//                categoryCheckboxes.forEach { checkBox ->
//                    selectedStatus =
//                        categoryCheckboxes.filter { it.isChecked }.map { it.text.toString() }
//                            .toSet()
//                }
//                genderCheckboxes.forEach { checkBox ->
//                    selectedGender =
//                        genderCheckboxes.filter { it.isChecked }.map { it.text.toString() }
//                            .toSet()
//                }
//                val selectedStatusList = selectedStatus?.toList()
//                val selectedGenderList = selectedGender?.toList()
//
//                Log.d(TAG, selectedStatusList.toString())
//
//                if (youngest.isChecked){
//                    selectedSort = "Youngest"
//                }else if (oldest.isChecked){
//                    selectedSort = "Oldest"
//                }else if (id.isChecked){
//                    selectedSort = "Id"
//
//                }
//
//                i.putStringArrayListExtra("selectedStatusList",
//                    selectedStatusList?.let { ArrayList(it) })
//                i.putStringArrayListExtra("selectedGenderList",
//                    selectedGenderList?.let { ArrayList(it) })
//                i.putExtra("selectedSort", selectedSort)


                setResult(Activity.RESULT_OK, i)
                finish()





                return true
            }
            R.id.ic_erase -> {
                // Handle the click event for your menu item here
                // You can add your logic here.
                return true
            }
            android.R.id.home -> {
                // Handle the up button click event here
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}