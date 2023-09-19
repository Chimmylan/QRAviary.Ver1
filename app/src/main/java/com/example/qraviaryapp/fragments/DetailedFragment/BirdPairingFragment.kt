package com.example.qraviaryapp.fragments.DetailedFragment

import BirdData
import PairData
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qraviaryapp.R
import com.example.qraviaryapp.adapter.DetailedAdapter.PairingAdapter
import com.example.qraviaryapp.fragments.DetailedFragment.ARG_PARAM1
import com.example.qraviaryapp.fragments.DetailedFragment.ARG_PARAM2
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BirdPairingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BirdPairingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var dataList: ArrayList<BirdData>
    private lateinit var adapter: PairingAdapter
    private var birdKey: String? = null
    private var flightKey: String? = null


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
        val view = inflater.inflate(R.layout.fragment_bird_pairing, container, false)
        birdKey = arguments?.getString("BirdKey")
        flightKey = arguments?.getString("FlightKey")
        mAuth = FirebaseAuth.getInstance()
        recyclerView = view.findViewById(R.id.RecyclerView)
        val gridLayoutManager = GridLayoutManager(requireContext(), 1)
        recyclerView.layoutManager = gridLayoutManager
        dataList = ArrayList()
        adapter = PairingAdapter(requireContext(), dataList)
        recyclerView.adapter = adapter

        lifecycleScope.launch {
            try {
                val data = getDataFromDatabase()
                dataList.clear()
                dataList.addAll(data)
                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, e.message.toString())
            }

        }
        return view
    }

    private suspend fun getDataFromDatabase(): List<BirdData> = withContext(Dispatchers.IO)
    {
        val currentUserId = mAuth.currentUser?.uid
        val db = FirebaseDatabase.getInstance().getReference("Users")
            .child("ID: ${currentUserId.toString()}").child("Birds")
            .child(birdKey.toString()).child("Pairs")
        val newRef = db.get().await()
        val dataList = ArrayList<BirdData>()

        if (newRef.exists()) {
            for (itemSnapshot in newRef.children) {
                val data = itemSnapshot.getValue(BirdData::class.java)

                var pairKey = itemSnapshot.child("Bird Key").value.toString()
                val Key = itemSnapshot.key
                if (data != null) {
                    data.birdKey = pairKey
                }




                val pairRef = FirebaseDatabase.getInstance().getReference("Users")
                    .child("ID: ${currentUserId.toString()}")
                    .child("Birds")
                    .child(pairKey)

                pairRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        if (snapshot.exists()) {
                            // Access specific values inside pairData
                            val gallery = snapshot.child("Gallery")

                            val pairIdentifier = snapshot.child("Identifier").value.toString()
                            val pairLegBand = snapshot.child("Legband").value.toString()
                            val pairGenderValue = snapshot.child("Gender").value.toString()
                            val mainPic = gallery.children.firstOrNull()?.value.toString()

                            val mutation1Value = if (snapshot.hasChild("Mutation1")) {
                                snapshot.child("Mutation1").value.toString()
                            } else {
                                ""
                            }
                            val mutation2Value = if (snapshot.hasChild("Mutation2")) {
                                snapshot.child("Mutation2").value.toString()
                            } else {
                                ""
                            }
                            val mutation3Value = if (snapshot.hasChild("Mutation3")) {
                                snapshot.child("Mutation3").value.toString()
                            } else {
                                ""
                            }
                            val mutation4Value = if (snapshot.hasChild("Mutation4")) {
                                snapshot.child("Mutation4").value.toString()
                            } else {
                                ""
                            }
                            val mutation5Value = if (snapshot.hasChild("Mutation5")) {
                                snapshot.child("Mutation5").value.toString()
                            } else {
                                ""
                            }
                            val mutation6Value = if (snapshot.hasChild("Mutation6")) {
                                snapshot.child("Mutation6").value.toString()
                            } else {
                                ""
                            }
                            val dateOfBandingValue = snapshot.child("Date of Banding").value
                            val dateOfBirthValue = snapshot.child("Date of Birth").value
                            val statusValue = snapshot.child("Status").value
                            val availCageValue = snapshot.child("Cage").value
                            val forSaleCageValue = snapshot.child("Cage").value
                            val forSaleRequestedPriceValue =
                                snapshot.child("Requested Price").value
                            val soldDateValue = snapshot.child("Sold Date").value
                            val soldPriceValue = snapshot.child("Sale Price").value
                            val soldContactValue = snapshot.child("Sale Contact").value
                            val deathDateValue = snapshot.child("Death Date").value
                            val deathReasonValue = snapshot.child("Death Reason").value
                            val exDateValue = snapshot.child("Exchange Date").value
                            val exReasonValue = snapshot.child("Exchange Reason").value
                            val exContactValue = snapshot.child("Exchange Contact").value
                            val lostDateValue = snapshot.child("Lost Date").value
                            val lostDetailsValue = snapshot.child("Lost Details").value
                            val donatedDateValue = snapshot.child("Donated Date").value
                            val donatedContactValue = snapshot.child("Donated Contact").value
                            val otherCommentsValue = snapshot.child("Comments").value
                            val buyPriceValue = snapshot.child("Buy Price").value
                            val boughtDateValue = snapshot.child("Bought Date").value
                            val breederContactValue = snapshot.child("Breeder Contact").value
                            val FatherValue = snapshot.child("Parents").child("Father").value
                            val MotherValue = snapshot.child("Parents").child("Mother").value
                            val fatherKeyValue =
                                snapshot.child("Parents").child("FatherKey").value
                            val motherKeyValue =
                                snapshot.child("Parents").child("MotherKey").value
                            // You can access other fields in the same way

                            val dateOfBanding = dateOfBandingValue.toString() ?: ""
                            val dateOfBirth = dateOfBirthValue.toString() ?: ""
                            val status = statusValue.toString() ?: ""
                            val availCage = availCageValue.toString() ?: ""
                            val forSaleCage = forSaleCageValue.toString() ?: ""
                            val forSaleRequestedPrice =
                                forSaleRequestedPriceValue.toString() ?: ""
                            val soldDate = soldDateValue.toString() ?: ""
                            val soldPrice = soldPriceValue.toString() ?: ""
                            val soldContact = soldContactValue.toString() ?: ""
                            val deathDate = deathDateValue.toString() ?: ""
                            val deathReason = deathReasonValue.toString() ?: ""
                            val exDate = exDateValue.toString() ?: ""
                            val exReason = exReasonValue.toString() ?: ""
                            val exContact = exContactValue.toString() ?: ""
                            val lostDate = lostDateValue.toString() ?: ""
                            val lostDetails = lostDetailsValue.toString() ?: ""
                            val donatedDate = donatedDateValue.toString() ?: ""
                            val donatedContact = donatedContactValue.toString() ?: ""
                            val otherComments = otherCommentsValue.toString() ?: ""
                            val buyPrice = buyPriceValue.toString() ?: ""
                            val boughtDate = boughtDateValue.toString() ?: ""
                            val boughtBreeder = breederContactValue.toString() ?: ""
                            val mother = MotherValue.toString() ?: ""
                            val father = FatherValue.toString() ?: ""

                            data?.img = mainPic
                            data?.flightKey = flightKey
                            data?.legband = pairLegBand
                            data?.identifier = pairIdentifier
                            data?.gender = pairGenderValue
                            data?.dateOfBanding = dateOfBanding
                            data?.dateOfBirth = dateOfBirth
                            data?.status = status
                            data?.mutation1 = mutation1Value
                            data?.mutation2 = mutation2Value
                            data?.mutation3 = mutation3Value
                            data?.mutation4 = mutation4Value
                            data?.mutation5 = mutation5Value
                            data?.mutation6 = mutation6Value
                            data?.availCage = availCage
                            data?.forSaleCage = forSaleCage
                            data?.reqPrice = forSaleRequestedPrice
                            data?.soldDate = soldDate
                            data?.soldPrice = soldPrice
                            data?.saleContact = soldContact
                            data?.deathDate = deathDate
                            data?.deathReason = deathReason
                            data?.exDate = exDate
                            data?.exReason = exReason
                            data?.exContact = exContact
                            data?.lostDate = lostDate
                            data?.lostDetails = lostDetails
                            data?.donatedDate = donatedDate
                            data?.donatedContact = donatedContact
                            data?.otherComments = otherComments
                            data?.buyPrice = buyPrice
                            data?.boughtDate = boughtDate
                            data?.breederContact = boughtBreeder
                            data?.father = father
                            data?.mother = mother
                            data?.fatherKey = fatherKeyValue.toString()
                            data?.motherKey = motherKeyValue.toString()

                            if (data != null) {
                                dataList.add(data)
                            }
                            adapter.notifyDataSetChanged()

                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle error if needed
                    }
                })
                if (data != null) {
                    dataList.add(data)
                }
                Log.d(ContentValues.TAG, "KeyCount: ${data?.identifier}")

            }

        }


        Log.d(ContentValues.TAG, "Count: ${dataList.count()}")

        dataList
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BirdPairingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BirdPairingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}