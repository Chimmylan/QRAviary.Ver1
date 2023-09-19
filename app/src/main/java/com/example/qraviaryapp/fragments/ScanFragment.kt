package com.example.qraviaryapp.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
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
import com.example.qraviaryapp.activities.mainactivities.LoginActivity
import com.example.qraviaryapp.activities.mainactivities.SettingsActivity
import com.example.qraviaryapp.activities.detailedactivities.PairsDetailedActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

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
        options = view.findViewById(R.id.options)
        mAuth = FirebaseAuth.getInstance()
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        gsc = GoogleSignIn.getClient(requireActivity(), gso)

        options.setOnClickListener {
            showPopupMenu(options)
        }

       addmanually= view.findViewById(R.id.AddManually)
        addmanually.setOnClickListener {
            showPopupDialog()
        }
        return view
    }
    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Logout")
        builder.setMessage("Are you sure you want to logout?")
        builder.setPositiveButton("Yes") { _, _ ->
            signOut()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
    fun signOut() {
        mAuth.signOut()
        gsc.signOut().addOnCompleteListener {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }
    }
    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.inflate(R.menu.home_menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_settings -> {
                    val intent = Intent(requireContext(), SettingsActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_logout -> {
                    showLogoutConfirmationDialog()
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }
    private fun showCageAddDialog() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val popUp = inflater.inflate(R.layout.dialog_addbird_option, null)
        val btnNursery = popUp.findViewById<MaterialButton>(R.id.btnNursery)
        val btnFlight = popUp.findViewById<MaterialButton>(R.id.btnFlight)

        btnNursery.setOnClickListener {
            val i = Intent(requireContext(),AddBirdActivity::class.java)
            startActivity(i)
        }
        btnFlight.setOnClickListener {
            val i = Intent(requireContext(),AddBirdFlightActivity::class.java)
            startActivity(i)
        }

        builder.setTitle("Add Bird Selection")
        builder.setView(popUp)
        val alertDialog = builder.create()


        alertDialog.show()

    }
    private fun showPopupDialog() {

        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.popup_dialog_layout)

        val birdcv =dialog.findViewById<CardView>(R.id.birdcv)
        val paircv =dialog.findViewById<CardView>(R.id.paircv)
        val cagecv =dialog.findViewById<CardView>(R.id.cagescv)
        val eggcv =dialog.findViewById<CardView>(R.id.eggcv)
        val salecv =dialog.findViewById<CardView>(R.id.salescv)
        val purchasescv =dialog.findViewById<CardView>(R.id.purchasescv)
        val incubatingcv =dialog.findViewById<CardView>(R.id.incubatingcv)
        val maturingcv =dialog.findViewById<CardView>(R.id.maturingcv)
        birdcv.setOnClickListener {

            showCageAddDialog()
        }
        paircv.setOnClickListener {

            val intent = Intent(requireContext(), AddPairActivity::class.java)
            startActivity(intent)
            dialog.dismiss()
            true
        }
       cagecv.setOnClickListener {

            val intent = Intent(requireContext(), PairsDetailedActivity::class.java)
            startActivity(intent)
            dialog.dismiss()
            true
        }
        eggcv.setOnClickListener {

            val intent = Intent(requireContext(), AddEggActivity::class.java)
            startActivity(intent)
            dialog.dismiss()
            true
        }
        salecv.setOnClickListener {

            val intent = Intent(requireContext(), AddSaleActivity::class.java)
            startActivity(intent)
            dialog.dismiss()
            true
        }
        purchasescv.setOnClickListener {

            val intent = Intent(requireContext(), AddPurchaseActivity::class.java)
            startActivity(intent)
            dialog.dismiss()
            true
        }
        incubatingcv.setOnClickListener {

            val intent = Intent(requireContext(), AddIncubatingActivity::class.java)
            startActivity(intent)
            dialog.dismiss()
            true
        }
        maturingcv.setOnClickListener {

            val intent = Intent(requireContext(), AddMaturingActivity::class.java)
            startActivity(intent)
            dialog.dismiss()
            true
        }
        textclose = dialog.findViewById(R.id.txtclose)
        textclose.setOnClickListener {
            dialog.dismiss()
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
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
                activity.runOnUiThread {
                    Toast.makeText(requireContext(), it.text, Toast.LENGTH_SHORT).show()
                }
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
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