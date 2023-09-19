package com.example.qraviaryapp.fragments.DetailedFragment

import BirdData
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qraviaryapp.R
import com.example.qraviaryapp.adapter.DetailedAdapter.BirdGalleryAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class BirdGalleryFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: DatabaseReference
    private lateinit var imageList: ArrayList<BirdData>
    private var birdKey: String? = null

    private lateinit var birdGalleryAdapter: BirdGalleryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bird_gallery, container, false)
        birdKey = arguments?.getString("BirdKey")

        imageList = ArrayList() // Initialize your imageUrls list

        val recyclerView = view.findViewById<RecyclerView>(R.id.imgRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        birdGalleryAdapter = BirdGalleryAdapter(requireContext(), imageList)
        recyclerView.adapter = birdGalleryAdapter

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().reference

        Log.d(TAG, "key: $birdKey")

        lifecycleScope.launch{
            try {
                val data = getDataFromDatabase()
                imageList.clear()
                imageList.addAll(data)
                birdGalleryAdapter.notifyDataSetChanged()

            }catch(e: Exception){
                Log.e(ContentValues.TAG, "Error retrieving data: ${e.message}")
            }
        }

        return view
    }

    private suspend fun getDataFromDatabase(): List<BirdData> = withContext(Dispatchers.IO) {
        val currentUserId = mAuth.currentUser?.uid
        val db = FirebaseDatabase.getInstance().getReference("Users")
            .child("ID: ${currentUserId.toString()}")
            .child("Birds")
            .child(birdKey.toString())
            .child("Gallery")
        val dataList = ArrayList<BirdData>()
        val snapshot = db.get().await()

        for (itemSnapshot in snapshot.children) {

            val data = itemSnapshot.value
            val newData = snapshot.getValue(BirdData::class.java)
            if (data != null) {
                val images = itemSnapshot.value

                if (newData != null) {
                    newData.imgUrl = images.toString()

//                  Log.d(TAG, "Images: ${images.toString()}")
                    dataList.add(newData)
                }
            }


        }
        dataList
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BirdGalleryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
