package com.example.qraviaryapp.fragments

import android.app.Activity
import android.app.AlertDialog
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
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.AddActivities.AddBirdActivity
import com.example.qraviaryapp.activities.AddActivities.AddBirdFlightActivity
import com.example.qraviaryapp.activities.AddActivities.GenerateQrActivity
import com.example.qraviaryapp.activities.CagesActivity.BreedingListActivity
import com.example.qraviaryapp.activities.CagesActivity.FlightListActivity
import com.example.qraviaryapp.activities.CagesActivity.NurseryListActivity
import com.example.qraviaryapp.activities.EditActivities.EditEggActivity
import com.example.qraviaryapp.activities.detailedactivities.BirdsDetailedActivity
import com.example.qraviaryapp.activities.detailedactivities.ClutchesDetailedActivity
import com.example.qraviaryapp.activities.detailedactivities.PairsDetailedActivity
import com.example.qraviaryapp.activities.mainactivities.LoginActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.MultiFormatReader
import com.google.zxing.NotFoundException
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
const val CAMERA_REQUEST_CODE = 101

/**
 * A simple [Fragment] subclass.
 * Use the [ScanFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScanFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var addmanually: Button
    private lateinit var textclose: TextView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var gso: GoogleSignInOptions
    private lateinit var gsc: GoogleSignInClient
    private lateinit var generate: MaterialButton
    private lateinit var options: TextView
    private lateinit var uploadqr: MaterialButton
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
        val view = inflater.inflate(R.layout.fragment_scan, container, false)

        mAuth = FirebaseAuth.getInstance()
        gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        gsc = GoogleSignIn.getClient(requireActivity(), gso)
        generate = view.findViewById(R.id.GenerateQR)
        uploadqr = view.findViewById(R.id.UploadQR)
        generate.setOnClickListener {
            startActivity(Intent(requireContext(), GenerateQrActivity::class.java))
        }

        return view
    }


    fun signOut() {
        mAuth.signOut()
        gsc.signOut().addOnCompleteListener {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }
    }



    private lateinit var codeScanner: CodeScanner
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val scannerView = view.findViewById<CodeScannerView>(R.id.scanner_view)

        val activity = requireActivity()
        setupPermission()
        codeScanner = CodeScanner(activity, scannerView)

        codeScanner.apply {

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                eggClutchesScanner(it.text)
                breedinCageScanner(it.text)
                flightCageScanner(it.text)
                nurseryCageScanner(it.text)
                pairScanner(it.text)
                clutchesScanner(it.text)
                detailedBirdScanner(it.text)


            }
        }

        uploadqr.setOnClickListener {
            openGalleryForImage()
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                // Handle the selected image URI
                decodeQRCodeFromGallery(uri)
            }
        }
    }
    private fun decodeQRCodeFromGallery(imageUri: Uri) {
        try {
            val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, imageUri)
            if (bitmap != null) {
                val result = decodeQRCode(bitmap)
                if (result != null) {
                    handleDecodedQRCode(result)
                } else {
                    showToast("Failed to decode QR code. Result is null.")
                }
            } else {
                showToast("Failed to load image from gallery. Bitmap is null.")
            }
        } catch (e: IOException) {
            e.printStackTrace()
            showToast("Error loading image from gallery: ${e.message}")
        }

    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }



    private fun decodeQRCode(bitmap: Bitmap): String? {
        try {
            val multiFormatReader = MultiFormatReader()
            val hints = mapOf(DecodeHintType.TRY_HARDER to true)
            Log.d(TAG, "Bitmap dimensions: ${bitmap.width} x ${bitmap.height}")

            val luminanceSource = RGBLuminanceSource(bitmap.width, bitmap.height, IntArray(bitmap.width * bitmap.height))
            val binaryBitmap = BinaryBitmap(HybridBinarizer(luminanceSource))

            return multiFormatReader.decodeWithState(binaryBitmap).text
        } catch (e: NotFoundException) {
            e.printStackTrace()
            Log.e(TAG, "QR code not found: ${e.message}")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "Error decoding QR code: ${e.message}")
        }
        return null
    }


    private fun handleDecodedQRCode(result: String) {
        Log.d(TAG, "Decoded QR Code Result: $result")
        // Implement the logic to handle the decoded QR code result
        // You can use your existing methods like eggClutchesScanner, breedinCageScanner, etc.
        eggClutchesScanner(result)
        breedinCageScanner(result)
        flightCageScanner(result)
        nurseryCageScanner(result)
        pairScanner(result)
        clutchesScanner(result)
        detailedBirdScanner(result)
    }


    fun breedinCageScanner(string: String) {
        try {
            val jsonData = JSONObject(string)
            if (jsonData.has("CageKey")) {
                if (jsonData.getString("CageType") == "Breeding") {
                    val key = jsonData.getString("CageKey")
                    val cageNumber = jsonData.getString("CageNumber")

                    requireActivity().runOnUiThread {
                        Toast.makeText(requireContext(), jsonData.toString(), Toast.LENGTH_SHORT)
                            .show()
                        Log.d(TAG, jsonData.toString())
                    }
                    val i = Intent(requireContext(), BreedingListActivity::class.java)
                    i.putExtra("CageKey", key)
                    i.putExtra("CageName", cageNumber)

                    startActivity(i)
                }
            }

            //if has cagekey
            // means we are scanning the cages
        } catch (e: JSONException) {

        }
    }

    fun flightCageScanner(string: String) {
        try {
            val jsonData = JSONObject(string)
            if (jsonData.has("CageKey")) {
                if (jsonData.getString("CageType") == "Flight") {
                    val key = jsonData.getString("CageKey")
                    val cageNumber = jsonData.getString("CageNumber")

                    val i = Intent(requireContext(), FlightListActivity::class.java)
                    i.putExtra("CageKey", key)
                    i.putExtra("CageName", cageNumber)
                    startActivity(i)
                }
            }

            //if has cagekey
            // means we are scanning the cages
        } catch (e: JSONException) {

        }
    }

    fun nurseryCageScanner(string: String) {
        try {
            val jsonData = JSONObject(string)
            if (jsonData.has("CageKey")) {
                if (jsonData.getString("CageType") == "Nursery") {
                    val key = jsonData.getString("CageKey")
                    val cageNumber = jsonData.getString("CageNumber")

                    val i = Intent(requireContext(), NurseryListActivity::class.java)
                    i.putExtra("CageKey", key)
                    i.putExtra("CageName", cageNumber)
                    startActivity(i)
                }
            }

            //if has cagekey
            // means we are scanning the cages
        } catch (e: JSONException) {

        }
    }

    fun pairScanner(string: String) {
        try {
            val jsonData = JSONObject(string)

            if (jsonData.has("PairQR")){
                val pairFemaleImg = jsonData.get("PairFemaleImg")
                val pairMaleImg = jsonData.get("PairMaleImg")
                val pairId = jsonData.get("PairId")
                val pairMaleKey = jsonData.get("PairMaleKey")
                val pairFemaleKey = jsonData.get("PairFemaleKey")
                val pairFlightMaleKey = jsonData.get("PairFlightMaleKey")
                val pairFlightFemaleKey = jsonData.get("PairFlightFemaleKey")
                val pairKey = jsonData.get("PairKey")
                val maleID = jsonData.get("MaleID")
                val femaleID = jsonData.get("FemaleID")
                val beginningDate = jsonData.get("BeginningDate")
                val maleGender = jsonData.get("MaleGender")
                val femaleGender = jsonData.get("FemaleGender")
                val cageKeyFemale = jsonData.get("CageKeyFemale")
                val cageKeyMale = jsonData.get("CageKeyMale")
                val cageBirdFemale = jsonData.get("CageBirdFemale")
                val cageBirdMale = jsonData.get("CageBirdMale")

                val bundle = Bundle()
                bundle.putString("PairFemaleImg", pairFemaleImg.toString())//
                bundle.putString("PairMaleImg", pairMaleImg.toString())//
                bundle.putString("PairId", pairId.toString())//
                bundle.putString("PairMaleKey", pairMaleKey.toString())//
                bundle.putString("PairFemaleKey", pairFemaleKey.toString())//
                bundle.putString("PairFlightMaleKey", pairFlightMaleKey.toString())//
                bundle.putString("PairFlightFemaleKey", pairFlightFemaleKey.toString())//
                bundle.putString("PairKey", pairKey.toString())//
                bundle.putString("MaleID", maleID.toString())//
                bundle.putString("FemaleID", femaleID.toString())//
                bundle.putString("BeginningDate", beginningDate.toString())//

                bundle.putString("MaleGender", maleGender.toString())//
                bundle.putString("FemaleGender", femaleGender.toString())//
                bundle.putString("CageKeyFemale", cageKeyFemale.toString())//
                bundle.putString("CageKeyMale", cageKeyMale.toString())//
                bundle.putString("CageBirdFemale", cageBirdFemale.toString())//
                bundle.putString("CageBirdMale", cageBirdMale.toString())//
                val i = Intent(requireContext(), PairsDetailedActivity::class.java)
                i.putExtras(bundle)
                startActivity(i)

            }


        } catch (e: JSONException) {

        }
    }

    fun clutchesScanner(string: String) {
        try {
            val jsonObject = JSONObject(string)
            if (jsonObject.has("ClutchQR")){
                val pairKey = jsonObject.get("PairKey")
                val clutchKey = jsonObject.get("ClutchKey")
                val eggKey = jsonObject.get("EggKey")
                val pairFlightMaleKey = jsonObject.get("PairFlightMaleKey")
                val pariFlightFemaleKey = jsonObject.get("PairFlightFemaleKey")
                val pairMaleKey = jsonObject.get("PairMaleKey")
                val pairFemaleKey = jsonObject.get("PairFemaleKey")
                val pairMaleID = jsonObject.get("PairMaleID")
                val pairFemaleID = jsonObject.get("PairFemaleID")
                val cageKeyFemale = jsonObject.get("CageKeyFemale")
                val cageKeyMale = jsonObject.get("CageKeyMale")
                val cageBirdFemale = jsonObject.get("CageBirdFemale")
                val cageBirdMale = jsonObject.get("CageBirdMale")

                val bundle = Bundle()
                Log.d(TAG, "LOG")

                bundle.putString("PairKey", pairKey.toString())
                bundle.putString("EggKey", eggKey.toString())
                bundle.putString("PairFlightMaleKey", pairFlightMaleKey.toString())
                bundle.putString("PairFlightFemaleKey", pariFlightFemaleKey.toString())
                bundle.putString("PairMaleKey", pairMaleKey.toString())
                bundle.putString("PairFemaleKey", pairFemaleKey.toString())
                bundle.putString("PairMaleID", pairMaleID.toString())
                bundle.putString("PairFemaleID", pairFemaleID.toString())
                bundle.putString("CageKeyFemale", cageKeyFemale.toString())
                bundle.putString("CageKeyMale", cageKeyMale.toString())
                bundle.putString("CageBirdFemale", cageBirdFemale.toString())
                bundle.putString("CageBirdMale", cageBirdMale.toString())
                val i = Intent(requireContext(), ClutchesDetailedActivity::class.java)
                i.putExtras(bundle)
                startActivity(i)
            }



        } catch (e: JSONException) {

        }
    }

    fun eggClutchesScanner(string: String) {
        try {
            val jsonObject = JSONObject(string)
            if (jsonObject.has("EggClutchQR")){
                val i = Intent(requireContext(), EditEggActivity::class.java)

                val incubatingStartDate = jsonObject.getString("IncubatingStartDate")
                val maturingStartDate = jsonObject.getString("MaturingStartDate")
                val eggKey = jsonObject.getString("EggKey")
                val individualEggKey = jsonObject.getString("IndividualEggKey")
                val pairKey = jsonObject.getString("PairKey")

                Log.d(TAG, "Incubating Start Date: $incubatingStartDate")
                Log.d(TAG, "Maturing Start Date: $maturingStartDate")
                Log.d(TAG, "Egg Key: $eggKey")
                Log.d(TAG, "Individual Egg Key: $individualEggKey")
                Log.d(TAG, "Pair Key: $pairKey")

                i.putExtra("IncubatingStartDate", incubatingStartDate)
                i.putExtra("MaturingStartDate", maturingStartDate)
                i.putExtra("EggKey", eggKey)
                i.putExtra("IndividualEggKey", individualEggKey)
                i.putExtra("PairKey", pairKey)

                startActivity(i)
            }

        } catch (e: JSONException) {
            Log.e(TAG, "JSON Parsing Error: ${e.message}")
            e.printStackTrace()
        }
    }

    fun detailedBirdScanner(string: String) {

        val jsonObject = JSONObject(string)
        if (jsonObject.has("BirdDetailedQR")){
            Log.d(TAG, "DetailedScanning")

            val i = Intent(requireContext(), BirdsDetailedActivity::class.java)

            val birdKey = jsonObject.optString("BirdKey", "")
            val nurseryKey = jsonObject.optString("NurseryKey", "")
            val flightKey = jsonObject.optString("FlightKey", "")
            val birdLegband = jsonObject.optString("BirdLegband", "")
            val birdId = jsonObject.optString("BirdId", "")
            val birdImg = jsonObject.optString("BirdImage", "")
            val birdGender = jsonObject.optString("BirdGender", "")
            val birdStatus = jsonObject.optString("BirdStatus", "")
            val birdDateBirth = jsonObject.optString("BirdDateBirth", "")
            val birdSalePrice = jsonObject.optString("BirdSalePrice", "")
            val birdBuyer = jsonObject.optString("BirdBuyer", "")
            val birdDeatherReason = jsonObject.optString("BirdDeathReason", "")
            val birdExchangeReason = jsonObject.optString("BirdExchangeReason", "")
            val birdExchangeWith = jsonObject.optString("BirdExchangeWith", "")
            val birdLostDetails = jsonObject.optString("BirdLostDetails", "")
            val birdAvailCage = jsonObject.optString("BirdAvailCage", "")
            val birdForSaleCage = jsonObject.optString("BirdForsaleCage", "")
            val birdRequestedPrice = jsonObject.optString("BirdRequestedPrice", "")
            val birdBuyPrice = jsonObject.optString("BirdBuyPrice", "")
            val birdBoughtOn = jsonObject.optString("BirdBoughtOn", "")
            val birdBoughtBreeder = jsonObject.optString("BirdBoughtBreeder", "")
            val birdMutation1 = jsonObject.optString("BirdMutation1", "")
            val birdMutation2 = jsonObject.optString("BirdMutation2", "")
            val birdMutation3 = jsonObject.optString("BirdMutation3", "")
            val birdMutation4 = jsonObject.optString("BirdMutation4", "")
            val birdMutation5 = jsonObject.optString("BirdMutation5", "")
            val birdMutation6 = jsonObject.optString("BirdMutation6", "")
            val birdSoldDate = jsonObject.optString("BirdSoldDate", "")
            val birdDeceasedDate = jsonObject.optString("BirdDeceaseDate", "")
            val birdExchangeDate = jsonObject.optString("BirdExchangeDate", "")
            val birdLostDate = jsonObject.optString("BirdLostDate", "")
            val birdDonatedDate = jsonObject.optString("BirdDonatedDate", "")
            val birdDonatedContact = jsonObject.optString("BirdDonatedContact", "")
            val birdFather = jsonObject.optString("BirdFather", "")
            val birdFatherKey = jsonObject.optString("BirdFatherKey", "")
            val birdMother = jsonObject.optString("BirdMother", "")
            val birdMotherKey = jsonObject.optString("BirdMotherKey", "")


            val bundle = Bundle()

            bundle.putString("BirdKey", birdKey)//
            bundle.putString("FlightKey", flightKey)
            bundle.putString("BirdLegband", birdLegband)
            bundle.putString("BirdId", birdId)
            bundle.putString("BirdImage", birdImg)
            bundle.putString("BirdGender", birdGender)
            bundle.putString("BirdStatus", birdStatus)
            bundle.putString("BirdDateBirth", birdDateBirth)
            bundle.putString("BirdSalePrice", birdSalePrice)
            bundle.putString("BirdBuyer", birdBuyer)
            bundle.putString("BirdDeathReason", birdDeatherReason)
            bundle.putString("BirdExchangeReason", birdExchangeReason)
            bundle.putString("BirdExchangeWith", birdExchangeWith)
            bundle.putString("BirdLostDetails", birdLostDetails)
            bundle.putString("BirdAvailCage", birdAvailCage)
            bundle.putString("BirdForsaleCage", birdForSaleCage)
            bundle.putString("BirdRequestedPrice", birdRequestedPrice)
            bundle.putString("BirdBuyPrice", birdBuyPrice)
            bundle.putString("BirdBoughtOn", birdBoughtOn)
            bundle.putString("BirdBoughtBreeder", birdBoughtBreeder)
            bundle.putString("BirdMutation1", birdMutation1)
            bundle.putString("BirdMutation2", birdMutation2)
            bundle.putString("BirdMutation3", birdMutation3)
            bundle.putString("BirdMutation4", birdMutation4)
            bundle.putString("BirdMutation5", birdMutation5)
            bundle.putString("BirdMutation6", birdMutation6)
            bundle.putString("BirdSoldDate", birdSoldDate)
            bundle.putString("BirdDeceaseDate", birdDeceasedDate)
            bundle.putString("BirdExchangeDate", birdExchangeDate)
            bundle.putString("BirdLostDate", birdLostDate)
            bundle.putString("BirdDonatedDate", birdDonatedDate)
            bundle.putString("BirdDonatedContact", birdDonatedContact)
            bundle.putString("BirdFather", birdFather)
            bundle.putString("BirdFatherKey", birdFatherKey)
            bundle.putString("BirdMother", birdMother)
            bundle.putString("BirdMotherKey", birdMotherKey)
            i.putExtras(bundle)
            startActivity(i)
        }


    }


    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun setupPermission() {
        val permission =
            ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }

    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        requireContext(),
                        "You ned camera permission to be able to use this qr",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    //Successful
                }
            }
        }
    }

    companion object {
        private const val GALLERY_REQUEST_CODE = 1
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ScanFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ScanFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}