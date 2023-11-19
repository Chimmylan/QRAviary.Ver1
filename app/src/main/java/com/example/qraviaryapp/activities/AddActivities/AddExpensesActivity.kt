package com.example.qraviaryapp.activities.AddActivities

import ExpensesData
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.example.qraviaryapp.R
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddExpensesActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: DatabaseReference
    private lateinit var btnCategory: MaterialButton
    private lateinit var etAmount: EditText
    private lateinit var datePickerDialogBeginning: DatePickerDialog
    private var beginningFormattedDate: String? = null
    private lateinit var btnBeginningDate: Button
    private lateinit var etComment: EditText
    private var btnCategoryValue: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.statusbar)
        }
        setContentView(R.layout.activity_add_expenses)

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
        supportActionBar?.title = HtmlCompat.fromHtml("<font color='$abcolortitle'>Add Expenses</font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
        // Check if night mode is enabled
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_white)
        btnCategory = findViewById(R.id.btnCategory)
        etComment = findViewById(R.id.etcomment)
        btnBeginningDate = findViewById(R.id.btndateband)
        etAmount = findViewById(R.id.etamount)
        mAuth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().reference

        initDatePickers()
        showDatePickerDialog(this, btnBeginningDate, datePickerDialogBeginning)


        btnCategory.setOnClickListener {
            val requestCode = 1
            val i = Intent(this, ChooseCategoryActivity::class.java)
            startActivityForResult(i, requestCode)
        }

    }

    fun addCategory() {
        val amountText = etAmount.text.toString()
        val commentValue = etComment.text.toString()
        val categoryValue = btnCategory.text.toString()
        var dateValue = btnBeginningDate.text.toString()
        val inputDateFormat = SimpleDateFormat("MMM d yyyy", Locale.getDefault())
        val outputDateFormat = SimpleDateFormat("dd - MM - yyyy", Locale.getDefault())
        if (amountText.isEmpty()) {
            etAmount.error = "Please input Amount"
            return
        }

        if (categoryValue.isEmpty()) {
            btnCategory.error = "Please select a Category..."
            return
        }

        if (dateValue.isEmpty()) {
            val currentDate = Calendar.getInstance().time
            dateValue = inputDateFormat.format(currentDate)
            btnBeginningDate.text = dateValue

        }


        val amountValue: Double
        try {
            amountValue = amountText.toDouble()
        } catch (e: NumberFormatException) {
            etAmount.error = "Invalid Amount"
            return
        }

        val userId = mAuth.currentUser?.uid.toString()
        val userBird = db.child("Users").child("ID: $userId").child("Expenses")
        val newExpenses = userBird.push()



        val date = inputDateFormat.parse(dateValue)
        val formattedDate = outputDateFormat.format(date)

        val monthYearParts = formattedDate.split(" - ")
        val day = monthYearParts[0]
        val month = monthYearParts[1]
        val year = monthYearParts[2]

        val data: Map<String, Any?> = hashMapOf(
            "Amount" to amountValue,
            "Beginning" to dateValue,
            "Comment" to commentValue,
            "Category" to categoryValue,
            "Date" to month.toFloat(),
            "Year" to year.toInt()
        )

        newExpenses.updateChildren(data)
        onBackPressed()
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                btnCategoryValue = data?.getStringExtra("CategoryName").toString()
                btnCategory.text = btnCategoryValue
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add_bird, menu)

        val saveMenuItem = menu.findItem(R.id.action_save)
        val scan =menu.findItem(R.id.menu_qr)
        scan.isVisible = false
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



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                addCategory()


                true
            }
            android.R.id.home -> {
                onBackPressed() // Call this to navigate back to the previous fragment
                true
            }
            else -> super.onOptionsItemSelected(item)
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
        datePickerDialogBeginning.datePicker.maxDate = System.currentTimeMillis()

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

