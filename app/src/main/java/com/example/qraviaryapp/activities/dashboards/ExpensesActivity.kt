package com.example.qraviaryapp.activities.dashboards

import ExpensesData
import android.content.ContentValues
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.AddActivities.AddBirdFlightActivity
import com.example.qraviaryapp.activities.AddActivities.AddExpensesActivity
import com.example.qraviaryapp.adapter.DetailedAdapter.ExpensesAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ExpensesActivity : AppCompatActivity() {
    private lateinit var dataList: ArrayList<ExpensesData>
    private lateinit var recyclerView: RecyclerView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: DatabaseReference
    private lateinit var adapter: ExpensesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.statusbar)
        }
        setContentView(R.layout.activity_expenses)

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
            "<font color='$abcolortitle'>Expenses</font>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        // Check if night mode is enabled
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_white)

        mAuth = FirebaseAuth.getInstance()

        db = FirebaseDatabase.getInstance().reference
        recyclerView = findViewById(R.id.recyclerView)
        val gridLayoutManager = GridLayoutManager(this,1)
        recyclerView.layoutManager = gridLayoutManager
        dataList = ArrayList()
        adapter = ExpensesAdapter(this,dataList)
        recyclerView.adapter = adapter

        lifecycleScope.launch {
            try {
                val data = getDataFromDataBase()
                dataList.addAll(data)

                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error retrieving data: ${e.message}")
            }
        }

    }
    fun fab(view: View) {
//        showAddMutationsDialog()
        val i = Intent(this, AddExpensesActivity::class.java)
        startActivity(i)
        finish()
    }

    private suspend fun getDataFromDataBase(): List<ExpensesData> =
        withContext(Dispatchers.IO){

            val currentUserId = mAuth.currentUser?.uid
            val db = FirebaseDatabase.getInstance().reference.child("Users")
                .child("ID: ${currentUserId.toString()}").child("Expenses")
            val dataList = ArrayList<ExpensesData>()
            val snapshot = db.get().await()
            for (itemSnapshot in snapshot.children){
                val data = itemSnapshot.getValue(ExpensesData::class.java)
                if (data != null){
                    val key = itemSnapshot.key.toString()
                    data.expensesId = key
                    val mutationName = itemSnapshot.child("Category").value
                    val PriceName = itemSnapshot.child("Amount").value
                    val date = itemSnapshot.child("Beginning").value
                    val comment = itemSnapshot.child("Comment").value
                    val mutationNameValue = mutationName.toString()
                    val priceNameValue = PriceName.toString()
                    val dateValue = date.toString()
                    val commentValue = comment.toString()
                    data.expenses = mutationNameValue
                    data.price = priceNameValue
                    data.expensesComment = commentValue
                    data.expensesDate = dateValue
                    if (Looper.myLooper() != Looper.getMainLooper()) {
                        Log.d(ContentValues.TAG, "Code is running on a background thread")
                    } else {
                        Log.d(ContentValues.TAG, "Code is running on the main thread")
                        //
                    }
                    dataList.add(data)
                }
            }
            dataList.sortBy { it.expenses }
            dataList
        }

    fun showAddMutationsDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_expenses, null)

        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(dialogView)
        alertDialogBuilder.setTitle("Add Expenses")

        val alertDialog = alertDialogBuilder.create()

        val currentUserId = mAuth.currentUser?.uid
        val newDb = FirebaseDatabase.getInstance().reference.child("Users")
            .child("ID: ${currentUserId.toString()}").child("Expenses")
        val newMutationRef = newDb.push()
        val mutationName = dialogView.findViewById<EditText>(R.id.mutationName)
        val price = dialogView.findViewById<EditText>(R.id.PriceName)

        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)
        val btnSave = dialogView.findViewById<Button>(R.id.btnSave)

        btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        btnSave.setOnClickListener {
            val mutationNameValue = mutationName.text.toString()
            val PriceNameValue = price.text.toString()
            if (TextUtils.isEmpty(mutationNameValue)) {
                mutationName.error = "Enter Expenses Item"
            } else {
                val data: Map<String, Any?> = hashMapOf(
                    "Expenses" to mutationNameValue,
                    "Price" to PriceNameValue
                )
                newMutationRef.updateChildren(data)

                // Create a new ExpensesData object and add it to the dataList
                val newExpensesData = ExpensesData()
                newExpensesData.expenses = mutationNameValue
                newExpensesData.price = PriceNameValue
                dataList.add(newExpensesData)

                // Notify the adapter of the data change and sort the dataList
                adapter.notifyItemInserted(dataList.size - 1)
                dataList.sortBy { it.expenses }
                adapter.notifyDataSetChanged()

                alertDialog.dismiss()
            }
        }

        alertDialog.show()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            android.R.id.home -> {
                onBackPressed() // Call this to navigate back to the previous fragment
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}