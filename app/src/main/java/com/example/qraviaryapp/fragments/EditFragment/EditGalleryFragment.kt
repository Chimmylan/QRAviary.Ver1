package com.example.qraviaryapp.fragments.AddFragment

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qraviaryapp.R
import com.example.qraviaryapp.adapter.ImageGalleryAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [AddGalleryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditGalleryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var storageRef = Firebase.storage.reference
    private var imageList: MutableList<Uri> = mutableListOf()
    private lateinit var imageAdapter: ImageGalleryAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: DatabaseReference
    private var birdKey: String? = null


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
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_gallery, container, false)


        mAuth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().reference

        val recyclerView: RecyclerView = view.findViewById(R.id.imgRecyclerView)
        val gridLayoutManager = GridLayoutManager(requireContext(), 1)
        recyclerView.layoutManager = gridLayoutManager
        imageList = ArrayList()
        imageAdapter = ImageGalleryAdapter(requireContext(), imageList)
        recyclerView.adapter = imageAdapter
        val fabCamera = view.findViewById<FloatingActionButton>(R.id.fabCamera)

        birdKey = arguments?.getString("BirdKey")

        fabCamera.setOnClickListener {
            val options = arrayOf("Take a Picture", "Choose from Gallery")

            AlertDialog.Builder(requireContext())
                .setTitle("Select an Option")
                .setItems(options) { _, which ->
                    when (which) {
                        0 -> {
                            // Take a picture
                            if (ContextCompat.checkSelfPermission(
                                    requireContext(),
                                    android.Manifest.permission.CAMERA
                                ) == PackageManager.PERMISSION_GRANTED
                            ) {
                                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                startActivityForResult(intent, CAMERA_REQUEST_CODE)
                            } else {
                                ActivityCompat.requestPermissions(
                                    requireActivity(),
                                    arrayOf(android.Manifest.permission.CAMERA),
                                    CAMERA_PERMISSION_CODE
                                )
                            }
                        }
                        1 -> {
                            // Choose from gallery
                            val galleryIntent =
                                Intent(
                                    Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                )
                            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
                        }
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
        imageDataGetter()
        return view
    }

   fun imageDataGetter(){
        val currentUser = mAuth.currentUser?.uid
        val birdRef = db.child("Users").child("ID: $currentUser")
            .child("Birds").child(birdKey.toString()).child("Gallery")

        birdRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(image in snapshot.children){
                    val i = image.value
                    imageList.add(Uri.parse(i.toString()))
                }
                imageAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAMERA_REQUEST_CODE)
            } else {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE -> {
                    val extras = data?.extras
                    val imageBitmap = extras?.get("data") as Bitmap
                    // Convert the Bitmap image to a URI and upload it to Firebase Storage
                    //uploadImageToStorage(getImageUri(imageBitmap))
                    imageList.clear()
                    imageList.add(getImageUri(imageBitmap))
                    imageAdapter.notifyDataSetChanged()

                }
                GALLERY_REQUEST_CODE -> {
                    val imageUri = data?.data
                    imageUri?.let {
                        // Upload the image to Firebase Storage
                        //uploadImageToStorage(imageUri)
                        imageList.clear()
                        imageList.add(imageUri)
                        imageAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    private fun getImageUri(imageBitmap: Bitmap): Uri {

        val tempUri = requireContext().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            ContentValues()
        )
        val outputStream = tempUri?.let {
            requireContext().contentResolver.openOutputStream(it)
        }
        if (outputStream != null) {
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        }
        outputStream?.close()
        return tempUri!!
    }

    fun uploadImageToStorage(
        birdId: String,
        NurseryId: String,
        newBundle: Bundle,
        motherKey: String,
        fatherKey: String,
        descendantfatherkey: String,
        descendantmotherkey: String,
        cagebirdkey: String,
        cagekeyvalue: String,
        soldId: String,
        purchaseId: String
    ) {
        storageRef = FirebaseStorage.getInstance().reference
        val currentUser = mAuth.currentUser?.uid
        val birdRef = db.child("Users").child("ID: $currentUser")
            .child("Birds").child(birdKey.toString()).child("Gallery")
        val nurseryRef = db.child("Users").child("ID: $currentUser")
            .child("Nursery Birds").child(NurseryId).child("Gallery")
        val descendantsfather = db.child("Users").child("ID: $currentUser")
            .child("Flight Birds").child(fatherKey).child("Descendants").child(descendantfatherkey)
            .child("Gallery")
        val descendantsmother = db.child("Users").child("ID: $currentUser")
            .child("Flight Birds").child(motherKey).child("Descendants").child(descendantmotherkey)
            .child("Gallery")
        val soldRef = db.child("Users").child("ID: $currentUser")
            .child("Sold Items").child(soldId).child("Gallery")
        val purchaseRef = db.child("Users").child("ID: $currentUser")
            .child("Purchase Items").child(purchaseId).child("Gallery")
        val cageRef = db.child("Users").child("ID: $currentUser").child("Cages")
            .child("Nursery Cages").child(cagekeyvalue).child("Birds").child(cagebirdkey)
            .child("Gallery")

        if (imageList.size == 0){
            birdRef.removeValue()
            nurseryRef.removeValue()
        }

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                for (imageUri in imageList) {
                    val imageRef = storageRef.child("images/${System.currentTimeMillis()}.jpg")
                    val uploadTask = imageRef.putFile(imageUri)
                    val imgIdKey = birdRef.push()
                    val imgId = imgIdKey.key
                    uploadTask.addOnSuccessListener { uploadTask ->
                        if (uploadTask.bytesTransferred == uploadTask.totalByteCount) {
                            imageRef.downloadUrl.addOnSuccessListener { uri ->
                                val imageUrl = uri.toString()
                                birdRef.removeValue()
                                nurseryRef.removeValue()

                                val data: Map<String, Any?> = hashMapOf(
                                    imgId.toString() to imageUrl
                                )
                                birdRef.setValue(data)
                                nurseryRef.updateChildren(data)

                                if (soldId != "null" && !soldId.isNullOrEmpty()) {
                                    soldRef.updateChildren(data)
                                }
                                if (purchaseId != "null" && !purchaseId.isNullOrEmpty()) {
                                    purchaseRef.updateChildren(data)
                                }
                                if (cagekeyvalue != "null" && !cagekeyvalue.isNullOrEmpty()) {
                                    cageRef.updateChildren(data)
                                }
                                if (fatherKey != "null" && !fatherKey.isNullOrEmpty()) {
                                    descendantsfather.updateChildren(data)
                                }
                                if (motherKey != "null" && !motherKey.isNullOrEmpty()) {
                                    descendantsmother.updateChildren(data)
                                }
                            }
                        }

                    }.addOnFailureListener {
                        // Handle upload failure here
                    }
                }
            }
        }

    }

    fun FlightuploadImageToStorage(
        birdId: String,
        FlightId: String,
        newBundle: Bundle,
        motherKey: String,
        fatherKey: String,
        descendantfatherkey: String,
        descendantmotherkey: String,
        cagebirdkey: String,
        cagekeyvalue: String,
        soldId: String,
        purchaseId: String
    ) {



        Log.d(ContentValues.TAG, "wwweeewww $soldId $purchaseId")
        storageRef = FirebaseStorage.getInstance().reference
        val currentUser = mAuth.currentUser?.uid
        val birdRef = db.child("Users").child("ID: $currentUser")
            .child("Birds").child(birdKey.toString()).child("Gallery")
        val nurseryRef = db.child("Users").child("ID: $currentUser")
            .child("Flight Birds").child(FlightId).child("Gallery")
        val soldRef = db.child("Users").child("ID: $currentUser")
            .child("Sold Items").child(soldId).child("Gallery")
        val descendantsfather = db.child("Users").child("ID: $currentUser")
            .child("Flight Birds").child(fatherKey).child("Descendants").child(descendantfatherkey)
            .child("Gallery")
        val descendantsmother = db.child("Users").child("ID: $currentUser")
            .child("Flight Birds").child(motherKey).child("Descendants").child(descendantmotherkey)
            .child("Gallery")
        val purchaseRef = db.child("Users").child("ID: $currentUser")
            .child("Purchase Items").child(purchaseId).child("Gallery")
        val cageRef = db.child("Users").child("ID: $currentUser").child("Cages")
            .child("Flight Cages").child(cagekeyvalue).child("Birds").child(cagebirdkey)
            .child("Gallery")

        if (imageList.size == 0){
            birdRef.removeValue()
            nurseryRef.removeValue()
        }

        for (imageUri in imageList) {
            val imageRef = storageRef.child("images/${System.currentTimeMillis()}.jpg")
            val uploadTask = imageRef.putFile(imageUri)
            val imgIdKey = birdRef.push()
            val imgId = imgIdKey.key
            uploadTask.addOnSuccessListener {



                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    birdRef.removeValue()
                    nurseryRef.removeValue()

                    val data: Map<String, Any?> = hashMapOf(
                        imgId.toString() to imageUrl
                    )
                    birdRef.updateChildren(data)
                    nurseryRef.updateChildren(data)
                    Log.d(TAG, "cagekeyflightgallery $cagekeyvalue")
                    if (soldId != "null" && !soldId.isNullOrEmpty()) {
                        soldRef.updateChildren(data)
                    }
                    if (purchaseId != "null" && !purchaseId.isNullOrEmpty()) {
                        purchaseRef.updateChildren(data)
                    }
                    if (cagekeyvalue != "null" && !cagekeyvalue.isNullOrEmpty()) {
                        cageRef.updateChildren(data)
                    }
                    if (fatherKey != "null" && !fatherKey.isNullOrEmpty()) {
                        descendantsfather.updateChildren(data)
                    }
                    if (motherKey != "null" && !motherKey.isNullOrEmpty()) {
                        descendantsmother.updateChildren(data)
                    }
                }

            }.addOnFailureListener {

            }
        }

    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddGalleryFragment.
         */
        // TODO: Rename and change types and number of parameters

        private const val CAMERA_PERMISSION_CODE = 1
        private const val CAMERA_REQUEST_CODE = 2
        private const val GALLERY_REQUEST_CODE = 3

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditGalleryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}