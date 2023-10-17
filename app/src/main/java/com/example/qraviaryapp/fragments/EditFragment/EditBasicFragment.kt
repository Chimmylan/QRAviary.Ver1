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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.dashboards.FlightCagesListActivity
import com.example.qraviaryapp.activities.dashboards.MutationsActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

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
    private var flightKey: String? = null
    private var nurseryKey: String? = null
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
    private lateinit var cageBirdkey: String
    private lateinit var cageKey: String
    private lateinit var breedingCageKey: String
    private lateinit var type: String

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


        LayoutLegband = view.findViewById(R.id.layoutlegband)
        layoutIdentifier = view.findViewById(R.id.layoutIdentifier)
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

        cageKey = arguments?.getString("CageKey").toString()
        cageBirdkey = arguments?.getString("CageBirdKey").toString()
        breedingCageKey = arguments?.getString("BreedingCageKey").toString()
        type = arguments?.getString("Type").toString()
        birdKey = arguments?.getString("BirdKey")
        flightKey = arguments?.getString("Flight Key")
        flightKey = arguments?.getString("Nursery Key")
        birdGender = arguments?.getString("BirdGender")
        birdLegband = arguments?.getString("BirdLegband")
        birdStatus = arguments?.getString("BirdStatus")
        birdId = arguments?.getString("BirdId")
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


        sharedPreferences = requireContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)

        Log.d(ContentValues.TAG, cageKey)

        etIdentifier.setText(birdId)

        if (birdLegband != "null" || !birdLegband.isNullOrEmpty()) {
            etLegband.setText(birdLegband)
        }

        if (birdGender == "Female") {
            rbFemale.isChecked
        } else if (birdGender == "Male") {
            rbMale.isChecked
        } else {
            rbUnknown.isChecked
        }

        datebirthButton.setText(birdDateBirth)

        if (birdAvailCage != "null" || !birdAvailCage.isNullOrEmpty()) {
            etAvailCage.setText(birdAvailCage)
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


        AddMutation()
        RemoveLastMutation()
        OnActiveSpinner()

        return view

    }


    fun editBird(callback: (birdId: String, flightKey: String, nurseryKey: String, cageFlightBirdKey: String, cageNurseryBirdKey: String, breedingCageKey: String) -> Unit) {
        mAuth = FirebaseAuth.getInstance()
        val currentUserId = mAuth.currentUser?.uid
        dbase = FirebaseDatabase.getInstance().reference.child("Users").child("ID: $currentUserId")

        val birdRef = dbase.child("Birds")
        val flightRef = dbase.child("Flight Birds")
        val nurseryRef = dbase.child("Nursery Birds")
        val cageFlightRef = dbase.child("Cages").child("Flight Cages").child(cageKey).child("Birds")
            .child(cageBirdkey)
        val cageNurseryRef =
            dbase.child("Cages").child("Nursery Cages").child(cageKey).child("Birds")
                .child(cageBirdkey)
        val cageBreedingRef =
            dbase.child("Cages").child("Breeding Cages").child(cageKey).child("Pair Birds")
                .child(breedingCageKey)


        val id = etIdentifier.text.toString()
        val legband = etLegband.text.toString()
        val dateOfBirth = birthFormattedDate
        val stat = status

        val selectedMutations = mutableListOf<String?>()
        val spinners = arrayOf(
            btnMutation1, btnMutation2, btnMutation3, btnMutation4, btnMutation5, btnMutation6
        )

        for (i in spinners.indices) {
            if (spinners[i].visibility == View.VISIBLE) {
                selectedMutations.add(spinners[i].text.toString())
            } else {
                selectedMutations.add(null)
            }
        }
        val dataSelectedGen: RadioButton

        val selectedOption: Int = rgGender.checkedRadioButtonId

        dataSelectedGen = view?.findViewById<RadioButton>(selectedOption)!!

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

        birdData = BirdData(
            legband = legband,
            identifier = id,
            /*  dateOfBanding = dataDateOfBanding,*/
            dateOfBirth = dateOfBirth,
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


        if (TextUtils.isEmpty(dateOfBirth)) {
            datebirthButton.error = "Date of birth must not be empty..."
            Toast.makeText(
                requireContext(),
                "Date of birth must not be empty...",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val dateFormat = SimpleDateFormat("MMM d yyyy", Locale.US)
            val birthDate = dateFormat.parse(dateOfBirth)
            val currentDate = Calendar.getInstance().time

            val ageInMillis = currentDate.time - birthDate.time
            val ageInDays = TimeUnit.MILLISECONDS.toDays(ageInMillis)

            if (ageInDays < 50) {
                // Age is less than 50 days, show an error message
                datebirthButton.error = "Age must be more than 50 days"
                Toast.makeText(
                    requireContext(),
                    "Age must be more than 50 days",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                validDateOfBirth = true
            }
        }

        if (validDateOfBirth && validMutation && validIdentifier) {
            validInputs = true
        }


        val mutation1 = hashMapOf(
            "Mutation Name" to selectedMutations[0],
            "Maturing Days" to mutation1MaturingDays,
            "Incubating Days" to mutation1IncubatingDays
        )
        val mutation2 = hashMapOf(
            "Mutation Name" to selectedMutations[1],
            "Maturing Days" to mutation2MaturingDays,
            "Incubating Days" to mutation2IncubatingDays
        )
        val mutation3 = hashMapOf(
            "Mutation Name" to selectedMutations[3],
            "Maturing Days" to mutation3MaturingDays,
            "Incubating Days" to mutation3IncubatingDays
        )
        val mutation4 = hashMapOf(
            "Mutation Name" to selectedMutations[4],
            "Maturing Days" to mutation4MaturingDays,
            "Incubating Days" to mutation4IncubatingDays
        )
        val mutation5 = hashMapOf(
            "Mutation Name" to selectedMutations[5],
            "Maturing Days" to mutation5MaturingDays,
            "Incubating Days" to mutation5IncubatingDays
        )
        val mutation6 = hashMapOf(
            "Mutation Name" to selectedMutations[6],
            "Maturing Days" to mutation6MaturingDays,
            "Incubating Days" to mutation6IncubatingDays
        )

        if (validInputs) {
            if (availableLayout.visibility == View.VISIBLE) {

                val data: Map<String, Any?> = hashMapOf(
                    "Type" to "Flight",

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
                    "Bird Key" to birdId,
                    "CageKey" to cageKeyValue,
                )

                birdRef.updateChildren(data)
                if (type == "Nursery") {
                    nurseryRef.updateChildren(data)
                    cageNurseryRef.updateChildren(data)
                } else {
                    flightRef.updateChildren(data)
                    cageFlightRef.updateChildren(data)
                }

                if (breedingCageKey.isNotEmpty()) {
                    cageBreedingRef.updateChildren(data)
                }


            } else if (forSaleLayout.visibility == View.VISIBLE) {
                val data: Map<String, Any?> = hashMapOf(
                    "Legband" to birdData.legband,
                    "Type" to "Flight",
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
                    "Bird Key" to birdId,
                    "CageKey" to cageKeyValue,
                )

                birdRef.updateChildren(data)
                if (type == "Nursery") {
                    nurseryRef.updateChildren(data)
                    cageNurseryRef.updateChildren(data)
                } else {
                    flightRef.updateChildren(data)
                    cageFlightRef.updateChildren(data)
                }

                if (breedingCageKey.isNotEmpty()) {
                    cageBreedingRef.updateChildren(data)
                }


            } else if (soldLayout.visibility == View.VISIBLE) {
                val date = inputDateFormat.parse(dataSoldSaleDate)
                val formattedDate = outputDateFormat.format(date)

                val monthYearParts = formattedDate.split(" - ")
                val day = monthYearParts[0]
                val month = monthYearParts[1]
                val year = monthYearParts[2]
                val data: Map<String, Any?> = hashMapOf(
                    "Legband" to birdData.legband,
                    "Type" to "Flight",
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
                    "Bird Key" to birdId,
                    "Month" to month.toFloat(),
                    "Year" to year.toFloat()
                )
                val solddata: Map<String, Any?> = hashMapOf(
                    "Bird Id" to birdId
                )

                birdRef.updateChildren(data)
                if (type == "Nursery") {
                    nurseryRef.updateChildren(data)
                    cageNurseryRef.updateChildren(data)
                } else {
                    flightRef.updateChildren(data)
                    cageFlightRef.updateChildren(data)
                }

                if (breedingCageKey.isNotEmpty()) {
                    cageBreedingRef.updateChildren(data)
                }

            } else if (deceasedLayout.visibility == View.VISIBLE) {
                val data: Map<String, Any?> = hashMapOf(
                    "Legband" to birdData.legband,
                    "Type" to "Flight",
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
                    "Bird Key" to birdId
                )

                birdRef.updateChildren(data)
                if (type == "Nursery") {
                    nurseryRef.updateChildren(data)
                    cageNurseryRef.updateChildren(data)
                } else {
                    flightRef.updateChildren(data)
                    cageFlightRef.updateChildren(data)
                }

                if (breedingCageKey.isNotEmpty()) {
                    cageBreedingRef.updateChildren(data)
                }


            } else if (exchangeLayout.visibility == View.VISIBLE) {
                val data: Map<String, Any?> = hashMapOf(
                    "Legband" to birdData.legband,
                    "Type" to "Flight",
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
                    "Bird Key" to birdId
                )

                birdRef.updateChildren(data)
                if (type == "Nursery") {
                    nurseryRef.updateChildren(data)
                    cageNurseryRef.updateChildren(data)
                } else {
                    flightRef.updateChildren(data)
                    cageFlightRef.updateChildren(data)
                }

                if (breedingCageKey.isNotEmpty()) {
                    cageBreedingRef.updateChildren(data)
                }

            } else if (lostLayout.visibility == View.VISIBLE) {
                val data: Map<String, Any?> = hashMapOf(
                    "Legband" to birdData.legband,
                    "Identifier" to birdData.identifier,
                    "Gender" to birdData.gender,
                    "Type" to "Flight",
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

                    "Bird Key" to birdId
                )
                birdRef.updateChildren(data)
                if (type == "Nursery") {
                    nurseryRef.updateChildren(data)
                    cageNurseryRef.updateChildren(data)
                } else {
                    flightRef.updateChildren(data)
                    cageFlightRef.updateChildren(data)
                }

                if (breedingCageKey.isNotEmpty()) {
                    cageBreedingRef.updateChildren(data)
                }


            } else if (donatedLayout.visibility == View.VISIBLE) {
                val data: Map<String, Any?> = hashMapOf(
                    "Legband" to birdData.legband,
                    "Identifier" to birdData.identifier,
                    "Gender" to birdData.gender,
                    "Mutation1" to mutation1,
                    "Type" to "Flight",
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
                    "Bird Key" to birdId
                )

                birdRef.updateChildren(data)
                if (type == "Nursery") {
                    nurseryRef.updateChildren(data)
                    cageNurseryRef.updateChildren(data)
                } else {
                    flightRef.updateChildren(data)
                    cageFlightRef.updateChildren(data)
                }

                if (breedingCageKey.isNotEmpty()) {
                    cageBreedingRef.updateChildren(data)
                }


            } else if (otherLayout.visibility == View.VISIBLE) {
                val data: Map<String, Any?> = hashMapOf(
                    "Legband" to birdData.legband,
                    "Identifier" to birdData.identifier,
                    "Gender" to birdData.gender,
                    "Type" to "Flight",
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
                    "Bird Key" to birdId
                )
                birdRef.updateChildren(data)
                if (type == "Nursery") {
                    nurseryRef.updateChildren(data)
                    cageNurseryRef.updateChildren(data)
                } else {
                    flightRef.updateChildren(data)
                    cageFlightRef.updateChildren(data)
                }

                if (breedingCageKey.isNotEmpty()) {
                    cageBreedingRef.updateChildren(data)
                }


            }

            Log.d(ContentValues.TAG, birdData.toString())
        } else {
            Toast.makeText(requireContext(), "No Id found", Toast.LENGTH_SHORT).show()
        }



        callback(
            birdKey.toString(),
            flightKey.toString(),
            nurseryKey.toString(),
            cageBirdkey,
            cageBirdkey,
            breedingCageKey
        )

    }


    private var spinnerCount = 0


    fun AddMutation() {

        btnMutation2.visibility = View.GONE
        btnMutation3.visibility = View.GONE
        btnMutation4.visibility = View.GONE
        btnMutation5.visibility = View.GONE
        btnMutation6.visibility = View.GONE

        addBtn.setOnClickListener {

            when (spinnerCount) {

                0 -> {
                    btnMutation2.visibility = View.VISIBLE
                    spinnerCount++
                }
                1 -> {
                    btnMutation3.visibility = View.VISIBLE
                    spinnerCount++
                }
                2 -> {
                    btnMutation4.visibility = View.VISIBLE
                    spinnerCount++
                }
                3 -> {
                    btnMutation5.visibility = View.VISIBLE
                    spinnerCount++
                }
                4 -> {
                    btnMutation6.visibility = View.VISIBLE
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
                    Toast.makeText(requireContext(), spinnerCount.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
                2 -> {
                    spinnerCount--
                    btnMutation3.visibility = View.GONE
                    Toast.makeText(requireContext(), spinnerCount.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
                3 -> {

                    spinnerCount--
                    btnMutation4.visibility = View.GONE
                    Toast.makeText(requireContext(), spinnerCount.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
                4 -> {

                    spinnerCount--
                    btnMutation5.visibility = View.GONE
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

                        availableLayout.visibility = View.VISIBLE

                    }
                    "For Sale" -> {
                        forSaleLayout.visibility = View.VISIBLE

                    }
                    "Sold" -> {
                        soldLayout.visibility = View.VISIBLE
                    }
                    "Deceased" -> {
                        deceasedLayout.visibility = View.VISIBLE
                    }
                    "Exchanged" -> {
                        exchangeLayout.visibility = View.VISIBLE
                    }
                    "Lost" -> {
                        lostLayout.visibility = View.VISIBLE
                    }
                    "Donated" -> {
                        donatedLayout.visibility = View.VISIBLE
                    }
                    "Other" -> {
                        otherLayout.visibility = View.VISIBLE
                    }
                    else -> {
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