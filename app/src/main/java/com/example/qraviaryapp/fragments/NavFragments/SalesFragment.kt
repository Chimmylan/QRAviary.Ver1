package com.example.qraviaryapp.fragments.NavFragments

import BirdData
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
import com.example.qraviaryapp.adapter.DetailedAdapter.BirdDescendantsAdapter
import com.example.qraviaryapp.adapter.DetailedAdapter.SoldAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass.
 * Use the [SalesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SalesFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private lateinit var recyclerView: RecyclerView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: DatabaseReference
    private lateinit var dataList: ArrayList<BirdData>
    private lateinit var adapter: SoldAdapter
    private var flightKey: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sales, container, false)
        flightKey = arguments?.getString("FlightKey")
        mAuth = FirebaseAuth.getInstance()
        recyclerView = view.findViewById(R.id.RecyclerView)
        val gridLayoutManager = GridLayoutManager(requireContext(), 1)
        recyclerView.layoutManager = gridLayoutManager
        dataList = ArrayList()
        adapter = SoldAdapter(requireContext(),dataList)
        recyclerView.adapter = adapter
        Log.d(ContentValues.TAG, "FLIGHT KEY! THIS SHEYT ${flightKey.toString()}")
        lifecycleScope.launch {
            try {
                val data = getDataFromDatabase()
                dataList.clear()
                dataList.addAll(data)
                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error retrieving data: ${e.message}")
            }
        }
        return view
    }
    private suspend fun getDataFromDatabase(): List<BirdData> = withContext(Dispatchers.IO){
        val currentUserId = mAuth.currentUser?.uid
        val db = FirebaseDatabase.getInstance().getReference("Users")
            .child("ID: ${currentUserId.toString()}").child("Sold Items")

        val dataList = ArrayList<BirdData>()
        val snapshot = db.get().await()

        for (itemSnapshot in snapshot.children){

            val data = itemSnapshot.getValue(BirdData::class.java)
            val gallery = itemSnapshot.child("Gallery")
            if (data!=null){

                val mainPic = gallery.children.firstOrNull()?.value.toString()


                val key = itemSnapshot.child("ChildKey").value.toString()
                val flightKey = itemSnapshot.child("Flight Key").value.toString()
                val LegbandValue = itemSnapshot.child("Legband").value
                val identifierValue = itemSnapshot.child("Identifier").value
                val genderValue = itemSnapshot.child("Gender").value
                val mutation1Value = if (itemSnapshot.hasChild("Mutation1")) {
                    itemSnapshot.child("Mutation1").value.toString()
                } else {
                    ""
                }
                val mutation2Value = if (itemSnapshot.hasChild("Mutation2")) {
                    itemSnapshot.child("Mutation2").value.toString()
                } else {
                    ""
                }
                val mutation3Value = if (itemSnapshot.hasChild("Mutation3")) {
                    itemSnapshot.child("Mutation3").value.toString()
                } else {
                    ""
                }
                val mutation4Value = if (itemSnapshot.hasChild("Mutation4")) {
                    itemSnapshot.child("Mutation4").value.toString()
                } else {
                    ""
                }
                val mutation5Value = if (itemSnapshot.hasChild("Mutation5")) {
                    itemSnapshot.child("Mutation5").value.toString()
                } else {
                    ""
                }
                val mutation6Value = if (itemSnapshot.hasChild("Mutation6")) {
                    itemSnapshot.child("Mutation6").value.toString()
                } else {
                    ""
                }
                val dateOfBandingValue = itemSnapshot.child("Date of Banding").value
                val dateOfBirthValue = itemSnapshot.child("Date Birth").value
                val statusValue = itemSnapshot.child("Status").value
                val availCageValue = itemSnapshot.child("Avail Cage").value
                val forSaleCageValue = itemSnapshot.child("For Sale Cage").value
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
                val FatherValue = itemSnapshot.child("Parents").child("Father").value
                val MotherValue = itemSnapshot.child("Parents").child("Mother").value
                val fatherKeyValue = itemSnapshot.child("Parents").child("FatherKey").value
                val motherKeyValue = itemSnapshot.child("Parents").child("MotherKey").value
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
                data.img = mainPic
                data.birdKey = key
                data.flightKey = flightKey
                data.legband = legband
                data.identifier = identifier
                data.gender = gender
                data.dateOfBanding = dateOfBanding
                data.dateOfBirth = dateOfBirth
                data.status = status
                data.mutation1 = mutation1Value
                data.mutation2 = mutation2Value
                data.mutation3 = mutation3Value
                data.mutation4 = mutation4Value
                data.mutation5 = mutation5Value
                data.mutation6 = mutation6Value
                data.availCage = availCage
                data.forSaleCage = forSaleCage
                data.reqPrice = forSaleRequestedPrice
                data.soldDate = soldDate
                data.soldPrice = soldPrice
                data.saleContact = soldContact
                data.deathDate = deathDate
                data.deathReason = deathReason
                data.exDate = exDate
                data.exReason = exReason
                data.exContact = exContact
                data.lostDate = lostDate
                data.lostDetails = lostDetails
                data.donatedDate = donatedDate
                data.donatedContact = donatedContact
                data.otherComments = otherComments
                data.buyPrice = buyPrice
                data.boughtDate = boughtDate
                data.breederContact = boughtBreeder
                data.father = father
                data.mother = mother
                data.fatherKey = fatherKeyValue.toString()
                data.motherKey = motherKeyValue.toString()


                dataList.add(data)


            }

        }

        dataList
    }


}