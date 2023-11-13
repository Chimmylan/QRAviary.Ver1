package com.example.qraviaryapp.fragments.generateFragment

import BirdData
import BirdDataListener
import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
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
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.AddActivities.AddCageScanActivity
import com.example.qraviaryapp.activities.dashboards.FemaleBirdListActivity
import com.example.qraviaryapp.activities.dashboards.MaleBirdListActivity
import com.example.qraviaryapp.activities.dashboards.MutationsActivity
import com.example.qraviaryapp.activities.dashboards.NurseryCagesListActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Calendar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GenerateBirdFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GenerateBirdFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
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

    private lateinit var btnGenerate: MaterialButton
    private lateinit var btndownload: MaterialButton
    private lateinit var dataSelectedGen: RadioButton
    private lateinit var qrimageLayout: LinearLayout
    private lateinit var qrImage: ImageView

    //

    private var boughtFormattedDate: String? = null
    private lateinit var datePickerDialogBought: DatePickerDialog

    private lateinit var boughtLayout: LinearLayout
    private lateinit var otLayout: LinearLayout
    private lateinit var radioGroup: RadioGroup
    private lateinit var radioButtonMe: RadioButton
    private lateinit var radioButtonBought: RadioButton
    private lateinit var radioButtonOther: RadioButton

    private lateinit var boughtDateBtn: MaterialButton
    private lateinit var etBreederContact: EditText
    private lateinit var etBuyPrice: EditText
    private lateinit var etOtBreederContact: EditText

    private lateinit var btnFather: MaterialButton
    private lateinit var btnMother: MaterialButton
    private lateinit var mAuth: FirebaseAuth
    private lateinit var dbase: DatabaseReference

    private lateinit var tvBirdId: TextView
    private lateinit var tvBirdLegband: TextView
    private lateinit var tvBirdSex: TextView
    private lateinit var tvBirdMutation: TextView
    private lateinit var tvBirdDateOfBirth: TextView
    private lateinit var tvBirdStatus: TextView
    private lateinit var tvBirdCage: TextView
    private lateinit var tvBirdFather: TextView
    private lateinit var tvBirdMother: TextView
    private lateinit var tvBirdBuyPrice: TextView
    private lateinit var tvBirdBreederContact: TextView
    private lateinit var tvBirdSoldDate: TextView
    private lateinit var tvBirdDeceasedDate: TextView
    private lateinit var tvBirdExchangeDate: TextView
    private lateinit var tvBirdLostDate: TextView
    private lateinit var tvBirdDonatedDate: TextView
    private lateinit var tvBirdSalePrice: TextView
    private lateinit var tvBirdBuyer: TextView
    private lateinit var tvBirdDeathReason: TextView
    private lateinit var tvBirdExchangeReason: TextView
    private lateinit var tvBirdExchangeWith: TextView
    private lateinit var tvBirdDonatedContact: TextView
    private lateinit var tvBirdLostDetails: TextView
    private lateinit var tvBirdRequestedPrice: TextView
    private lateinit var tvBirdComment: TextView
    private lateinit var tvBirdBreederOtherBreed: TextView

    private lateinit var cagescan: CardView
    private lateinit var cagescan1: CardView

    private lateinit var qrLayout: LinearLayout

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
        val view = inflater.inflate(R.layout.fragment_generate_bird, container, false)
        btndownload = view.findViewById(R.id.btndownload)
        qrimageLayout = view.findViewById(R.id.qrimagelayout)
        datebirthButton = view.findViewById(R.id.btndatebirth)
        btnSoldSaleDate = view.findViewById(R.id.soldSaleDate)
        btnDonatedDate = view.findViewById(R.id.btnDonatedDate)
        btnLostDate = view.findViewById(R.id.lostDateBtn)
        btnDeathDate = view.findViewById(R.id.deathDate)
        btnExDate = view.findViewById(R.id.exDate)
        boughtDateBtn = view.findViewById(R.id.boughtDate)

        qrLayout = view.findViewById(R.id.qrlayout)

        initDatePickers()
        showDatePickerDialog(requireContext(), datebirthButton, datePickerDialogBirth)
        /*showDatePickerDialog(requireContext(), datebandButton, datePickerDialogBanding)*/
        showDatePickerDialog(requireContext(), btnDeathDate, datePickerDialogDeathDate)
        showDatePickerDialog(requireContext(), btnSoldSaleDate, datePickerDialogSoldDate)
        showDatePickerDialog(requireContext(), btnExDate, datePickerDialogExDate)
        showDatePickerDialog(requireContext(), btnLostDate, datepickerDialogLostDate)
        showDatePickerDialog(requireContext(), btnDonatedDate, datepickerDialogDonatedDate)
        showDatePickerDialog(requireContext(), boughtDateBtn, datePickerDialogBought)


//        LayoutLegband = view.findViewById(R.id.layoutlegband)
//        layoutIdentifier = view.findViewById(R.id.layoutIdentifier)
        slash1 = view.findViewById(R.id.slash1)
        slash2 = view.findViewById(R.id.slash2)
        slash3 = view.findViewById(R.id.slash3)
        slash4 = view.findViewById(R.id.slash4)
        slash5 = view.findViewById(R.id.slash5)
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

        btnGenerate = view.findViewById(R.id.generate)

        //

        boughtLayout = view.findViewById(R.id.boughtLayout)
        otLayout = view.findViewById(R.id.otLayout)
        radioGroup = view.findViewById(R.id.radioGroupprovenance)
        radioButtonMe = view.findViewById(R.id.radioButtonMe)
        radioButtonBought = view.findViewById(R.id.radioButtonBought)
        radioButtonOther = view.findViewById(R.id.radioButtonOther)

        boughtDateBtn = view.findViewById(R.id.boughtDate)
        etBreederContact = view.findViewById(R.id.etBreederContact)
        etBuyPrice = view.findViewById(R.id.etBuyPrice)
        etOtBreederContact = view.findViewById(R.id.otherContact)
        btnFather = view.findViewById(R.id.btnFather)
        btnMother = view.findViewById(R.id.btnMother)

        qrImage = view.findViewById(R.id.QRimage)

        tvBirdId = view.findViewById(R.id.BirdId)
        tvBirdLegband = view.findViewById(R.id.BirdLegBand)
        tvBirdSex = view.findViewById(R.id.BirdSex)
        tvBirdMutation = view.findViewById(R.id.BirdMutation)
        tvBirdDateOfBirth = view.findViewById(R.id.BirdDateOfBirth)
        tvBirdStatus = view.findViewById(R.id.BirdStatus)
        tvBirdCage = view.findViewById(R.id.BirdCage)
        tvBirdFather = view.findViewById(R.id.BirdFather)
        tvBirdMother = view.findViewById(R.id.BirdMother)
        tvBirdBuyPrice = view.findViewById(R.id.BirdBuyPrice)
        tvBirdBreederContact = view.findViewById(R.id.BirdBreederContact)
        tvBirdSoldDate = view.findViewById(R.id.BirdSoldDate)
        tvBirdDeceasedDate = view.findViewById(R.id.BirdDeceasedDate)
        tvBirdExchangeDate = view.findViewById(R.id.BirdExchangedDate)
        tvBirdLostDate = view.findViewById(R.id.BirdLostDate)
        tvBirdDonatedDate = view.findViewById(R.id.BirdDonatedDate)
        tvBirdSalePrice = view.findViewById(R.id.BirdSalePrice)
        tvBirdBuyer = view.findViewById(R.id.BirdBuyer)
        tvBirdDeathReason = view.findViewById(R.id.BirdDeathReason)
        tvBirdExchangeReason = view.findViewById(R.id.BirdExchangeReason)
        tvBirdExchangeWith = view.findViewById(R.id.BirdExchangeWith)
        tvBirdDonatedContact = view.findViewById(R.id.BirdDonatedContact)
        tvBirdLostDetails = view.findViewById(R.id.BirdLostDetails)
        tvBirdRequestedPrice = view.findViewById(R.id.BirdRequestedPrice)
        tvBirdComment = view.findViewById(R.id.BirdOtherComment)
        tvBirdBreederOtherBreed = view.findViewById(R.id.BirdOtherBreeder)

        rbUnknown.isChecked
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
            val intent = Intent(requireContext(), NurseryCagesListActivity::class.java)
            startActivityForResult(intent, requestCode)
        }
        etForSaleCage.setOnClickListener {
            val requestCode = 7 // You can use any integer as the request code
            val intent = Intent(requireContext(), NurseryCagesListActivity::class.java)
            startActivityForResult(intent, requestCode)

        }

        boughtLayout.visibility = View.GONE
        otLayout.visibility = View.GONE

        btnFather.setOnClickListener {
            val requestCode = 8
            val i = Intent(requireContext(), MaleBirdListActivity::class.java)
            startActivityForResult(i, requestCode)
        }
        btnMother.setOnClickListener {
            val requestCode = 9
            val i = Intent(requireContext(), FemaleBirdListActivity::class.java)
            startActivityForResult(i, requestCode)
        }

        rbUnknown.isChecked = true
        radioButtonMe.isChecked = true

        AddMutation()
        RemoveLastMutation()
        OnActiveSpinner()
        radioSelection()








        btnGenerate.setOnClickListener {
            qrLayout.visibility = View.VISIBLE
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
            val dataDonatedContact =
                getTextFromVisibleEditText(etDonateChooseContract, donatedLayout)
            val dataOtherComments = getTextFromVisibleEditText(etOtherComm, otherLayout)

            val selectedMutations = mutableListOf<String?>()
            val spinners = arrayOf(
                btnMutation1,
                btnMutation2,
                btnMutation3,
                btnMutation4,
                btnMutation5,
                btnMutation6
            )

            for (i in spinners.indices) {
                if (spinners[i].visibility == View.VISIBLE) {
                    selectedMutations.add(spinners[i].text.toString())
                } else {
                    selectedMutations.add(null)
                }
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
                "Mutation Name" to selectedMutations[2],
                "Maturing Days" to mutation3MaturingDays,
                "Incubating Days" to mutation3IncubatingDays
            )
            val mutation4 = hashMapOf(
                "Mutation Name" to selectedMutations[3],
                "Maturing Days" to mutation4MaturingDays,
                "Incubating Days" to mutation4IncubatingDays
            )
            val mutation5 = hashMapOf(
                "Mutation Name" to selectedMutations[4],
                "Maturing Days" to mutation5MaturingDays,
                "Incubating Days" to mutation5IncubatingDays
            )
            val mutation6 = hashMapOf(
                "Mutation Name" to selectedMutations[5],
                "Maturing Days" to mutation6MaturingDays,
                "Incubating Days" to mutation6IncubatingDays
            )
            val dataSelectedProvenence: RadioButton
            val dataProvenence: Int = radioGroup.checkedRadioButtonId


            dataSelectedProvenence = view.findViewById(dataProvenence)
                ?: throw IllegalStateException("RadioButton not found")


            val birdData = JSONObject()


            birdData.put("AddBirdQR", true)
            birdData.put("Identifier", etIdentifier.text.toString())
            birdData.put("LegBand", etLegband.text.toString())
            birdData.put("Gender", dataSelectedGen.text.toString())
            birdData.put("Mutation1", selectedMutations[0])
            birdData.put("Mutation2", selectedMutations[1])
            birdData.put("Mutation3", selectedMutations[2])
            birdData.put("Mutation4", selectedMutations[3])
            birdData.put("Mutation5", selectedMutations[4])
            birdData.put("Mutation6", selectedMutations[5])
            birdData.put("Mutation1Map", mutation1)
            birdData.put("Mutation2Map", mutation2)
            birdData.put("Mutation3Map", mutation3)
            birdData.put("Mutation4Map", mutation4)
            birdData.put("Mutation5Map", mutation5)
            birdData.put("Mutation6Map", mutation6)
            birdData.put("BirthDate", birthFormattedDate)
            birdData.put("Status", status)
            birdData.put("AvailableCage", dataAvailCage)
            birdData.put("ForSaleCage", dataSaleCage)
            birdData.put("ForSalePrice", dataReqPrice)
            birdData.put("SoldDate", soldFormattedDate)
            birdData.put("SoldPrice", dataSoldSalePrice)
            birdData.put("SoldContact", dataSoldContact)
            birdData.put("DeceasedDate", deathFormattedDate)
            birdData.put("DeceasedReason", dataDeathReason)
            birdData.put("ExchangedDate", exchangeFormattedDate)
            birdData.put("ExchangedReason", dataExReason)
            birdData.put("ExchangedContact", dataExContact)
            birdData.put("LostDate", lostFormattedDate)
            birdData.put("LostDetails", dataLostDetails)
            birdData.put("DonatedDate", donatedFormattedDate)
            birdData.put("DonatedContact", dataDonatedContact)
            birdData.put("OtherComment", dataOtherComments)
            birdData.put("FatherId", btnFather.text.toString())
            birdData.put("FatherKey", birdFatherKey)
            birdData.put("FatherBirdKey", birdBirdsFatherKey)
            birdData.put("MotherId", btnMother.text.toString())
            birdData.put("MotherKey", birdMotherKey)
            birdData.put("MotherBirdKey", birdBirdsMotherKey)
            birdData.put("CageName", cageNameValue)
            birdData.put("CageKey", cageKeyValue)
            birdData.put("Provenance", dataSelectedProvenence.text.toString())
            birdData.put("BreederContact", etBreederContact.text.toString())
            birdData.put("BreederBuyPrice", etBuyPrice.text.toString())
            birdData.put("BreederBuyDate", boughtFormattedDate)
            birdData.put("OtherOrigin", etOtBreederContact.text.toString())


            val nonNullMutations = listOf(
                selectedMutations[0],
                selectedMutations[1],
                selectedMutations[2],
                selectedMutations[3],
                selectedMutations[4],
                selectedMutations[5]
            ).filter { !it.isNullOrBlank() }
            val NonNullMutations = mutableListOf<String>()
            for (mutation in nonNullMutations) {
                if (mutation != "null") {
                    NonNullMutations.add(mutation.toString())
                }
            }
            var CombinedMutations = if (NonNullMutations.isNotEmpty()) {
                NonNullMutations.joinToString(" / ")

            } else {
                "Mutation: None"
            }

            if (etIdentifier.text.isNullOrEmpty()) {
                tvBirdId.visibility = View.GONE
            } else {
                tvBirdId.visibility = View.VISIBLE
                tvBirdId.text = "Bird ID: ${etIdentifier.text}"
            }

            if (etLegband.text.isNullOrBlank()) {
                tvBirdLegband.visibility = View.GONE
            } else {
                tvBirdLegband.visibility = View.VISIBLE
                tvBirdLegband.text = "Legband: ${etLegband.text}"
            }

            if (dataSelectedGen.text.isNullOrBlank()) {
                tvBirdSex.visibility = View.GONE
            } else {
                tvBirdSex.visibility = View.VISIBLE
                tvBirdSex.text = "Gender: ${dataSelectedGen.text}"
            }

            if (CombinedMutations == "None") {
                tvBirdMutation.visibility = View.GONE
            } else {
                tvBirdMutation.visibility = View.VISIBLE
                tvBirdMutation.text = "Mutation: $CombinedMutations"

            }

            if (birthFormattedDate.isNullOrBlank()) {
                tvBirdDateOfBirth.visibility = View.GONE
            } else {
                tvBirdDateOfBirth.visibility = View.VISIBLE
                tvBirdDateOfBirth.text = "Date of Birth: $birthFormattedDate"

            }

            if (status.isNullOrBlank()) {
                tvBirdStatus.visibility = View.GONE
            } else {
                tvBirdStatus.visibility = View.VISIBLE
                tvBirdStatus.text = "Status: $status"
            }

            if (cageNameValue.isNullOrBlank()) {
                tvBirdCage.visibility = View.GONE
            } else {
                tvBirdCage.visibility = View.VISIBLE
                tvBirdCage.text = "Cage: $cageNameValue"
            }

            if (soldFormattedDate.isNullOrBlank()) {
                tvBirdSoldDate.visibility = View.GONE
            } else {
                tvBirdSoldDate.visibility = View.VISIBLE

                tvBirdSoldDate.text = "Sold Date: $soldFormattedDate"
            }

            if (deathFormattedDate.isNullOrBlank()) {
                tvBirdDeceasedDate.visibility = View.GONE
            } else {
                tvBirdDeceasedDate.visibility = View.VISIBLE

                tvBirdDeceasedDate.text = "Deceased Date: $deathFormattedDate"
            }

            if (exchangeFormattedDate.isNullOrBlank()) {
                tvBirdExchangeDate.visibility = View.GONE
            } else {
                tvBirdExchangeDate.visibility = View.VISIBLE
                tvBirdExchangeDate.text = "Exchange Date: $exchangeFormattedDate"
            }

            if (lostFormattedDate.isNullOrBlank()) {
                tvBirdLostDate.visibility = View.GONE
            } else {
                tvBirdLostDate.visibility = View.VISIBLE
                tvBirdLostDate.text = "Lost Date: $lostFormattedDate"
            }

            if (donatedFormattedDate.isNullOrBlank()) {
                tvBirdDonatedDate.visibility = View.GONE
            } else {
                tvBirdDonatedDate.visibility = View.VISIBLE

                tvBirdDonatedDate.text = "Donated Date: $donatedFormattedDate"
            }

            if (etSoldSalePrice.text.isNullOrBlank()) {
                tvBirdSalePrice.visibility = View.GONE
            } else {
                tvBirdSalePrice.visibility = View.VISIBLE

                tvBirdSalePrice.text = "Sale Price: ${etSoldSalePrice.text}"
            }

            if (etSoldBuyer.text.isNullOrBlank()) {
                tvBirdBuyer.visibility = View.GONE
            } else {
                tvBirdBuyer.visibility = View.VISIBLE

                tvBirdBuyer.text = "Buyer: ${etSoldBuyer.text}"
            }

            if (etSoldBuyer.text.isNullOrBlank()) {
                tvBirdBuyer.visibility = View.GONE
            } else {
                tvBirdBuyer.visibility = View.VISIBLE

                tvBirdBuyer.text = "Buyer: ${etSoldBuyer.text}"
            }

            if (etDeathReason.text.isNullOrBlank()) {
                tvBirdDeathReason.visibility = View.GONE
            } else {
                tvBirdDeathReason.visibility = View.VISIBLE

                tvBirdDeathReason.text = "Death Reason: ${etDeathReason.text}"
            }

            if (etExReason.text.isNullOrBlank()) {
                tvBirdExchangeReason.visibility = View.GONE
            } else {
                tvBirdExchangeReason.visibility = View.VISIBLE

                tvBirdExchangeReason.text = "Exchange Reason: ${etExReason.text}"
            }

            if (etExWith.text.isNullOrBlank()) {
                tvBirdExchangeWith.visibility = View.GONE
            } else {
                tvBirdExchangeWith.visibility = View.VISIBLE

                tvBirdExchangeWith.text = "Exchange With: ${etExWith.text}"
            }


            if (etDonateChooseContract.text.isNullOrBlank()) {
                tvBirdDonatedContact.visibility = View.GONE
            } else {
                tvBirdDonatedContact.visibility = View.VISIBLE
                tvBirdDonatedContact.text = "Donated Contact: ${etDonateChooseContract.text}"
            }

            if (etLostDetails.text.isNullOrBlank()) {
                tvBirdLostDetails.visibility = View.GONE
            } else {
                tvBirdLostDetails.visibility = View.VISIBLE
                tvBirdLostDetails.text = "Lost Details: ${etLostDetails.text}"
            }

            if (etForSaleReqPrice.text.isNullOrBlank()) {
                tvBirdRequestedPrice.visibility = View.GONE
            } else {
                tvBirdRequestedPrice.visibility = View.VISIBLE
                tvBirdRequestedPrice.text = "Requested Price: ${etForSaleReqPrice.text}"
            }

            if (etOtherComm.text.isNullOrBlank()) {
                tvBirdComment.visibility = View.GONE
            } else {
                tvBirdComment.visibility = View.VISIBLE

                tvBirdComment.text = "Comment: ${etOtherComm.text}"
            }

            if (btnFather.text == "None") {
                tvBirdFather.visibility = View.GONE
            } else {
                tvBirdFather.visibility = View.VISIBLE

                tvBirdFather.text = "Father: ${btnFather.text}"
            }

            if (btnMother.text == "None") {
                tvBirdMother.visibility = View.GONE
            } else {
                tvBirdMother.visibility = View.VISIBLE

                tvBirdMother.text = "Mother: ${btnMother.text}"
            }

            if (etBuyPrice.text.isNullOrBlank()) {
                tvBirdBuyPrice.visibility = View.GONE
            } else {
                tvBirdBuyPrice.visibility = View.VISIBLE

                tvBirdBuyPrice.text = "Buy Price: ${etBuyPrice.text}"
            }

            if (etBreederContact.text.isNullOrBlank()) {
                tvBirdBreederContact.visibility = View.GONE
            } else {
                tvBirdBreederContact.visibility = View.VISIBLE

                tvBirdBreederContact.text = "Bought to Breeder: $etBreederContact"
            }


            if (etOtBreederContact.text.isNullOrBlank()) {
                tvBirdBreederOtherBreed.visibility = View.GONE
            } else {
                tvBirdBreederOtherBreed.visibility = View.VISIBLE

                tvBirdBreederOtherBreed.text = "Breeder: $etOtBreederContact"
            }

//            etIdentifier.text = null
//            etLegband.text = null
//            dataSelectedGen.text = null
//            CombinedMutations = "None"
//            birthFormattedDate = null
//            status = null
//            cageNameValue = null
//            soldFormattedDate = null
//            deathFormattedDate = null
//            exchangeFormattedDate = null
//            lostFormattedDate = null
//            donatedFormattedDate = null
//            etSoldSalePrice.text = null
//            etSoldBuyer.text = null
//            etDeathReason.text = null
//            etExReason.text = null
//            etExWith.text = null
//            etDonateChooseContract.text = null
//            etLostDetails.text = null
//            etForSaleReqPrice.text = null
//            etOtherComm.text = null
//            btnFather.text = "None"
//            btnMother.text = "None"
//            etBuyPrice.text = null
//            etBreederContact.text = null
//            etOtBreederContact.text = null


            qrImage.setImageBitmap(generateQRCodeUri(birdData.toString()))
        }

        btndownload.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // Permission is already granted. You can proceed with saving the image.
                saveImage()
            } else {
                // Request the WRITE_EXTERNAL_STORAGE permission.
                requestPermissions(
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    GenerateEggFragment.WRITE_EXTERNAL_STORAGE_REQUEST_CODE
                )
            }
        }




        return view
    }

    fun checkTextRadio(text: RadioButton? = null, view: TextView) {
        if (text?.text.isNullOrEmpty()) {
            view.visibility = View.GONE
            return
        }


        view.text = "${view.text}: ${text?.text}"
    }

    fun checkTextMatButton(text: MaterialButton? = null, view: TextView) {
        if (text?.text == "None") {
            view.visibility = View.GONE
            return
        }
        view.text = "${view.text}: ${text?.text}"
    }

    fun checkTextMutation(text: String? = null, view: TextView) {
        if (text == "None") {
            view.visibility = View.GONE
            return
        }
        view.text = "${view.text}: ${text}"
    }


    fun checkText(text: EditText? = null, view: TextView) {
        if (text?.text.isNullOrEmpty()) {
            view.visibility = View.GONE
            return
        }
        view.text = "${view.text}: ${text?.text}"
    }

    fun checkTextString(text: String? = null, view: TextView) {
        if (text.isNullOrBlank()) {
            view.visibility = View.GONE
            return
        }
        view.text = "${view.text}: ${text}"
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            GenerateEggFragment.WRITE_EXTERNAL_STORAGE_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, you can now save the image.
                    saveImage()
                } else {
                    // Permission denied, show a message or handle it accordingly.
                    Toast.makeText(
                        requireContext(),
                        "Permission denied. Image cannot be saved.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    fun captureLayoutAsBitmap(view: View): Bitmap {
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        val bitmap = Bitmap.createBitmap(view.drawingCache)
        view.isDrawingCacheEnabled = false // Release the cache
        return bitmap
    }


    //    fun saveImage(){
//        qrimageLayout.isDrawingCacheEnabled = true
//        qrimageLayout.buildDrawingCache()
//        qrimageLayout.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
//        val bitmap: Bitmap? = qrimageLayout.drawingCache
//
//        save(bitmap)
//    }
    fun saveImage() {
        val layoutBitmap = captureLayoutAsBitmap(qrimageLayout)

        save(layoutBitmap)
    }

    fun save(bitmap: Bitmap?) {
        val displayName = "image.jpg"
        val mimeType = "image/jpeg"

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, displayName)
            put(MediaStore.Images.Media.MIME_TYPE, mimeType)
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }

        val resolver = requireActivity().contentResolver
        val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        try {
            if (imageUri != null) {
                resolver.openOutputStream(imageUri)?.use { outputStream ->
                    bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                }
            }

            Toast.makeText(requireContext(), "Image saved to gallery", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            Toast.makeText(requireContext(), "Error: ${e.toString()}", Toast.LENGTH_SHORT).show()
        }
    }

    public var birdFatherKey: String? = null
    public var birdMotherKey: String? = null
    private var birdBirdsFatherKey: String? = null
    private var birdBirdsMotherKey: String? = null


    private var cageNameValue: String? = null
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
                Log.d(ContentValues.TAG, "cage key : $cageKeyValue")
                if (availableLayout.visibility == View.VISIBLE) {
                    etAvailCage.setText(cageNameValue)
                } else if (forSaleLayout.visibility == View.VISIBLE) {
                    etForSaleCage.setText(cageNameValue)
                }
            }
        }

        if (requestCode == 8) {
            if (resultCode == Activity.RESULT_OK) {
                val btnFatherValue: String = data?.getStringExtra("MaleBirdId").toString()
                val btnFatherMutationValue: String =
                    data?.getStringExtra("MaleBirdMutation").toString()
                birdFatherKey = data?.getStringExtra("MaleFlightKey").toString()
                birdBirdsFatherKey = data?.getStringExtra("MaleBirdKey").toString()
                btnFather.text = btnFatherValue
            }
        }
        if (requestCode == 9) {
            if (resultCode == Activity.RESULT_OK) {
                val btnMotherValue: String = data?.getStringExtra("FemaleBirdId").toString()
                val btnMotherMutationValue: String =
                    data?.getStringExtra("FemaleBirdMutation").toString()
                birdMotherKey = data?.getStringExtra("FemaleFlightKey").toString()
                birdBirdsMotherKey = data?.getStringExtra("FemaleBirdKey").toString()
                btnMother.text = btnMotherValue
            }
        }
    }


    private fun generateQRCodeUri(bundleBirdData: String): Bitmap? {
        val multiFormatWriter = MultiFormatWriter()
        val bitMatrix = multiFormatWriter.encode(bundleBirdData, BarcodeFormat.QR_CODE, 400, 400)
        val barcodeEncoder = BarcodeEncoder()
        val bitmap = barcodeEncoder.createBitmap(bitMatrix)

        // Create a file to store the QR code image
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
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
        return bitmap
    }

    fun radioSelection() {

        radioGroup.setOnCheckedChangeListener { group, checkId ->
            val radio: RadioButton = requireView().findViewById(checkId)

            when (checkId) {
                R.id.radioButtonMe -> {
                    Toast.makeText(requireContext(), radio.text, Toast.LENGTH_LONG).show()
                    boughtLayout.visibility = View.GONE
                    otLayout.visibility = View.GONE

                }
                R.id.radioButtonBought -> {
                    boughtLayout.visibility = View.VISIBLE
                    otLayout.visibility = View.GONE
                }
                R.id.radioButtonOther -> {
                    otLayout.visibility = View.VISIBLE
                    boughtLayout.visibility = View.GONE
                }
                else -> {
                    // handle other cases if needed
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
                    slash5.visibility = View.GONE

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
        val dateSetListenerBought =
            DatePickerDialog.OnDateSetListener { datePicker: DatePicker, year: Int, month: Int, day: Int ->
                boughtFormattedDate = makeDateString(day, month + 1, year)
                boughtDateBtn.text = boughtFormattedDate
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
        datePickerDialogBought =
            DatePickerDialog(requireContext(), style, dateSetListenerBought, year, month, day)

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
//        private const val WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GenerateBirdFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GenerateBirdFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}