package com.example.qraviaryapp.fragments.DetailedFragment

import BirdData
import android.content.ContentValues
import android.net.ConnectivityManager
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
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
    private lateinit var snackbar: Snackbar
    private lateinit var connectivityManager: ConnectivityManager
    private var isNetworkAvailable = true

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

    private suspend fun getDataFromDatabase(): List<BirdData> = withContext(Dispatchers.IO) {
        val currentUserId = mAuth.currentUser?.uid
        val db = FirebaseDatabase.getInstance().getReference("Users")
            .child("ID: ${currentUserId.toString()}").child("Birds")
            .child(birdKey.toString()).child("Pairs")
        val newRef = db.get().await()
        val dataList = ArrayList<BirdData>()

        if (newRef.exists()) {
            for (itemSnapshot in newRef.children) {
                val data = itemSnapshot.getValue(BirdData::class.java)

                val pairKey = itemSnapshot.child("Bird Key").value.toString()
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

                            val mainPic = gallery.children.firstOrNull()?.value.toString()
                            val flightKey = itemSnapshot.child("Flight Key").value.toString()
                            val birdKey = itemSnapshot.key
                            val LegbandValue = itemSnapshot.child("Legband").value
                            val identifierValue = itemSnapshot.child("Identifier").value
                            val genderValue = itemSnapshot.child("Gender").value
                            val clutch = itemSnapshot.child("Clutch").value.toString().toBoolean()


                            val mutation1Value = if (itemSnapshot.hasChild("Mutation1")) {
                                itemSnapshot.child("Mutation1").child("Mutation Name").value.toString()
                            } else {
                                ""
                            }
                            val mutation2Value = if (itemSnapshot.hasChild("Mutation2")) {
                                itemSnapshot.child("Mutation2").child("Mutation Name").value.toString()
                            } else {
                                ""
                            }
                            val mutation3Value = if (itemSnapshot.hasChild("Mutation3")) {
                                itemSnapshot.child("Mutation3").child("Mutation Name").value.toString()
                            } else {
                                ""
                            }
                            val mutation4Value = if (itemSnapshot.hasChild("Mutation4")) {
                                itemSnapshot.child("Mutation4").child("Mutation Name").value.toString()
                            } else {
                                ""
                            }
                            val mutation5Value = if (itemSnapshot.hasChild("Mutation5")) {
                                itemSnapshot.child("Mutation5").child("Mutation Name").value.toString()
                            } else {
                                ""
                            }
                            val mutation6Value = if (itemSnapshot.hasChild("Mutation6")) {
                                itemSnapshot.child("Mutation6").child("Mutation Name").value.toString()
                            } else {
                                ""
                            }
                            val mutation1incubatingValue = if (itemSnapshot.hasChild("Mutation1")) {
                                itemSnapshot.child("Mutation1").child("Incubating Days").value.toString()
                            } else {
                                ""
                            }
                            val mutation2incubatingValue = if (itemSnapshot.hasChild("Mutation2")) {
                                itemSnapshot.child("Mutation2").child("Incubating Days").value.toString()
                            } else {
                                ""
                            }
                            val mutation3incubatingValue = if (itemSnapshot.hasChild("Mutation3")) {
                                itemSnapshot.child("Mutation3").child("Incubating Days").value.toString()
                            } else {
                                ""
                            }
                            val mutation4incubatingValue = if (itemSnapshot.hasChild("Mutation4")) {
                                itemSnapshot.child("Mutation4").child("Incubating Days").value.toString()
                            } else {
                                ""
                            }
                            val mutation5incubatingValue = if (itemSnapshot.hasChild("Mutation5")) {
                                itemSnapshot.child("Mutation5").child("Incubating Days").value.toString()
                            } else {
                                ""
                            }
                            val mutation6incubatingValue = if (itemSnapshot.hasChild("Mutation6")) {
                                itemSnapshot.child("Mutation6").child("Incubating Days").value.toString()
                            } else {
                                ""
                            }
                            val mutation1maturingValue = if (itemSnapshot.hasChild("Mutation1")) {
                                itemSnapshot.child("Mutation1").child("Maturing Days").value.toString()
                            } else {
                                ""
                            }
                            val mutation2maturingValue = if (itemSnapshot.hasChild("Mutation2")) {
                                itemSnapshot.child("Mutation2").child("Maturing Days").value.toString()
                            } else {
                                ""
                            }
                            val mutation3maturingValue = if (itemSnapshot.hasChild("Mutation3")) {
                                itemSnapshot.child("Mutation3").child("Maturing Days").value.toString()
                            } else {
                                ""
                            }
                            val mutation4maturingValue = if (itemSnapshot.hasChild("Mutation4")) {
                                itemSnapshot.child("Mutation4").child("Maturing Days").value.toString()
                            } else {
                                ""
                            }
                            val mutation5maturingValue = if (itemSnapshot.hasChild("Mutation5")) {
                                itemSnapshot.child("Mutation5").child("Maturing Days").value.toString()
                            } else {
                                ""
                            }
                            val mutation6maturingValue = if (itemSnapshot.hasChild("Mutation6")) {
                                itemSnapshot.child("Mutation6").child("Maturing Days").value.toString()
                            } else {
                                ""
                            }
                            val dateOfBandingValue = itemSnapshot.child("Date of Banding").value
                            val dateOfBirthValue = itemSnapshot.child("Date of Birth").value
                            val statusValue = itemSnapshot.child("Status").value
                            val availCageValue = itemSnapshot.child("Cage").value
                            val forSaleCageValue = itemSnapshot.child("Cage").value
                            val cageKey = itemSnapshot.child("CageKey").value
                            val cageBirdKey = itemSnapshot.child("Cage Bird Key").value
                            val forSaleRequestedPriceValue = itemSnapshot.child("Requested Price").value
                            val soldDateValue = itemSnapshot.child("Sold Date").value
                            val soldPriceValue = itemSnapshot.child("Sale Price").value
                            val soldContactValue = itemSnapshot.child("Sale Contact").value
                            val deathDateValue = itemSnapshot.child("Death Date").value
                            val deathReasonValue = itemSnapshot.child("Death Reason").value
                            val exDateValue = itemSnapshot.child("Exchange Date").value
                            val exReasonValue = itemSnapshot.child("Exchange Reason").value
                            val exContactValue = itemSnapshot.child("Exchange Contact").value
                            val lostDateValue = itemSnapshot.child("Lost Date").value
                            val lostDetailsValue = itemSnapshot.child("Lost Details").value
                            val donatedDateValue = itemSnapshot.child("Donated Date").value
                            val donatedContactValue = itemSnapshot.child("Donated Contact").value
                            val otherCommentsValue = itemSnapshot.child("Comments").value
                            val buyPriceValue = itemSnapshot.child("Buy Price").value
                            val boughtDateValue = itemSnapshot.child("Bought Date").value
                            val breederContactValue = itemSnapshot.child("Breeder Contact").value
                            val otherBreederContact = itemSnapshot.child("Other Breeder Contact").value.toString()
                            val FatherValue = itemSnapshot.child("Parents").child("Father").value
                            val MotherValue = itemSnapshot.child("Parents").child("Mother").value
                            val fatherKeyValue = itemSnapshot.child("Parents").child("FatherKey").value
                            val motherKeyValue = itemSnapshot.child("Parents").child("MotherKey").value
                            val nurseryType = itemSnapshot.child("Nursery Key").value.toString()
                            /*==++==*/
                            val legband = LegbandValue.toString() ?: ""
                            val identifier = identifierValue.toString() ?: ""
                            val gender = genderValue.toString() ?: ""
                            val dateOfBanding = dateOfBandingValue.toString() ?: ""
                            val dateOfBirth = dateOfBirthValue.toString() ?: ""
                            val status = statusValue.toString() ?: ""
                            val availCage = availCageValue.toString() ?: ""
                            val forSaleCage = forSaleCageValue.toString() ?: ""
                            val forSaleRequestedPrice = forSaleRequestedPriceValue.toString() ?: ""
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


                            data?.cagebirdkey = cageBirdKey?.toString()
                            data?.otOtherContact = otherBreederContact
                            data?.clutch = clutch
                            data?.nurseryType = nurseryType
                            data?.flightType = flightKey
                            data?.cageKey = cageKey?.toString()
                            data?.img = mainPic
                            data?.birdKey = birdKey
                            data?.flightKey = flightKey
                            data?.legband = legband
                            data?.identifier = identifier
                            data?.gender = gender
                            data?.dateOfBanding = dateOfBanding
                            data?.dateOfBirth = dateOfBirth
                            data?.status = status
                            data?.mutation1 = mutation1Value
                            data?.mutation2 = mutation2Value
                            data?.mutation3 = mutation3Value
                            data?.mutation4 = mutation4Value
                            data?.mutation5 = mutation5Value
                            data?.mutation6 = mutation6Value
                            data?.maturingdays1 = mutation1maturingValue
                            data?.maturingdays2 = mutation2maturingValue
                            data?.maturingdays3 = mutation3maturingValue
                            data?.maturingdays4 = mutation4maturingValue
                            data?.maturingdays5 = mutation5maturingValue
                            data?.maturingdays6 = mutation6maturingValue
                            data?.incubatingdays1 = mutation1incubatingValue
                            data?.incubatingdays2 = mutation1incubatingValue
                            data?.incubatingdays3 = mutation1incubatingValue
                            data?.incubatingdays4 = mutation1incubatingValue
                            data?.incubatingdays5 = mutation1incubatingValue
                            data?.incubatingdays6 = mutation1incubatingValue
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
                            data?.fatherKey = fatherKeyValue?.toString()
                            data?.motherKey = motherKeyValue?.toString()

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