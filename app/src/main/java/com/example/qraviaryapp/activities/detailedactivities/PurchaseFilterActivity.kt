package com.example.qraviaryapp.activities.detailedactivities

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.example.qraviaryapp.R
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar

class PurchaseFilterActivity : AppCompatActivity() {


    private lateinit var fromDatePickerDialog: DatePickerDialog
    private lateinit var toatePickerDialog: DatePickerDialog

    private lateinit var fromBtn: MaterialButton
    private lateinit var toBtn: MaterialButton
    private var fromFormattedDate: String? = null
    private var toFormattedDate: String? = null

    private lateinit var etBuyer: EditText

    private lateinit var maleCb: CheckBox
    private lateinit var femaleCb: CheckBox
    private lateinit var unknownCb: CheckBox
    private lateinit var genderCheckboxes: List<CheckBox>

    private var currentUserId: String? = null

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.statusbar)
        }
        setContentView(R.layout.activity_purchase_filter)
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
            "<font color='$abcolortitle'>Sale Filter</font>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        // Check if night mode is enabled
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_white)

        currentUserId = FirebaseAuth.getInstance().currentUser?.uid

        sharedPreferences = getSharedPreferences("${currentUserId}_PurchasesFilter", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        fromBtn = findViewById(R.id.btnFrom)
        toBtn = findViewById(R.id.btnTo)

        etBuyer = findViewById(R.id.etSeller)

        maleCb = findViewById(R.id.maleCb)
        femaleCb = findViewById(R.id.femaleCb)
        unknownCb = findViewById(R.id.unknownCb)

        maleCb.isChecked = sharedPreferences.contains("gender_Male")

        femaleCb.isChecked = sharedPreferences.contains("gender_Female")

        unknownCb.isChecked = sharedPreferences.contains("gender_Unknown")

        genderCheckboxes = listOf(
            maleCb,
            femaleCb,
            unknownCb,
        )

        initDatePickers()
        showDatePickerDialog(this, toBtn, toatePickerDialog)
        showDatePickerDialog(this, fromBtn, fromDatePickerDialog)

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_filters, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.ic_done -> {

                val i = Intent()
                genderCheckboxes.forEach { checkBox ->
                    val checkboxKey = "gender_${checkBox.text}"
                    val isChecked = checkBox.isChecked
                    if (isChecked) {
                        editor.putString(checkboxKey, checkBox.text.toString())
                    } else {
                        editor.remove(checkboxKey)
                    }
                }
                editor.apply()

                Log.d(TAG, toFormattedDate.toString())
                Log.d(TAG, fromFormattedDate.toString())

                i.putExtra("ToDate", toFormattedDate)
                i.putExtra("FromDate", fromFormattedDate)
                i.putExtra("Buyer", etBuyer.text.toString())

                setResult(Activity.RESULT_OK, i)
                finish()
                return true
            }

            android.R.id.home -> {
                onBackPressed() // Call this to navigate back to the previous fragment
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initDatePickers() {
        val dateSetListenerTo =
            DatePickerDialog.OnDateSetListener { datePicker: DatePicker, year: Int, month: Int, day: Int ->
                toFormattedDate = makeDateString(day, month + 1, year)
                toBtn.text = toFormattedDate

            }

        /* val dateSetListenerBanding =
             DatePickerDialog.OnDateSetListener { datePicker: DatePicker, year: Int, month: Int, day: Int ->
                 bandFormattedDate = makeDateString(day, month + 1, year)
                 datebandButton.text = bandFormattedDate
             }*/
        val dateSetListenerFrom =
            DatePickerDialog.OnDateSetListener { datePicker: DatePicker, year: Int, month: Int, day: Int ->
                fromFormattedDate = makeDateString(day, month + 1, year)

                fromBtn.text = fromFormattedDate
            }

        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        val style = AlertDialog.THEME_HOLO_LIGHT

        toatePickerDialog =
            DatePickerDialog(this, style, dateSetListenerTo, year, month, day)
        /*  datePickerDialogBanding =
              DatePickerDialog(requireContext(), style, dateSetListenerBanding, year, month, day)*/
        fromDatePickerDialog =
            DatePickerDialog(this, style, dateSetListenerFrom, year, month, day)

        // You can set the max date for each dialog if needed.
        // For example:
        // datePickerDialogBirth.datePicker.maxDate = System.currentTimeMillis()
        // datePickerDialogBanding.datePicker.maxDate = System.currentTimeMillis()
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
}