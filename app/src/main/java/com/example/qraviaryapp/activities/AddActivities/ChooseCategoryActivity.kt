package com.example.qraviaryapp.activities.AddActivities

import ClickListener
import ExpensesData
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.text.InputType
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.qraviaryapp.R
import com.example.qraviaryapp.adapter.CategoryAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ChooseCategoryActivity : AppCompatActivity(), ClickListener {

    private lateinit var dataList: ArrayList<ExpensesData>
    private lateinit var recyclerView: RecyclerView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: DatabaseReference
    private lateinit var adapter: CategoryAdapter
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var swipeToRefresh: SwipeRefreshLayout
    private lateinit var totalBirds: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.statusbar)
        }
        setContentView(R.layout.activity_choose_category)

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
        val abcolortitle = resources.getColor(R.color.white)
        supportActionBar?.title = HtmlCompat.fromHtml(
            "<font color='$abcolortitle'>Choose Category</font>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        // Check if night mode is enabled
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_white)
        swipeToRefresh = findViewById(R.id.swipeToRefresh)
        totalBirds = findViewById(R.id.tvBirdCount)
        loadingProgressBar = findViewById(R.id.loadingProgressBar)
        mAuth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().reference
        recyclerView = findViewById(R.id.recyclerView)
        val gridLayoutManager = GridLayoutManager(this, 1)
        recyclerView.layoutManager = gridLayoutManager
        dataList = ArrayList()
        adapter = CategoryAdapter(this, dataList)
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
        refreshApp()

    }
    private fun refreshApp() {
        swipeToRefresh.setOnRefreshListener {
            lifecycleScope.launch {
                try {

                    val data = getDataFromDataBase()
                    dataList.clear()
                    dataList.addAll(data)
                    swipeToRefresh.isRefreshing = false

                    adapter.notifyDataSetChanged()
                } catch (e: Exception) {
                    Log.e(ContentValues.TAG, "Error reloading data: ${e.message}")
                }

            }
            Toast.makeText(this, "Refreshed", Toast.LENGTH_SHORT).show()
        }
    }
    fun fab(view: View) {
        showAddCategoryDialog()
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

                    if (Looper.myLooper() != Looper.getMainLooper()) {
                        Log.d(ContentValues.TAG, "Code is running on a background thread")
                    } else {
                        Log.d(ContentValues.TAG, "Code is running on the main thread")
                        //
                    }
                    dataList.add(data)
                }
            }
            if (dataList.count() > 1) {
                totalBirds.text = dataList.count().toString() + " Categories"
            } else {
                totalBirds.text = dataList.count().toString() + " Category"
            }

            dataList.sortBy { it.expenses }
            dataList
        }

    fun showAddCategoryDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_category, null)

        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(dialogView)
        alertDialogBuilder.setTitle("Add Category")

        val alertDialog = alertDialogBuilder.create()

        val currentUserId = mAuth.currentUser?.uid
        val newDb = FirebaseDatabase.getInstance().reference.child("Users")
            .child("ID: ${currentUserId.toString()}").child("Category")
        val newMutationRef = newDb.push()

        val mutationName = dialogView.findViewById<EditText>(R.id.mutationName)
//        mutationName.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)
        val btnSave = dialogView.findViewById<Button>(R.id.btnSave)

        btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        btnSave.setOnClickListener {
            val newCategoryValue = mutationName.text.toString()

            if (TextUtils.isEmpty(newCategoryValue)) {
                mutationName.error = "Enter Category Name"
                return@setOnClickListener
            }

            // Check if the category already exists in dataList
            val categoryExists = dataList.any { it.expenses.equals(newCategoryValue, ignoreCase = true) }


            if (categoryExists) {
                // Display an error message or handle the case where the category already exists
                mutationName.error = "Category Already Exist"
                return@setOnClickListener
            } else {
                val data: Map<String, Any?> = hashMapOf(
                    "Category" to newCategoryValue.capitalize()
                )
                newMutationRef.updateChildren(data)

                val newCategory = ExpensesData()
                newCategory.expenses = newCategoryValue.capitalize()

                dataList.add(newCategory)

                adapter.notifyItemInserted(dataList.size - 1)
                dataList.sortBy { it.expenses }
                adapter.notifyDataSetChanged()
            }

            alertDialog.dismiss()
        }

        alertDialog.show()


    }
//    fun capitalizeFirstLetter(input: String): String {
//        return input.take(1).toUpperCase() + input.drop(1)
//    }
    override fun onResume() {
        super.onResume()

        // Call a function to reload data from the database and update the RecyclerView
        reloadDataFromDatabase()

    }

    private fun reloadDataFromDatabase() {
        loadingProgressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {

                val data = getDataFromDataBase()
                dataList.clear()
                dataList.addAll(data)

                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error reloading data: ${e.message}")
            } finally {

                loadingProgressBar.visibility = View.GONE
            }
        }}

    private fun getSortedGenesWithHeaders(sortedGenes: Array<String>): List<Pair<String, String>> {
        val genesWithHeaders = mutableListOf<Pair<String, String>>()
        var currentHeader = ""
        for (gene in sortedGenes) {
            val header = gene[0].toUpperCase().toString()
            if (header != currentHeader) {
                genesWithHeaders.add(
                    Pair(
                        header,
                        ""
                    )
                ) // Add a header with an empty gene to show only the header
                currentHeader = header
            }
            genesWithHeaders.add(Pair("", gene)) // Add an empty header with the gene
        }
        return genesWithHeaders
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

    override fun onClick(nameValue: String) {
        val intent = Intent()
        intent.putExtra("CategoryName", nameValue)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
    override fun onClick(nameValue: String, id: String) {
        TODO("Not yet implemented")
    }

    override fun onClick(nameValue: String, id: String, mutation: String) {
        TODO("Not yet implemented")
    }


}