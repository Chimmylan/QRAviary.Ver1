package com.example.qraviaryapp.activities.detailedactivities

import ExpensesData
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qraviaryapp.R
import com.example.qraviaryapp.adapter.CategoryFragmentAdapter
import com.example.qraviaryapp.adapter.ExpensesFilterAdapter
import com.example.qraviaryapp.adapter.StickyHeaderItemDecorationcategories
import com.example.qraviaryapp.adapter.StickyHeaderItemDecorationexpensesfilter
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Calendar

class ExpensesFilterActivity : AppCompatActivity() {
    private lateinit var dataList: ArrayList<ExpensesData>
    private val checkedItems: MutableList<String> = mutableListOf()
    private lateinit var recyclerView: RecyclerView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: DatabaseReference
    private lateinit var adapter: ExpensesFilterAdapter

    private lateinit var fromDatePickerDialog: DatePickerDialog
    private lateinit var toatePickerDialog: DatePickerDialog

    private lateinit var fromBtn: MaterialButton
    private lateinit var toBtn: MaterialButton
    private var fromFormattedDate: String? = null
    private var toFormattedDate: String? = null

    private lateinit var etMinimum: EditText
    private lateinit var etMaximum: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.statusbar)
        }
        setContentView(R.layout.activity_expenses_filter)
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
            "<font color='$abcolortitle'>Expenses Filter</font>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        // Check if night mode is enabled
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_white)
        mAuth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().reference
        recyclerView = findViewById(R.id.recyclerView)

        dataList = ArrayList()
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ExpensesFilterAdapter(this, dataList)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(StickyHeaderItemDecorationexpensesfilter(adapter))

        fromBtn = findViewById(R.id.btnFrom)
        toBtn = findViewById(R.id.btnTo)

        etMaximum = findViewById(R.id.etMaximum)
        etMinimum = findViewById(R.id.etMinimum)

        initDatePickers()
        showDatePickerDialog(this, toBtn, toatePickerDialog)
        showDatePickerDialog(this, fromBtn, fromDatePickerDialog)

        lifecycleScope.launch {
            try {
                val data = getDataFromDataBase()
                dataList.addAll(data)

                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error retrieving data: ${e.message}")
            }
        }

        adapter.checkboxClickListener = { position, isChecked ->
            // Handle checkbox click event here
            // You can access the checkbox state and position
            val item = dataList[position]
            if (isChecked) {
                item.expenses?.let { checkedItems.add(it) }
            } else {
                checkedItems.remove(item.expenses)
            }
            Log.d(TAG, item.expenses.toString())
        }

    }

    private suspend fun getDataFromDataBase(): List<ExpensesData> =
        withContext(Dispatchers.IO) {
            val currentUserId = mAuth.currentUser?.uid
            val db = FirebaseDatabase.getInstance().reference.child("Users")
                .child("ID: ${currentUserId.toString()}").child("Category")
            val dataList = ArrayList<ExpensesData>()
            val snapshot = db.get().await()
            for (itemSnapshot in snapshot.children) {
                val data = itemSnapshot.getValue(ExpensesData::class.java)
                if (data != null) {
                    val key = itemSnapshot.key.toString()
                    data.expensesId = key
                    val categoryName = itemSnapshot.child("Category").value
                    val categoryNameValue = categoryName.toString()
                    data.expenses = categoryNameValue
                    dataList.add(data)
                }
            }


            dataList.sortBy { it.expenses }

            dataList
        }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_filters, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.ic_done -> {

                val i = Intent()

                Log.d(ContentValues.TAG, toFormattedDate.toString())
                Log.d(ContentValues.TAG, fromFormattedDate.toString())
                Log.d(ContentValues.TAG, "Checked " + checkedItems.toString())

                i.putExtra("ToDate", toFormattedDate)
                i.putExtra("FromDate", fromFormattedDate)
                i.putExtra("minimum", etMinimum.text.toString())
                i.putExtra("maximum", etMaximum.text.toString())
                i.putStringArrayListExtra("checkedCategory", ArrayList(checkedItems))

                setResult(Activity.RESULT_OK, i)
                finish()
                return true
            }

            android.R.id.home -> {
                onBackPressed() // Call this to navigate back to the previous fragment
                true
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