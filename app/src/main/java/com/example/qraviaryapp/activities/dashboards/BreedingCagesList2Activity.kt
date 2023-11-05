package com.example.qraviaryapp.activities.dashboards

import CageData
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
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
import com.example.qraviaryapp.adapter.CageListAdapter2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class BreedingCagesList2Activity : AppCompatActivity(){

    private lateinit var recyclerView: RecyclerView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: DatabaseReference
    private lateinit var dataList: ArrayList<CageData>
    private lateinit var adapter: CageListAdapter2
    private lateinit var fabBtn: FloatingActionButton
    private lateinit var editText: EditText
    private lateinit var totalBirds: TextView
    private var cageCount = 0
    private lateinit var swipeToRefresh: SwipeRefreshLayout
    private var storageRef = Firebase.storage.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.statusbar)
        }
        setContentView(R.layout.activity_cages_list)

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
            "<font color='$abcolortitle'>Breeding Cages</font>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        // Check if night mode is enabled
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_white)
        totalBirds = findViewById(R.id.tvBirdCount)
        recyclerView = findViewById(R.id.cageRecyclerView)
        val gridLayoutManager = GridLayoutManager(this, 1)
        recyclerView.layoutManager = gridLayoutManager
        dataList = ArrayList()
        adapter = CageListAdapter2(this, dataList)
        recyclerView.adapter = adapter
        swipeToRefresh = findViewById(R.id.swipeToRefresh)
        mAuth = FirebaseAuth.getInstance()

        fabBtn = findViewById(R.id.fabCage)

        fabBtn.setOnClickListener {
            showCageAddDialog()
        }

        lifecycleScope.launch {
            try {
                val data = getDataFromDataBase()
                dataList.clear()
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
            Toast.makeText(applicationContext, "Refreshed", Toast.LENGTH_SHORT).show()
        }
    }
//    override fun onResume() {
//        super.onResume()
//        lifecycleScope.launch {
//            try {
//                val data = getDataFromDataBase()
//                dataList.clear()
//                dataList.addAll(data)
//                adapter.notifyDataSetChanged()
//            } catch (e: Exception) {
//                Log.e(ContentValues.TAG, "Error retrieving data: ${e.message}")
//            }
//        }
//    }

    private fun showCageAddDialog() {

        storageRef = FirebaseStorage.getInstance().reference
        val currentUserId = mAuth.currentUser?.uid
        val db = FirebaseDatabase.getInstance().reference.child("Users")
            .child("ID: ${currentUserId.toString()}").child("Cages")
            .child("Breeding Cages")

        val pushKey = db.push()

        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.cage_add_showlayout, null)
        editText = dialogLayout.findViewById<EditText>(R.id.etAddCage)
        val btncustom = dialogLayout.findViewById<Button>(R.id.custom)
        val btnclose = dialogLayout.findViewById<Button>(R.id.close)

        // Initially, show the EditText and btncustom

        btncustom.visibility = View.VISIBLE

        btncustom.setOnClickListener {
            // When custom button is clicked, hide EditText and btncustom, and show btnclose
            editText.visibility = View.VISIBLE
            btncustom.visibility = View.GONE
            btnclose.visibility = View.VISIBLE
        }

        btnclose.setOnClickListener {
            // When close button is clicked, hide btnclose and show EditText and btncustom
            btnclose.visibility = View.GONE
            editText.visibility = View.GONE
            btncustom.visibility = View.VISIBLE
        }

        builder.setTitle("Add Cage")
        builder.setPositiveButton("Add", null)

        builder.setNegativeButton("Cancel") { dialog, which ->
            Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show()
        }
        builder.setView(dialogLayout)

        val alertDialog = builder.create()
        alertDialog.setOnShowListener {
            val addButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            addButton.setOnClickListener {
                val newCageNumber = editText.text.toString().trim()

                if (newCageNumber.isNotEmpty()) {
                    // User entered a custom cage name, use it
                    val data: Map<String, Any?> = hashMapOf(
                        "Cage" to newCageNumber.toInt()
                    )
                    pushKey.updateChildren(data)
                    alertDialog.dismiss()
                    val bundleData = JSONObject()

                    bundleData.put("CageType", "Breeding")
                    bundleData.put("CageKey", "${pushKey.key}")
                    bundleData.put("CageNumber", newCageNumber)



                    qrAdd(bundleData, pushKey)


                    val newCage = CageData()
                    newCage.cage = newCageNumber
                    dataList.add(newCage)
                    adapter.notifyItemInserted(dataList.size - 1)
                    dataList.sortBy { it.cage }
                    lifecycleScope.launch {
                        try {
                            val data = getDataFromDataBase()
                            dataList.clear()
                            dataList.addAll(data)
                            adapter.notifyDataSetChanged()
                        } catch (e: Exception) {
                            Log.e(ContentValues.TAG, "Error retrieving data: ${e.message}")
                        }
                    }
                }else{
                    // Find the highest numbered cage and increment it
                    db.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {


                            var maxCageNumber = 0

                            for (cageSnapshot in snapshot.children) {
                                val cageNumber =
                                    cageSnapshot.child("Cage").getValue(Int::class.java)
                                if (cageNumber != null && cageNumber > maxCageNumber) {
                                    maxCageNumber = cageNumber
                                }
                            }

                            // Increment the cage number
                            val newCageNumber = maxCageNumber + 1

                            // Create a new cage with the incremented number
                            val data: Map<String, Any?> = hashMapOf(
                                "Cage" to newCageNumber
                            )

                            pushKey.updateChildren(data)

                            val bundleData = JSONObject()

                            bundleData.put("CageType", "Breeding")
                            bundleData.put("CageKey", "${pushKey.key}")
                            bundleData.put("CageNumber", newCageNumber)

                            qrAdd(bundleData, pushKey)


                            val newCage = CageData()
                            newCage.cage = newCageNumber.toString()
                            dataList.add(newCage)
                            adapter.notifyItemInserted(dataList.size - 1)
                            dataList.sortBy { it.cage }
                            lifecycleScope.launch {
                                try {
                                    val data = getDataFromDataBase()
                                    dataList.clear()
                                    dataList.addAll(data)
                                    adapter.notifyDataSetChanged()
                                } catch (e: Exception) {
                                    Log.e(ContentValues.TAG, "Error retrieving data: ${e.message}")
                                }
                            }
                            alertDialog.dismiss()
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Handle error
                        }
                    })
                }
            }
            val closeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            closeButton.setOnClickListener {
                alertDialog.dismiss()
            }
        }
        alertDialog.show()

    }


    private fun generateQRCodeUri(bundleCageData: String): Uri? {
        val multiFormatWriter = MultiFormatWriter()
        val bitMatrix = multiFormatWriter.encode(bundleCageData, BarcodeFormat.QR_CODE, 400, 400)
        val barcodeEncoder = BarcodeEncoder()
        val bitmap = barcodeEncoder.createBitmap(bitMatrix)

        // Create a file to store the QR code image
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File.createTempFile("QRCode", ".png", storageDir)

        try {
            val stream = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        // Convert the file URI to a string and return
        return Uri.fromFile(imageFile)
    }

    fun qrAdd(bundle: JSONObject, pushKey: DatabaseReference){


        val imageUri = generateQRCodeUri(bundle.toString())

        val imageRef = storageRef.child("images/${System.currentTimeMillis()}.jpg")
        val uploadTask = imageUri?.let { it1 -> imageRef.putFile(it1) }

        uploadTask?.addOnSuccessListener { task ->
            imageRef.downloadUrl.addOnSuccessListener{ uri->
                val imageUrl = uri.toString()

                val dataQR: Map<String, Any?> = hashMapOf(
                    "QR" to imageUrl
                )
                pushKey.updateChildren(dataQR)
            }
        }
    }


    private suspend fun getDataFromDataBase(): List<CageData> = withContext(Dispatchers.IO) {

        val currentUserId = mAuth.currentUser?.uid
        val db = FirebaseDatabase.getInstance().reference.child("Users")
            .child("ID: ${currentUserId.toString()}").child("Cages").child("Breeding Cages")
        val dataList = ArrayList<CageData>()
        val snapshot = db.get().await()
        for (itemSnapshot in snapshot.children) {
            val data = itemSnapshot.getValue(CageData::class.java)
            if (data != null) {


                val birds = itemSnapshot.child("Pair Birds")
                for (birdSnapshot in birds.children){

                    val pairCount = birds.childrenCount
                    val birdsCount = pairCount * 2
                    data.cagePairBirdCount = pairCount.toString()
                    data.cageBirdCount = birdsCount.toString()

                }
                val key = itemSnapshot.key.toString()

                val cageName = itemSnapshot.child("Cage").value
                val cageQR = itemSnapshot.child("QR").value
                val cageNameValue = cageName.toString()


                data.cageQR = cageQR.toString()
                data.cage = cageNameValue
                data.cageId = key
                cageCount++
                data.cageCount = cageCount.toString()
                if (Looper.myLooper() != Looper.getMainLooper()) {
                    Log.d(ContentValues.TAG, "Code is running on a background thread")
                } else {
                    Log.d(ContentValues.TAG, "Code is running on the main thread")
                    //
                }
                dataList.add(data)
            }

        }
        if(dataList.count()>1){
            totalBirds.text = dataList.count().toString() + " Cages"
        }
        else{
            totalBirds.text = dataList.count().toString() + " Cage"
        }
        dataList.sortBy { it.cage }
        dataList
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