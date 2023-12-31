package com.example.qraviaryapp.fragments.EditFragment

import BirdData
import BirdDataListener
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.AddActivities.AddCageScanActivity
import com.example.qraviaryapp.activities.dashboards.FlightCagesListActivity
import com.example.qraviaryapp.activities.dashboards.MutationsActivity
import com.example.qraviaryapp.fragments.AddFragment.OriginFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EditBasicFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditBasicFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var dbase: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private var birdKey: String? = null
    private var birdLegband: String? = null
    private var birdGender: String? = null
    private var birdStatus: String? = null
    private var birdId: String? = null
    private var birdsalePrice: String? = null
    private var birdDateBirth: String? = null
    private var birdMutation1: String? = null
    private var birdMutation2: String? = null
    private var birdMutation3: String? = null
    private var birdMutation4: String? = null
    private var birdMutation5: String? = null
    private var birdMutation6: String? = null
    private var birdBuyer: String? = null
    private var birdDeathReason: String? = null
    private var birdExchangeReason: String? = null
    private var birdExchangeWith: String? = null
    private var birdLostDetails: String? = null
    private var birdAvailCage: String? = null
    private var birdForsaleCage: String? = null
    private var birdRequestedPrice: String? = null
    private var birdComment: String? = null
    private var birdBuyPrice: String? = null
    private var birdBoughtOn: String? = null
    private var birdBoughtBreeder: String? = null
    private var birdBreeder: String? = null
    private var birdDeceaseDate: String? = null
    private var birdLostDate: String? = null
    private var birdSoldDate: String? = null
    private var birdExchangeDate: String? = null
    private var birdDonatedDate: String? = null
    private var birdDonatedContact: String? = null
    private var fatherKey: String? = null
    private var motherKey: String? = null

    private lateinit var bird_gender: ImageView
    private lateinit var bird_id: TextView
    private lateinit var bird_status: TextView
    private lateinit var bird_mutation: TextView
    private lateinit var bird_age: TextView
    private lateinit var bird_dateBirth: TextView
    private lateinit var bird_salePrice: TextView
    private lateinit var bird_buyer: TextView
    private lateinit var bird_deathreason: TextView
    private lateinit var bird_exchangereason: TextView
    private lateinit var bird_exchangewith: TextView
    private lateinit var bird_lostdetails: TextView
    private lateinit var bird_availcage: TextView
    private lateinit var bird_requestedprice: TextView
    private lateinit var bird_comment: TextView
    private lateinit var bird_buyprice: TextView
    private lateinit var bird_boughton: TextView
    private lateinit var bird_boughtbreeder: TextView
    private lateinit var bird_breeder: TextView
    private lateinit var bird_solddate: TextView
    private lateinit var bird_deceasedate: TextView
    private lateinit var bird_lostdate: TextView
    private lateinit var bird_donateddate: TextView
    private lateinit var bird_exchangedate: TextView
    private lateinit var bird_donatedcontact: TextView
    private lateinit var bird_legband: TextView
    private lateinit var snackbar: Snackbar
    private lateinit var connectivityManager: ConnectivityManager
    private var isNetworkAvailable = true

    private lateinit var datePickerDialogBirth: DatePickerDialog
    /*  private lateinit var datePickerDialogBanding: DatePickerDialog*/
    private lateinit var datePickerDialogSoldDate: DatePickerDialog
    private lateinit var datePickerDialogDeathDate: DatePickerDialog
    private lateinit var datePickerDialogExDate: DatePickerDialog
    private lateinit var datepickerDialogLostDate: DatePickerDialog
    private lateinit var datepickerDialogDonatedDate: DatePickerDialog
    private lateinit var rgGender: RadioGroup

    /*RadioGroup*/
    private lateinit var rbFemale: RadioButton
    private lateinit var rbMale: RadioButton
    private lateinit var rbUnknown: RadioButton

    /*Spinner*/
    private lateinit var spinnerStatus: Spinner
    private lateinit var btnMutation1: MaterialButton
    private lateinit var btnMutation2: MaterialButton
    private lateinit var btnMutation3: MaterialButton
    private lateinit var btnMutation4: MaterialButton
    private lateinit var btnMutation5: MaterialButton
    private lateinit var btnMutation6: MaterialButton
    private lateinit var slash1: TextView
    private lateinit var slash2: TextView
    private lateinit var slash3: TextView
    private lateinit var slash4: TextView
    private lateinit var slash5: TextView

    /*Button*/
    private lateinit var btnLostDate: MaterialButton
    private lateinit var btnDeathDate: MaterialButton
    private lateinit var btnSoldSaleDate: Button
    private lateinit var btnExDate: MaterialButton
    private lateinit var datebandButton: MaterialButton
    private lateinit var datebirthButton: MaterialButton
    private lateinit var btnDonatedDate: MaterialButton

    private lateinit var addBtn: Button
    private lateinit var removeBtn: Button


    /*Edit Text*/
    private lateinit var etExWith: EditText
    private lateinit var etDonateChooseContract: EditText
    private lateinit var etSoldBuyer: EditText
    private lateinit var etOtherComm: EditText
    private lateinit var etExReason: EditText
    private lateinit var etDeathReason: EditText
    private lateinit var etSoldSalePrice: EditText
    private lateinit var etAvailCage: MaterialButton
    private lateinit var etForSaleCage: MaterialButton
    private lateinit var etForSaleReqPrice: EditText
    lateinit var etIdentifier: EditText
    private lateinit var etLostDetails: EditText
    private lateinit var LayoutLegband: LinearLayout
    private lateinit var layoutIdentifier: LinearLayout
    private lateinit var tvLegband: TextView
    private lateinit var tvIdentifier: TextView
    private lateinit var etLegband: EditText
    /*Layouts*/
    private lateinit var editTextContainer: LinearLayout
    private lateinit var availableLayout: LinearLayout
    private lateinit var forSaleLayout: LinearLayout
    private lateinit var soldLayout: LinearLayout
    private lateinit var deceasedLayout: LinearLayout
    private lateinit var exchangeLayout: LinearLayout
    private lateinit var lostLayout: LinearLayout
    private lateinit var donatedLayout: LinearLayout
    private lateinit var otherLayout: LinearLayout

    /**/
    private var birdDataListener: BirdDataListener? = null
    private var bandFormattedDate: String? = null
    private var birthFormattedDate: String? = null
    private var soldFormattedDate: String? = null
    private var deathFormattedDate: String? = null
    private var exchangeFormattedDate: String? = null
    private var lostFormattedDate: String? = null
    private var donatedFormattedDate: String? = null

    private var mutation1IncubatingDays: String? = null
    private var mutation1MaturingDays: String? = null
    private var mutation2IncubatingDays: String? = null
    private var mutation2MaturingDays: String? = null
    private var mutation3IncubatingDays: String? = null
    private var mutation3MaturingDays: String? = null
    private var mutation4IncubatingDays: String? = null
    private var mutation4MaturingDays: String? = null
    private var mutation5IncubatingDays: String? = null
    private var mutation5MaturingDays: String? = null
    private var mutation6IncubatingDays: String? = null
    private var mutation6MaturingDays: String? = null

    private lateinit var cagescan: CardView
    private lateinit var cagescan1: CardView
    private var status: String? = null
    private lateinit var cageReference: DatabaseReference
    //endregion
    private lateinit var sharedPreferences: SharedPreferences

    var birdData = BirdData()

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
        val view = inflater.inflate(R.layout.fragment_basic, container, false)


        datebirthButton = view.findViewById(R.id.btndatebirth)
        btnSoldSaleDate = view.findViewById(R.id.soldSaleDate)
        btnDonatedDate = view.findViewById(R.id.btnDonatedDate)
        btnLostDate = view.findViewById(R.id.lostDateBtn)
        btnDeathDate = view.findViewById(R.id.deathDate)
        btnExDate = view.findViewById(R.id.exDate)

        dbase = FirebaseDatabase.getInstance().reference
        mAuth = FirebaseAuth.getInstance()
        initDatePickers()
//        datebandButton.text = getTodaysDate()
//        datebirthButton.text = getTodaysDate()

        showDatePickerDialog(requireContext(), datebirthButton, datePickerDialogBirth)
        /*showDatePickerDialog(requireContext(), datebandButton, datePickerDialogBanding)*/
        showDatePickerDialog(requireContext(), btnDeathDate, datePickerDialogDeathDate)
        showDatePickerDialog(requireContext(), btnSoldSaleDate, datePickerDialogSoldDate)
        showDatePickerDialog(requireContext(), btnExDate, datePickerDialogExDate)
        showDatePickerDialog(requireContext(), btnLostDate, datepickerDialogLostDate)
        showDatePickerDialog(requireContext(), btnDonatedDate, datepickerDialogDonatedDate)


//        LayoutLegband = view.findViewById(R.id.layoutlegband)
//        layoutIdentifier = view.findViewById(R.id.layoutIdentifier)
        tvLegband = view.findViewById(R.id.tvLegband)
        tvIdentifier = view.findViewById(R.id.tvidentifier)
        editTextContainer = view.findViewById(R.id.editTextContainer)
        availableLayout = view.findViewById(R.id.availableLayout)
        forSaleLayout = view.findViewById(R.id.forSaleLayout)
        soldLayout = view.findViewById(R.id.soldLayout)
        deceasedLayout = view.findViewById(R.id.deceasedLayout)
        exchangeLayout = view.findViewById(R.id.exchangeLayout)
        lostLayout = view.findViewById(R.id.lostLayout)
        donatedLayout = view.findViewById(R.id.donatedLayout)
        otherLayout = view.findViewById(R.id.otherLayout)
        /*========+++++++++++++==============*/
        /*ImageView*/
        /*RadioGroup*/
        rgGender = view.findViewById(R.id.radioGroupGender)
        /*RadioButtons*/
        rbMale = view.findViewById(R.id.radioButtonMale)
        rbFemale = view.findViewById(R.id.radioButtonFemale)
        rbUnknown = view.findViewById(R.id.radioButtonUnknown)


        /*EditText*/
        etLegband = view.findViewById(R.id.etLegband)
        etAvailCage = view.findViewById(R.id.etAvailCage)
        etForSaleCage = view.findViewById(R.id.etForSaleCage)
        etForSaleReqPrice = view.findViewById(R.id.etForSaleReqPrice)
        etSoldSalePrice = view.findViewById(R.id.soldSalePrice)
        etOtherComm = view.findViewById(R.id.etOthersComm)
        etExReason = view.findViewById(R.id.exReason)
        etDeathReason = view.findViewById(R.id.deathReason)
        etIdentifier = view.findViewById(R.id.etIdentifier)
        etLostDetails = view.findViewById(R.id.etLostDetails)
        etSoldBuyer = view.findViewById(R.id.etSaleContact)
        etDonateChooseContract = view.findViewById(R.id.etDonateChooseContact)
        etExWith = view.findViewById(R.id.exchangeWithBtn)

        /*Buttons*/
        addBtn = view.findViewById(R.id.addBtn)
        removeBtn = view.findViewById(R.id.removeBtn)


        /*Spinners*/
        btnMutation1 = view.findViewById(R.id.mutationBtn1)
        btnMutation2 = view.findViewById(R.id.mutationBtn2)
        btnMutation3 = view.findViewById(R.id.mutationBtn3)
        btnMutation4 = view.findViewById(R.id.mutationBtn4)
        btnMutation5 = view.findViewById(R.id.mutationBtn5)
        btnMutation6 = view.findViewById(R.id.mutationBtn6)
        spinnerStatus = view.findViewById(R.id.spinnerstatus)
        slash1 = view.findViewById(R.id.slash1)
        slash2 = view.findViewById(R.id.slash2)
        slash3 = view.findViewById(R.id.slash3)
        slash4 = view.findViewById(R.id.slash4)
        slash5 = view.findViewById(R.id.slash5)


        birdKey = arguments?.getString("BirdKey")
        birdGender = arguments?.getString("BirdGender")
        birdLegband = arguments?.getString("BirdLegband")
        birdStatus = arguments?.getString("BirdStatus")
        birdId = arguments?.getString("BirdId")
        cageKeyValue = arguments?.getString("CageKey")
        birdDonatedContact = arguments?.getString("BirdDonatedContact")
        birdsalePrice = arguments?.getString("BirdSalePrice")
        birdDateBirth = arguments?.getString("BirdDateBirth")
        birdBuyer = arguments?.getString("BirdBuyer")
        birdDeathReason = arguments?.getString("BirdDeathReason")
        birdExchangeReason = arguments?.getString("BirdExchangeReason")
        birdExchangeWith = arguments?.getString("BirdExchangeWith")
        birdLostDetails = arguments?.getString("BirdLostDetails")
        birdAvailCage = arguments?.getString("BirdAvailCage")
        birdForsaleCage = arguments?.getString("BirdForsaleCage")
        birdRequestedPrice = arguments?.getString("BirdRequestedPrice")
        birdComment = arguments?.getString("BirdComment")
        birdBuyPrice = arguments?.getString("BirdBuyPrice")
        birdBoughtOn = arguments?.getString("BirdBoughtOn")
        birdBoughtBreeder = arguments?.getString("BirdBoughtBreeder")
        birdBreeder = arguments?.getString("BirdBreeder")
        birdDeceaseDate = arguments?.getString("BirdDeceaseDate")
        birdSoldDate = arguments?.getString("BirdSoldDate")
        birdLostDate = arguments?.getString("BirdLostDate")
        birdExchangeDate = arguments?.getString("BirdExchangeDate")
        birdDonatedDate = arguments?.getString("BirdDonatedDate")
        birdMutation1 = arguments?.getString("BirdMutation1")
        birdMutation2 = arguments?.getString("BirdMutation2")
        birdMutation3 = arguments?.getString("BirdMutation3")
        birdMutation4 = arguments?.getString("BirdMutation4")
        birdMutation5 = arguments?.getString("BirdMutation5")
        birdMutation6 = arguments?.getString("BirdMutation6")
        fatherKey = arguments?.getString("BirdFatherKey")
        motherKey = arguments?.getString("BirdMotherKey")
        val flightType = arguments?.getString("FlightType").toString()
        val nurseryType = arguments?.getString("NurseryType").toString()


        Log.d(ContentValues.TAG, cageKeyValue.toString())

        sharedPreferences = requireContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)

        cagescan = view.findViewById(R.id.cagescan)
        cagescan.setOnClickListener {
            startActivity(Intent(requireContext(), AddCageScanActivity::class.java))
        }
        cagescan1 = view.findViewById(R.id.cagescan1)
        cagescan1.setOnClickListener {
            startActivity(Intent(requireContext(), AddCageScanActivity::class.java))
        }
        btnMutation1.setOnClickListener {
            val requestCode = 1 // You can use any integer as the request code
            val intent = Intent(requireContext(), MutationsActivity::class.java)
            startActivityForResult(intent, requestCode)

        }
        btnMutation2.setOnClickListener {
            val requestCode = 2 // You can use any integer as the request code
            val intent = Intent(requireContext(), MutationsActivity::class.java)
            startActivityForResult(intent, requestCode)

        }
        btnMutation3.setOnClickListener {
            val requestCode = 3 // You can use any integer as the request code
            val intent = Intent(requireContext(), MutationsActivity::class.java)
            startActivityForResult(intent, requestCode)

        }
        btnMutation4.setOnClickListener {
            val requestCode = 4 // You can use any integer as the request code
            val intent = Intent(requireContext(), MutationsActivity::class.java)
            startActivityForResult(intent, requestCode)

        }
        btnMutation5.setOnClickListener {
            val requestCode = 5 // You can use any integer as the request code
            val intent = Intent(requireContext(), MutationsActivity::class.java)
            startActivityForResult(intent, requestCode)

        }
        btnMutation6.setOnClickListener {

            val requestCode = 6 // You can use any integer as the request code
            val intent = Intent(requireContext(), MutationsActivity::class.java)
            startActivityForResult(intent, requestCode)

        }

        etAvailCage.setOnClickListener {
            val requestCode = 7 // You can use any integer as the request code
            val intent = Intent(requireContext(), FlightCagesListActivity::class.java)
            startActivityForResult(intent, requestCode)
        }
        etForSaleCage.setOnClickListener {
            val requestCode = 7 // You can use any integer as the request code
            val intent = Intent(requireContext(), FlightCagesListActivity::class.java)
            startActivityForResult(intent, requestCode)
        }
        cagescan.setOnClickListener {
            val requestCode = 7 // You can use any integer as the request code
            val intent = Intent(requireContext(), AddCageScanActivity::class.java)
            startActivityForResult(intent, requestCode)
        }




        etIdentifier.setText(birdId)

        if (birdLegband != "null" || !birdLegband.isNullOrEmpty()){
            etLegband.setText(birdLegband)
        }

        if (birdGender == "Female"){
            rbFemale.isChecked = true
        }
        else if (birdGender == "Male"){
            rbMale.isChecked = true
        }
        else{
            rbUnknown.isChecked = true
        }

        datebirthButton.setText(birdDateBirth)
        birthFormattedDate = birdDateBirth




        AddMutation()
        RemoveLastMutation()
        OnActiveSpinner()

        if (!birdMutation1.isNullOrEmpty()) {
            btnMutation1.setText(birdMutation1);
        } else {
            btnMutation1.visibility = View.GONE
        }

        if (!birdMutation2.isNullOrEmpty()) {
            btnMutation2.setText(birdMutation2);
            slash1.visibility = View.VISIBLE
            btnMutation2.visibility = View.VISIBLE
        } else {
            slash1.visibility = View.GONE
            btnMutation2.visibility = View.GONE
        }

        if (!birdMutation3.isNullOrEmpty()) {
            btnMutation3.setText(birdMutation3);
            btnMutation3.visibility = View.VISIBLE
            slash2.visibility = View.VISIBLE
        } else {
            slash2.visibility = View.GONE
            btnMutation3.visibility = View.GONE
        }

        if (!birdMutation4.isNullOrEmpty()) {
            btnMutation4.setText(birdMutation4);
            slash3.visibility = View.VISIBLE
            btnMutation4.visibility = View.VISIBLE
        } else {
            slash3.visibility = View.GONE
            btnMutation4.visibility = View.GONE
        }

        if (!birdMutation5.isNullOrEmpty()) {
            btnMutation5.setText(birdMutation5);
            slash4.visibility = View.VISIBLE
            btnMutation5.visibility = View.VISIBLE
        } else {
            slash4.visibility = View.GONE
            btnMutation5.visibility = View.GONE
        }

        if (!birdMutation6.isNullOrEmpty()) {
            btnMutation6.setText(birdMutation6);
            slash5.visibility = View.VISIBLE
            btnMutation6.visibility = View.VISIBLE
        } else
            slash5.visibility = View.GONE

        if (birdStatus == "Available"){
            if (etAvailCage.text.isEmpty() && birdAvailCage != "null"){
                etAvailCage.text = birdAvailCage
                //cageKeyValue = //
            }
        }


        if(birdStatus == "For Sale"){
            if (etForSaleCage.text.isEmpty() && birdForsaleCage != "null"){
                spinnerStatus.setSelection(1)
                etForSaleCage.text = birdForsaleCage
                //cageKeyValue = //
            }
        }



        return view

    }

    private fun getTextFromVisibleEditText(editText: EditText, layout: View): String {
        return if (layout.visibility == View.VISIBLE) {
            editText.text.toString()
        } else {
            ""
        }
    }

    private fun getTextFromVisibleMaterialButton(
        materialbtn: MaterialButton, layout: View
    ): String {
        return if (layout.visibility == View.VISIBLE) {
            materialbtn.text.toString()
        } else {
            ""
        }
    }

    private fun getTextFromVisibleDatePicker(Button: Button, layout: View): String {
        return if (layout.visibility == View.VISIBLE) {
            Button.text.toString()
        } else {
            ""
        }
    }

    private lateinit var cageNameValue: String
    private var cageKeyValue: String? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {

                val btnMutation1Value: String =
                    data?.getStringExtra("selectedMutationId").toString()
                mutation1MaturingDays =
                    data?.getStringExtra("selectedMutationMaturingDays").toString()
                mutation1IncubatingDays =
                    data?.getStringExtra("selectedMutationIncubatingDays").toString()

                btnMutation1.text = btnMutation1Value


            }
        }
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                val btnMutation2Value: String =
                    data?.getStringExtra("selectedMutationId").toString()
                mutation2MaturingDays =
                    data?.getStringExtra("selectedMutationMaturingDays").toString()
                mutation2IncubatingDays =
                    data?.getStringExtra("selectedMutationIncubatingDays").toString()
                btnMutation2.text = btnMutation2Value
            }
        }
        if (requestCode == 3) {
            if (resultCode == Activity.RESULT_OK) {
                val btnMutation3Value: String =
                    data?.getStringExtra("selectedMutationId").toString()
                mutation3MaturingDays =
                    data?.getStringExtra("selectedMutationMaturingDays").toString()
                mutation3IncubatingDays =
                    data?.getStringExtra("selectedMutationIncubatingDays").toString()
                btnMutation3.text = btnMutation3Value
            }
        }
        if (requestCode == 4) {
            if (resultCode == Activity.RESULT_OK) {
                val btnMutation4Value: String =
                    data?.getStringExtra("selectedMutationId").toString()
                mutation4MaturingDays =
                    data?.getStringExtra("selectedMutationMaturingDays").toString()
                mutation4IncubatingDays =
                    data?.getStringExtra("selectedMutationIncubatingDays").toString()
                btnMutation4.text = btnMutation4Value
            }
        }
        if (requestCode == 5) {
            if (resultCode == Activity.RESULT_OK) {
                val btnMutation5Value: String =
                    data?.getStringExtra("selectedMutationId").toString()
                mutation5MaturingDays =
                    data?.getStringExtra("selectedMutationMaturingDays").toString()
                mutation5IncubatingDays =
                    data?.getStringExtra("selectedMutationIncubatingDays").toString()
                btnMutation5.text = btnMutation5Value
            }
        }
        if (requestCode == 6) {
            if (resultCode == Activity.RESULT_OK) {
                val btnMutation6Value: String =
                    data?.getStringExtra("selectedMutationId").toString()
                mutation6MaturingDays =
                    data?.getStringExtra("selectedMutationMaturingDays").toString()
                mutation6IncubatingDays =
                    data?.getStringExtra("selectedMutationIncubatingDays").toString()
                btnMutation6.text = btnMutation6Value
            }
        }
        if (requestCode == 7) {
            if (resultCode == Activity.RESULT_OK) {
                cageNameValue = data?.getStringExtra("CageName").toString()
                cageKeyValue = data?.getStringExtra("CageKey").toString()
                Log.d(ContentValues.TAG, "cage name : $cageNameValue")
                Log.d(ContentValues.TAG, "cage Key : $cageKeyValue")
                if (availableLayout.visibility == View.VISIBLE) {
                    etAvailCage.setText(cageNameValue)
                } else if (forSaleLayout.visibility == View.VISIBLE) {
                    etForSaleCage.setText(cageNameValue)
                }
            }
        }
    }


    private var spinnerCount = 0


    fun AddMutation() {

        btnMutation2.visibility = View.GONE
        btnMutation3.visibility = View.GONE
        btnMutation4.visibility = View.GONE
        btnMutation5.visibility = View.GONE
        btnMutation6.visibility = View.GONE
        slash1.visibility = View.GONE
        slash2.visibility = View.GONE
        slash3.visibility = View.GONE
        slash4.visibility = View.GONE
        slash5.visibility = View.GONE
        addBtn.setOnClickListener {

            when (spinnerCount) {

                0 -> {

                    btnMutation2.visibility = View.VISIBLE
                    slash1.visibility = View.VISIBLE
                    spinnerCount++
                }
                1 -> {
                    btnMutation3.visibility = View.VISIBLE
                    slash2.visibility = View.VISIBLE
                    spinnerCount++
                }
                2 -> {
                    btnMutation4.visibility = View.VISIBLE
                    slash3.visibility = View.VISIBLE
                    spinnerCount++
                }
                3 -> {
                    btnMutation5.visibility = View.VISIBLE
                    slash4.visibility = View.VISIBLE
                    spinnerCount++
                }
                4 -> {
                    btnMutation6.visibility = View.VISIBLE
                    slash5.visibility = View.VISIBLE
                    spinnerCount++
                }
                else -> {
                    Toast.makeText(requireContext(), "added $spinnerCount", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }
    }

    fun RemoveLastMutation() {
        removeBtn.setOnClickListener {

            when (spinnerCount) {
                0 -> {
                    Toast.makeText(requireContext(), spinnerCount.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
                1 -> {
                    spinnerCount--
                    btnMutation2.visibility = View.GONE
                    slash1.visibility = View.GONE
                    Toast.makeText(requireContext(), spinnerCount.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
                2 -> {
                    spinnerCount--
                    btnMutation3.visibility = View.GONE
                    slash2.visibility = View.GONE
                    Toast.makeText(requireContext(), spinnerCount.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
                3 -> {

                    spinnerCount--
                    btnMutation4.visibility = View.GONE
                    slash3.visibility = View.GONE
                    Toast.makeText(requireContext(), spinnerCount.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
                4 -> {

                    spinnerCount--
                    btnMutation5.visibility = View.GONE
                    slash4.visibility = View.GONE
                    Toast.makeText(requireContext(), spinnerCount.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                    spinnerCount--
                    btnMutation6.visibility = View.GONE

                    Toast.makeText(requireContext(), spinnerCount.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
    suspend fun checkIdentifierExistence(identifier: String): Boolean =
        suspendCoroutine { continuaton ->

            val userId = mAuth.currentUser?.uid.toString()
            val userBirdsRef = dbase.child("Users").child("ID: $userId").child("Birds")

            var identifierExists = false

            userBirdsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val birds = snapshot.children
                    for (birdId in birds) {
                        if (identifier == birdId.child("Identifier").value) {
                            etIdentifier.error = "Identifier already exists"
                            identifierExists = true
                            break
                        }
                    }
                    continuaton.resume(identifierExists)
                }
                override fun onCancelled(error: DatabaseError) {
                    continuaton.resume(false)  // Handle onCancelled
                }
            })
        }
    suspend fun checkLegbandExistence(identifier: String): Boolean =
        suspendCoroutine { continuaton ->

            val userId = mAuth.currentUser?.uid.toString()
            val userBirdsRef = dbase.child("Users").child("ID: $userId").child("Birds")

            var identifierExists = false

            userBirdsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val birds = snapshot.children
                    for (birdId in birds) {
                        if (identifier == birdId.child("Legband").value) {
                            etLegband.error = "Legband already exists"
                            identifierExists = true
                            break
                        }
                    }
                    continuaton.resume(identifierExists)
                }
                override fun onCancelled(error: DatabaseError) {
                    continuaton.resume(false)  // Handle onCancelled
                }
            })
        }
   suspend fun birdDataGetters(callback: (birdId: String, NurseryId: String, newBundle: Bundle, soldId: String, cagebirdkey: String, cagekeyvalue: String) -> Unit) {

        /* val dataDateOfBanding = bandFormattedDate*/
        val dataDateOfBirth = birthFormattedDate
        /*==++==*/
        val dataLegband = etLegband.text.toString()
        val dataSelectedGen: RadioButton
        /*==++==*/
        val dataIdentifier = etIdentifier.text.toString()

        val stat = status

        /*Uses a function for comparison of visibility layouts*/
        val dataAvailCage = getTextFromVisibleMaterialButton(etAvailCage, availableLayout)
        val dataSaleCage = getTextFromVisibleMaterialButton(etForSaleCage, forSaleLayout)
        val dataReqPrice = getTextFromVisibleEditText(etForSaleReqPrice, forSaleLayout)
        val dataSoldSalePrice = getTextFromVisibleEditText(etSoldSalePrice, soldLayout)
        val dataSoldContact = getTextFromVisibleEditText(etSoldBuyer, soldLayout)
        val dataSoldSaleDate = getTextFromVisibleDatePicker(btnSoldSaleDate, soldLayout)
        val dataDeathDate = getTextFromVisibleDatePicker(btnDeathDate, deceasedLayout)
        val dataDeathReason = getTextFromVisibleEditText(etDeathReason, deceasedLayout)
        val dataExDate = getTextFromVisibleDatePicker(btnExDate, exchangeLayout)
        val dataExReason = getTextFromVisibleEditText(etExReason, exchangeLayout)
        val dataExContact = getTextFromVisibleEditText(etExWith, exchangeLayout)
        val dataLostDate = getTextFromVisibleDatePicker(btnLostDate, lostLayout)
        val dataLostDetails = getTextFromVisibleEditText(etLostDetails, lostLayout)
        val dataDonatedDate = getTextFromVisibleDatePicker(btnDonatedDate, donatedLayout)
        val dataDonatedContact = getTextFromVisibleEditText(etDonateChooseContract, donatedLayout)
        val dataOtherComments = getTextFromVisibleEditText(etOtherComm, otherLayout)
        /**/
        /*==++==*/
        val selectedMutations = mutableListOf<String?>()
        val spinners = arrayOf(
            btnMutation1, btnMutation2, btnMutation3, btnMutation4, btnMutation5, btnMutation6
        )

        val uniqueValues = HashSet<String>()

        for (i in spinners.indices) {
            if (spinners[i].visibility == View.VISIBLE) {
                val spinnerText = spinners[i].text.toString()

                if (spinnerText !in uniqueValues) {
                    selectedMutations.add(spinnerText)
                    uniqueValues.add(spinnerText)
                } else {
                    spinners[i].error = "Duplicate Mutation"
                    // Spinner has a duplicate value, show an error message
                    // You might want to replace this with your own error handling logic
                    Toast.makeText(
                        context,
                        "Duplicate Mutation",
                        Toast.LENGTH_SHORT
                    ).show()
                    return  // You can return or break out of the loop, depending on your requirements
                }
            } else {
                selectedMutations.add(null)
            }
        }

        val selectedOption: Int = rgGender.checkedRadioButtonId

        dataSelectedGen = view?.findViewById<RadioButton>(selectedOption)!!

        birdData = BirdData(
            legband = dataLegband,
            identifier = dataIdentifier,
            /*  dateOfBanding = dataDateOfBanding,*/
            dateOfBirth = dataDateOfBirth,
            mutation1 = selectedMutations[0],
            mutation2 = selectedMutations[1],
            mutation3 = selectedMutations[2],
            mutation4 = selectedMutations[3],
            mutation5 = selectedMutations[4],
            mutation6 = selectedMutations[5],
            gender = dataSelectedGen.text.toString(),
            status = stat,
            availCage = dataAvailCage,
            forSaleCage = dataSaleCage,
            reqPrice = dataReqPrice,
            soldDate = dataSoldSaleDate,
            soldPrice = dataSoldSalePrice,
            saleContact = dataSoldContact,
            deathDate = dataDeathDate,
            deathReason = dataDeathReason,
            exDate = dataExDate,
            exContact = dataExContact,
            exReason = dataExReason,
            lostDate = dataLostDate,
            lostDetails = dataLostDetails,
            donatedDate = dataDonatedDate,
            donatedContact = dataDonatedContact,
            otherComments = dataOtherComments
        )
        val inputDateFormat = SimpleDateFormat("MMM d yyyy", Locale.getDefault())
        val outputDateFormat = SimpleDateFormat("dd - MM - yyyy", Locale.getDefault())


        var validInputs = false
        var validIdentifier = false
        var validMutation = false
        /* var validDateOfBanding = false*/
        var validDateOfBirth = false
        var validforsale = false
        var valididexist = false
        var validlegbandexist = false
        //Validation
        if (TextUtils.isEmpty(dataIdentifier)) {
            etIdentifier.error = "Identifier cannot be empty"
        }
        else if (dataIdentifier == birdId){
            valididexist = true
        }else {

            val result = checkIdentifierExistence(dataIdentifier)

            if (result) {
                etIdentifier.error = "Identifier already exists"

            } else {
                valididexist = true
            }


        }
        if (TextUtils.isEmpty(dataLegband)) {

            validlegbandexist = true
        }
        else{
            val result = checkLegbandExistence(dataLegband)
            if (result) {
                etLegband.error = "Legband already exists"
            }
            else{
                validlegbandexist = true
            }
        }

        if (birdData.mutation1 == "None" || birdData.mutation2 == "None" || birdData.mutation3 == "None" || birdData.mutation4 == "None" || birdData.mutation5 == "None" || birdData.mutation6 == "None") {
            btnMutation1.error = "Mutation must not be empty"
        } else {
            validMutation = true
        }

        /*  if (TextUtils.isEmpty(dataDateOfBanding)) {
              datebandButton.error = "Date of banding must not be empty..."
          } else {
              validDateOfBanding = true
          }*/

        // Assuming dataDateOfBirth is a valid date string in the format "MM dd yyyy" (e.g., "09 29 1990")
        if (TextUtils.isEmpty(dataDateOfBirth)) {
            datebirthButton.error = "Date of birth must not be empty..."
            Toast.makeText(
                requireContext(),
                "Date of birth must not be empty...",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val dateFormat = SimpleDateFormat("MMM d yyyy", Locale.US)
            val birthDate = dateFormat.parse(dataDateOfBirth)
            val currentDate = Calendar.getInstance().time

            val ageInMillis = currentDate.time - birthDate.time
            val ageInDays = TimeUnit.MILLISECONDS.toDays(ageInMillis)

            if (ageInDays > 50) {
                // Age is less than 50 days, show an error message
                datebirthButton.error = "Age must be less than 50 days"
                Toast.makeText(
                    requireContext(),
                    "Age must be less than 50 days",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                validDateOfBirth = true
            }
        }

//soldlayout
       var validSold = false
       if (soldLayout.visibility == View.VISIBLE) {

           if (TextUtils.isEmpty(btnSoldSaleDate.text)) {
               btnSoldSaleDate.error = "Select a date..."
           }
           if (TextUtils.isEmpty(etSoldSalePrice.text)) {
               etSoldSalePrice.error = "Set a Price..."
           } else if (!TextUtils.isDigitsOnly(etSoldSalePrice.text)) {
               etSoldSalePrice.error = "Enter a valid price..."
           } else {
               validSold = true
           }
       }
       if (forSaleLayout.visibility == View.VISIBLE) {
           if (!TextUtils.isDigitsOnly(etForSaleReqPrice.text)) {
               etForSaleReqPrice.error = "Enter a valid price..."
           } else {
               validforsale = true
           }
       }


       if (validDateOfBirth && validMutation && valididexist && validlegbandexist) {
           validInputs = true
       }
        //


        val userId = mAuth.currentUser?.uid.toString()
        var cageBirdKey = ""
        if (!cageKeyValue.isNullOrEmpty()) {
            cageReference = cageKeyValue?.let {
                dbase.child("Users").child("ID: $userId").child("Cages")
                    .child("Flight Cages").child(it).child("Birds").push()
            }!!

            cageBirdKey = cageReference.key.toString()
        }

        val userBird = dbase.child("Users").child("ID: $userId").child("Birds")
        val NurseryBird = dbase.child("Users").child("ID: $userId").child("Flight Birds")

        val newBirdPref = userBird.child(birdKey.toString())
        val SoldRef = dbase.child("Users").child("ID: $userId").child("Sold Items")
        val SoldBirdRef = SoldRef.push()
        var soldId: String? = null

        val parentRef = newBirdPref.child("Parents")
        val newNurseryPref = NurseryBird.child(birdKey.toString())

        val birdId = newBirdPref.key
        val FlightId = newNurseryPref.key
        val args = Bundle()

        val originFragment = OriginFragment()
        originFragment.arguments = args
        val mutation1 = hashMapOf(
            "Mutation Name" to birdData.mutation1,
            "Maturing Days" to mutation1MaturingDays,
            "Incubating Days" to mutation1IncubatingDays
        )
        val mutation2 = hashMapOf(
            "Mutation Name" to birdData.mutation2,
            "Maturing Days" to mutation2MaturingDays,
            "Incubating Days" to mutation2IncubatingDays
        )
        val mutation3 = hashMapOf(
            "Mutation Name" to birdData.mutation3,
            "Maturing Days" to mutation3MaturingDays,
            "Incubating Days" to mutation3IncubatingDays
        )
        val mutation4 = hashMapOf(
            "Mutation Name" to birdData.mutation4,
            "Maturing Days" to mutation4MaturingDays,
            "Incubating Days" to mutation4IncubatingDays
        )
        val mutation5 = hashMapOf(
            "Mutation Name" to birdData.mutation5,
            "Maturing Days" to mutation5MaturingDays,
            "Incubating Days" to mutation5IncubatingDays
        )
        val mutation6 = hashMapOf(
            "Mutation Name" to birdData.mutation6,
            "Maturing Days" to mutation6MaturingDays,
            "Incubating Days" to mutation6IncubatingDays
        )
        val newBundle = Bundle()

        newBundle.putString("BirdIdentifier", birdData.identifier)//
        newBundle.putString("BirdLegband", birdData.legband)//
        newBundle.putString("BirdImage", birdData.img)//
        newBundle.putString("BirdKey", newBirdPref.key)//
        newBundle.putString("FlightKey", newNurseryPref.key)
        newBundle.putString("BirdGender", birdData.gender)//
        newBundle.putString("BirdStatus", birdData.status)//
        newBundle.putString("BirdDateBirth", birdData.dateOfBirth)//
        newBundle.putString("BirdSalePrice", birdData.soldPrice)//
        newBundle.putString("BirdBuyer", birdData.saleContact)//
        newBundle.putString("BirdDeathReason", birdData.deathReason)//
        newBundle.putString("BirdExchangeReason", birdData.exReason)//
        newBundle.putString("BirdExchangeWith", birdData.exContact)//
        newBundle.putString("BirdLostDetails", birdData.lostDetails)//
        newBundle.putString("BirdAvailCage", birdData.availCage)//
        newBundle.putString("BirdForsaleCage", birdData.forSaleCage)//
        newBundle.putString("BirdRequestedPrice", birdData.reqPrice)//
        newBundle.putString("BirdComment", birdData.otherComments)//
        newBundle.putString("BirdBuyPrice", birdData.buyPrice)//
        newBundle.putString("BirdBoughtOn", birdData.boughtDate)//
        newBundle.putString("BirdBoughtBreeder", birdData.breederContact)//
        newBundle.putString("BirdBreeder", birdData.otOtherContact)//
        newBundle.putString("BirdDeceaseDate", birdData.deathDate)//
        newBundle.putString("BirdSoldDate", birdData.soldDate)//
        newBundle.putString("BirdLostDate", birdData.lostDate)//
        newBundle.putString("BirdExchangeDate", birdData.exDate)//
        newBundle.putString("BirdDonatedDate", birdData.donatedDate)//
        newBundle.putString("BirdDonatedContact", birdData.donatedContact)//
        newBundle.putSerializable("BirdMutation1", mutation1)//
        newBundle.putSerializable("BirdMutation2", mutation2)//
        newBundle.putSerializable("BirdMutation3", mutation3)//
        newBundle.putSerializable("BirdMutation4", mutation4)//
        newBundle.putSerializable("BirdMutation5", mutation5)//
        newBundle.putSerializable("BirdMutation6", mutation6)//
        newBundle.putString("BirdMutation1Name", birdData.mutation1)//
        newBundle.putString("BirdMutation2Name", birdData.mutation2)//
        newBundle.putString("BirdMutation3Name", birdData.mutation3)//
        newBundle.putString("BirdMutation4Name", birdData.mutation4)//
        newBundle.putString("BirdMutation5Name", birdData.mutation5)//
        newBundle.putString("BirdMutation6Name", birdData.mutation6)//
        newBundle.putString("BirdFather", birdData.father)//
        newBundle.putString("BirdFatherKey", birdData.fatherKey)//
        newBundle.putString("BirdMother", birdData.mother)//
        newBundle.putString("BirdMotherKey", birdData.motherKey)//
        newBundle.putString("CageKeyValue", cageKeyValue)//
        newBundle.putString("CageBirdKeyValue", cageBirdKey)
        newBundle.putBoolean("fromFlightAdapterList", true)//

        Log.d(ContentValues.TAG, userId)




        if (validInputs) {
            if (availableLayout.visibility == View.VISIBLE) {

                val data: Map<String, Any?> = hashMapOf(
                    "Legband" to birdData.legband,
                    "Identifier" to birdData.identifier,
                    "Gender" to birdData.gender,
                    "Mutation1" to mutation1,
                    "Mutation2" to mutation2,
                    "Mutation3" to mutation3,
                    "Mutation4" to mutation4,
                    "Mutation5" to mutation5,
                    "Mutation6" to mutation6,
                    /* "Date of Banding" to birdData.dateOfBanding,*/
                    "Date of Birth" to birdData.dateOfBirth,
                    "Status" to birdData.status,
                    "Cage" to birdData.availCage,
                    "Flight Key" to FlightId,
                    "Bird Key" to birdId,
                    "CageKey" to cageKeyValue,
                    "Cage Bird Key" to cageBirdKey
                )

                if (!cageKeyValue.isNullOrEmpty()) {
                    cageReference.updateChildren(data)
                }
                newBirdPref.updateChildren(data)
                newNurseryPref.updateChildren(data)


            } else if (forSaleLayout.visibility == View.VISIBLE) {
                if (validforsale) {
                val data: Map<String, Any?> = hashMapOf(
                    "Legband" to birdData.legband,
                    "Identifier" to birdData.identifier,
                    "Gender" to birdData.gender,
                    "Mutation1" to mutation1,
                    "Mutation2" to mutation2,
                    "Mutation3" to mutation3,
                    "Mutation4" to mutation4,
                    "Mutation5" to mutation5,
                    "Mutation6" to mutation6,
                    /*"Date of Banding" to birdData.dateOfBanding,*/
                    "Date of Birth" to birdData.dateOfBirth,
                    "Status" to birdData.status,
                    "Cage" to birdData.forSaleCage,
                    "Requested Price" to birdData.reqPrice,
                    "Flight Key" to FlightId,
                    "Bird Key" to birdId,
                    "CageKey" to cageKeyValue,
                    "Cage Bird Key" to cageBirdKey


                )
                if (!cageKeyValue.isNullOrEmpty()) {
                    cageReference.updateChildren(data)
                }
                newBirdPref.updateChildren(data)
                newNurseryPref.updateChildren(data)
                } else {
                    return
                }
            } else if (soldLayout.visibility == View.VISIBLE) {
                if (validSold) {
                    soldId = SoldBirdRef.key
                    val date = inputDateFormat.parse(dataSoldSaleDate)
                    val formattedDate = outputDateFormat.format(date)

                    val monthYearParts = formattedDate.split(" - ")
                    val day = monthYearParts[0]
                    val month = monthYearParts[1]
                    val year = monthYearParts[2]
                    val data: Map<String, Any?> = hashMapOf(
                        "Legband" to birdData.legband,
                        "Identifier" to birdData.identifier,
                        "Gender" to birdData.gender,
                        "Mutation1" to mutation1,
                        "Mutation2" to mutation2,
                        "Mutation3" to mutation3,
                        "Mutation4" to mutation4,
                        "Mutation5" to mutation5,
                        "Mutation6" to mutation6,
                        /*"Date of Banding" to birdData.dateOfBanding,*/
                        "Date of Birth" to birdData.dateOfBirth,
                        "Status" to birdData.status,
                        "Sold Date" to birdData.soldDate,
                        "Sale Price" to birdData.soldPrice,
                        "Sale Contact" to birdData.saleContact,
                        "Flight Key" to FlightId,
                        "Bird Key" to birdId,
                        "Sold Id" to soldId,
                        "Month" to month.toFloat(),
                        "Year" to year.toFloat()
                    )
                    val solddata: Map<String, Any?> = hashMapOf(
                        "Bird Id" to birdId
                    )
                    SoldBirdRef.updateChildren(data)
                    newBirdPref.updateChildren(data)
                    newNurseryPref.updateChildren(data)
                    SoldBirdRef.updateChildren(solddata)
                } else if (deceasedLayout.visibility == View.VISIBLE) {
                    val data: Map<String, Any?> = hashMapOf(
                        "Legband" to birdData.legband,
                        "Identifier" to birdData.identifier,
                        "Gender" to birdData.gender,
                        "Mutation1" to mutation1,
                        "Mutation2" to mutation2,
                        "Mutation3" to mutation3,
                        "Mutation4" to mutation4,
                        "Mutation5" to mutation5,
                        "Mutation6" to mutation6,
                        /*    "Date of Banding" to birdData.dateOfBanding,*/
                        "Date of Birth" to birdData.dateOfBirth,
                        "Status" to birdData.status,
                        "Death Date" to birdData.deathDate,
                        "Death Reason" to birdData.deathReason,
                        "Flight Key" to FlightId,
                        "Bird Key" to birdId
                    )
                    newBirdPref.updateChildren(data)
                    newNurseryPref.updateChildren(data)

                } else {
                    return
                }

            } else if (exchangeLayout.visibility == View.VISIBLE) {
                val data: Map<String, Any?> = hashMapOf(
                    "Legband" to birdData.legband,
                    "Identifier" to birdData.identifier,
                    "Gender" to birdData.gender,
                    "Mutation1" to mutation1,
                    "Mutation2" to mutation2,
                    "Mutation3" to mutation3,
                    "Mutation4" to mutation4,
                    "Mutation5" to mutation5,
                    "Mutation6" to mutation6,
                    /* "Date of Banding" to birdData.dateOfBanding,*/
                    "Date of Birth" to birdData.dateOfBirth,
                    "Status" to birdData.status,
                    "Exchange Date" to birdData.exDate,
                    "Exchange Reason" to birdData.exReason,
                    "Exchange Contact" to birdData.exContact,
                    "Flight Key" to FlightId,
                    "Bird Key" to birdId
                )
                newBirdPref.updateChildren(data)
                newNurseryPref.updateChildren(data)

            } else if (lostLayout.visibility == View.VISIBLE) {
                val data: Map<String, Any?> = hashMapOf(
                    "Legband" to birdData.legband,
                    "Identifier" to birdData.identifier,
                    "Gender" to birdData.gender,
                    "Mutation1" to mutation1,
                    "Mutation2" to mutation2,
                    "Mutation3" to mutation3,
                    "Mutation4" to mutation4,
                    "Mutation5" to mutation5,
                    "Mutation6" to mutation6,
                    /*"Date of Banding" to birdData.dateOfBanding,*/
                    "Date of Birth" to birdData.dateOfBirth,
                    "Status" to birdData.status,
                    "Lost Date" to birdData.lostDate,
                    "Lost Details" to birdData.lostDetails,
                    "Flight Key" to FlightId,
                    "Bird Key" to birdId
                )
                newBirdPref.updateChildren(data)
                newNurseryPref.updateChildren(data)

            } else if (donatedLayout.visibility == View.VISIBLE) {
                val data: Map<String, Any?> = hashMapOf(
                    "Legband" to birdData.legband,
                    "Identifier" to birdData.identifier,
                    "Gender" to birdData.gender,
                    "Mutation1" to mutation1,
                    "Mutation2" to mutation2,
                    "Mutation3" to mutation3,
                    "Mutation4" to mutation4,
                    "Mutation5" to mutation5,
                    "Mutation6" to mutation6,
                    /*"Date of Banding" to birdData.dateOfBanding,*/
                    "Date of Birth" to birdData.dateOfBirth,
                    "Status" to birdData.status,
                    "Donated Date" to birdData.donatedDate,
                    "Donated Contact" to birdData.donatedContact,
                    "Flight Key" to FlightId,
                    "Bird Key" to birdId
                )
                newBirdPref.updateChildren(data)
                newNurseryPref.updateChildren(data)

            } else if (otherLayout.visibility == View.VISIBLE) {
                val data: Map<String, Any?> = hashMapOf(
                    "Legband" to birdData.legband,
                    "Identifier" to birdData.identifier,
                    "Gender" to birdData.gender,
                    "Mutation1" to mutation1,
                    "Mutation2" to mutation2,
                    "Mutation3" to mutation3,
                    "Mutation4" to mutation4,
                    "Mutation5" to mutation5,
                    "Mutation6" to mutation6,
                    /*  "Date of Banding" to birdData.dateOfBanding,*/
                    "Date of Birth" to birdData.dateOfBirth,
                    "Status" to birdData.status,
                    "Comments" to birdData.otherComments,
                    "Flight Key" to FlightId,
                    "Bird Key" to birdId
                )
                newBirdPref.updateChildren(data)
                newNurseryPref.updateChildren(data)

            }



            if (birdId != null) {
                if (FlightId != null) {
                    callback(
                        birdId,
                        FlightId,
                        newBundle,
                        soldId.toString(),
                        cageBirdKey,
                        cageKeyValue.toString()
                    )
                    Log.d(ContentValues.TAG, "cagekeyflight $cageKeyValue")
                }
            }
            Log.d(ContentValues.TAG, birdData.toString())
        } else {
            Toast.makeText(requireContext(), "Empty/Invalid Inputs", Toast.LENGTH_SHORT).show()
        }

        args.putString("birdId", birdId)
        args.putString("nurseryId", FlightId)
        args.putString("SoldId", soldId)
    }

    fun OnActiveSpinner() {
        val spinnerItems = resources.getStringArray(R.array.Status)
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spinnerItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerStatus.adapter = adapter

        spinnerStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                var selectedItem = p0?.getItemAtPosition(p2).toString()
                status = selectedItem
                availableLayout.visibility = View.GONE
                forSaleLayout.visibility = View.GONE
                soldLayout.visibility = View.GONE
                deceasedLayout.visibility = View.GONE
                exchangeLayout.visibility = View.GONE
                lostLayout.visibility = View.GONE
                donatedLayout.visibility = View.GONE
                otherLayout.visibility = View.GONE

                when (p0?.getItemAtPosition(p2).toString()) {
                    "Available" -> {

                        cageKeyValue = null
                        availableLayout.visibility = View.VISIBLE

                    }
                    "For Sale" -> {
                        cageKeyValue = null
                        forSaleLayout.visibility = View.VISIBLE
                    }
                    "Sold" -> {
                        cageKeyValue = null
                        soldLayout.visibility = View.VISIBLE
                    }
                    "Deceased" -> {
                        cageKeyValue = null

                        deceasedLayout.visibility = View.VISIBLE
                    }
                    "Exchanged" -> {
                        cageKeyValue = null

                        exchangeLayout.visibility = View.VISIBLE
                    }
                    "Lost" -> {
                        cageKeyValue = null

                        lostLayout.visibility = View.VISIBLE
                    }
                    "Donated" -> {
                        cageKeyValue = null

                        donatedLayout.visibility = View.VISIBLE
                    }
                    "Other" -> {
                        cageKeyValue = null

                        otherLayout.visibility = View.VISIBLE
                    }
                    else -> {
                        cageKeyValue = null

                        editTextContainer.visibility = View.GONE
                    }
                }


            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
        return

    }

    private fun initDatePickers() {
        val dateSetListenerBirth =
            DatePickerDialog.OnDateSetListener { datePicker: DatePicker, year: Int, month: Int, day: Int ->
                birthFormattedDate = makeDateString(day, month + 1, year)
                datebirthButton.text = birthFormattedDate
            }

        /*val dateSetListenerBanding =
            DatePickerDialog.OnDateSetListener { datePicker: DatePicker, year: Int, month: Int, day: Int ->
                bandFormattedDate = makeDateString(day, month + 1, year)
                datebandButton.text = bandFormattedDate
            }*/
        val dateSetListenerSoldDate =
            DatePickerDialog.OnDateSetListener { datePicker: DatePicker, year: Int, month: Int, day: Int ->
                soldFormattedDate = makeDateString(day, month + 1, year)

                btnSoldSaleDate.text = soldFormattedDate
            }
        val dateSetListenerDeathDate =
            DatePickerDialog.OnDateSetListener { datePicker: DatePicker, year: Int, month: Int, day: Int ->
                deathFormattedDate = makeDateString(day, month + 1, year)
                btnDeathDate.text = deathFormattedDate
            }
        val dateSetListenerExchangeDate =
            DatePickerDialog.OnDateSetListener { datePicker: DatePicker, year: Int, month: Int, day: Int ->
                exchangeFormattedDate = makeDateString(day, month + 1, year)
                btnExDate.text = exchangeFormattedDate
            }
        val dateSetListenerLostDate =
            DatePickerDialog.OnDateSetListener { datePicker: DatePicker, year: Int, month: Int, day: Int ->
                lostFormattedDate = makeDateString(day, month + 1, year)
                btnLostDate.text = lostFormattedDate
            }
        val dateSetListenerDonatedDate =
            DatePickerDialog.OnDateSetListener { datePicker: DatePicker, year: Int, month: Int, day: Int ->
                donatedFormattedDate = makeDateString(day, month + 1, year)
                btnDonatedDate.text = donatedFormattedDate
            }

        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        val style = AlertDialog.THEME_HOLO_LIGHT

        datePickerDialogBirth =
            DatePickerDialog(requireContext(), style, dateSetListenerBirth, year, month, day)
        /* datePickerDialogBanding =
             DatePickerDialog(requireContext(), style, dateSetListenerBanding, year, month, day)*/
        datePickerDialogSoldDate =
            DatePickerDialog(requireContext(), style, dateSetListenerSoldDate, year, month, day)
        datePickerDialogDeathDate =
            DatePickerDialog(requireContext(), style, dateSetListenerDeathDate, year, month, day)
        datePickerDialogExDate =
            DatePickerDialog(requireContext(), style, dateSetListenerExchangeDate, year, month, day)
        datepickerDialogLostDate =
            DatePickerDialog(requireContext(), style, dateSetListenerLostDate, year, month, day)
        datepickerDialogDonatedDate =
            DatePickerDialog(requireContext(), style, dateSetListenerDonatedDate, year, month, day)

        // You can set the max date for each dialog if needed.
        // For example:
        // datePickerDialogBirth.datePicker.maxDate = System.currentTimeMillis()
        // datePickerDialogBanding.datePicker.maxDate = System.currentTimeMillis()
    }

    fun showDatePickerDialog(context: Context, button: Button, datePickerDialog: DatePickerDialog) {
        button.setOnClickListener {
            datePickerDialog.show()
        }
    }

    private fun makeDateString(day: Int, month: Int, year: Int): String {
        return getMonthFormat(month) + " " + day + " " + year
    }

    private fun getMonthFormat(month: Int): String {
        return when (month) {
            1 -> "JAN"
            2 -> "FEB"
            3 -> "MAR"
            4 -> "APR"
            5 -> "MAY"
            6 -> "JUN"
            7 -> "JUL"
            8 -> "AUG"
            9 -> "SEP"
            10 -> "OCT"
            11 -> "NOV"
            12 -> "DEC"
            else -> "JAN" // Default should never happen
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EditBasicFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditBasicFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}