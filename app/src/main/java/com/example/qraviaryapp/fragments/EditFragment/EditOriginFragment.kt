package com.example.qraviaryapp.fragments.AddFragment

import BirdData
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.dashboards.FemaleBirdListActivity
import com.example.qraviaryapp.activities.dashboards.MaleBirdListActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OriginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditOriginFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }


    private var birdKey: String? = null
    private var nuerseryKey: String? = null
    private var flightKey: String? = null
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
    var birdData = BirdData()
    private lateinit var cageReference: DatabaseReference
    private lateinit var soldReference: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_origin, container, false)


        dbase = FirebaseDatabase.getInstance().reference
        mAuth = FirebaseAuth.getInstance()


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

        birdKey = arguments?.getString("BirdKey")
        flightKey = arguments?.getString("FlightKey")
        nuerseryKey = arguments?.getString("NureseryKey")
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



        initDatePickers()
        showDatePickerDialog(requireContext(), boughtDateBtn, datePickerDialogBought)
        radioButtonMe.isChecked = true
        radioSelection()



        return view
    }

    public var birdFatherKey: String? = null
    public var birdMotherKey: String? = null
    private var birdBirdsFatherKey: String? = null
    private var birdBirdsMotherKey: String? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 8) {
            if (resultCode == RESULT_OK) {
                val btnFatherValue: String = data?.getStringExtra("MaleBirdId").toString()
                val btnFatherMutationValue: String =
                    data?.getStringExtra("MaleBirdMutation").toString()
                birdFatherKey = data?.getStringExtra("MaleFlightKey").toString()
                birdBirdsFatherKey = data?.getStringExtra("MaleBirdKey").toString()
                btnFather.text = btnFatherValue
            }
        }
        if (requestCode == 9) {
            if (resultCode == RESULT_OK) {
                val btnMotherValue: String = data?.getStringExtra("FemaleBirdId").toString()
                val btnMotherMutationValue: String =
                    data?.getStringExtra("FemaleBirdMutation").toString()
                birdMotherKey = data?.getStringExtra("FemaleFlightKey").toString()
                birdBirdsMotherKey = data?.getStringExtra("FemaleBirdKey").toString()
                btnMother.text = btnMotherValue
            }
        }

    }

    override fun onResume() {
        super.onResume()

        if (arguments?.getString("BirdFather")?.isNotEmpty() == true) {
            btnFather.text = arguments?.getString("BirdFatherId")
        }
        if (arguments?.getString("BirdMother")?.isNotEmpty() == true) {
            btnMother.text = arguments?.getString("BirdMotherId")
        }
        if (arguments?.getString("BirdFatherKey")?.isNotEmpty() == true) {
            birdFatherKey = arguments?.getString("BirdFatherKey")
        }
        if (arguments?.getString("BirdMotherKey")?.isNotEmpty() == true) {
            birdMotherKey = arguments?.getString("BirdMotherKey")
        }
        if (arguments?.getString("BirdFatherBirdKey")?.isNotEmpty() == true) {
            birdBirdsFatherKey = arguments?.getString("BirdFatherBirdKey")
        }
        if (arguments?.getString("BirdMotherBirdKey")?.isNotEmpty() == true) {
            birdBirdsMotherKey = arguments?.getString("BirdMotherBirdKey")
        }
        val provenance = arguments?.getString("BirdOtherOrigin")

        Log.d(TAG, "PROVENANCE: $provenance")


        if (birdBoughtOn != "null"){

            radioButtonBought.isChecked = true

            if (arguments?.getString("BirdBoughtBreeder")?.isNotEmpty() == true) {
                etBreederContact.setText(arguments?.getString("BirdBoughtBreeder"))
            }
            if (arguments?.getString("BirdBuyPrice")?.isNotEmpty() == true) {
                etBuyPrice.setText(arguments?.getString("BirdBuyPrice"))
            }
            if (arguments?.getString("BirdBoughtOn")?.isNotEmpty() == true) {
                boughtDateBtn.text = arguments?.getString("BirdBoughtOn")
            }
        }
        if (arguments?.getString("BirdOtherOrigin")?.isNotEmpty() == true) {
            radioButtonOther.isChecked = true
            etOtBreederContact.setText(arguments?.getString("BirdOtherOrigin"))
        }

        val clutch = arguments?.getBoolean("Clutch")
        Log.d(TAG, clutch.toString())
        if (arguments?.getBoolean("Clutch") == true){
            btnMother.text = null
            btnMother.hint = arguments?.getString("BirdMother")
            btnFather.text = null
            btnFather.hint = arguments?.getString("BirdFather")
            btnFather.isEnabled = false
            btnMother.isEnabled = false
            radioButtonBought.isEnabled = false
            radioButtonMe.isEnabled = false
            radioButtonOther.isEnabled = false
        }


        arguments?.clear()
    }

    private fun getTextFromVisibleDatePicker(Button: Button, layout: View): String {
        return if (layout.visibility == View.VISIBLE) {
            Button.text.toString()
        } else {
            ""
        }
    }

    fun addOirigin(
        birdId: String,
        NurseryId: String,
        newBundle: Bundle,
        soldId: String,
        successBasic: Boolean,

        callback: (motherKey: String, fatherKey: String, descendantfatherkey: String, descendantmotherkey: String, purchaseId: String, originBundle: Bundle) -> Unit
    ) {
        val fragment = BasicFragment()
        //  val dataSpinnerFather = spinnerFather.selectedItem.toString()
        // val dataSpinnerMother = spinnerMother.selectedItem.toString()
        val dataSelectedProvenence: RadioButton
        val dataBreederContact = etBreederContact.text.toString()
        val dataBoughtPrice = etBuyPrice.text.toString()
        val dataBreederOtContact = etOtBreederContact.text.toString()
        val dataProvenence: Int = radioGroup.checkedRadioButtonId
        var dataBoughtDate = getTextFromVisibleDatePicker(boughtDateBtn, boughtLayout)

        val birdIdentifier = newBundle.getString("BirdIdentifier")
        val birdGender = newBundle.getString("BirdGender")
        val birdLegband = newBundle.getString("BirdLegband")
        val birdStatus = newBundle.getString("BirdStatus")
        val birdDonatedContact = newBundle.getString("BirdDonatedContact")
        val birdsalePrice = newBundle.getString("BirdSalePrice")
        val birdDateBirth = newBundle.getString("BirdDateBirth")
        val birdBuyer = newBundle.getString("BirdBuyer")
        val birdDeathReason = newBundle.getString("BirdDeathReason")
        val birdExchangeReason = newBundle.getString("BirdExchangeReason")
        val birdExchangeWith = newBundle.getString("BirdExchangeWith")
        val birdLostDetails = newBundle.getString("BirdLostDetails")
        val birdAvailCage = newBundle.getString("BirdAvailCage")
        val birdForsaleCage = newBundle.getString("BirdForsaleCage")
        val birdRequestedPrice = newBundle.getString("BirdRequestedPrice")
        val birdComment = newBundle.getString("BirdComment")
        val birdBuyPrice = newBundle.getString("BirdBuyPrice")
        val birdBoughtOn = newBundle.getString("BirdBoughtOn")
        val birdBoughtBreeder = newBundle.getString("BirdBoughtBreeder")
        val birdBreeder = newBundle.getString("BirdBreeder")
        val birdDeceaseDate = newBundle.getString("BirdDeceaseDate")
        val birdSoldDate = newBundle.getString("BirdSoldDate")
        val birdLostDate = newBundle.getString("BirdLostDate")
        val birdExchangeDate = newBundle.getString("BirdExchangeDate")
        val birdDonatedDate = newBundle.getString("BirdDonatedDate")
        val birdMutation1 = newBundle.getSerializable("BirdMutation1")
        val birdMutation2 = newBundle.getSerializable("BirdMutation2")
        val birdMutation3 = newBundle.getSerializable("BirdMutation3")
        val birdMutation4 = newBundle.getSerializable("BirdMutation4")
        val birdMutation5 = newBundle.getSerializable("BirdMutation5")
        val birdMutation6 = newBundle.getSerializable("BirdMutation6")
        val fatherKey = newBundle.getString("BirdFatherKey")
        val motherKey = newBundle.getString("BirdMotherKey")
        val cageKeyValue = newBundle.getString("CageKeyValue")
        val cageBirdKey = newBundle.getString("CageBirdKeyValue")

        dataSelectedProvenence = view?.findViewById(dataProvenence)
            ?: throw IllegalStateException("RadioButton not found")
        val inputDateFormat = SimpleDateFormat("MMM d yyyy", Locale.getDefault())
        val outputDateFormat = SimpleDateFormat("dd - MM - yyyy", Locale.getDefault())
        if (boughtLayout.visibility == View.VISIBLE) {
            if (boughtDateBtn.text.isNullOrEmpty()){
                val currentDate = Date()
                val dateFormat = SimpleDateFormat("MMM d yyyy", Locale.getDefault())
                dataBoughtDate = dateFormat.format(currentDate)
            }
        }
        birdData = BirdData(
            breederContact = dataBreederContact,
            provenance = dataSelectedProvenence.text.toString(),
            buyPrice = dataBoughtPrice,
            boughtDate = dataBoughtDate,
            otOtherContact = dataBreederOtContact,
            father = btnFather.text.toString(),
            mother = btnMother.text.toString()
        )


        val userId = mAuth.currentUser?.uid.toString()


        val bundle = Bundle()

        bundle.putString("BreederContact", birdData.breederContact)
        bundle.putString("Provenance", birdData.provenance)
        bundle.putString("BuyPrice", birdData.buyPrice)
        bundle.putString("BoughtDate", birdData.boughtDate)
        bundle.putString("OtOtherComment", birdData.otOtherContact)
        bundle.putString("Father", birdData.father)
        bundle.putString("Mother", birdData.mother)
        bundle.putString("BirdFather", birdData.father)//
        bundle.putString("BirdFatherKey", birdData.fatherKey)//
        bundle.putString("BirdMother", birdData.mother)//
        bundle.putString("BirdMotherKey", birdData.motherKey)//


        val newOriginBundle = Bundle()

        newOriginBundle.putString("BirdFather", birdData.father)
        newOriginBundle.putString("BirdMother", birdData.mother)
        newOriginBundle.putString("BirdMotherKey", birdMotherKey)
        newOriginBundle.putString("BirdFatherKey", birdFatherKey)




        if (!cageKeyValue.isNullOrEmpty()) {
            cageReference = cageKeyValue?.let {
                dbase.child("Users").child("ID: $userId").child("Cages")
                    .child("Nursery Cages").child(it).child("Birds").child(cageBirdKey.toString())
                    .child("Parents")
            }!!
        }

        val birdRef =
            dbase.child("Users").child("ID: $userId").child("Birds").child(birdKey.toString())
        val relationshipRef =
            dbase.child("Users").child("ID: $userId").child("Birds").child(birdKey.toString())
                .child("Parents")

        var nurseryRef = FirebaseDatabase.getInstance().reference


        if (nuerseryKey != null) {
            nurseryRef = dbase.child("Users").child("ID: $userId").child("Nursery Birds")
                .child(birdKey.toString())
        } else if (flightKey != null)
            nurseryRef = dbase.child("Users").child("ID: $userId").child("Flight Birds")
                .child(birdKey.toString())

        Log.d(TAG, "RefKey " + flightKey + " " + nuerseryKey)


        val nurseryRelationshipRef = nurseryRef.child("Parents")
        val purchasesRef = dbase.child("Users").child("ID: $userId").child("Purchase Items").push()
        val descendantsFatherRef = dbase.child("Users").child("ID: $userId").child("Flight Birds")
            .child(birdFatherKey.toString()).child("Descendants").push()
        val descendantMotherRef = dbase.child("Users").child("ID: $userId").child("Flight Birds")
            .child(birdMotherKey.toString()).child("Descendants").push()
        val soldidref = dbase.child("Users").child("ID: $userId").child("Sold Items")
            .child(soldId).child("Parents")
        val motherRef = descendantMotherRef.child("Parents")
        val fatherRef = descendantsFatherRef.child("Parents")
        val purchasekey = purchasesRef.key
        var purchaseId: String? = null
        val purchaseRef = purchasesRef.child("Parents")


        val descendantsfatherkey = descendantsFatherRef.key
        val descendantsmotherkey = descendantMotherRef.key
        if (boughtLayout.visibility == View.VISIBLE) {
            val defaultBuyPrice = "0"

            if (TextUtils.isEmpty(etBuyPrice.text)) {
                birdData.buyPrice = defaultBuyPrice
            }


            if (successBasic) {
                purchaseId = purchasesRef.key
                var date: Date? = null
                if (!dataBoughtDate.isNullOrBlank()) {
                    date = inputDateFormat.parse(dataBoughtDate)
                    // Do something with the parsed date
                } else {
                    // Handle the case when dataBoughtDate is empty or null
                }
                val formattedDate = outputDateFormat.format(date)

                val monthYearParts = formattedDate.split(" - ")
                val day = monthYearParts[0]
                val month = monthYearParts[1]
                val year = monthYearParts[2]
                if (btnFather.text == "None" && btnMother.text == "None") {
                    Log.d(TAG, "NONEEE")
                    val descendantdata: Map<String, Any?> = hashMapOf(
                        "ChildKey" to birdId,
                        "Identifier" to birdIdentifier,
                        "Legband" to birdLegband,
                        "Status" to birdStatus,
                        "Donated Contact" to birdDonatedContact,
                        "Gender" to birdGender,
                        "Sale Price" to birdsalePrice,
                        "Date of Birth" to birdDateBirth,
                        "Breeder Contact" to birdBuyer,
                        "Death Reason" to birdDeathReason,
                        "Exchange Reason" to birdExchangeReason,
                        "Exchange With" to birdExchangeWith,
                        "Exchange Date" to birdExchangeDate,
                        "Lost Details" to birdLostDetails,
                        "Avail Cage" to birdAvailCage,
                        "For Sale Cage" to birdForsaleCage,
                        "Requested Price" to birdRequestedPrice,
                        "Comments" to birdComment,
                        "Purchase Id" to purchasekey,
                        "Buy Price" to birdData.buyPrice,
                        "Bought On" to birdData.boughtDate,
                        "Bought Breeder" to birdData.breederContact,
                        "Breeder" to birdBreeder,
                        "Death Date" to birdDeceaseDate,
                        "Sold Date" to birdSoldDate,
                        "Lost Date" to birdLostDate,
                        "Donated Date" to birdDonatedDate,
                        "Mutation1" to birdMutation1,
                        "Mutation2" to birdMutation2,
                        "Mutation3" to birdMutation3,
                        "Mutation4" to birdMutation4,
                        "Mutation5" to birdMutation5,
                        "Mutation6" to birdMutation6,
                        "Month" to month.toFloat(),
                        "Year" to year.toFloat()
                    )
//                descendantsFatherRef.updateChildren(descendantdata)
                    val fatherRefdata: Map<String, Any?> = hashMapOf(
                        "Father" to birdData.father,
                        "Mother" to birdData.mother,
                        "FatherKey" to birdFatherKey,
                        "BirdFatherKey" to birdBirdsFatherKey,
                        "BirdMotherKey" to birdBirdsMotherKey,
                        "MotherKey" to birdMotherKey,
                        "Bird Key" to birdId
                    )

                    purchaseRef.updateChildren(fatherRefdata)
                    purchasesRef.updateChildren(descendantdata)
//                fatherRef.updateChildren(fatherRefdata)


                } else if (btnFather.text != "None" && btnMother.text == "None") {
                    // Update descendantsFatherRef
                    val descendantdata: Map<String, Any?> = hashMapOf(
                        "ChildKey" to birdId,
                        "Identifier" to birdIdentifier,
                        "Legband" to birdLegband,
                        "Status" to birdStatus,
                        "Donated Contact" to birdDonatedContact,
                        "Gender" to birdGender,
                        "Sale Price" to birdsalePrice,
                        "Date of Birth" to birdDateBirth,
                        "Breeder Contact" to birdBuyer,
                        "Death Reason" to birdDeathReason,
                        "Exchange Reason" to birdExchangeReason,
                        "Exchange With" to birdExchangeWith,
                        "Exchange Date" to birdExchangeDate,
                        "Lost Details" to birdLostDetails,
                        "Avail Cage" to birdAvailCage,
                        "For Sale Cage" to birdForsaleCage,
                        "Requested Price" to birdRequestedPrice,
                        "Comments" to birdComment,
                        "Buy Price" to birdData.buyPrice,
                        "Purchase Id" to purchasekey,
                        "Bought On" to birdData.boughtDate,
                        "Bought Breeder" to birdData.breederContact,
                        "Breeder" to birdBreeder,
                        "Death Date" to birdDeceaseDate,
                        "Sold Date" to birdSoldDate,
                        "Lost Date" to birdLostDate,
                        "Donated Date" to birdDonatedDate,
                        "Mutation1" to birdMutation1,
                        "Mutation2" to birdMutation2,
                        "Mutation3" to birdMutation3,
                        "Mutation4" to birdMutation4,
                        "Mutation5" to birdMutation5,
                        "Mutation6" to birdMutation6,
                        "Month" to month.toFloat(),
                        "Year" to year.toFloat()
                    )
                    descendantsFatherRef.updateChildren(descendantdata)
                    val fatherRefdata: Map<String, Any?> = hashMapOf(
                        "Father" to birdData.father,
                        "Mother" to birdData.mother,
                        "FatherKey" to birdFatherKey,
                        "BirdFatherKey" to birdBirdsFatherKey,
                        "BirdMotherKey" to birdBirdsMotherKey,
                        "MotherKey" to birdMotherKey
                    )

                    purchaseRef.updateChildren(fatherRefdata)
                    purchasesRef.updateChildren(descendantdata)
                    fatherRef.updateChildren(fatherRefdata)
                    Log.d(TAG, "FatherRef! ${btnFather.text}  ${btnMother.text} ")
                } else if (btnMother.text != "None" && btnFather.text == "None") {
                    // Update descendantMotherRef
                    val descendantdata: Map<String, Any?> = hashMapOf(
                        "ChildKey" to birdId,
                        "Identifier" to birdIdentifier,
                        "Legband" to birdLegband,
                        "Status" to birdStatus,
                        "Donated Contact" to birdDonatedContact,
                        "Gender" to birdGender,
                        "Sale Price" to birdsalePrice,
                        "Date of Birth" to birdDateBirth,
                        "Breeder Contact" to birdBuyer,
                        "Death Reason" to birdDeathReason,
                        "Exchange Reason" to birdExchangeReason,
                        "Exchange With" to birdExchangeWith,
                        "Exchange Date" to birdExchangeDate,
                        "Lost Details" to birdLostDetails,
                        "Avail Cage" to birdAvailCage,
                        "For Sale Cage" to birdForsaleCage,
                        "Requested Price" to birdRequestedPrice,
                        "Comments" to birdComment,
                        "Buy Price" to birdData.buyPrice,
                        "Purchase Id" to purchasekey,
                        "Bought On" to birdData.boughtDate,
                        "Bought Breeder" to birdData.breederContact,
                        "Breeder" to birdBreeder,
                        "Death Date" to birdDeceaseDate,
                        "Sold Date" to birdSoldDate,
                        "Lost Date" to birdLostDate,
                        "Donated Date" to birdDonatedDate,
                        "Mutation1" to birdMutation1,
                        "Mutation2" to birdMutation2,
                        "Mutation3" to birdMutation3,
                        "Mutation4" to birdMutation4,
                        "Mutation5" to birdMutation5,
                        "Mutation6" to birdMutation6,
                        "Month" to month.toFloat(),
                        "Year" to year.toFloat()
                    )
                    descendantMotherRef.updateChildren(descendantdata)
                    val motherRefdata: Map<String, Any?> = hashMapOf(
                        "Father" to birdData.father,
                        "Mother" to birdData.mother,
                        "FatherKey" to birdFatherKey,
                        "BirdFatherKey" to birdBirdsFatherKey,
                        "BirdMotherKey" to birdBirdsMotherKey,
                        "MotherKey" to birdMotherKey
                    )
                    purchaseRef.updateChildren(motherRefdata)
                    purchasesRef.updateChildren(descendantdata)
                    motherRef.updateChildren(motherRefdata)
                    Log.d(TAG, "MotherRef!!")
                } else {
                    val descendantdata: Map<String, Any?> = hashMapOf(
                        "ChildKey" to birdId,
                        "Identifier" to birdIdentifier,
                        "Legband" to birdLegband,
                        "Status" to birdStatus,
                        "Donated Contact" to birdDonatedContact,
                        "Gender" to birdGender,
                        "Sale Price" to birdsalePrice,
                        "Date of Birth" to birdDateBirth,
                        "Breeder Contact" to birdBuyer,
                        "Death Reason" to birdDeathReason,
                        "Exchange Reason" to birdExchangeReason,
                        "Exchange With" to birdExchangeWith,
                        "Exchange Date" to birdExchangeDate,
                        "Purchase Id" to purchasekey,
                        "Lost Details" to birdLostDetails,
                        "Avail Cage" to birdAvailCage,
                        "For Sale Cage" to birdForsaleCage,
                        "Requested Price" to birdRequestedPrice,
                        "Comments" to birdComment,
                        "Buy Price" to birdData.buyPrice,
                        "Bought On" to birdData.boughtDate,
                        "Bought Breeder" to birdData.breederContact,
                        "Breeder" to birdBreeder,
                        "Death Date" to birdDeceaseDate,
                        "Sold Date" to birdSoldDate,
                        "Lost Date" to birdLostDate,
                        "Donated Date" to birdDonatedDate,
                        "Mutation1" to birdMutation1,
                        "Mutation2" to birdMutation2,
                        "Mutation3" to birdMutation3,
                        "Mutation4" to birdMutation4,
                        "Mutation5" to birdMutation5,
                        "Mutation6" to birdMutation6,
                        "Month" to month.toFloat(),
                        "Year" to year.toFloat()
                    )
                    descendantMotherRef.updateChildren(descendantdata)
                    descendantsFatherRef.updateChildren(descendantdata)
                    val motherRefdata: Map<String, Any?> = hashMapOf(
                        "Father" to birdData.father,
                        "Mother" to birdData.mother,
                        "FatherKey" to birdFatherKey,
                        "BirdFatherKey" to birdBirdsFatherKey,
                        "BirdMotherKey" to birdBirdsMotherKey,
                        "MotherKey" to birdMotherKey
                    )
                    purchaseRef.updateChildren(motherRefdata)
                    purchasesRef.updateChildren(descendantdata)
                    motherRef.updateChildren(motherRefdata)
                    fatherRef.updateChildren(motherRefdata)
                    Log.d(TAG, "MotherRef!! and FatherRef!!")
                }

                val parentdata: Map<String, Any?> = hashMapOf(
                    "Father" to birdData.father,
                    "Mother" to birdData.mother,
                    "FatherKey" to birdFatherKey,
                    "BirdFatherKey" to birdBirdsFatherKey,
                    "BirdMotherKey" to birdBirdsMotherKey,
                    "MotherKey" to birdMotherKey
                )

                if (!cageKeyValue.isNullOrEmpty()) {
                    cageReference.updateChildren(parentdata)
                }
                if (soldId != "null" && !soldId.isNullOrEmpty()) {
                    soldidref.updateChildren(parentdata)
                }
                purchaseRef.updateChildren(parentdata)
                relationshipRef.updateChildren(parentdata)
                nurseryRelationshipRef.updateChildren(parentdata)
                val data: Map<String, Any?> = hashMapOf(

                    "Breeder Contact" to birdData.breederContact,
                    "Buy Price" to birdData.buyPrice,
                    "Bought Date" to birdData.boughtDate,
                    "Purchase Id" to purchasekey
                )
                birdRef.updateChildren(data)
                nurseryRef.updateChildren(data)
            } else {
                return
            }

        } else if (otLayout.visibility == View.VISIBLE) {

            if (successBasic) {

                if (btnFather.text == "None" && btnMother.text == "None") {
                    Log.d(TAG, "NONEEE")
                } else if (btnFather.text != "None" && btnMother.text == "None") {
                    // Update descendantsFatherRef
                    val descendantdata: Map<String, Any?> = hashMapOf(
                        "ChildKey" to birdId,
                        "Identifier" to birdIdentifier,
                        "Legband" to birdLegband,
                        "Status" to birdStatus,
                        "Donated Contact" to birdDonatedContact,
                        "Gender" to birdGender,
                        "Sale Price" to birdsalePrice,
                        "Date of Birth" to birdDateBirth,
                        "Breeder Contact" to birdBuyer,
                        "Death Reason" to birdDeathReason,
                        "Exchange Reason" to birdExchangeReason,
                        "Exchange With" to birdExchangeWith,
                        "Exchange Date" to birdExchangeDate,
                        "Lost Details" to birdLostDetails,
                        "Avail Cage" to birdAvailCage,
                        "For Sale Cage" to birdForsaleCage,
                        "Requested Price" to birdRequestedPrice,
                        "Comments" to birdComment,
                        "Buy Price" to birdBuyPrice,
                        "Bought On" to birdBoughtOn,
                        "Bought Breeder" to birdBoughtBreeder,
                        "Breeder" to birdData.otOtherContact,
                        "Death Date" to birdDeceaseDate,
                        "Sold Date" to birdSoldDate,
                        "Lost Date" to birdLostDate,
                        "Donated Date" to birdDonatedDate,
                        "Mutation1" to birdMutation1,
                        "Mutation2" to birdMutation2,
                        "Mutation3" to birdMutation3,
                        "Mutation4" to birdMutation4,
                        "Mutation5" to birdMutation5,
                        "Mutation6" to birdMutation6,
                    )
                    descendantsFatherRef.updateChildren(descendantdata)
                    val fatherRefdata: Map<String, Any?> = hashMapOf(
                        "Father" to birdData.father,
                        "Mother" to birdData.mother,
                        "FatherKey" to birdFatherKey,
                        "BirdFatherKey" to birdBirdsFatherKey,
                        "BirdMotherKey" to birdBirdsMotherKey,
                        "MotherKey" to birdMotherKey
                    )
                    fatherRef.updateChildren(fatherRefdata)
                    Log.d(TAG, "FatherRef! ${btnFather.text}  ${btnMother.text} ")
                } else if (btnMother.text != "None" && btnFather.text == "None") {
                    // Update descendantMotherRef
                    val descendantdata: Map<String, Any?> = hashMapOf(
                        "ChildKey" to birdId,
                        "Identifier" to birdIdentifier,
                        "Legband" to birdLegband,
                        "Status" to birdStatus,
                        "Donated Contact" to birdDonatedContact,
                        "Gender" to birdGender,
                        "Sale Price" to birdsalePrice,
                        "Date of Birth" to birdDateBirth,
                        "Breeder Contact" to birdBuyer,
                        "Death Reason" to birdDeathReason,
                        "Exchange Reason" to birdExchangeReason,
                        "Exchange With" to birdExchangeWith,
                        "Exchange Date" to birdExchangeDate,
                        "Lost Details" to birdLostDetails,
                        "Avail Cage" to birdAvailCage,
                        "For Sale Cage" to birdForsaleCage,
                        "Requested Price" to birdRequestedPrice,
                        "Comments" to birdComment,
                        "Buy Price" to birdBuyPrice,
                        "Bought On" to birdBoughtOn,
                        "Bought Breeder" to birdBoughtBreeder,
                        "Breeder" to birdData.otOtherContact,
                        "Death Date" to birdDeceaseDate,
                        "Sold Date" to birdSoldDate,
                        "Lost Date" to birdLostDate,
                        "Donated Date" to birdDonatedDate,
                        "Mutation1" to birdMutation1,
                        "Mutation2" to birdMutation2,
                        "Mutation3" to birdMutation3,
                        "Mutation4" to birdMutation4,
                        "Mutation5" to birdMutation5,
                        "Mutation6" to birdMutation6,
                    )
                    descendantMotherRef.updateChildren(descendantdata)
                    val motherRefdata: Map<String, Any?> = hashMapOf(
                        "Father" to birdData.father,
                        "Mother" to birdData.mother,
                        "FatherKey" to birdFatherKey,
                        "BirdFatherKey" to birdBirdsFatherKey,
                        "BirdMotherKey" to birdBirdsMotherKey,
                        "MotherKey" to birdMotherKey
                    )
                    motherRef.updateChildren(motherRefdata)
                    Log.d(TAG, "MotherRef!!")
                } else {
                    val descendantdata: Map<String, Any?> = hashMapOf(
                        "ChildKey" to birdId,
                        "Identifier" to birdIdentifier,
                        "Legband" to birdLegband,
                        "Status" to birdStatus,
                        "Donated Contact" to birdDonatedContact,
                        "Gender" to birdGender,
                        "Sale Price" to birdsalePrice,
                        "Date of Birth" to birdDateBirth,
                        "Breeder Contact" to birdBuyer,
                        "Death Reason" to birdDeathReason,
                        "Exchange Reason" to birdExchangeReason,
                        "Exchange With" to birdExchangeWith,
                        "Exchange Date" to birdExchangeDate,
                        "Lost Details" to birdLostDetails,
                        "Avail Cage" to birdAvailCage,
                        "For Sale Cage" to birdForsaleCage,
                        "Requested Price" to birdRequestedPrice,
                        "Comments" to birdComment,
                        "Buy Price" to birdBuyPrice,
                        "Bought On" to birdBoughtOn,
                        "Bought Breeder" to birdBoughtBreeder,
                        "Breeder" to birdData.otOtherContact,
                        "Death Date" to birdDeceaseDate,
                        "Sold Date" to birdSoldDate,
                        "Lost Date" to birdLostDate,
                        "Donated Date" to birdDonatedDate,
                        "Mutation1" to birdMutation1,
                        "Mutation2" to birdMutation2,
                        "Mutation3" to birdMutation3,
                        "Mutation4" to birdMutation4,
                        "Mutation5" to birdMutation5,
                        "Mutation6" to birdMutation6,
                    )
                    descendantMotherRef.updateChildren(descendantdata)
                    descendantsFatherRef.updateChildren(descendantdata)
                    val motherRefdata: Map<String, Any?> = hashMapOf(
                        "Father" to birdData.father,
                        "Mother" to birdData.mother,
                        "FatherKey" to birdFatherKey,
                        "BirdFatherKey" to birdBirdsFatherKey,
                        "BirdMotherKey" to birdBirdsMotherKey,
                        "MotherKey" to birdMotherKey
                    )
                    motherRef.updateChildren(motherRefdata)
                    fatherRef.updateChildren(motherRefdata)
                    Log.d(TAG, "MotherRef!! and FatherRef!!")
                }
                val parentdata: Map<String, Any?> = hashMapOf(
                    "Father" to birdData.father,
                    "Mother" to birdData.mother,
                    "FatherKey" to birdFatherKey,
                    "BirdFatherKey" to birdBirdsFatherKey,
                    "BirdMotherKey" to birdBirdsMotherKey,
                    "MotherKey" to birdMotherKey
                )

                if (!cageKeyValue.isNullOrEmpty()) {
                    cageReference.updateChildren(parentdata)
                }
                if (soldId != "null" && !soldId.isNullOrEmpty()) {
                    soldidref.updateChildren(parentdata)
                }
                relationshipRef.updateChildren(parentdata)
                nurseryRelationshipRef.updateChildren(parentdata)

                val data: Map<String, Any?> = hashMapOf(

                    "Other Breeder Contact" to birdData.otOtherContact
                )
                birdRef.updateChildren(data)
                nurseryRef.updateChildren(data)

            } else {
                return
            }

        } else {

            if (successBasic) {
                if (btnFather.text == "None" && btnMother.text == "None") {
                    Log.d(TAG, "NONEEE")
                } else if (btnFather.text != "None" && btnMother.text == "None") {
                    // Update descendantsFatherRef
                    val descendantdata: Map<String, Any?> = hashMapOf(
                        "ChildKey" to birdId,
                        "Identifier" to birdIdentifier,
                        "Legband" to birdLegband,
                        "Status" to birdStatus,
                        "Donated Contact" to birdDonatedContact,
                        "Gender" to birdGender,
                        "Sale Price" to birdsalePrice,
                        "Date of Birth" to birdDateBirth,
                        "Breeder Contact" to birdBuyer,
                        "Death Reason" to birdDeathReason,
                        "Exchange Reason" to birdExchangeReason,
                        "Exchange With" to birdExchangeWith,
                        "Exchange Date" to birdExchangeDate,
                        "Lost Details" to birdLostDetails,
                        "Avail Cage" to birdAvailCage,
                        "For Sale Cage" to birdForsaleCage,
                        "Requested Price" to birdRequestedPrice,
                        "Comments" to birdComment,
                        "Buy Price" to birdBuyPrice,
                        "Bought On" to birdBoughtOn,
                        "Bought Breeder" to birdBoughtBreeder,
                        "Breeder" to birdBreeder,
                        "Death Date" to birdDeceaseDate,
                        "Sold Date" to birdSoldDate,
                        "Lost Date" to birdLostDate,
                        "Donated Date" to birdDonatedDate,
                        "Mutation1" to birdMutation1,
                        "Mutation2" to birdMutation2,
                        "Mutation3" to birdMutation3,
                        "Mutation4" to birdMutation4,
                        "Mutation5" to birdMutation5,
                        "Mutation6" to birdMutation6,
                    )
                    descendantsFatherRef.updateChildren(descendantdata)
                    val fatherRefdata: Map<String, Any?> = hashMapOf(
                        "Father" to birdData.father,
                        "Mother" to birdData.mother,
                        "FatherKey" to birdFatherKey,
                        "BirdFatherKey" to birdBirdsFatherKey,
                        "BirdMotherKey" to birdBirdsMotherKey,
                        "MotherKey" to birdMotherKey
                    )
                    fatherRef.updateChildren(fatherRefdata)
                    Log.d(TAG, "FatherRef! ${btnFather.text}  ${btnMother.text} ")
                } else if (btnMother.text != "None" && btnFather.text == "None") {
                    // Update descendantMotherRef
                    val descendantdata: Map<String, Any?> = hashMapOf(
                        "ChildKey" to birdId,
                        "Identifier" to birdIdentifier,
                        "Legband" to birdLegband,
                        "Status" to birdStatus,
                        "Donated Contact" to birdDonatedContact,
                        "Gender" to birdGender,
                        "Sale Price" to birdsalePrice,
                        "Date of Birth" to birdDateBirth,
                        "Breeder Contact" to birdBuyer,
                        "Death Reason" to birdDeathReason,
                        "Exchange Reason" to birdExchangeReason,
                        "Exchange With" to birdExchangeWith,
                        "Exchange Date" to birdExchangeDate,
                        "Lost Details" to birdLostDetails,
                        "Avail Cage" to birdAvailCage,
                        "For Sale Cage" to birdForsaleCage,
                        "Requested Price" to birdRequestedPrice,
                        "Comments" to birdComment,
                        "Buy Price" to birdBuyPrice,
                        "Bought On" to birdBoughtOn,
                        "Bought Breeder" to birdBoughtBreeder,
                        "Breeder" to birdBreeder,
                        "Death Date" to birdDeceaseDate,
                        "Sold Date" to birdSoldDate,
                        "Lost Date" to birdLostDate,
                        "Donated Date" to birdDonatedDate,
                        "Mutation1" to birdMutation1,
                        "Mutation2" to birdMutation2,
                        "Mutation3" to birdMutation3,
                        "Mutation4" to birdMutation4,
                        "Mutation5" to birdMutation5,
                        "Mutation6" to birdMutation6,
                    )
                    descendantMotherRef.updateChildren(descendantdata)
                    val motherRefdata: Map<String, Any?> = hashMapOf(
                        "Father" to birdData.father,
                        "Mother" to birdData.mother,
                        "FatherKey" to birdFatherKey,
                        "BirdFatherKey" to birdBirdsFatherKey,
                        "BirdMotherKey" to birdBirdsMotherKey,
                        "MotherKey" to birdMotherKey
                    )
                    motherRef.updateChildren(motherRefdata)
                    Log.d(TAG, "MotherRef!!")
                } else {
                    val descendantdata: Map<String, Any?> = hashMapOf(
                        "ChildKey" to birdId,
                        "Identifier" to birdIdentifier,
                        "Legband" to birdLegband,
                        "Status" to birdStatus,
                        "Donated Contact" to birdDonatedContact,
                        "Gender" to birdGender,
                        "Sale Price" to birdsalePrice,
                        "Date of Birth" to birdDateBirth,
                        "Breeder Contact" to birdBuyer,
                        "Death Reason" to birdDeathReason,
                        "Exchange Reason" to birdExchangeReason,
                        "Exchange With" to birdExchangeWith,
                        "Exchange Date" to birdExchangeDate,
                        "Lost Details" to birdLostDetails,
                        "Avail Cage" to birdAvailCage,
                        "For Sale Cage" to birdForsaleCage,
                        "Requested Price" to birdRequestedPrice,
                        "Comments" to birdComment,
                        "Buy Price" to birdBuyPrice,
                        "Bought On" to birdBoughtOn,
                        "Bought Breeder" to birdBoughtBreeder,
                        "Breeder" to birdBreeder,
                        "Death Date" to birdDeceaseDate,
                        "Sold Date" to birdSoldDate,
                        "Lost Date" to birdLostDate,
                        "Donated Date" to birdDonatedDate,
                        "Mutation1" to birdMutation1,
                        "Mutation2" to birdMutation2,
                        "Mutation3" to birdMutation3,
                        "Mutation4" to birdMutation4,
                        "Mutation5" to birdMutation5,
                        "Mutation6" to birdMutation6,
                    )
                    descendantMotherRef.updateChildren(descendantdata)
                    descendantsFatherRef.updateChildren(descendantdata)
                    val motherRefdata: Map<String, Any?> = hashMapOf(
                        "Father" to birdData.father,
                        "Mother" to birdData.mother,
                        "FatherKey" to birdFatherKey,
                        "BirdFatherKey" to birdBirdsFatherKey,
                        "BirdMotherKey" to birdBirdsMotherKey,
                        "MotherKey" to birdMotherKey
                    )
                    motherRef.updateChildren(motherRefdata)
                    fatherRef.updateChildren(motherRefdata)
                    Log.d(TAG, "MotherRef!! and FatherRef!!")
                }
                val data: Map<String, Any?> = hashMapOf(
                    "Father" to birdData.father,
                    "Mother" to birdData.mother,
                    "FatherKey" to birdFatherKey,
                    "BirdFatherKey" to birdBirdsFatherKey,
                    "BirdMotherKey" to birdBirdsMotherKey,
                    "MotherKey" to birdMotherKey
                )
                if (!cageKeyValue.isNullOrEmpty()) {
                    cageReference.updateChildren(data)
                }
                relationshipRef.updateChildren(data)
                nurseryRelationshipRef.updateChildren(data)
                if (soldId != "null" && !soldId.isNullOrEmpty()) {
                    soldidref.updateChildren(data)
                }
            } else {
                return
            }


        }
        callback(
            birdMotherKey.toString(),
            birdFatherKey.toString(),
            descendantsfatherkey.toString(),
            descendantsmotherkey.toString(),
            purchaseId.toString(),
            newOriginBundle
        )
    }

    fun addFlightOrigin(
        birdId: String,
        FlightId: String,
        newBundle: Bundle, soldId: String,
        successBasic: Boolean,
        callback: (motherKey: String, fatherKey: String, descendantfatherkey: String, descendantmotherkey: String, purchaseId: String, originBundle: Bundle) -> Unit
    ) {
        val fragment = BasicFragment()
        //  val dataSpinnerFather = spinnerFather.selectedItem.toString()
        // val dataSpinnerMother = spinnerMother.selectedItem.toString()
        val dataSelectedProvenence: RadioButton
        val dataBreederContact = etBreederContact.text.toString()
        val dataBoughtPrice = etBuyPrice.text.toString()
        val dataBreederOtContact = etOtBreederContact.text.toString()
        val dataProvenence: Int = radioGroup.checkedRadioButtonId
        var dataBoughtDate = getTextFromVisibleDatePicker(boughtDateBtn, boughtLayout)

        Log.d(ContentValues.TAG, "BiRDIDDDD " + birdId)


        dataSelectedProvenence = view?.findViewById(dataProvenence)
            ?: throw IllegalStateException("RadioButton not found")
        val inputDateFormat = SimpleDateFormat("MMM d yyyy", Locale.getDefault())
        val outputDateFormat = SimpleDateFormat("dd - MM - yyyy", Locale.getDefault())
        if (boughtLayout.visibility == View.VISIBLE) {
            if (boughtDateBtn.text.isNullOrEmpty()){
                val currentDate = Date()
                val dateFormat = SimpleDateFormat("MMM d yyyy", Locale.getDefault())
                dataBoughtDate = dateFormat.format(currentDate)
            }
        }
        birdData = BirdData(
            breederContact = dataBreederContact,
            provenance = dataSelectedProvenence.text.toString(),
            buyPrice = dataBoughtPrice,
            boughtDate = dataBoughtDate,
            otOtherContact = dataBreederOtContact,
            father = btnFather.text.toString(),
            mother = btnMother.text.toString()
        )


        val soldRefKey = newBundle.getString("SoldRefKey")
        val birdIdentifier = newBundle.getString("BirdIdentifier")
        val birdGender = newBundle.getString("BirdGender")
        val birdLegband = newBundle.getString("BirdLegband")
        val birdStatus = newBundle.getString("BirdStatus")
        val birdDonatedContact = newBundle.getString("BirdDonatedContact")
        val birdsalePrice = newBundle.getString("BirdSalePrice")
        val birdDateBirth = newBundle.getString("BirdDateBirth")
        val birdBuyer = newBundle.getString("BirdBuyer")
        val birdDeathReason = newBundle.getString("BirdDeathReason")
        val birdExchangeReason = newBundle.getString("BirdExchangeReason")
        val birdExchangeWith = newBundle.getString("BirdExchangeWith")
        val birdLostDetails = newBundle.getString("BirdLostDetails")
        val birdAvailCage = newBundle.getString("BirdAvailCage")
        val birdForsaleCage = newBundle.getString("BirdForsaleCage")
        val birdRequestedPrice = newBundle.getString("BirdRequestedPrice")
        val birdComment = newBundle.getString("BirdComment")
        val birdBuyPrice = newBundle.getString("BirdBuyPrice")
        val birdBoughtOn = newBundle.getString("BirdBoughtOn")
        val birdBoughtBreeder = newBundle.getString("BirdBoughtBreeder")
        val birdBreeder = newBundle.getString("BirdBreeder")
        val birdDeceaseDate = newBundle.getString("BirdDeceaseDate")
        val birdSoldDate = newBundle.getString("BirdSoldDate")
        val birdLostDate = newBundle.getString("BirdLostDate")
        val birdExchangeDate = newBundle.getString("BirdExchangeDate")
        val birdDonatedDate = newBundle.getString("BirdDonatedDate")
        val birdMutation1 = newBundle.getSerializable("BirdMutation1")
        val birdMutation2 = newBundle.getSerializable("BirdMutation2")
        val birdMutation3 = newBundle.getSerializable("BirdMutation3")
        val birdMutation4 = newBundle.getSerializable("BirdMutation4")
        val birdMutation5 = newBundle.getSerializable("BirdMutation5")
        val birdMutation6 = newBundle.getSerializable("BirdMutation6")
        val fatherKey = newBundle.getString("BirdFatherKey")
        val motherKey = newBundle.getString("BirdMotherKey")
        val cageKeyValue = newBundle.getString("CageKeyValue")
        val cageBirdKey = newBundle.getString("CageBirdKeyValue")


        val newOriginBundle = Bundle()

        newOriginBundle.putString("BirdFather", birdData.father)
        newOriginBundle.putString("BirdMother", birdData.mother)
        newOriginBundle.putString("BirdMotherKey", birdMotherKey)
        newOriginBundle.putString("BirdFatherKey", birdFatherKey)



        Log.d(TAG, birdIdentifier.toString())
        val userId = mAuth.currentUser?.uid.toString()

        if (!cageKeyValue.isNullOrEmpty()) {
            cageReference = cageKeyValue?.let {
                dbase.child("Users").child("ID: $userId").child("Cages")
                    .child("Flight Cages").child(it).child("Birds").child(birdKey.toString())
                    .child("Parents")
            }!!
        }

        if (!soldRefKey.isNullOrEmpty()){
            soldReference = soldRefKey?.let{
                dbase.child("Users").child("ID: $userId").child("Sold Items").child(it)
            }!!
        }


        val birdRef = dbase.child("Users").child("ID: $userId").child("Birds").child(birdKey.toString())
        val relationshipRef =
            dbase.child("Users").child("ID: $userId").child("Birds").child(birdId).child("Parents")

        val nurseryRef =
            dbase.child("Users").child("ID: $userId").child("Flight Birds").child(flightKey.toString())
        val nurseryRelationshipRef = nurseryRef.child("Parents")
//        val descendantsRef = dbase.child("Users").child("ID: $userId").child("Flight Birds")
//            .child(birdFatherKey.toString()).child("Descendants").push()
        val purchasesRef = dbase.child("Users").child("ID: $userId").child("Purchase Items").push()

        val descendantsFatherRef = dbase.child("Users").child("ID: $userId").child("Flight Birds")
            .child(birdFatherKey.toString()).child("Descendants").push()
        val descendantMotherRef = dbase.child("Users").child("ID: $userId").child("Flight Birds")
            .child(birdMotherKey.toString()).child("Descendants").push()
        val soldidref = dbase.child("Users").child("ID: $userId").child("Sold Items")
            .child(soldId).child("Parents")
        val motherRef = descendantMotherRef.child("Parents")
        val fatherRef = descendantsFatherRef.child("Parents")
        var purchaseId: String? = null
        val purchaseRef = purchasesRef.child("Parents")
        val descendantsfatherkey = descendantsFatherRef.key
        val descendantsmotherkey = descendantMotherRef.key
        val purchasekey = purchasesRef.key
        if (boughtLayout.visibility == View.VISIBLE) {

            val defaultBuyPrice = "0"

            if (TextUtils.isEmpty(etBuyPrice.text)) {
                birdData.buyPrice = defaultBuyPrice
            }

            if (successBasic) {
                purchaseId = purchasesRef.key
                val date = inputDateFormat.parse(dataBoughtDate)
                val formattedDate = outputDateFormat.format(date)

                val monthYearParts = formattedDate.split(" - ")
                val day = monthYearParts[0]
                val month = monthYearParts[1]
                val year = monthYearParts[2]
                if (btnFather.text == "None" && btnMother.text == "None") {
                    Log.d(TAG, "NONEEE")

                    val descendantdata: Map<String, Any?> = hashMapOf(
                        "Flight Key" to FlightId,
                        "ChildKey" to birdId,
                        "Identifier" to birdIdentifier,
                        "Legband" to birdLegband,
                        "Status" to birdStatus,
                        "Donated Contact" to birdDonatedContact,
                        "Gender" to birdGender,
                        "Sale Price" to birdsalePrice,
                        "Date of Birth" to birdDateBirth,
                        "Breeder Contact" to birdBuyer,
                        "Death Reason" to birdDeathReason,
                        "Exchange Reason" to birdExchangeReason,
                        "Exchange With" to birdExchangeWith,
                        "Exchange Date" to birdExchangeDate,
                        "Lost Details" to birdLostDetails,
                        "Avail Cage" to birdAvailCage,
                        "For Sale Cage" to birdForsaleCage,
                        "Requested Price" to birdRequestedPrice,
                        "Comments" to birdComment,
                        "Purchase Id" to purchasekey,
                        "Buy Price" to birdData.buyPrice,
                        "Bought On" to birdData.boughtDate,
                        "Bought Breeder" to birdData.breederContact,
                        "Breeder" to birdBreeder,
                        "Death Date" to birdDeceaseDate,
                        "Sold Date" to birdSoldDate,
                        "Lost Date" to birdLostDate,
                        "Donated Date" to birdDonatedDate,
                        "Mutation1" to birdMutation1,
                        "Mutation2" to birdMutation2,
                        "Mutation3" to birdMutation3,
                        "Mutation4" to birdMutation4,
                        "Mutation5" to birdMutation5,
                        "Mutation6" to birdMutation6,
                        "Month" to month.toFloat(),
                        "Year" to year.toFloat()
                    )
//                descendantsFatherRef.updateChildren(descendantdata)
                    val fatherRefdata: Map<String, Any?> = hashMapOf(
                        "Father" to birdData.father,
                        "Mother" to birdData.mother,
                        "FatherKey" to birdFatherKey,
                        "MotherKey" to birdMotherKey,
                        "BirdFatherKey" to birdBirdsFatherKey,
                        "BirdMotherKey" to birdBirdsMotherKey,
                        "Bird Key" to birdId
                    )
                    purchaseRef.updateChildren(fatherRefdata)
                    purchasesRef.updateChildren(descendantdata)
//                fatherRef.updateChildren(fatherRefdata)
                } else if (btnFather.text != "None" && btnMother.text == "None") {
                    // Update descendantsFatherRef
                    val descendantdata: Map<String, Any?> = hashMapOf(
                        "Flight Key" to FlightId,
                        "ChildKey" to birdId,
                        "Identifier" to birdIdentifier,
                        "Legband" to birdLegband,
                        "Status" to birdStatus,
                        "Donated Contact" to birdDonatedContact,
                        "Gender" to birdGender,
                        "Sale Price" to birdsalePrice,
                        "Date of Birth" to birdDateBirth,
                        "Breeder Contact" to birdBuyer,
                        "Death Reason" to birdDeathReason,
                        "Exchange Reason" to birdExchangeReason,
                        "Exchange With" to birdExchangeWith,
                        "Exchange Date" to birdExchangeDate,
                        "Lost Details" to birdLostDetails,
                        "Avail Cage" to birdAvailCage,
                        "For Sale Cage" to birdForsaleCage,
                        "Requested Price" to birdRequestedPrice,
                        "Comments" to birdComment,
                        "Purchase Id" to purchasekey,
                        "Buy Price" to birdData.buyPrice,
                        "Bought On" to birdData.boughtDate,
                        "Bought Breeder" to birdData.breederContact,
                        "Breeder" to birdBreeder,
                        "Death Date" to birdDeceaseDate,
                        "Sold Date" to birdSoldDate,
                        "Lost Date" to birdLostDate,
                        "Donated Date" to birdDonatedDate,
                        "Mutation1" to birdMutation1,
                        "Mutation2" to birdMutation2,
                        "Mutation3" to birdMutation3,
                        "Mutation4" to birdMutation4,
                        "Mutation5" to birdMutation5,
                        "Mutation6" to birdMutation6,
                        "Month" to month.toFloat(),
                        "Year" to year.toFloat()
                    )
                    descendantsFatherRef.updateChildren(descendantdata)
                    val fatherRefdata: Map<String, Any?> = hashMapOf(
                        "Father" to birdData.father,
                        "Mother" to birdData.mother,
                        "FatherKey" to birdFatherKey,
                        "MotherKey" to birdMotherKey,
                        "BirdFatherKey" to birdBirdsFatherKey,
                        "BirdMotherKey" to birdBirdsMotherKey,
                        "Bird Key" to birdId
                    )

                    purchaseRef.updateChildren(fatherRefdata)
                    purchasesRef.updateChildren(descendantdata)
                    fatherRef.updateChildren(fatherRefdata)
                    Log.d(TAG, "FatherRef! ${btnFather.text}  ${btnMother.text} ")
                } else if (btnMother.text != "None" && btnFather.text == "None") {
                    // Update descendantMotherRef
                    val descendantdata: Map<String, Any?> = hashMapOf(
                        "Flight Key" to FlightId,
                        "ChildKey" to birdId,
                        "Identifier" to birdIdentifier,
                        "Legband" to birdLegband,
                        "Status" to birdStatus,
                        "Donated Contact" to birdDonatedContact,
                        "Gender" to birdGender,
                        "Sale Price" to birdsalePrice,
                        "Date of Birth" to birdDateBirth,
                        "Breeder Contact" to birdBuyer,
                        "Death Reason" to birdDeathReason,
                        "Exchange Reason" to birdExchangeReason,
                        "Exchange With" to birdExchangeWith,
                        "Exchange Date" to birdExchangeDate,
                        "Lost Details" to birdLostDetails,
                        "Avail Cage" to birdAvailCage,
                        "For Sale Cage" to birdForsaleCage,
                        "Requested Price" to birdRequestedPrice,
                        "Comments" to birdComment,
                        "Purchase Id" to purchasekey,
                        "Buy Price" to birdData.buyPrice,
                        "Bought On" to birdData.boughtDate,
                        "Bought Breeder" to birdData.breederContact,
                        "Breeder" to birdBreeder,
                        "Death Date" to birdDeceaseDate,
                        "Sold Date" to birdSoldDate,
                        "Lost Date" to birdLostDate,
                        "Donated Date" to birdDonatedDate,
                        "Mutation1" to birdMutation1,
                        "Mutation2" to birdMutation2,
                        "Mutation3" to birdMutation3,
                        "Mutation4" to birdMutation4,
                        "Mutation5" to birdMutation5,
                        "Mutation6" to birdMutation6,
                        "Month" to month.toFloat(),
                        "Year" to year.toFloat()
                    )
                    descendantMotherRef.updateChildren(descendantdata)
                    val motherRefdata: Map<String, Any?> = hashMapOf(
                        "Father" to birdData.father,
                        "Mother" to birdData.mother,
                        "FatherKey" to birdFatherKey,
                        "MotherKey" to birdMotherKey,
                        "BirdFatherKey" to birdBirdsFatherKey,
                        "BirdMotherKey" to birdBirdsMotherKey,
                        "Bird Key" to birdId
                    )
                    purchaseRef.updateChildren(motherRefdata)
                    purchasesRef.updateChildren(descendantdata)
                    motherRef.updateChildren(motherRefdata)
                    Log.d(TAG, "MotherRef!!")
                } else {
                    val descendantdata: Map<String, Any?> = hashMapOf(
                        "Flight Key" to FlightId,
                        "ChildKey" to birdId,
                        "Identifier" to birdIdentifier,
                        "Legband" to birdLegband,
                        "Status" to birdStatus,
                        "Donated Contact" to birdDonatedContact,
                        "Gender" to birdGender,
                        "Sale Price" to birdsalePrice,
                        "Date of Birth" to birdDateBirth,
                        "Breeder Contact" to birdBuyer,
                        "Death Reason" to birdDeathReason,
                        "Exchange Reason" to birdExchangeReason,
                        "Exchange With" to birdExchangeWith,
                        "Exchange Date" to birdExchangeDate,
                        "Lost Details" to birdLostDetails,
                        "Avail Cage" to birdAvailCage,
                        "For Sale Cage" to birdForsaleCage,
                        "Requested Price" to birdRequestedPrice,
                        "Comments" to birdComment,
                        "Purchase Id" to purchasekey,
                        "Buy Price" to birdData.buyPrice,
                        "Bought On" to birdData.boughtDate,
                        "Bought Breeder" to birdData.breederContact,
                        "Breeder" to birdBreeder,
                        "Death Date" to birdDeceaseDate,
                        "Sold Date" to birdSoldDate,
                        "Lost Date" to birdLostDate,
                        "Donated Date" to birdDonatedDate,
                        "Mutation1" to birdMutation1,
                        "Mutation2" to birdMutation2,
                        "Mutation3" to birdMutation3,
                        "Mutation4" to birdMutation4,
                        "Mutation5" to birdMutation5,
                        "Mutation6" to birdMutation6,
                        "Month" to month.toFloat(),
                        "Year" to year.toFloat()
                    )
                    descendantMotherRef.updateChildren(descendantdata)
                    descendantsFatherRef.updateChildren(descendantdata)
                    val motherRefdata: Map<String, Any?> = hashMapOf(
                        "Father" to birdData.father,
                        "Mother" to birdData.mother,
                        "FatherKey" to birdFatherKey,
                        "MotherKey" to birdMotherKey,
                        "BirdFatherKey" to birdBirdsFatherKey,
                        "BirdMotherKey" to birdBirdsMotherKey,
                        "Bird Key" to birdId
                    )
                    purchaseRef.updateChildren(motherRefdata)
                    purchasesRef.updateChildren(descendantdata)
                    fatherRef.updateChildren(motherRefdata)
                    motherRef.updateChildren(motherRefdata)
                    Log.d(TAG, "MotherRef!! and FatherRef!!")
                }


                val parentdata: Map<String, Any?> = hashMapOf(
                    "Father" to birdData.father,
                    "Mother" to birdData.mother,
                    "FatherKey" to birdFatherKey,
                    "MotherKey" to birdMotherKey,
                    "BirdFatherKey" to birdBirdsFatherKey,
                    "BirdMotherKey" to birdBirdsMotherKey,
                )
                if (!cageKeyValue.isNullOrEmpty()) {
                    cageReference.updateChildren(parentdata)
                }

                purchaseRef.updateChildren(parentdata)
                relationshipRef.updateChildren(parentdata)
                nurseryRelationshipRef.updateChildren(parentdata)

                val data: Map<String, Any?> = hashMapOf(

                    "Breeder Contact" to birdData.breederContact,
                    "Buy Price" to birdData.buyPrice,
                    "Bought Date" to birdData.boughtDate,
                    "Purchase Id" to purchasekey,
                )
                if (soldId != "null" && !soldId.isNullOrEmpty()) {
                    soldidref.updateChildren(parentdata)
                    soldReference.updateChildren(data)
                }

                birdRef.updateChildren(data)
                nurseryRef.updateChildren(data)
            } else {
                return
            }

        } else if (otLayout.visibility == View.VISIBLE) {

            if (successBasic) {
                if (btnFather.text == "None" && btnMother.text == "None") {
                    Log.d(TAG, "NONEEE")
                } else if (btnFather.text != "None" && btnMother.text == "None") {
                    if (descendantsFatherRef != null) {
                        // Update descendantsFatherRef
                        val descendantdata: Map<String, Any?> = hashMapOf(
                            "Flight Key" to FlightId,
                            "ChildKey" to birdId,
                            "Identifier" to birdIdentifier,
                            "Legband" to birdLegband,
                            "Status" to birdStatus,
                            "Donated Contact" to birdDonatedContact,
                            "Gender" to birdGender,
                            "Sale Price" to birdsalePrice,
                            "Date of Birth" to birdDateBirth,
                            "Breeder Contact" to birdBuyer,
                            "Death Reason" to birdDeathReason,
                            "Exchange Reason" to birdExchangeReason,
                            "Exchange With" to birdExchangeWith,
                            "Exchange Date" to birdExchangeDate,
                            "Lost Details" to birdLostDetails,
                            "Avail Cage" to birdAvailCage,
                            "For Sale Cage" to birdForsaleCage,
                            "Requested Price" to birdRequestedPrice,
                            "Comments" to birdComment,
                            "Buy Price" to birdBuyPrice,
                            "Bought On" to birdBoughtOn,
                            "Bought Breeder" to birdBoughtBreeder,
                            "Breeder" to birdData.otOtherContact,
                            "Death Date" to birdDeceaseDate,
                            "Sold Date" to birdSoldDate,
                            "Lost Date" to birdLostDate,
                            "Donated Date" to birdDonatedDate,
                            "Mutation1" to birdMutation1,
                            "Mutation2" to birdMutation2,
                            "Mutation3" to birdMutation3,
                            "Mutation4" to birdMutation4,
                            "Mutation5" to birdMutation5,
                            "Mutation6" to birdMutation6,
                        )
                        descendantsFatherRef.updateChildren(descendantdata)
                        val fatherRefdata: Map<String, Any?> = hashMapOf(
                            "Father" to birdData.father,
                            "Mother" to birdData.mother,
                            "FatherKey" to birdFatherKey,
                            "MotherKey" to birdMotherKey,
                            "BirdFatherKey" to birdBirdsFatherKey,
                            "BirdMotherKey" to birdBirdsMotherKey,
                            "Bird Key" to birdId
                        )
                        fatherRef.updateChildren(fatherRefdata)
                        Log.d(TAG, "FatherRef! ${btnFather.text}  ${btnMother.text} ")
                    }
                } else if (btnMother.text != "None" && btnFather.text == "None") {
                    if (descendantMotherRef != null) {
                        // Update descendantMotherRef
                        val descendantdata: Map<String, Any?> = hashMapOf(
                            "Flight Key" to FlightId,
                            "ChildKey" to birdId,
                            "Identifier" to birdIdentifier,
                            "Legband" to birdLegband,
                            "Status" to birdStatus,
                            "Donated Contact" to birdDonatedContact,
                            "Gender" to birdGender,
                            "Sale Price" to birdsalePrice,
                            "Date of Birth" to birdDateBirth,
                            "Breeder Contact" to birdBuyer,
                            "Death Reason" to birdDeathReason,
                            "Exchange Reason" to birdExchangeReason,
                            "Exchange With" to birdExchangeWith,
                            "Exchange Date" to birdExchangeDate,
                            "Lost Details" to birdLostDetails,
                            "Avail Cage" to birdAvailCage,
                            "For Sale Cage" to birdForsaleCage,
                            "Requested Price" to birdRequestedPrice,
                            "Comments" to birdComment,
                            "Buy Price" to birdBuyPrice,
                            "Bought On" to birdBoughtOn,
                            "Bought Breeder" to birdBoughtBreeder,
                            "Breeder" to birdData.otOtherContact,
                            "Death Date" to birdDeceaseDate,
                            "Sold Date" to birdSoldDate,
                            "Lost Date" to birdLostDate,
                            "Donated Date" to birdDonatedDate,
                            "Mutation1" to birdMutation1,
                            "Mutation2" to birdMutation2,
                            "Mutation3" to birdMutation3,
                            "Mutation4" to birdMutation4,
                            "Mutation5" to birdMutation5,
                            "Mutation6" to birdMutation6,
                        )
                        descendantMotherRef.updateChildren(descendantdata)
                        val motherRefdata: Map<String, Any?> = hashMapOf(
                            "Father" to birdData.father,
                            "Mother" to birdData.mother,
                            "FatherKey" to birdFatherKey,
                            "MotherKey" to birdMotherKey,
                            "BirdFatherKey" to birdBirdsFatherKey,
                            "BirdMotherKey" to birdBirdsMotherKey,
                            "Bird Key" to birdId
                        )
                        motherRef.updateChildren(motherRefdata)
                        Log.d(TAG, "MotherRef!!")
                    }
                } else {
                    val descendantdata: Map<String, Any?> = hashMapOf(
                        "Flight Key" to FlightId,
                        "ChildKey" to birdId,
                        "Identifier" to birdIdentifier,
                        "Legband" to birdLegband,
                        "Status" to birdStatus,
                        "Donated Contact" to birdDonatedContact,
                        "Gender" to birdGender,
                        "Sale Price" to birdsalePrice,
                        "Date of Birth" to birdDateBirth,
                        "Breeder Contact" to birdBuyer,
                        "Death Reason" to birdDeathReason,
                        "Exchange Reason" to birdExchangeReason,
                        "Exchange With" to birdExchangeWith,
                        "Exchange Date" to birdExchangeDate,
                        "Lost Details" to birdLostDetails,
                        "Avail Cage" to birdAvailCage,
                        "For Sale Cage" to birdForsaleCage,
                        "Requested Price" to birdRequestedPrice,
                        "Comments" to birdComment,
                        "Buy Price" to birdBuyPrice,
                        "Bought On" to birdBoughtOn,
                        "Bought Breeder" to birdBoughtBreeder,
                        "Breeder" to birdData.otOtherContact,
                        "Death Date" to birdDeceaseDate,
                        "Sold Date" to birdSoldDate,
                        "Lost Date" to birdLostDate,
                        "Donated Date" to birdDonatedDate,
                        "Mutation1" to birdMutation1,
                        "Mutation2" to birdMutation2,
                        "Mutation3" to birdMutation3,
                        "Mutation4" to birdMutation4,
                        "Mutation5" to birdMutation5,
                        "Mutation6" to birdMutation6,
                    )
                    descendantMotherRef.updateChildren(descendantdata)
                    descendantsFatherRef.updateChildren(descendantdata)
                    val motherRefdata: Map<String, Any?> = hashMapOf(
                        "Father" to birdData.father,
                        "Mother" to birdData.mother,
                        "FatherKey" to birdFatherKey,
                        "MotherKey" to birdMotherKey,
                        "BirdFatherKey" to birdBirdsFatherKey,
                        "BirdMotherKey" to birdBirdsMotherKey,
                        "Bird Key" to birdId
                    )
                    fatherRef.updateChildren(motherRefdata)
                    motherRef.updateChildren(motherRefdata)
                    Log.d(TAG, "MotherRef!! and FatherRef!!")
                }

                val parentdata: Map<String, Any?> = hashMapOf(
                    "Father" to birdData.father,
                    "Mother" to birdData.mother,
                    "FatherKey" to birdFatherKey,
                    "BirdFatherKey" to birdBirdsFatherKey,
                    "BirdMotherKey" to birdBirdsMotherKey,
                    "MotherKey" to birdMotherKey
                )
                if (!cageKeyValue.isNullOrEmpty()) {
                    cageReference.updateChildren(parentdata)
                }


                relationshipRef.updateChildren(parentdata)
                nurseryRelationshipRef.updateChildren(parentdata)

                val data: Map<String, Any?> = hashMapOf(

                    "Other Breeder Contact" to birdData.otOtherContact
                )

                if (soldId != "null" && !soldId.isNullOrEmpty()) {
                    soldidref.updateChildren(parentdata)
                    soldReference.updateChildren(data)
                }
                birdRef.updateChildren(data)
                nurseryRef.updateChildren(data)



            } else {
                return
            }


        } else {

            if (successBasic) {
                val data: Map<String, Any?> = hashMapOf(
                    "Father" to birdData.father,
                    "Mother" to birdData.mother,
                    "FatherKey" to birdFatherKey,
                    "BirdFatherKey" to birdBirdsFatherKey,
                    "BirdMotherKey" to birdBirdsMotherKey,
                    "MotherKey" to birdMotherKey
                )
                if (!cageKeyValue.isNullOrEmpty()) {
                    cageReference.updateChildren(data)
                }
                relationshipRef.updateChildren(data)
                nurseryRelationshipRef.updateChildren(data)
                if (soldId != "null" && !soldId.isNullOrEmpty()) {
                    soldidref.updateChildren(data)
                }
                if (btnFather.text == "None" && btnMother.text == "None") {
                    Log.d(TAG, "NONEEE")
                } else if (btnFather.text != "None" && btnMother.text == "None") {
                    if (descendantsFatherRef != null) {
                        // Update descendantsFatherRef
                        val descendantdata: Map<String, Any?> = hashMapOf(
                            "ChildKey" to birdId,
                            "Identifier" to birdIdentifier,
                            "Legband" to birdLegband,
                            "Status" to birdStatus,
                            "Donated Contact" to birdDonatedContact,
                            "Gender" to birdGender,
                            "Sale Price" to birdsalePrice,
                            "Date of Birth" to birdDateBirth,
                            "Breeder Contact" to birdBuyer,
                            "Death Reason" to birdDeathReason,
                            "Exchange Reason" to birdExchangeReason,
                            "Exchange With" to birdExchangeWith,
                            "Exchange Date" to birdExchangeDate,
                            "Lost Details" to birdLostDetails,
                            "Avail Cage" to birdAvailCage,
                            "For Sale Cage" to birdForsaleCage,
                            "Requested Price" to birdRequestedPrice,
                            "Comments" to birdComment,
                            "Buy Price" to birdBuyPrice,
                            "Bought On" to birdBoughtOn,
                            "Bought Breeder" to birdBoughtBreeder,
                            "Breeder" to birdBreeder,
                            "Death Date" to birdDeceaseDate,
                            "Sold Date" to birdSoldDate,
                            "Lost Date" to birdLostDate,
                            "Donated Date" to birdDonatedDate,
                            "Mutation1" to birdMutation1,
                            "Mutation2" to birdMutation2,
                            "Mutation3" to birdMutation3,
                            "Mutation4" to birdMutation4,
                            "Mutation5" to birdMutation5,
                            "Mutation6" to birdMutation6,
                        )
                        descendantsFatherRef.updateChildren(descendantdata)
                        val fatherRefdata: Map<String, Any?> = hashMapOf(
                            "Father" to birdData.father,
                            "Mother" to birdData.mother,
                            "FatherKey" to birdFatherKey,
                            "MotherKey" to birdMotherKey,
                            "BirdFatherKey" to birdBirdsFatherKey,
                            "BirdMotherKey" to birdBirdsMotherKey,
                            "Bird Key" to birdId
                        )
                        fatherRef.updateChildren(fatherRefdata)
                        Log.d(TAG, "FatherRef! ${btnFather.text}  ${btnMother.text} ")
                    }
                } else if (btnMother.text != "None" && btnFather.text == "None") {
                    if (descendantMotherRef != null) {
                        // Update descendantMotherRef
                        val descendantdata: Map<String, Any?> = hashMapOf(
                            "Flight Key" to FlightId,
                            "ChildKey" to birdId,
                            "Identifier" to birdIdentifier,
                            "Legband" to birdLegband,
                            "Status" to birdStatus,
                            "Donated Contact" to birdDonatedContact,
                            "Gender" to birdGender,
                            "Sale Price" to birdsalePrice,
                            "Date of Birth" to birdDateBirth,
                            "Breeder Contact" to birdBuyer,
                            "Death Reason" to birdDeathReason,
                            "Exchange Reason" to birdExchangeReason,
                            "Exchange With" to birdExchangeWith,
                            "Exchange Date" to birdExchangeDate,
                            "Lost Details" to birdLostDetails,
                            "Avail Cage" to birdAvailCage,
                            "For Sale Cage" to birdForsaleCage,
                            "Requested Price" to birdRequestedPrice,
                            "Comments" to birdComment,
                            "Buy Price" to birdBuyPrice,
                            "Bought On" to birdBoughtOn,
                            "Bought Breeder" to birdBoughtBreeder,
                            "Breeder" to birdBreeder,
                            "Death Date" to birdDeceaseDate,
                            "Sold Date" to birdSoldDate,
                            "Lost Date" to birdLostDate,
                            "Donated Date" to birdDonatedDate,
                            "Mutation1" to birdMutation1,
                            "Mutation2" to birdMutation2,
                            "Mutation3" to birdMutation3,
                            "Mutation4" to birdMutation4,
                            "Mutation5" to birdMutation5,
                            "Mutation6" to birdMutation6,
                        )
                        descendantMotherRef.updateChildren(descendantdata)
                        val motherRefdata: Map<String, Any?> = hashMapOf(
                            "Father" to birdData.father,
                            "Mother" to birdData.mother,
                            "FatherKey" to birdFatherKey,
                            "MotherKey" to birdMotherKey,
                            "BirdFatherKey" to birdBirdsFatherKey,
                            "BirdMotherKey" to birdBirdsMotherKey,
                            "Bird Key" to birdId
                        )
                        motherRef.updateChildren(motherRefdata)
                        Log.d(TAG, "MotherRef!!")
                    }
                } else {
                    val descendantdata: Map<String, Any?> = hashMapOf(
                        "Flight Key" to FlightId,
                        "ChildKey" to birdId,
                        "Identifier" to birdIdentifier,
                        "Legband" to birdLegband,
                        "Status" to birdStatus,
                        "Donated Contact" to birdDonatedContact,
                        "Gender" to birdGender,
                        "Sale Price" to birdsalePrice,
                        "Date of Birth" to birdDateBirth,
                        "Breeder Contact" to birdBuyer,
                        "Death Reason" to birdDeathReason,
                        "Exchange Reason" to birdExchangeReason,
                        "Exchange With" to birdExchangeWith,
                        "Exchange Date" to birdExchangeDate,
                        "Lost Details" to birdLostDetails,
                        "Avail Cage" to birdAvailCage,
                        "For Sale Cage" to birdForsaleCage,
                        "Requested Price" to birdRequestedPrice,
                        "Comments" to birdComment,
                        "Buy Price" to birdBuyPrice,
                        "Bought On" to birdBoughtOn,
                        "Bought Breeder" to birdBoughtBreeder,
                        "Breeder" to birdBreeder,
                        "Death Date" to birdDeceaseDate,
                        "Sold Date" to birdSoldDate,
                        "Lost Date" to birdLostDate,
                        "Donated Date" to birdDonatedDate,
                        "Mutation1" to birdMutation1,
                        "Mutation2" to birdMutation2,
                        "Mutation3" to birdMutation3,
                        "Mutation4" to birdMutation4,
                        "Mutation5" to birdMutation5,
                        "Mutation6" to birdMutation6,
                    )
                    descendantMotherRef.updateChildren(descendantdata)
                    descendantsFatherRef.updateChildren(descendantdata)
                    val motherRefdata: Map<String, Any?> = hashMapOf(
                        "Father" to birdData.father,
                        "Mother" to birdData.mother,
                        "FatherKey" to birdFatherKey,
                        "MotherKey" to birdMotherKey,
                        "BirdFatherKey" to birdBirdsFatherKey,
                        "BirdMotherKey" to birdBirdsMotherKey,
                        "Bird Key" to birdId
                    )

                    fatherRef.updateChildren(motherRefdata)
                    motherRef.updateChildren(motherRefdata)
                    Log.d(TAG, "MotherRef!! and FatherRef!!")
                }
            } else {
                return
            }
        }


        callback(
            birdMotherKey.toString(),
            birdFatherKey.toString(),
            descendantsfatherkey.toString(),
            descendantsmotherkey.toString(),
            purchaseId.toString(),
            newOriginBundle
        )
    }



    interface OriginFragmentCallback {
        fun onOriginDatSaved(
            birdId: String,
            nurseryId: String,
            newBundle: Bundle,
            birdMotherKey: String?,
            birdFatherKey: String?
        )
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


    private fun initDatePickers() {
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

        datePickerDialogBought =
            DatePickerDialog(requireContext(), style, dateSetListenerBought, year, month, day)
        datePickerDialogBought.datePicker.maxDate = System.currentTimeMillis()
    }

    fun showDatePickerDialog(
        context: Context, button: Button, datePickerDialog: DatePickerDialog
    ) {
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
         * @return A new instance of fragment OriginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) = OriginFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
            }
        }
    }
}