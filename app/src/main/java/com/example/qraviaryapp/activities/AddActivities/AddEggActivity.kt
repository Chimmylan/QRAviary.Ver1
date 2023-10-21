package com.example.qraviaryapp.activities.AddActivities

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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class AddEggActivity : AppCompatActivity() {


    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: DatabaseReference


    private lateinit var hatchedDatePickerDialog: DatePickerDialog
    private lateinit var incubatingDatePickerDialog: DatePickerDialog

    private lateinit var hatchedLinearLayout: LinearLayout
    private lateinit var incubatingLinearLayout: LinearLayout

    private lateinit var etIncubatingDate: EditText
    private lateinit var etMaturingDate: EditText

    private lateinit var btnHatched: MaterialButton
    private lateinit var btnIncubating: MaterialButton

    private var hatchedFormattedDate: String? = null
    private var incubatingFormattedDate: String? = null

    private lateinit var spinnerStatus: Spinner
    private lateinit var save: Button
    private lateinit var edit: Button
    private var status: String? = null

    private var incubatingStartDate: String? = null
    private var maturingStartDate: String? = null
    private var eggKey: String? = null
    private var pairKey: String? = null
    private var individualEggKey: String? = null
    private var currentUserId: String? = null
    private lateinit var hatchingDateTime: LocalDateTime
    private val formatter1 = DateTimeFormatter.ofPattern("MMM d yyyy hh:mm a", Locale.US)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.statusbar)
        }
        setContentView(R.layout.activity_add_egg)

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
            "<font color='$abcolortitle'>Add Egg</font>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        // Check if night mode is enabled
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_white)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().reference

        currentUserId = mAuth.currentUser?.uid
        save = findViewById(R.id.save)
        edit = findViewById(R.id.edit)
        etIncubatingDate = findViewById(R.id.etincubationdays)
        etMaturingDate = findViewById(R.id.etmaturingdays)
        btnHatched = findViewById(R.id.btn_hatchedstartdate)
        btnIncubating = findViewById(R.id.btn_incubatingstartdate)
        incubatingLinearLayout = findViewById(R.id.incubationDateLayout)
        hatchedLinearLayout = findViewById(R.id.hatchedDateLayout)
        spinnerStatus = findViewById(R.id.spinnerstatus)

        /* incubatingStartDate = intent.getStringExtra("IncubatingStartDate")
         maturingStartDate = intent.getStringExtra("MaturingStartDate")*/
        eggKey = intent.getStringExtra("EggKey")
        individualEggKey = intent.getStringExtra("IndividualEggKey")
        pairKey = intent.getStringExtra("PairKey")


        val sharedPrefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val edited = sharedPrefs.getBoolean("Edited", false)
        val maturingValue = sharedPrefs.getString("maturingValue", "50") // Default to 50 if not set
        val maturingDays = maturingValue?.toIntOrNull() ?: 50

        val incubatingValue = sharedPrefs.getString("incubatingValue", "21")
        val incubatingDays = incubatingValue?.toIntOrNull() ?: 21

        val currentUserId = mAuth.currentUser?.uid

        val dbase = FirebaseDatabase.getInstance().reference.child("Users")
            .child("ID: ${currentUserId.toString()}")
            .child("Pairs").child(pairKey.toString()).child("Clutches").child(eggKey.toString())
            .child(individualEggKey.toString())

/*        if (!edited) {
            etMaturingDate.setText(maturingDays.toString())
            etIncubatingDate.setText(incubatingDays.toString())
        } else {
            dbase.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot != null) {
                        val maturingDaysValue = snapshot.child("Maturing Days").value.toString()
                        val incubatingDaysValue = snapshot.child("Incubating Days").value.toString()

                        etIncubatingDate.setText(incubatingDaysValue)
                        etMaturingDate.setText(maturingDaysValue)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }*/



        var maturingDateText =  sharedPrefs?.getString("maturingValue", "50") // Default to 50 if not set
        var incubatingDateText = sharedPrefs?.getString("incubatingValue", "21")

        etIncubatingDate.setText(incubatingDateText)
        etMaturingDate.setText(maturingDateText)


            edit.setOnClickListener {
            etMaturingDate.isEnabled = true
            etIncubatingDate.isEnabled = true


            save.setOnClickListener {

                val newPrefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
                val editor = newPrefs.edit()
                editor.putBoolean("Edited", true)
                editor.apply()

                // Disable the EditText fields after saving
                etMaturingDate.isEnabled = false
                etIncubatingDate.isEnabled = false
            }
        }


        OnActiveSpinner()
        initDatePickers()

        showDatePickerDialog(this, btnHatched, hatchedDatePickerDialog)
        showDatePickerDialog(this, btnIncubating, incubatingDatePickerDialog)


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

    fun saveEdit() {
        val eggRef =
            db.child("Users").child("ID: $currentUserId").child("Pairs").child(pairKey.toString())
                .child("Clutches")
                .child(eggKey.toString()).push()


        val sharedPrefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)

        val maturingValue =
            sharedPrefs?.getString("maturingValue", "50") // Default to 50 if not set
        val maturingDays = maturingValue?.toIntOrNull() ?: 50

        val incubatingValue = sharedPrefs?.getString("incubatingValue", "21")
        val incubatingDays = incubatingValue?.toIntOrNull() ?: 21

        var currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("MMM d yyyy", Locale.US)

        val formattedDate = currentDate.format(formatter)

        val currentDateTime = LocalDateTime.now()
        hatchingDateTime = currentDateTime.plusDays(incubatingDays.toLong())


        eggRef.child("Status").setValue(status)

        if (btnHatched.text.toString() == "TODAY") {
            // Set the incubating date to the current date and time


            btnHatched.text = formattedDate
        }
        if (btnIncubating.text.toString() == "TODAY") {
            // Set the incubating date to the current date and time

            btnIncubating.text = formattedDate
        }

        if (incubatingLinearLayout.visibility == View.VISIBLE) {
            eggRef.child("Date").setValue(btnIncubating.text)
            val data: Map<String, Any?> = hashMapOf(
                "Incubating Days" to etIncubatingDate.text.toString(),
                "Maturing Days" to etMaturingDate.text.toString(),
                "Estimated Hatching Date" to hatchingDateTime.format(formatter1)
            )
            //TODO: Current date else statement
            eggRef.updateChildren(data)

        } else if (hatchedLinearLayout.visibility == View.VISIBLE) {
            eggRef.child("Date").setValue(btnHatched.text)
            val data: Map<String, Any?> = hashMapOf(
                "Incubating Days" to etIncubatingDate.text.toString(),
                "Maturing Days" to etMaturingDate.text.toString(),
            )
            //TODO: Current date else statement
            eggRef.updateChildren(data)

        } else {

            val data: Map<String, Any?> = hashMapOf(
                "Incubating Days" to etIncubatingDate.text.toString(),
                "Maturing Days" to etMaturingDate.text.toString(),
                "Date" to formattedDate
            )
            eggRef.updateChildren(data)
        }



        onBackPressed()


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


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                saveEdit()
                // Handle the Save button click here
                // Implement the logic to save the bird or perform any action you need.
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

