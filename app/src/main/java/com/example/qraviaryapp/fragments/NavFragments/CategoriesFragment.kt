package com.example.qraviaryapp.fragments.NavFragments

import ExpensesData
import android.content.ContentValues
import android.os.Bundle
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.qraviaryapp.R
import com.example.qraviaryapp.adapter.CategoryFragmentAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class CategoriesFragment : Fragment(){

    private lateinit var dataList: ArrayList<ExpensesData>
    private lateinit var recyclerView: RecyclerView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: DatabaseReference
    private lateinit var adapter: CategoryFragmentAdapter

    private lateinit var totalBirds: TextView
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var fab: FloatingActionButton
    private lateinit var swipeToRefresh: SwipeRefreshLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_categories, container, false)
        swipeToRefresh = view.findViewById(R.id.swipeToRefresh)
        loadingProgressBar = view.findViewById(R.id.loadingProgressBar)
        totalBirds =view.findViewById(R.id.tvBirdCount)
        fab = view.findViewById(R.id.fab)
        mAuth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().reference
        recyclerView = view.findViewById(R.id.recyclerView)
        val gridLayoutManager = GridLayoutManager(requireContext(), 1)
        recyclerView.layoutManager = gridLayoutManager
        dataList = ArrayList()
        adapter = CategoryFragmentAdapter(requireContext(), dataList)
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
        fab.setOnClickListener {
            showAddCategoryDialog()

        }
        refreshApp()
        return view
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
            Toast.makeText(requireContext(), "Refreshed", Toast.LENGTH_SHORT).show()
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

            if (dataList.count() > 1) {
                totalBirds.text = dataList.count().toString() + " Categories"
            } else {
                totalBirds.text = dataList.count().toString() + " Category"
            }

            dataList.sortBy { it.expenses }

            dataList
        }

    fun showAddCategoryDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_category, null)

        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setView(dialogView)
        alertDialogBuilder.setTitle("Add Category")

        val alertDialog = alertDialogBuilder.create()

        val currentUserId = mAuth.currentUser?.uid
        val newDb = FirebaseDatabase.getInstance().reference.child("Users")
            .child("ID: ${currentUserId.toString()}").child("Category")
        val newMutationRef = newDb.push()

        val mutationName = dialogView.findViewById<EditText>(R.id.mutationName)

        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)
        val btnSave = dialogView.findViewById<Button>(R.id.btnSave)

        btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        btnSave.setOnClickListener {

            val newCategoryValue = mutationName.text.toString()
            if (TextUtils.isEmpty(newCategoryValue)) {
                mutationName.error = "Enter Category Name"
            } else {

                val data: Map<String, Any?> = hashMapOf(
                    "Category" to newCategoryValue
                )
                newMutationRef.updateChildren(data)
                val newCategory = ExpensesData()
                newCategory.expenses = newCategoryValue
                dataList.add(newCategory)
                adapter.notifyItemInserted(dataList.size - 1)
                dataList.sortBy { it.expenses }
                adapter.notifyDataSetChanged()

                alertDialog.dismiss()
            }


        }

        alertDialog.show()
    }
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


}
