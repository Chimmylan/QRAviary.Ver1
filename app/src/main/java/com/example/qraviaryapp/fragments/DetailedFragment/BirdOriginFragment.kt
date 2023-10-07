package com.example.qraviaryapp.fragments.DetailedFragment

import BirdData
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.detailedactivities.BirdsDetailedActivity
import com.example.qraviaryapp.adapter.DetailedAdapter.BirdSiblingsAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BirdOriginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BirdOriginFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var birdMotherStatus: String? = null
    private var birdFatherStatus: String? = null
    private var birdMotherLegband: String? = null
    private var birdFatherLegband: String? = null
    private var birdFatherMutation: String? = null
    private var birdMotherMutation: String? = null
    private var birdMotherCage: String? = null
    private var birdFatherCage: String? = null
    private var birdFatherKey: String? = null
    private var birdMotherKey: String? = null

    private var birdKey: String? = null
    private var fromFlightAdapter: Boolean = false
    private var fromNurseryAdapter: Boolean = false


    private lateinit var recyclerView: RecyclerView
    private lateinit var db: DatabaseReference
    private lateinit var dataList: ArrayList<BirdData>
    private lateinit var adapter: BirdSiblingsAdapter
    //++===++//

    private lateinit var tvFatherId: TextView
    private lateinit var tvMotherId: TextView
    private lateinit var tvMotherStatus: TextView
    private lateinit var tvFatherStatus: TextView
    private lateinit var tvMotherLegband: TextView
    private lateinit var tvFatherLegband: TextView
    private lateinit var tvMotherMutation: TextView
    private lateinit var tvFatherMutation: TextView
    private lateinit var tvMotherCage: TextView
    private lateinit var tvFatherCage: TextView
    private lateinit var tvSiblings: TextView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var dbase: DatabaseReference
    private lateinit var parentlinear: LinearLayout
    private lateinit var fatherLinearLayout: LinearLayout
    private lateinit var motherLinearLayout: LinearLayout
    private lateinit var snackbar: Snackbar
    private lateinit var connectivityManager: ConnectivityManager
    private var isNetworkAvailable = true
    private lateinit var layoutnofound: LinearLayout

    private lateinit var ParentRef: DatabaseReference
    private lateinit var cageKey: String
    private var nurseryCageValue: String? = null
    private var nofoundparent = false
    private var nofoundsiblings = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_bird_origin, container, false)
        mAuth = FirebaseAuth.getInstance()
        recyclerView = view.findViewById(R.id.RecyclerView)
        val gridLayoutManager = GridLayoutManager(requireContext(), 1)
        recyclerView.layoutManager = gridLayoutManager
        dataList = ArrayList()
        adapter = BirdSiblingsAdapter(requireContext(), dataList)
        recyclerView.adapter = adapter

        tvSiblings = view.findViewById(R.id.tvSiblings)
        tvFatherId = view.findViewById(R.id.tvFatherIdentifier)
        tvMotherId = view.findViewById(R.id.tvMotherIdentifier)
        tvFatherLegband = view.findViewById(R.id.tvFatherLegband)
        tvMotherLegband = view.findViewById(R.id.tvMotherLegband)
        tvFatherCage = view.findViewById(R.id.tvFatherCage)
        tvMotherCage = view.findViewById(R.id.tvMotherCage)
        tvFatherMutation = view.findViewById(R.id.tvFatherMutation)
        tvMotherMutation = view.findViewById(R.id.tvMotherMutation)
        tvFatherStatus = view.findViewById(R.id.tvFatherStatus)
        tvMotherStatus = view.findViewById(R.id.tvMotherStatus)
        parentlinear = view.findViewById(R.id.ParentLinearLayout)
        fatherLinearLayout = view.findViewById(R.id.fatherLayout)
        motherLinearLayout = view.findViewById(R.id.motherLayout)
        layoutnofound = view.findViewById(R.id.layouttvnofound)
        mAuth = FirebaseAuth.getInstance()
        dbase = FirebaseDatabase.getInstance().reference

        val currenUserId = mAuth.currentUser?.uid

        birdFatherKey = arguments?.getString("BirdFatherKey")
        birdMotherKey = arguments?.getString("BirdMotherKey")

        cageKey = arguments?.getString("CageKey").toString()

        fromFlightAdapter = arguments?.getBoolean("fromFlightListAdapter") == true
        fromNurseryAdapter = arguments?.getBoolean("fromNurseryListAdapter") == true
        birdKey = arguments?.getString("BirdKey")

        Log.d(TAG, "FatherKey ORIGIN: ${birdFatherKey.toString()}")
        Log.d(TAG, "MotherKey ORIGIN: ${birdMotherKey.toString()}")
        Log.d(TAG, "birdKey ORIGIN: ${birdKey.toString()}")
        Log.d(TAG, "From flight: ${fromFlightAdapter}")
        Log.d(TAG, "From Nursery: ${fromNurseryAdapter}")
        Log.d(TAG, "birdCageKey ORIGIN: ${cageKey}")


        val fatherRef =
            dbase.child("Users").child("ID: ${currenUserId.toString()}").child("Flight Birds")
                .child(birdFatherKey.toString())
        if (fromFlightAdapter) {
//FlightCage
            ParentRef =
                dbase.child("Users").child("ID: ${currenUserId.toString()}").child("Cages").child("Flight Cages")
                    .child(cageKey.toString()).child("Birds").child(birdKey.toString()).child("Parents")
        }else if(fromNurseryAdapter){
            ParentRef =
                dbase.child("Users").child("ID: ${currenUserId.toString()}").child("Cages").child("Nursery Cages")
                    .child(cageKey).child("Birds").child(birdKey.toString()).child("Parents")
        }
        else {
            ParentRef = dbase.child("Users").child("ID: ${currenUserId.toString()}").child("Birds")
                .child(birdKey.toString()).child("Parents")
        }

        val motherRef =
            dbase.child("Users").child("ID: ${currenUserId.toString()}").child("Flight Birds")
                .child(birdMotherKey.toString())

        var fatherIdValue = ""
        var fatherLegbandValue = ""
        var fatherMutation1Value = ""
        var fatherMutation2Value = ""
        var fatherMutation3Value = ""
        var fatherMutation4Value = ""
        var fatherMutation5Value = ""
        var fatherMutation6Value = ""
        var fatherCageValue = ""
        var fatherStatusValue = ""
        var fatherGender = ""
        var fatherDateBirth = ""
        var fatherSalePrice = ""
        var fatherBuyer = ""
        var fatherDeathReason = ""
        var fatherExchangeReason = ""
        var fatherExchangeWith = ""
        var fatherLostDetails = ""
        var fatherBirdAvailCage = ""
        var fatherForsaleCage = ""
        var fatherRequestedPrice = ""
        var fatherBuyPrice = ""
        var fatherBoughtOn = ""
        var fatherBoughtBreeder = ""
        var fatherSoldDate = ""
        var fatherDeceaseDate = ""
        var fatherExchangeDate = ""
        var fatherLostDate = ""
        var fatherDonatedDate = ""
        var fatherDonatedContact = ""
        var fatherbirdFather = ""
        var fatherbirdMother = ""
        var fatherbirdFatherKey = ""
        var fatherbirdMotherKey = ""
        var fathermainPic = ""
        var newbirdFatherKey = ""
        var birdFatherKey = ""
        fatherRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    fatherIdValue = snapshot.child("Identifier").value.toString()
                    fatherLegbandValue = snapshot.child("Legband").value.toString()
                    fatherMutation1Value =
                        snapshot.child("Mutation1").child("Mutation Name").value.toString()
                    fatherMutation2Value =
                        snapshot.child("Mutation2").child("Mutation Name").value.toString()
                    fatherMutation3Value =
                        snapshot.child("Mutation3").child("Mutation Name").value.toString()
                    fatherMutation4Value =
                        snapshot.child("Mutation4").child("Mutation Name").value.toString()
                    fatherMutation5Value =
                        snapshot.child("Mutation5").child("Mutation Name").value.toString()
                    fatherMutation6Value =
                        snapshot.child("Mutation6").child("Mutation Name").value.toString()
                    fatherCageValue = snapshot.child("Cage").value.toString()
                    fatherStatusValue = snapshot.child("Status").value.toString()
                    newbirdFatherKey = snapshot.child("Flight Key").value.toString()
                    birdFatherKey = snapshot.child("Bird Key").value.toString()

                    Log.d(TAG, "newBirdFatherKey: $newbirdFatherKey")

                    var fatherImgValue = snapshot.child("Gallery")
                    fathermainPic = fatherImgValue.children.firstOrNull()?.value.toString()

                    fatherGender = snapshot.child("Gender").value.toString()
                    fatherDateBirth = snapshot.child("Date of Birth").value.toString()
                    fatherSalePrice = snapshot.child("Sale Price").value.toString()
                    fatherBuyer = snapshot.child("Other Breeder Contact").value.toString()
                    fatherDeathReason = snapshot.child("Death Reason").value.toString()
                    fatherExchangeReason = snapshot.child("Exchange Reason").value.toString()
                    fatherExchangeWith = snapshot.child("Exchange Contact").value.toString()
                    fatherLostDetails = snapshot.child("Lost Details").value.toString()
                    fatherBirdAvailCage = snapshot.child("Cage").value.toString()
                    fatherForsaleCage = snapshot.child("Cage").value.toString()
                    fatherRequestedPrice = snapshot.child("Requested Price").value.toString()
                    fatherBuyPrice = snapshot.child("Buy Price").value.toString()
                    fatherBoughtOn = snapshot.child("Bought Date").value.toString()
                    fatherBoughtBreeder = snapshot.child("Breeder Contact").value.toString()
                    fatherSoldDate = snapshot.child("Sold Date").value.toString()
                    fatherDeceaseDate = snapshot.child("Death Date").value.toString()
                    fatherExchangeDate = snapshot.child("Exchange Date").value.toString()
                    fatherLostDate = snapshot.child("Lost Date").value.toString()
                    fatherDonatedDate = snapshot.child("Donated Date").value.toString()
                    fatherDonatedContact = snapshot.child("Donated Contact").value.toString()
                    fatherbirdFather = snapshot.child("Parents").child("Father").value.toString()
                    fatherbirdMother = snapshot.child("Parents").child("Mother").value.toString()
                    fatherbirdFatherKey =
                        snapshot.child("Parents").child("FatherKey").value.toString()
                    fatherbirdMotherKey =
                        snapshot.child("Parents").child("MotherKey").value.toString()



                    Log.d(TAG, fatherIdValue)

                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        var motherIdValue = ""
        var motherLegbandValue = ""
        var motherMutation1Value = ""
        var motherMutation2Value = ""
        var motherMutation3Value = ""
        var motherMutation4Value = ""
        var motherMutation5Value = ""
        var motherMutation6Value = ""
        var motherCageValue = ""
        var motherStatusValue = ""
        var motherGender = ""
        var motherDateBirth = ""
        var motherSalePrice = ""
        var motherBuyer = ""
        var motherDeathReason = ""
        var motherExchangeReason = ""
        var motherExchangeWith = ""
        var motherLostDetails = ""
        var motherBirdAvailCage = ""
        var motherForsaleCage = ""
        var motherRequestedPrice = ""
        var motherBuyPrice = ""
        var motherBoughtOn = ""
        var motherBoughtBreeder = ""
        var motherSoldDate = ""
        var motherDeceaseDate = ""
        var motherExchangeDate = ""
        var motherLostDate = ""
        var motherDonatedDate = ""
        var motherDonatedContact = ""
        var motherbirdFather = ""
        var motherbirdMother = ""
        var motherbirdFatherKey = ""
        var motherbirdMotherKey = ""
        var mothermainPic = ""
        var newbirdMotherKey = ""
        var birdMotherKey = ""
        motherRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    motherIdValue = snapshot.child("Identifier").value.toString()
                    motherLegbandValue = snapshot.child("Legband").value.toString()
                    motherMutation1Value =
                        snapshot.child("Mutation1").child("Mutation Name").value.toString()
                    motherMutation2Value =
                        snapshot.child("Mutation2").child("Mutation Name").value.toString()
                    motherMutation3Value =
                        snapshot.child("Mutation3").child("Mutation Name").value.toString()
                    motherMutation4Value =
                        snapshot.child("Mutation4").child("Mutation Name").value.toString()
                    motherMutation5Value =
                        snapshot.child("Mutation5").child("Mutation Name").value.toString()
                    motherMutation6Value =
                        snapshot.child("Mutation6").child("Mutation Name").value.toString()
                    newbirdMotherKey = snapshot.child("Flight Key").value.toString()
                    birdMotherKey = snapshot.child("Bird Key").value.toString()
                    motherCageValue = snapshot.child("Cage").value.toString()
                    motherStatusValue = snapshot.child("Status").value.toString()
                    var motherImgValue = snapshot.child("Gallery")
                    mothermainPic = motherImgValue.children.firstOrNull()?.value.toString()
                    motherGender = snapshot.child("Gender").value.toString()
                    motherDateBirth = snapshot.child("Date of Birth").value.toString()
                    motherSalePrice = snapshot.child("Sale Price").value.toString()
                    motherBuyer = snapshot.child("Other Breeder Contact").value.toString()
                    motherDeathReason = snapshot.child("Death Reason").value.toString()
                    motherExchangeReason = snapshot.child("Exchange Reason").value.toString()
                    motherExchangeWith = snapshot.child("Exchange Contact").value.toString()
                    motherLostDetails = snapshot.child("Lost Details").value.toString()
                    motherBirdAvailCage = snapshot.child("Cage").value.toString()
                    motherForsaleCage = snapshot.child("Cage").value.toString()
                    motherRequestedPrice = snapshot.child("Requested Price").value.toString()
                    motherBuyPrice = snapshot.child("Buy Price").value.toString()
                    motherBoughtOn = snapshot.child("Bought Date").value.toString()
                    motherBoughtBreeder = snapshot.child("Breeder Contact").value.toString()
                    motherSoldDate = snapshot.child("Sold Date").value.toString()
                    motherDeceaseDate = snapshot.child("Death Date").value.toString()
                    motherExchangeDate = snapshot.child("Exchange Date").value.toString()
                    motherLostDate = snapshot.child("Lost Date").value.toString()
                    motherDonatedDate = snapshot.child("Donated Date").value.toString()
                    motherDonatedContact = snapshot.child("Donated Contact").value.toString()
                    motherbirdFather = snapshot.child("Parents").child("Father").value.toString()
                    motherbirdMother = snapshot.child("Parents").child("Mother").value.toString()
                    motherbirdFatherKey =
                        snapshot.child("Parents").child("FatherKey").value.toString()
                    motherbirdMotherKey =
                        snapshot.child("Parents").child("MotherKey").value.toString()


                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        ParentRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val father = snapshot.child("Father").value.toString()
                    val mother = snapshot.child("Mother").value.toString()

                    if (father.isNotEmpty() && mother.isNotEmpty()) {
                        Log.d(TAG, "IF!!")
                        if (motherCageValue.isNullOrEmpty()) {
                            tvMotherCage.visibility = GONE
                        } else {
                            tvMotherCage.text = motherCageValue
                        }
                        if (motherLegbandValue.isNullOrEmpty()) {
                            tvMotherLegband.visibility = GONE
                        } else {
                            tvMotherLegband.text = motherLegbandValue
                        }

                        val fatherMutations = listOf(
                            motherMutation1Value,
                            motherMutation2Value,
                            motherMutation3Value,
                            motherMutation4Value,
                            motherMutation5Value,
                            motherMutation6Value
                        ).filter { !it.isNullOrBlank() }
                        val fatherNonNullMutations = mutableListOf<String>()
                        for (mutation in fatherMutations) {
                            if (mutation != "null") {
                                fatherNonNullMutations.add(mutation)
                            }
                        }
                        val fatherCombinedMutations = if (fatherNonNullMutations.isNotEmpty()) {
                            fatherNonNullMutations.joinToString(" x ")

                        } else {
                            "Mutation: None"
                        }
                        Log.d(TAG, "mutation:$fatherCombinedMutations ")
                        tvFatherMutation.text = fatherCombinedMutations

                        tvMotherStatus.text = motherStatusValue
                        tvMotherId.text = motherIdValue

                        tvFatherId.text = fatherIdValue
                        tvFatherStatus.text = fatherStatusValue

                        if (fatherCageValue.isNullOrEmpty()) {
                            tvFatherCage.visibility = GONE
                        } else {
                            tvFatherCage.text = fatherCageValue
                        }
                        if (fatherLegbandValue.isNullOrEmpty()) {
                            tvFatherLegband.visibility = GONE
                        } else {
                            tvFatherLegband.text = fatherLegbandValue
                        }
                        val motherMutations = listOf(
                            fatherMutation1Value,
                            fatherMutation2Value,
                            fatherMutation3Value,
                            fatherMutation4Value,
                            fatherMutation5Value,
                            fatherMutation6Value
                        ).filter { !it.isNullOrBlank() }

                        val motherNonNullMutations = mutableListOf<String>()
                        for (mutation in motherMutations) {
                            if (mutation != "null") {
                                motherNonNullMutations.add(mutation)
                            }
                        }
                        val motherCombinedMutations = if (motherNonNullMutations.isNotEmpty()) {
                            motherNonNullMutations.joinToString(" x ")

                        } else {
                            "Mutation: None"
                        }
                        Log.d(TAG, "mutation:$motherCombinedMutations ")
                        tvMotherMutation.text = motherCombinedMutations


                    }
                    if (father == "None" && mother.isNotEmpty()) {
                        fatherLinearLayout.visibility = GONE
                        tvMotherId.text = motherIdValue
                        tvMotherStatus.text = motherStatusValue
                        if (motherCageValue.isNullOrEmpty()) {
                            tvMotherCage.visibility = GONE
                        } else {
                            tvMotherCage.text = motherCageValue
                        }
                        if (motherLegbandValue.isNullOrEmpty()) {
                            tvMotherLegband.visibility = GONE
                        } else {
                            tvMotherLegband.text = motherLegbandValue
                        }

                        val mutations = listOf(
                            motherMutation1Value,
                            motherMutation2Value,
                            motherMutation3Value,
                            motherMutation4Value,
                            motherMutation5Value,
                            motherMutation6Value
                        ).filter { !it.isNullOrBlank() }
                        val nonNullMutations = mutableListOf<String>()
                        for (mutation in mutations) {
                            if (mutation != "null") {
                                nonNullMutations.add(mutation)
                            }
                        }
                        val combinedMutations = if (nonNullMutations.isNotEmpty()) {
                            nonNullMutations.joinToString(" x ")

                        } else {
                            "Mutation: None"
                        }
                        Log.d(TAG, "mutation:$combinedMutations ")
                        tvFatherMutation.text = combinedMutations

                    }
                    if (father.isNotEmpty() && mother == "None") {
                        motherLinearLayout.visibility = GONE

                        tvFatherId.text = fatherIdValue
                        tvFatherStatus.text = fatherStatusValue

                        if (fatherCageValue.isNullOrEmpty()) {
                            tvFatherCage.visibility = GONE
                        } else {
                            tvFatherCage.text = fatherCageValue
                        }
                        if (fatherLegbandValue.isNullOrEmpty()) {
                            tvFatherLegband.visibility = GONE
                        } else {
                            tvFatherLegband.text = fatherLegbandValue
                        }
                        val mutations = listOf(
                            fatherMutation1Value,
                            fatherMutation2Value,
                            fatherMutation3Value,
                            fatherMutation4Value,
                            fatherMutation5Value,
                            fatherMutation6Value
                        ).filter { !it.isNullOrBlank() }

                        val nonNullMutations = mutableListOf<String>()
                        for (mutation in mutations) {
                            if (mutation != "null") {
                                nonNullMutations.add(mutation)
                            }
                        }
                        val combinedMutations = if (nonNullMutations.isNotEmpty()) {
                            nonNullMutations.joinToString(" x ")

                        } else {
                            "Mutation: None"
                        }
                        Log.d(TAG, "mutation:$combinedMutations ")
                        tvFatherMutation.text = combinedMutations
                    }
                    if (father == "None" && mother == "None") {
                        parentlinear.visibility = GONE
                        nofoundparent = true
                        Log.d(TAG, "ELSE!")
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


        fatherLinearLayout.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("BirdId", fatherIdValue)
            bundle.putString("FlightKey", newbirdFatherKey)
            bundle.putString("BirdKey", birdFatherKey)
            bundle.putString("BirdLegband", fatherLegbandValue)
            bundle.putString("BirdImage", fathermainPic)
            bundle.putString("BirdGender", fatherGender)
            bundle.putString("BirdStatus", fatherStatusValue)
            bundle.putString("BirdDateBirth", fatherDateBirth)
            bundle.putString("BirdSalePrice", fatherSalePrice)
            bundle.putString("BirdBuyer", fatherBuyer)
            bundle.putString("BirdDeathReason", fatherDeathReason)
            bundle.putString("BirdExchangeReason", fatherExchangeReason)
            bundle.putString("BirdExchangeWith", fatherExchangeWith)
            bundle.putString("BirdLostDetails", fatherLostDetails)
            bundle.putString("BirdAvailCage", fatherBirdAvailCage)
            bundle.putString("BirdForsaleCage", fatherForsaleCage)
            bundle.putString("BirdRequestedPrice", fatherRequestedPrice)
            bundle.putString("BirdBuyPrice", fatherBuyPrice)
            bundle.putString("BirdBoughtOn", fatherBoughtOn)
            bundle.putString("BirdBoughtBreeder", fatherBoughtBreeder)
            bundle.putString("BirdSoldDate", fatherSoldDate)
            bundle.putString("BirdDeceaseDate", fatherDeceaseDate)
            bundle.putString("BirdExchangeDate", fatherExchangeDate)
            bundle.putString("BirdLostDate", fatherLostDate)
            bundle.putString("BirdDonatedDate", fatherDonatedDate)
            bundle.putString("BirdDonatedContact", fatherDonatedContact)
            bundle.putString("BirdFather", fatherbirdFather)
            bundle.putString("BirdFatherKey", fatherbirdFatherKey)
            bundle.putString("BirdMother", fatherbirdMother)
            bundle.putString("BirdMotherKey", fatherbirdMotherKey)
            bundle.putString("BirdMutation1", fatherMutation1Value)
            bundle.putString("BirdMutation2", fatherMutation2Value)
            bundle.putString("BirdMutation3", fatherMutation3Value)
            bundle.putString("BirdMutation4", fatherMutation4Value)
            bundle.putString("BirdMutation5", fatherMutation5Value)
            bundle.putString("BirdMutation6", fatherMutation6Value)

            val i = Intent(fatherLinearLayout.context, BirdsDetailedActivity::class.java)

            i.putExtras(bundle)

            fatherLinearLayout.context.startActivity(i)
        }
        motherLinearLayout.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("BirdId", motherIdValue)
            bundle.putString("BirdKey", birdMotherKey)
            bundle.putString("FlightKey", newbirdMotherKey)
            bundle.putString("BirdLegband", motherLegbandValue)
            bundle.putString("BirdImage", mothermainPic)
            bundle.putString("BirdGender", motherGender)
            bundle.putString("BirdStatus", motherStatusValue)
            bundle.putString("BirdDateBirth", motherDateBirth)
            bundle.putString("BirdSalePrice", motherSalePrice)
            bundle.putString("BirdBuyer", motherBuyer)
            bundle.putString("BirdDeathReason", motherDeathReason)
            bundle.putString("BirdExchangeReason", motherExchangeReason)
            bundle.putString("BirdExchangeWith", motherExchangeWith)
            bundle.putString("BirdLostDetails", motherLostDetails)
            bundle.putString("BirdAvailCage", motherBirdAvailCage)
            bundle.putString("BirdForsaleCage", motherForsaleCage)
            bundle.putString("BirdRequestedPrice", motherRequestedPrice)
            bundle.putString("BirdBuyPrice", motherBuyPrice)
            bundle.putString("BirdBoughtOn", motherBoughtOn)
            bundle.putString("BirdBoughtBreeder", motherBoughtBreeder)
            bundle.putString("BirdSoldDate", motherSoldDate)
            bundle.putString("BirdDeceaseDate", motherDeceaseDate)
            bundle.putString("BirdExchangeDate", motherExchangeDate)
            bundle.putString("BirdLostDate", motherLostDate)
            bundle.putString("BirdDonatedDate", motherDonatedDate)
            bundle.putString("BirdDonatedContact", motherDonatedContact)
            bundle.putString("BirdFather", motherbirdFather)
            bundle.putString("BirdFatherKey", motherbirdFatherKey)
            bundle.putString("BirdMother", motherbirdMother)
            bundle.putString("BirdMotherKey", motherbirdMotherKey)
            bundle.putString("BirdMutation1", motherMutation1Value)
            bundle.putString("BirdMutation2", motherMutation2Value)
            bundle.putString("BirdMutation3", motherMutation3Value)
            bundle.putString("BirdMutation4", motherMutation4Value)
            bundle.putString("BirdMutation5", motherMutation5Value)
            bundle.putString("BirdMutation6", motherMutation6Value)


            val i = Intent(motherLinearLayout.context, BirdsDetailedActivity::class.java)
            i.putExtras(bundle)
            motherLinearLayout.context.startActivity(i)
        }

        lifecycleScope.launch {
            try {
                val data = getDataFromDatabase { data ->
                    dataList.clear()
                    dataList.addAll(data)
                    adapter.notifyDataSetChanged()
                    Log.d(TAG,"$nofoundparent+$nofoundsiblings")
//                    layoutnofound.visibility = VISIBLE
                    if (nofoundsiblings == true && nofoundparent == true){
                        layoutnofound.visibility = VISIBLE
                        Log.d(TAG,"$nofoundparent+$nofoundsiblings")
                    }
                    else{
                        layoutnofound.visibility = GONE
                    }
                }

            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error retrieving data: ${e.message}")
            }
        }


        return view
    }

    private var fatherkey: String? = null
    private var motherkey: String? = null
    private suspend fun getDataFromDatabase(callback: (List<BirdData>) -> Unit) {
        val currentUserId = mAuth.currentUser?.uid
        Log.d(TAG, "Siblings Bird Key $birdKey")
        val db = FirebaseDatabase.getInstance().getReference("Users")
            .child("ID: ${currentUserId.toString()}").child("Birds").child(birdKey.toString())
            .child("Parents")
        val siblingRef = FirebaseDatabase.getInstance().getReference("Users")
            .child("ID: ${currentUserId.toString()}").child("Birds")
        val newSiblingsRef = siblingRef.get().await()

        db.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                fatherkey = snapshot.child("FatherKey").value.toString()
                motherkey = snapshot.child("MotherKey").value.toString()
                val dataList = ArrayList<BirdData>()

                for (itemSnapshot in newSiblingsRef.children) {
                    val data = itemSnapshot.getValue(BirdData::class.java)
                    val gallery = itemSnapshot.child("Gallery")
                    if (data != null) {

                        val siblingBirdKey = itemSnapshot.child("Bird Key").value.toString()
                        val siblingFlightKey = itemSnapshot.child("Flight Key").value.toString()
                        val siblingBirdLegband = itemSnapshot.child("Legband").value.toString()
                        val siblingIdentifier = itemSnapshot.child("Identifier").value.toString()
                        val siblingGender = itemSnapshot.child("Gender").value.toString()
                        val siblingMutation1Value = if (itemSnapshot.hasChild("Mutation1")) {
                            itemSnapshot.child("Mutation1").child("Mutation Name").value.toString()
                        } else {
                            ""
                        }
                        val siblingMutation2Value = if (itemSnapshot.hasChild("Mutation2")) {
                            itemSnapshot.child("Mutation2").child("Mutation Name").value.toString()
                        } else {
                            ""
                        }
                        val siblingMutation3Value = if (itemSnapshot.hasChild("Mutation3")) {
                            itemSnapshot.child("Mutation3").child("Mutation Name").value.toString()
                        } else {
                            ""
                        }
                        val siblingMutation4Value = if (itemSnapshot.hasChild("Mutation4")) {
                            itemSnapshot.child("Mutation4").child("Mutation Name").value.toString()
                        } else {
                            ""
                        }
                        val siblingMutation5Value = if (itemSnapshot.hasChild("Mutation5")) {
                            itemSnapshot.child("Mutation5").child("Mutation Name").value.toString()
                        } else {
                            ""
                        }
                        val siblingMutation6Value = if (itemSnapshot.hasChild("Mutation6")) {
                            itemSnapshot.child("Mutation6").child("Mutation Name").value.toString()
                        } else {
                            ""
                        }
                        val dateOfBandingValue =
                            itemSnapshot.child("Date of Banding").value.toString()
                        val dateOfBirthValue = itemSnapshot.child("Date of Birth").value.toString()
                        val statusValue = itemSnapshot.child("Status").value.toString()
                        val availCageValue = itemSnapshot.child("Cage").value.toString()
                        val forSaleCageValue = itemSnapshot.child("Cage").value.toString()
                        val forSaleRequestedPriceValue =
                            itemSnapshot.child("Requested Price").value.toString()
                        val soldDateValue = itemSnapshot.child("Sold Date").value.toString()
                        val soldPriceValue = itemSnapshot.child("Sale Price").value.toString()
                        val soldContactValue = itemSnapshot.child("Sale Contact").value.toString()
                        val deathDateValue = itemSnapshot.child("Death Date").value.toString()
                        val deathReasonValue = itemSnapshot.child("Death Reason").value.toString()
                        val exDateValue = itemSnapshot.child("Exchange Date").value.toString()
                        val exReasonValue = itemSnapshot.child("Exchange Reason").value.toString()
                        val exContactValue = itemSnapshot.child("Exchange Contact").value.toString()
                        val lostDateValue = itemSnapshot.child("Lost Date").value.toString()
                        val lostDetailsValue = itemSnapshot.child("Lost Details").value.toString()
                        val donatedDateValue = itemSnapshot.child("Donated Date").value.toString()
                        val donatedContactValue =
                            itemSnapshot.child("Donated Contact").value.toString()
                        val otherCommentsValue = itemSnapshot.child("Comments").value.toString()
                        val buyPriceValue = itemSnapshot.child("Buy Price").value.toString()
                        val boughtDateValue = itemSnapshot.child("Bought Date").value.toString()
                        val breederContactValue =
                            itemSnapshot.child("Breeder Contact").value.toString()
                        val FatherValue =
                            itemSnapshot.child("Parents").child("Father").value.toString()
                        val MotherValue =
                            itemSnapshot.child("Parents").child("Mother").value.toString()
                        val parentRef = itemSnapshot.child("Parents")
                        val siblingsFatherKey = parentRef.child("FatherKey")
                        val siblingsFatherKeyValue = siblingsFatherKey.value.toString()
                        val siblingsMotherKey = parentRef.child("MotherKey")
                        val siblingsMotherKeyValue = siblingsMotherKey.value.toString()
                        data.identifier = siblingIdentifier
                        data.flightKey = siblingFlightKey
                        data.birdKey = siblingBirdKey
                        data.legband = siblingBirdLegband
                        data.gender = siblingGender
                        data.mutation1 = siblingMutation1Value
                        data.mutation2 = siblingMutation2Value
                        data.mutation3 = siblingMutation3Value
                        data.mutation4 = siblingMutation4Value
                        data.mutation5 = siblingMutation5Value
                        data.mutation6 = siblingMutation6Value
                        data.dateOfBanding = dateOfBandingValue
                        data.dateOfBirth = dateOfBirthValue
                        data.status = statusValue
                        data.availCage = availCageValue
                        data.forSaleCage = forSaleCageValue
                        data.reqPrice = forSaleRequestedPriceValue
                        data.soldDate = soldDateValue
                        data.soldPrice = soldPriceValue
                        data.saleContact = soldContactValue
                        data.deathDate = deathDateValue
                        data.deathReason = deathReasonValue
                        data.exDate = exDateValue
                        data.exReason = exReasonValue
                        data.exContact = exContactValue
                        data.lostDate = lostDateValue
                        data.lostDetails = lostDetailsValue
                        data.donatedDate = donatedDateValue
                        data.donatedContact = donatedContactValue
                        data.otherComments = otherCommentsValue
                        data.buyPrice = buyPriceValue
                        data.boughtDate = boughtDateValue
                        data.breederContact = breederContactValue
                        data.father = FatherValue
                        data.mother = MotherValue
                        data.fatherKey = fatherkey
                        data.motherKey = motherkey

                        if (birdKey == itemSnapshot.key) {
                            continue
                        }
                        Log.d(TAG, "loop $fatherkey")
                        Log.d(TAG, "loop $motherkey")

                        if (siblingsFatherKey.exists() && siblingsMotherKey.exists()) {
                            // Both parents exist for the sibling
                            if (fatherkey == siblingsFatherKeyValue && motherkey == siblingsMotherKeyValue) {
                                // Sibling has the same father and mother as the selected bird
                                Log.d(TAG, "Both parents!")
                                val identifier = itemSnapshot.child("Identifier").value.toString()
                                data.identifier = identifier
                                tvSiblings.visibility = View.VISIBLE
                                dataList.add(data)
                            } else if (fatherkey != siblingsFatherKeyValue && motherkey == siblingsMotherKeyValue) {
                                // Sibling has the same father and mother as the selected bird
                                Log.d(TAG, "Both parents!")
                                val identifier = itemSnapshot.child("Identifier").value.toString()
                                data.identifier = identifier
                                tvSiblings.visibility = View.VISIBLE
                                dataList.add(data)
                            }
                            if (fatherkey == siblingsFatherKeyValue && motherkey != siblingsMotherKeyValue) {
                                // Sibling has the same father and mother as the selected bird
                                Log.d(TAG, "Both parents!")
                                val identifier = itemSnapshot.child("Identifier").value.toString()
                                data.identifier = identifier
                                tvSiblings.visibility = View.VISIBLE
                                dataList.add(data)
                            }
                        } else if (siblingsFatherKey.exists() && !siblingsMotherKey.exists()) {
                            // Only the father exists for the sibling
                            if (fatherkey == siblingsFatherKeyValue && motherkey != siblingsMotherKeyValue) {
                                // Sibling has the same father but different mother
                                Log.d(TAG, "Father Only!")
                                val identifier = itemSnapshot.child("Identifier").value.toString()
                                tvSiblings.visibility = VISIBLE
                                data.identifier = identifier
                                dataList.add(data)
                            }
                        } else if (siblingsMotherKey.exists() && !siblingsFatherKey.exists()) {
                            // Only the mother exists for the sibling
                            if (fatherkey != siblingsFatherKeyValue && motherkey == siblingsMotherKeyValue) {
                                // Sibling has the same mother but different father
                                Log.d(TAG, "Mother Only!")
                                val identifier = itemSnapshot.child("Identifier").value.toString()
                                data.identifier = identifier
                                dataList.add(data)
                                tvSiblings.visibility = VISIBLE
                            }
                        } else {
                               nofoundsiblings = true
                        }


                    }
                }
                Log.d(TAG, dataList.count().toString())

                // Invoke the callback with the populated dataList
                callback(dataList)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BirdOriginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) = BirdOriginFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
            }
        }
    }
}