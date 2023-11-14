package com.example.qraviaryapp.activities.mainactivities

import AccountData
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qraviaryapp.R
import com.example.qraviaryapp.adapter.ManageUserAdapter

class ManageUserActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<AccountData>
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var adapter: ManageUserAdapter
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_user)


        recyclerView = findViewById(R.id.recyclerView)
        dataList = ArrayList()
        sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE )
        imageView = findViewById(R.id.imageView)


        imageView.setOnClickListener{
            startActivity(Intent(this, SaveLoginActivity::class.java))
        }

        val accounts = getSavedAccounts(4)
        recyclerView.layoutManager = GridLayoutManager(this,1)
        adapter = ManageUserAdapter(this,accounts)
        recyclerView.adapter = adapter
    }

    fun getSavedAccounts(maxAccounts: Int): MutableList<AccountData> {
        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val dataList = mutableListOf<AccountData>()

        for (i in 1..maxAccounts) {
            val userKey = "user$i"
            val passKey = "userpass$i"

            val username = sharedPreferences.getString(userKey, "")
            val password = sharedPreferences.getString(passKey, "")

            if (!username.isNullOrBlank() && !password.isNullOrBlank()) {
                dataList.add(AccountData(username, password))
            }
            Log.d(TAG, "$username $password")
        }
        Log.d(TAG, dataList.toString())
        return dataList
    }



}