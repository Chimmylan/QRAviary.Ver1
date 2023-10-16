package com.example.qraviaryapp.fragments.NavFragments

import ExpensesData
import ClickListener
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qraviaryapp.R
import com.example.qraviaryapp.adapter.CategoryAdapter
import com.example.qraviaryapp.adapter.CategoryFragmentAdapter
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_categories, container, false)

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

        return view
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
            dataList.sortBy { it.expenses }
            dataList
        }


    // Add the other onClick functions as needed

}
