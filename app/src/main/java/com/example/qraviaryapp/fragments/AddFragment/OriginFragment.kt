package com.example.qraviaryapp.fragments.AddFragment

import BirdData
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.dashboards.FemaleBirdListActivity
import com.example.qraviaryapp.activities.dashboards.MaleBirdListActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OriginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OriginFragment : Fragment() {
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

    private var birdFatherKey: String? = null
    private var birdMotherKey: String? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 8) {
            if (resultCode == RESULT_OK) {
                val btnFatherValue: String = data?.getStringExtra("MaleBirdId").toString()
                val btnFatherMutationValue: String =
                    data?.getStringExtra("MaleBirdMutation").toString()
                birdFatherKey = data?.getStringExtra("MaleFlightKey").toString()
                btnFather.text = btnFatherValue
            }
        }
        if (requestCode == 9) {
            if (resultCode == RESULT_OK) {
                val btnMotherValue: String = data?.getStringExtra("FemaleBirdId").toString()
                val btnMotherMutationValue: String =
                    data?.getStringExtra("FemaleBirdMutation").toString()
                birdMotherKey = data?.getStringExtra("FemaleFlightKey").toString()
                btnMother.text = btnMotherValue
            }
        }

    }

    private fun getTextFromVisibleDatePicker(Button: Button, layout: View): String {
        return if (layout.visibility == View.VISIBLE) {
            Button.text.toString()
        } else {
            ""
        }
    }

    fun addOirigin(birdId: String, NurseryId: String, newBundle: Bundle) {
        val fragment = BasicFragment()
        //  val dataSpinnerFather = spinnerFather.selectedItem.toString()
        // val dataSpinnerMother = spinnerMother.selectedItem.toString()
        val dataSelectedProvenence: RadioButton
        val dataBreederContact = etBreederContact.text.toString()
        val dataBoughtPrice = etBuyPrice.text.toString()
        val dataBreederOtContact = etOtBreederContact.text.toString()
        val dataProvenence: Int = radioGroup.checkedRadioButtonId
        val dataBoughtDate = getTextFromVisibleDatePicker(boughtDateBtn, boughtLayout)


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
        val birdMutation1 = newBundle.getString("BirdMutation1")
        val birdMutation2 = newBundle.getString("BirdMutation2")
        val birdMutation3 = newBundle.getString("BirdMutation3")
        val birdMutation4 = newBundle.getString("BirdMutation4")
        val birdMutation5 = newBundle.getString("BirdMutation5")
        val birdMutation6 = newBundle.getString("BirdMutation6")
        val fatherKey = newBundle.getString("BirdFatherKey")
        val motherKey = newBundle.getString("BirdMotherKey")
        val cageKeyValue = newBundle.getString("CageKeyValue")
        val cageBirdKey = newBundle.getString("CageBirdKeyValue")

        dataSelectedProvenence = view?.findViewById(dataProvenence)
            ?: throw IllegalStateException("RadioButton not found")

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



        if (!cageKeyValue.isNullOrEmpty()) {
            cageReference = cageKeyValue?.let {
                dbase.child("Users").child("ID: $userId").child("Cages")
                    .child("Nursery Cages").child(it).child("Birds").child(cageBirdKey.toString()).child("Parents")
            }!!
        }

        val birdRef = dbase.child("Users").child("ID: $userId").child("Birds").child(birdId)
        val relationshipRef =
            dbase.child("Users").child("ID: $userId").child("Birds").child(birdId).child("Parents")

        val nurseryRef =
            dbase.child("Users").child("ID: $userId").child("Nursery Birds").child(NurseryId)
        val nurseryRelationshipRef = nurseryRef.child("Parents")
        val purchasesRef = dbase.child("Users").child("ID: $userId").child("Purchase Items").push()
        val descendantsFatherRef = dbase.child("Users").child("ID: $userId").child("Flight Birds")
            .child(birdFatherKey.toString()).child("Descendants").push()
        val descendantMotherRef = dbase.child("Users").child("ID: $userId").child("Flight Birds")
            .child(birdMotherKey.toString()).child("Descendants").push()

        val motherRef = descendantMotherRef.child("Parents")
        val fatherRef = descendantsFatherRef.child("Parents")


        if (boughtLayout.visibility == View.VISIBLE) {

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
                    "Date Birth" to birdDateBirth,
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
                    "Bird Key" to birdId
                )

                purchasesRef.updateChildren(fatherRefdata)
                purchasesRef.updateChildren(descendantdata)
                fatherRef.updateChildren(fatherRefdata)


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
                    "Date Birth" to birdDateBirth,
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
                    "MotherKey" to birdMotherKey
                )

                purchasesRef.updateChildren(fatherRefdata)
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
                    "Date Birth" to birdDateBirth,
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
                    "MotherKey" to birdMotherKey
                )
                purchasesRef.updateChildren(motherRefdata)
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
                    "Date Birth" to birdDateBirth,
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
                    "MotherKey" to birdMotherKey
                )
                purchasesRef.updateChildren(motherRefdata)
                purchasesRef.updateChildren(descendantdata)
                motherRef.updateChildren(motherRefdata)
                fatherRef.updateChildren(motherRefdata)
                Log.d(TAG, "MotherRef!! and FatherRef!!")
            }

            val parentdata: Map<String, Any?> = hashMapOf(
                "Father" to birdData.father,
                "Mother" to birdData.mother,
                "FatherKey" to birdFatherKey,
                "MotherKey" to birdMotherKey
            )

            if (!cageKeyValue.isNullOrEmpty()){
                cageReference.updateChildren(parentdata)
            }

            relationshipRef.updateChildren(parentdata)
            nurseryRelationshipRef.updateChildren(parentdata)
            val data: Map<String, Any?> = hashMapOf(

                "Breeder Contact" to birdData.breederContact,
                "Buy Price" to birdData.buyPrice,
                "Bought Date" to birdData.boughtDate
            )
            birdRef.updateChildren(data)
            nurseryRef.updateChildren(data)
        } else if (otLayout.visibility == View.VISIBLE) {

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
                    "Date Birth" to birdDateBirth,
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
                    "Date Birth" to birdDateBirth,
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
                    "Date Birth" to birdDateBirth,
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
                "MotherKey" to birdMotherKey
            )

            if (!cageKeyValue.isNullOrEmpty()){
                cageReference.updateChildren(parentdata)
            }

            relationshipRef.updateChildren(parentdata)
            nurseryRelationshipRef.updateChildren(parentdata)

            val data: Map<String, Any?> = hashMapOf(

                "Other Breeder Contact" to birdData.otOtherContact
            )
            birdRef.updateChildren(data)
            nurseryRef.updateChildren(data)

        } else {

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
                    "Date Birth" to birdDateBirth,
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
                    "Date Birth" to birdDateBirth,
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
                    "Date Birth" to birdDateBirth,
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
                "MotherKey" to birdMotherKey
            )
            if (!cageKeyValue.isNullOrEmpty()){
                cageReference.updateChildren(data)
            }
            relationshipRef.updateChildren(data)
            nurseryRelationshipRef.updateChildren(data)


        }
    }


    fun addFlightOrigin(birdId: String, FlightId: String, newBundle: Bundle) {
        val fragment = BasicFragment()
        //  val dataSpinnerFather = spinnerFather.selectedItem.toString()
        // val dataSpinnerMother = spinnerMother.selectedItem.toString()
        val dataSelectedProvenence: RadioButton
        val dataBreederContact = etBreederContact.text.toString()
        val dataBoughtPrice = etBuyPrice.text.toString()
        val dataBreederOtContact = etOtBreederContact.text.toString()
        val dataProvenence: Int = radioGroup.checkedRadioButtonId
        val dataBoughtDate = getTextFromVisibleDatePicker(boughtDateBtn, boughtLayout)



        dataSelectedProvenence = view?.findViewById(dataProvenence)
            ?: throw IllegalStateException("RadioButton not found")

        birdData = BirdData(
            breederContact = dataBreederContact,
            provenance = dataSelectedProvenence.text.toString(),
            buyPrice = dataBoughtPrice,
            boughtDate = dataBoughtDate,
            otOtherContact = dataBreederOtContact,
            father = btnFather.text.toString(),
            mother = btnMother.text.toString()
        )



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
        val birdMutation1 = newBundle.getString("BirdMutation1")
        val birdMutation2 = newBundle.getString("BirdMutation2")
        val birdMutation3 = newBundle.getString("BirdMutation3")
        val birdMutation4 = newBundle.getString("BirdMutation4")
        val birdMutation5 = newBundle.getString("BirdMutation5")
        val birdMutation6 = newBundle.getString("BirdMutation6")
        val fatherKey = newBundle.getString("BirdFatherKey")
        val motherKey = newBundle.getString("BirdMotherKey")
        val cageKeyValue = newBundle.getString("CageKeyValue")
        val cageBirdKey = newBundle.getString("CageBirdKeyValue")

        Log.d(TAG, birdIdentifier.toString())
        val userId = mAuth.currentUser?.uid.toString()

        if (!cageKeyValue.isNullOrEmpty()) {
            cageReference = cageKeyValue?.let {
                dbase.child("Users").child("ID: $userId").child("Cages")
                    .child("Flight Cages").child(it).child("Birds").child(cageBirdKey.toString()).child("Parents")
            }!!
        }


        val birdRef = dbase.child("Users").child("ID: $userId").child("Birds").child(birdId)
        val relationshipRef =
            dbase.child("Users").child("ID: $userId").child("Birds").child(birdId).child("Parents")

        val nurseryRef =
            dbase.child("Users").child("ID: $userId").child("Flight Birds").child(FlightId)
        val nurseryRelationshipRef = nurseryRef.child("Parents")
//        val descendantsRef = dbase.child("Users").child("ID: $userId").child("Flight Birds")
//            .child(birdFatherKey.toString()).child("Descendants").push()
        val purchasesRef = dbase.child("Users").child("ID: $userId").child("Purchase Items").push()

        val descendantsFatherRef = dbase.child("Users").child("ID: $userId").child("Flight Birds")
            .child(birdFatherKey.toString()).child("Descendants").push()
        val descendantMotherRef = dbase.child("Users").child("ID: $userId").child("Flight Birds")
            .child(birdMotherKey.toString()).child("Descendants").push()
        val motherRef = descendantMotherRef.child("Parents")
        val fatherRef = descendantsFatherRef.child("Parents")

        if (boughtLayout.visibility == View.VISIBLE) {

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
                    "Date Birth" to birdDateBirth,
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
                    "Bird Key" to birdId
                )
                purchasesRef.updateChildren(fatherRefdata)
                purchasesRef.updateChildren(descendantdata)
                fatherRef.updateChildren(fatherRefdata)
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
                    "Date Birth" to birdDateBirth,
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
                    "Bird Key" to birdId
                )

                purchasesRef.updateChildren(fatherRefdata)
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
                    "Date Birth" to birdDateBirth,
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
                    "Bird Key" to birdId
                )
                purchasesRef.updateChildren(motherRefdata)
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
                    "Date Birth" to birdDateBirth,
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
                    "Bird Key" to birdId
                )
                purchasesRef.updateChildren(motherRefdata)
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
            )
            if (!cageKeyValue.isNullOrEmpty()){
                cageReference.updateChildren(parentdata)
            }
            relationshipRef.updateChildren(parentdata)
            nurseryRelationshipRef.updateChildren(parentdata)

            val data: Map<String, Any?> = hashMapOf(

                "Breeder Contact" to birdData.breederContact,
                "Buy Price" to birdData.buyPrice,
                "Bought Date" to birdData.boughtDate
            )
            birdRef.updateChildren(data)
            nurseryRef.updateChildren(data)
        } else if (otLayout.visibility == View.VISIBLE) {

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
                        "Date Birth" to birdDateBirth,
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
                        "Date Birth" to birdDateBirth,
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
                    "Date Birth" to birdDateBirth,
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
                "MotherKey" to birdMotherKey
            )
            if (!cageKeyValue.isNullOrEmpty()){
                cageReference.updateChildren(parentdata)
            }
            relationshipRef.updateChildren(parentdata)
            nurseryRelationshipRef.updateChildren(parentdata)

            val data: Map<String, Any?> = hashMapOf(

                "Other Breeder Contact" to birdData.otOtherContact
            )
            birdRef.updateChildren(data)
            nurseryRef.updateChildren(data)


        } else {
            val data: Map<String, Any?> = hashMapOf(
                "Father" to birdData.father,
                "Mother" to birdData.mother,
                "FatherKey" to birdFatherKey,
                "MotherKey" to birdMotherKey
            )
            if (!cageKeyValue.isNullOrEmpty()){
                cageReference.updateChildren(data)
            }
            relationshipRef.updateChildren(data)
            nurseryRelationshipRef.updateChildren(data)

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
                        "Date Birth" to birdDateBirth,
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
                        "Date Birth" to birdDateBirth,
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
                    "Date Birth" to birdDateBirth,
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
                    "Bird Key" to birdId
                )
                fatherRef.updateChildren(motherRefdata)
                motherRef.updateChildren(motherRefdata)
                Log.d(TAG, "MotherRef!! and FatherRef!!")
            }
        }
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