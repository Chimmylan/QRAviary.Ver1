package com.example.qraviaryapp.activities.EditActivities

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.example.qraviaryapp.R
import com.google.android.material.button.MaterialButton
import java.util.*

class EditEggActivity : AppCompatActivity() {


    private lateinit var hatchedDatePickerDialog: DatePickerDialog
    private lateinit var incubatingDatePickerDialog: DatePickerDialog

    private lateinit var hatchedLinearLayout: LinearLayout
    private lateinit var incubatingLinearLayout: LinearLayout

    private lateinit var btnHatched: MaterialButton
    private lateinit var btnIncubating: MaterialButton

    private var hatchedFormattedDate: String? = null
    private var incubatingFormattedDate: String? = null

    private lateinit var spinnerStatus: Spinner
    private var status: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.bottom_nav_background)
        }
        setContentView(R.layout.activity_edit_egg)
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
            "<font color='$abcolortitle'>Edit Egg</font>",
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


        btnHatched = findViewById(R.id.btn_hatchedstartdate)
        btnIncubating = findViewById(R.id.btn_incubatingstartdate)
        incubatingLinearLayout = findViewById(R.id.incubationDateLayout)
        hatchedLinearLayout = findViewById(R.id.hatchedDateLayout)
        spinnerStatus = findViewById(R.id.spinnerstatus)


        OnActiveSpinner()
        initDatePickers()
        showDatePickerDialog(this,btnHatched,hatchedDatePickerDialog)
        showDatePickerDialog(this,btnIncubating,incubatingDatePickerDialog)

    }

    fun saveEdit() {

    }


    fun OnActiveSpinner() {
        val spinnerItems = resources.getStringArray(R.array.EggStatus)
        val adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerStatus.adapter = adapter

        spinnerStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                val selectedItem = p0?.getItemAtPosition(p2).toString()
                status = selectedItem
                incubatingLinearLayout.visibility = View.GONE
                hatchedLinearLayout.visibility = View.GONE


                when (p0?.getItemAtPosition(p2).toString()) {
                    "Incubating" -> {

                        incubatingLinearLayout.visibility = View.VISIBLE
                    }
                    "Hatched" -> {

                        hatchedLinearLayout.visibility = View.VISIBLE
                    }

                }


            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
        return

    }

    private fun initDatePickers() {
        val dateSetListenerIncubating =
            DatePickerDialog.OnDateSetListener { datePicker: DatePicker, year: Int, month: Int, day: Int ->
                incubatingFormattedDate = makeDateString(day, month + 1, year)
                btnIncubating.text = incubatingFormattedDate
            }
        val dateSetListenerHatched =
            DatePickerDialog.OnDateSetListener { datePicker: DatePicker, year: Int, month: Int, day: Int ->
                hatchedFormattedDate = makeDateString(day, month + 1, year)
                btnHatched.text = hatchedFormattedDate
            }

        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        val style = AlertDialog.THEME_HOLO_LIGHT

        incubatingDatePickerDialog =
            DatePickerDialog(this, style, dateSetListenerIncubating, year, month, day)
        hatchedDatePickerDialog =
            DatePickerDialog(this, style, dateSetListenerHatched, year, month, day)

    }

    fun showDatePickerDialog(
        context: Context, button: Button, datePickerDialog: DatePickerDialog
    ) {
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.delete_egg, menu)


        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {

                true
            }
            R.id.menu_remove -> {

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