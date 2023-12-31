package com.example.qraviaryapp.fragments.AddFragment

import BirdData
import BirdDataListener
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.AddActivities.AddCageScanActivity
import com.example.qraviaryapp.activities.dashboards.MutationsActivity
import com.example.qraviaryapp.activities.dashboards.NurseryCagesListActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.inject.Deferred
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlinx.coroutines.*

import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BasicFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BasicFragment : Fragment() {

    //region
    /*__*/
    private lateinit var dbase: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private var storageRef = Firebase.storage.reference

    /*DatePicker*/
    private lateinit var datePickerDialogBirth: DatePickerDialog

    /*  private lateinit var datePickerDialogBanding: DatePickerDialog*/
    private lateinit var datePickerDialogSoldDate: DatePickerDialog
    private lateinit var datePickerDialogDeathDate: DatePickerDialog
    private lateinit var datePickerDialogExDate: DatePickerDialog
    private lateinit var datepickerDialogLostDate: DatePickerDialog
    private lateinit var datepickerDialogDonatedDate: DatePickerDialog

    /*ImageView*/
    private lateinit var imageView: ImageView

    /*RadioGroup*/
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_basic, container, false)


        /* datebandButton = view.findViewById(R.id.btndateband)*/
        datebirthButton = view.findViewById(R.id.btndatebirth)
        btnSoldSaleDate = view.findViewById(R.id.soldSaleDate)
        btnDonatedDate = view.findViewById(R.id.btnDonatedDate)
        btnLostDate = view.findViewById(R.id.lostDateBtn)
        btnDeathDate = view.findViewById(R.id.deathDate)
        btnExDate = view.findViewById(R.id.exDate)
        slash1 = view.findViewById(R.id.slash1)
        slash2 = view.findViewById(R.id.slash2)
        slash3 = view.findViewById(R.id.slash3)
        slash4 = view.findViewById(R.id.slash4)
        slash5 = view.findViewById(R.id.slash5)
        cageReference = FirebaseDatabase.getInstance().reference
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
        cagescan1 = view.findViewById(R.id.cagescan1)
        cagescan1.setOnClickListener {
            startActivity(Intent(requireContext(), AddCageScanActivity::class.java))
        }
        cagescan = view.findViewById(R.id.cagescan)
        cagescan.setOnClickListener {
            startActivity(Intent(requireContext(), AddCageScanActivity::class.java))
        }
        sharedPreferences = requireContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val isCheckBool = sharedPreferences.getBoolean("Check", false)

//        if (isCheckBool){ //pag nakacheck dapat mawawala identifier
//            val layoutParams = LayoutLegband.layoutParams as LinearLayout.LayoutParams
//            layoutIdentifier.visibility = GONE
//            tvIdentifier.visibility = GONE
//            layoutParams.weight = 2F
//            layoutIdentifier.layoutParams = layoutParams
//        } else {
//
//        }

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
            val intent = Intent(requireContext(), NurseryCagesListActivity::class.java)
            startActivityForResult(intent, requestCode)
        }
        etForSaleCage.setOnClickListener {
            val requestCode = 7 // You can use any integer as the request code
            val intent = Intent(requireContext(), NurseryCagesListActivity::class.java)
            startActivityForResult(intent, requestCode)

        }


        rbUnknown.isChecked = true
        AddMutation()
        RemoveLastMutation()
        OnActiveSpinner()



        return view
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BirdDataListener) {
            birdDataListener = context
        } else {
            throw RuntimeException("$context must implement BirdDataListener")
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

    private fun getTextFromVisibleEditText(editText: EditText, layout: View): String {
        return if (layout.visibility == View.VISIBLE) {
            editText.text.toString()
        } else {
            ""
        }
    }

    private fun getTextFromVisibleMaterialButton(
        materialbtn: MaterialButton,
        layout: View
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
            if (resultCode == RESULT_OK) {

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
            if (resultCode == RESULT_OK) {
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
            if (resultCode == RESULT_OK) {
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
            if (resultCode == RESULT_OK) {
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
            if (resultCode == RESULT_OK) {
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
            if (resultCode == RESULT_OK) {
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
            if (resultCode == RESULT_OK) {
                cageNameValue = data?.getStringExtra("CageName").toString()
                cageKeyValue = data?.getStringExtra("CageKey").toString()
                Log.d(TAG, "cage name : $cageNameValue")
                Log.d(TAG, "cage key : $cageKeyValue")
                if (availableLayout.visibility == View.VISIBLE) {
                    etAvailCage.setText(cageNameValue)
                } else if (forSaleLayout.visibility == View.VISIBLE) {
                    etForSaleCage.setText(cageNameValue)
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
    suspend fun birdDataGetters(callback: (birdId: String, nurseryId: String, newBundle: Bundle, soldId: String, cagebirdKey: String, cagekeyvalue: String) -> Unit) {

        val dataDateOfBirth = birthFormattedDate
        /*==++==*/
        val dataSelectedGen: RadioButton
        /*==++==*/
        val dataIdentifier = etIdentifier.text.toString()
        val dataLegband = etLegband.text.toString()
        val stat = status

        val maturingDays = sharedPreferences.getString("maturingValue", "50")

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
            btnMutation1,
            btnMutation2,
            btnMutation3,
            btnMutation4,
            btnMutation5,
            btnMutation6
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
            /* dateOfBanding = dataDateOfBanding,*/
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
        /*var validDateOfBanding = false*/
        var validDateOfBirth = false
        var validCage = false
        var validforsale = false
        var valididexist = false
        var validlegbandexist = false
        //Validation

        if (birdData.mutation1 == "None" || birdData.mutation2 == "None" || birdData.mutation3 == "None" || birdData.mutation4 == "None" || birdData.mutation5 == "None" || birdData.mutation6 == "None") {
            btnMutation1.error = "Mutation must not be empty"
        } else {
            validMutation = true
        }

        var ageInDays = 0
        if (TextUtils.isEmpty(dataDateOfBirth)) {
            datebirthButton.error = "Date of birth must not be empty..."
        } else {
            val dateFormat = SimpleDateFormat("MMM d yyyy", Locale.US)
            val birthDate = dateFormat.parse(dataDateOfBirth)
            val currentDate = Calendar.getInstance().time

            val ageInMillis = currentDate.time - birthDate.time
            ageInDays = TimeUnit.MILLISECONDS.toDays(ageInMillis).toInt()


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
        if (TextUtils.isEmpty(dataIdentifier)) {
            etIdentifier.error = "Identifier cannot be empty"
        } else {

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

        Log.d(TAG, "Check isssss " + valididexist)

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


        //

        val userId = mAuth.currentUser?.uid.toString()
        var cageBirdKey = ""
        if (!cageKeyValue.isNullOrEmpty()) {
            cageReference = cageKeyValue?.let {
                dbase.child("Users").child("ID: $userId").child("Cages")
                    .child("Nursery Cages").child(it).child("Birds").push()
            }!!
            cageBirdKey = cageReference.key.toString()
        }
        Log.d(TAG, valididexist.toString())

        if (validDateOfBirth && validMutation && valididexist && validlegbandexist) {
            validInputs = true
        }
        val userBird = dbase.child("Users").child("ID: $userId").child("Birds")
        val NurseryBird = dbase.child("Users").child("ID: $userId").child("Nursery Birds")
        val SoldRef = dbase.child("Users").child("ID: $userId").child("Sold Items")
        val SoldBirdRef = SoldRef.push()
        var soldId: String? = null

        val newBirdPref = userBird.push()
        val newNurseryPref = NurseryBird.push()
        val birdId = newBirdPref.key
        val flightKey = newNurseryPref.key
        val args = Bundle()

        val originFragment = OriginFragment()
        originFragment.arguments = args

        val newBundle = Bundle()
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
        newBundle.putString("BirdIdentifier", birdData.identifier)//
        newBundle.putString("BirdLegband", birdData.legband)//
        newBundle.putString("BirdImage", birdData.img)//
        newBundle.putString("BirdKey", newBirdPref.key)//
        newBundle.putString("NurseryKey", newNurseryPref.key)
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
        newBundle.putBoolean("fromNurseryListAdapter", true)//


        if (validInputs) {
            if (availableLayout.visibility == View.VISIBLE) {
//                if (TextUtils.isEmpty(etAvailCage.text)) {
//                    etAvailCage.error = "Cage must not be empty..."
//                }
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
                    "Maturing Days" to maturingDays,
                    /*   "Date of Banding" to birdData.dateOfBanding,*/
                    "Date of Birth" to birdData.dateOfBirth,
                    "Status" to birdData.status,
                    "Cage" to birdData.availCage,
                    "CageKey" to cageKeyValue,
                    "Cage Bird Key" to cageReference.key,
                    "Nursery Key" to flightKey,
                    "Bird Key" to birdId,
                    "Age" to ageInDays,
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
                        "Maturing Days" to maturingDays,
                        /*  "Date of Banding" to birdData.dateOfBanding,*/
                        "Date of Birth" to birdData.dateOfBirth,
                        "Status" to birdData.status,
                        "Cage" to birdData.forSaleCage,
                        "CageKey" to cageKeyValue,
                        "Cage Bird Key" to cageReference.key,
                        "Requested Price" to birdData.reqPrice,
                        "Nursery Key" to flightKey,
                        "Bird Key" to birdId,
                        "Age" to ageInDays

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
                        /* "Date of Banding" to birdData.dateOfBanding,*/
                        "Date of Birth" to birdData.dateOfBirth,
                        "Status" to birdData.status,
                        "Sold Date" to birdData.soldDate,
                        "Sale Price" to birdData.soldPrice,
                        "Sale Contact" to birdData.saleContact,
                        "Nursery Key" to flightKey,
                        "Sold Id" to soldId,
                        "Bird Key" to birdId,
                        "Age" to ageInDays,
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
                } else {
                    return
                }

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
                    /* "Date of Banding" to birdData.dateOfBanding,*/
                    "Date of Birth" to birdData.dateOfBirth,
                    "Status" to birdData.status,
                    "Death Date" to birdData.deathDate,
                    "Death Reason" to birdData.deathReason,
                    "Nursery Key" to flightKey,
                    "Bird Key" to birdId,
                    "Age" to ageInDays


                )
                newBirdPref.updateChildren(data)
                newNurseryPref.updateChildren(data)
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
                    /*  "Date of Banding" to birdData.dateOfBanding,*/
                    "Date of Birth" to birdData.dateOfBirth,
                    "Status" to birdData.status,
                    "Exchange Date" to birdData.exDate,
                    "Exchange Reason" to birdData.exReason,
                    "Exchange Contact" to birdData.exContact,
                    "Nursery Key" to flightKey,
                    "Bird Key" to birdId,
                    "Age" to ageInDays


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
                    /*   "Date of Banding" to birdData.dateOfBanding,*/
                    "Date of Birth" to birdData.dateOfBirth,
                    "Status" to birdData.status,
                    "Lost Date" to birdData.lostDate,
                    "Lost Details" to birdData.lostDetails,
                    "Nursery Key" to flightKey,
                    "Bird Key" to birdId,
                    "Age" to ageInDays
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
                    /* "Date of Banding" to birdData.dateOfBanding,*/
                    "Date of Birth" to birdData.dateOfBirth,
                    "Status" to birdData.status,
                    "Donated Date" to birdData.donatedDate,
                    "Donated Contact" to birdData.donatedContact,
                    "Nursery Key" to flightKey,
                    "Bird Key" to birdId,
                    "Age" to ageInDays
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
                    /* "Date of Banding" to birdData.dateOfBanding,*/
                    "Date of Birth" to birdData.dateOfBirth,
                    "Status" to birdData.status,
                    "Comments" to birdData.otherComments,
                    "Nursery Key" to flightKey,
                    "Bird Key" to birdId,
                    "Age" to ageInDays
                )
                newBirdPref.updateChildren(data)
                newNurseryPref.updateChildren(data)
            }
            if (birdId != null) {
                if (flightKey != null) {
                    callback(
                        birdId,
                        flightKey,
                        newBundle,
                        soldId.toString(),
                        cageBirdKey,
                        cageKeyValue.toString()
                    )
                }
            }
            args.putString("birdId", birdId)
            args.putString("SoldId", soldId)
            args.putString("nurseryId", flightKey)
            Log.d(TAG, birdData.toString())
        } else {
            Toast.makeText(requireContext(), "Empty Inputs/Invalid Inputs", Toast.LENGTH_SHORT)
                .show()
        }

        Log.d(ContentValues.TAG, "sold id $soldId")
    }


    fun OnActiveSpinner() {
        val spinnerItems = resources.getStringArray(R.array.Status)
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spinnerItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerStatus.adapter = adapter

        spinnerStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                val selectedItem = p0?.getItemAtPosition(p2).toString()
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
                        etAvailCage.text = ""

                    }
                    "For Sale" -> {
                        cageKeyValue = null
                        forSaleLayout.visibility = View.VISIBLE
                        etForSaleCage.text = ""
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

    override fun onResume() {
        super.onResume()

        if (arguments?.getString("BirdIdentifier")?.isNotEmpty() == true) {
            etIdentifier.setText(arguments?.getString("BirdIdentifier"))
        }
        if (arguments?.getString("BirdLegband")?.isNotEmpty() == true) {
            etLegband.setText(arguments?.getString("BirdLegband"))
        }

        val sex = arguments?.getString("BirdGender").toString()

        if (sex == "Female") {
            rbFemale.isChecked = true
        } else if (sex == "Male") {
            rbMale.isChecked = true
        } else if (sex == "Unknown") {
            rbUnknown.isChecked = true
        }

        val mutation1 = arguments?.getString("BirdMutation1")
        val mutation2 = arguments?.getString("BirdMutation2")
        val mutation3 = arguments?.getString("BirdMutation3")
        val mutation4 = arguments?.getString("BirdMutation4")
        val mutation5 = arguments?.getString("BirdMutation5")
        val mutation6 = arguments?.getString("BirdMutation6")
        //Checking Mutation
        if (mutation1?.isNotEmpty() == true) {
            btnMutation1.text = mutation1
            btnMutation1.visibility = View.VISIBLE
            spinnerCount = 0
        }
        if (mutation2?.isNotEmpty() == true) {
            btnMutation2.text = mutation2
            btnMutation2.visibility = View.VISIBLE
            spinnerCount = 1
        }
        if (mutation3?.isNotEmpty() == true) {
            btnMutation3.text = mutation3
            btnMutation3.visibility = View.VISIBLE
            spinnerCount = 2
        }
        if (mutation4?.isNotEmpty() == true) {
            btnMutation4.text = mutation4
            btnMutation4.visibility = View.VISIBLE
            spinnerCount = 3
        }
        if (mutation5?.isNotEmpty() == true) {
            btnMutation5.text = mutation5
            btnMutation5.visibility = View.VISIBLE
            spinnerCount = 4
        }
        if (mutation6?.isNotEmpty() == true) {
            btnMutation6.text = mutation6
            btnMutation6.visibility = View.VISIBLE
            spinnerCount = 5
        }

        val birdDateofBirth = arguments?.getString("BirdBirthDate")
        if (birdDateofBirth?.isNotEmpty() == true && datebirthButton.text == "Pick Date") {
            datebirthButton.text = birdDateofBirth
        }

        when (arguments?.getString("BirdStatus")) {
            "Available" -> {
                spinnerStatus.setSelection(0)
                //cage
            }
            "For Sale" -> {
                spinnerStatus.setSelection(1)
                //cage
                if (etForSaleReqPrice.text.isEmpty()) {
                    etForSaleReqPrice.setText(arguments?.getString("BirdForSalePrice"))
                }
            }
            "Sold" -> {
                spinnerStatus.setSelection(2)
                if (arguments?.getString("BirdSoldDate")?.isNotEmpty() == true) {
                    btnSoldSaleDate.text = arguments?.getString("BirdSoldDate")
                }
                if (arguments?.getString("BirdSoldPrice")?.isNotEmpty() == true) {
                    etSoldSalePrice.setText(arguments?.getString("BirdSoldPrice"))
                }
                if (arguments?.getString("BirdSoldContact")?.isNotEmpty() == true) {
                    etSoldBuyer.setText(arguments?.getString("BirdSoldContact"))
                }

            }
            "Deceased" -> {
                spinnerStatus.setSelection(3)
                if (arguments?.getString("BirdDeceasedDate")?.isNotEmpty() == true) {
                    btnDeathDate.text = arguments?.getString("BirdDeceasedDate")
                }

                if (arguments?.getString("BirdDeceasedReason")?.isNotEmpty() == true) {
                    etDeathReason.setText(arguments?.getString("BirdDeceasedReason"))
                }
            }
            "Exchange" -> {
                spinnerStatus.setSelection(4)
                if (arguments?.getString("BirdExchangeDate")?.isNotEmpty() == true) {
                    btnExDate.text = arguments?.getString("BirdExchangeDate")
                }

                if (arguments?.getString("BirdExchangeReason")?.isNotEmpty() == true) {
                    etExReason.setText(arguments?.getString("ExchangeReason"))
                }
                if (arguments?.getString("BirdExchangeContact")?.isNotEmpty() == true) {
                    etExWith.setText(arguments?.getString("BirdExchangeContact"))
                }
            }
            "Lost" -> {
                spinnerStatus.setSelection(5)
                if (arguments?.getString("BirdLostDate")?.isNotEmpty() == true) {
                    btnLostDate.text = arguments?.getString("BirdLostDate")
                }

                if (arguments?.getString("BirdLostDetails")?.isNotEmpty() == true) {
                    etLostDetails.setText(arguments?.getString("BirdLostDetails"))
                }
            }
            "Donated" -> {
                spinnerStatus.setSelection(6)
                if (arguments?.getString("BirdDonatedDate")?.isNotEmpty() == true) {
                    btnDonatedDate.text = arguments?.getString("BirdDonatedDate")
                }

                if (arguments?.getString("BirdDonatedContact")?.isNotEmpty() == true) {
                    etDonateChooseContract.setText(arguments?.getString("BirdDonatedContact"))
                }
            }
            "Other" -> {
                spinnerStatus.setSelection(7)
                if (arguments?.getString("BirdOtherComment")?.isNotEmpty() == true) {
                    etOtherComm.setText(arguments?.getString("BirdOtherComment"))
                }
            }
        }

        val mutationmap1 = arguments?.getString("BirdMutationMap1")
        if (!mutationmap1.isNullOrEmpty()) {
            val map1 = JSONObject(mutationmap1)

            mutation1IncubatingDays = map1.optString("Mutation Name", "")
            mutation1MaturingDays = map1.optString("Incubating Days", "")
        }
        val mutationmap2 = arguments?.getString("BirdMutationMap2")
        if (!mutationmap2.isNullOrEmpty()) {
            val map2 = JSONObject(mutationmap2)

            mutation2IncubatingDays = map2.optString("Mutation Name", "")
            mutation2MaturingDays = map2.optString("Incubating Days", "")
        }
        val mutationmap3 = arguments?.getString("BirdMutationMap3")
        if (!mutationmap3.isNullOrEmpty()) {
            val map3 = JSONObject(mutationmap3)

            mutation3IncubatingDays = map3.optString("Mutation Name", "")
            mutation3MaturingDays = map3.optString("Incubating Days", "")
        }
        val mutationmap4 = arguments?.getString("BirdMutationMap4")
        if (!mutationmap4.isNullOrEmpty()) {
            val map4 = JSONObject(mutationmap4)

            mutation4IncubatingDays = map4.optString("Mutation Name", "")
            mutation4MaturingDays = map4.optString("Incubating Days", "")
        }
        val mutationmap5 = arguments?.getString("BirdMutationMap5")
        if (!mutationmap5.isNullOrEmpty()) {
            val map5 = JSONObject(mutationmap5)

            mutation5IncubatingDays = map5.optString("Mutation Name", "")
            mutation5MaturingDays = map5.optString("Incubating Days", "")
        }
        val mutationmap6 = arguments?.getString("BirdMutationMap6")
        if (!mutationmap6.isNullOrEmpty()) {
            val map6 = JSONObject(mutationmap6)

            mutation6IncubatingDays = map6.optString("Mutation Name", "")
            mutation6MaturingDays = map6.optString("Incubating Days", "")
        }



        Log.d(TAG, mutation1IncubatingDays.toString())


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BasicFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BasicFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}