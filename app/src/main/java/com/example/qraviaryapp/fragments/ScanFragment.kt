package com.example.qraviaryapp.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.AddActivities.*
import com.example.qraviaryapp.activities.CagesActivity.BreedingListActivity
import com.example.qraviaryapp.activities.CagesActivity.FlightListActivity
import com.example.qraviaryapp.activities.mainactivities.LoginActivity
import com.example.qraviaryapp.activities.mainactivities.SettingsActivity
import com.example.qraviaryapp.activities.detailedactivities.PairsDetailedActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.json.JSONException
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val CAMERA_REQUEST_CODE = 101

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
    private lateinit  var gsc: GoogleSignInClient
    private lateinit var generate: MaterialButton
    private lateinit var options: TextView
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
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        gsc = GoogleSignIn.getClient(requireActivity(), gso)
        generate = view.findViewById(R.id.GenerateQR)

        generate.setOnClickListener {
            startActivity(Intent(requireContext(), GenerateQrActivity::class.java))
        }

        return view
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
                breedinCageScanner(it.text)
                flightCageScanner(it.text)
                nurseryCageScanner(it.text)
                pairScanner(it.text)

            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    fun breedinCageScanner(string: String){
        try {
            val jsonData = JSONObject(string)
            if (jsonData.has("CageKey")){
                if (jsonData.getString("CageType") == "Breeding"){
                    val key = jsonData.getString("CageKey")
                    val cageNumber = jsonData.getString("CageNumber")

                    requireActivity().runOnUiThread {
                        Toast.makeText(requireContext(), jsonData.toString(), Toast.LENGTH_SHORT).show()
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
        }
        catch (e: JSONException){

        }
    }

    fun flightCageScanner(string: String){
        try {
            val jsonData = JSONObject(string)
            if (jsonData.has("CageKey")){
                if (jsonData.getString("CageType") == "Flight"){
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
        }
        catch (e: JSONException){

        }
    }
    fun nurseryCageScanner(string: String){
        try {
            val jsonData = JSONObject(string)
            if (jsonData.has("CageKey")){
                if (jsonData.getString("CageType") == "Nursery"){
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
        }
        catch (e: JSONException){

        }
    }

    fun pairScanner(string: String){
        try {
            val jsonData = JSONObject(string)

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
        catch (e: JSONException){

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

    private fun setupPermission(){
        val permission = ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA)

        if(permission != PackageManager.PERMISSION_GRANTED){
            makeRequest()
        }

    }

    private fun makeRequest(){
        ActivityCompat.requestPermissions(requireActivity(),
            arrayOf(android.Manifest.permission.CAMERA),CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(requireContext(), "You ned camera permission to be able to use this qr", Toast.LENGTH_LONG).show()
                }else{
                    //Successful
                }
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