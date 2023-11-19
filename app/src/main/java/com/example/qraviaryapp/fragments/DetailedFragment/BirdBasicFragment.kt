package com.example.qraviaryapp.fragments.DetailedFragment

import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.qraviaryapp.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BirdBasicFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BirdBasicFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: DatabaseReference
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
    private lateinit var otherComment: TextView
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
        val view = inflater.inflate(R.layout.fragment_bird_basic, container, false)
        bird_gender = view.findViewById(R.id.imgGender)
        bird_status = view.findViewById(R.id.tvStatus)
        bird_id = view.findViewById(R.id.tvId)
        bird_mutation = view.findViewById(R.id.tvMutation)
        bird_age = view.findViewById(R.id.tvAge)
        bird_dateBirth = view.findViewById(R.id.tvdateBirth)
        bird_salePrice = view.findViewById(R.id.tvSalePrice)
        bird_buyer = view.findViewById(R.id.tvBuyer)
        bird_deathreason = view.findViewById(R.id.tvDeathReason)
        bird_exchangereason = view.findViewById(R.id.tvExchangeReason)
        bird_exchangewith = view.findViewById(R.id.tvExchangeWith)
        bird_lostdetails = view.findViewById(R.id.tvLostDetails)
        bird_availcage = view.findViewById(R.id.tvAvailCage)
        bird_requestedprice = view.findViewById(R.id.tvRequestedPrice)
        bird_comment = view.findViewById(R.id.tvComment)
        bird_buyprice = view.findViewById(R.id.tvbuyPrice)
        bird_boughton = view.findViewById(R.id.tvdateBought)
        bird_boughtbreeder = view.findViewById(R.id.tvbreederBought)
        bird_breeder = view.findViewById(R.id.tvbreeder)
        bird_solddate = view.findViewById(R.id.tvSoldDate)
        bird_deceasedate = view.findViewById(R.id.tvDeceaseDate)
        bird_lostdate = view.findViewById(R.id.tvLostDate)
        bird_exchangedate = view.findViewById(R.id.tvExchangeDate)
        bird_donateddate = view.findViewById(R.id.tvDonatedDate)
        bird_donatedcontact = view.findViewById(R.id.tvDonatedContact)
        bird_legband = view.findViewById(R.id.tvLegband)
        otherComment = view.findViewById(R.id.tvotOtherComment)

        birdKey = arguments?.getString("BirdKey")
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



        bird_status.text = birdStatus
        val status = birdStatus
        bird_status.text = status
        when (status) {
            "Available" -> bird_status.setTextColor(Color.parseColor("#006400"))
            "For Sale" -> bird_status.setTextColor(Color.parseColor("#000080")) // Dark blue
            "Sold" -> bird_status.setTextColor(Color.parseColor("#8B0000")) // Dark red
            "Deceased" -> bird_status.setTextColor(Color.BLACK)
            "Exchanged" -> bird_status.setTextColor(Color.CYAN) // You can change this color
            "Lost" -> bird_status.setTextColor(Color.MAGENTA)
            "Donated" -> bird_status.setTextColor(Color.YELLOW)
            "Paired" -> bird_status.setTextColor(Color.parseColor("#FF69B4"))
            else -> bird_status.setTextColor(Color.GRAY)
        }
        bird_id.text = birdId
        val genderIcon = when (birdGender) {
            "Male" -> {
                R.drawable.baseline_male_24
            }

            "Female" -> {
                R.drawable.female_logo
            }

            else -> {
                R.drawable.unknown
            }
        }

        bird_gender.setImageResource(genderIcon)

        val nonNullMutations = listOf(
            birdMutation1,
            birdMutation2,
            birdMutation3,
            birdMutation4,
            birdMutation5,
            birdMutation6
        ).filter { !it.isNullOrBlank() }
        val NonNullMutations = mutableListOf<String>()
        for (mutation in nonNullMutations) {
            if (mutation != "null") {
                NonNullMutations.add(mutation.toString())
            }
        }
        val CombinedMutations = if (NonNullMutations.isNotEmpty()) {
            NonNullMutations.joinToString(" / ")

        } else {
            "Mutation: None"
        }

        bird_mutation.text = CombinedMutations

        bird_dateBirth.text = "Date of Birth: " + birdDateBirth

        val currencyFormat: NumberFormat = NumberFormat.getCurrencyInstance(Locale("en", "PH"))
        val dateFormatter = SimpleDateFormat("MMM d yyyy", Locale.US)
        val birthDate = dateFormatter.parse(birdDateBirth)

        if (birthDate != null) {
            val today = Calendar.getInstance().time
            val age = calculateAge(birthDate, today)
            bird_age.text = "Age: $age"
        } else {
            bird_age.text = "Age: N/A"
        }

        if (birdLegband.isNullOrEmpty() || birdLegband == "null") {
            bird_legband.visibility = GONE
        } else {
            bird_legband.text = birdLegband
        }

        if (birdAvailCage.isNullOrEmpty() || birdAvailCage == "null") {
            bird_availcage.visibility = GONE
        } else {
            bird_availcage.text = "Cage: " + birdAvailCage
        }
        if (birdForsaleCage.isNullOrEmpty() || birdForsaleCage == "null") {
            bird_availcage.visibility = GONE
        } else {
            bird_availcage.text = "Cage: " + birdForsaleCage
        }

        if (birdRequestedPrice.isNullOrEmpty() || birdRequestedPrice == "null") {
            bird_requestedprice.visibility = GONE
        } else {
            val price = birdRequestedPrice?.toDouble()
            bird_requestedprice.text = "Requested Price: " + currencyFormat.format(price)
        }

        if (birdBuyPrice.isNullOrEmpty() || birdBuyPrice == "null") {
            bird_buyprice.visibility = GONE
        } else {
            val price = birdBuyPrice?.toDouble()
            bird_buyprice.text = "Buy Price: " + currencyFormat.format(price)
        }

        if (birdBoughtOn.isNullOrEmpty() || birdBoughtOn == "null") {
            bird_boughton.visibility = GONE
        } else {
            bird_boughton.text = "Bought On: " + birdBoughtOn
        }

        if (birdBoughtBreeder.isNullOrEmpty() || birdBoughtBreeder == "null") {
            bird_boughtbreeder.visibility = GONE
        } else {
            bird_boughtbreeder.text = "Bought to Breeder: " + birdBoughtBreeder
        }

        if (birdSoldDate.isNullOrEmpty() || birdSoldDate == "null") {
            bird_solddate.visibility = GONE
        } else {
            bird_solddate.text = "Sale Date: " + birdSoldDate
        }
        if (birdsalePrice.isNullOrEmpty() || birdsalePrice == "null") {
            bird_salePrice.visibility = GONE
        } else {
            val price = birdsalePrice?.toDouble()
            bird_salePrice.text = "Sale Price: " + currencyFormat.format(price)
        }
        if (birdBuyer.isNullOrEmpty() || birdBuyer == "null") {
            bird_buyer.visibility = GONE
        } else {
            bird_buyer.text = "Buyer: " + birdBuyer
        }

        if (birdDeceaseDate.isNullOrEmpty() || birdDeceaseDate == "null") {
            bird_deceasedate.visibility = GONE
        } else {
            bird_deceasedate.text = "Death Date: " + birdDeceaseDate
        }

        if (birdDeathReason.isNullOrEmpty() || birdDeathReason == "null") {
            bird_deathreason.visibility = GONE
        } else {
            bird_deathreason.text = "Death Reason: " + birdDeathReason
        }
        if (birdDonatedDate.isNullOrEmpty() || birdDonatedDate == "null") {
            bird_donateddate.visibility = GONE
        } else {
            bird_donateddate.text = "Donate Date: " + birdDonatedDate

        }
        if (birdDonatedContact.isNullOrEmpty() || birdDonatedContact == "null") {
            bird_donatedcontact.visibility = GONE
        } else {
            bird_donatedcontact.text = "Donate Contact: " + birdDonatedContact

        }
        if (birdExchangeDate.isNullOrEmpty() || birdExchangeDate == "null") {
            bird_exchangedate.visibility = GONE
        } else {
            bird_exchangedate.text = "Exchanged Date: " + birdExchangeDate
        }

        if (birdExchangeReason.isNullOrEmpty() || birdExchangeReason == "null") {
            bird_exchangereason.visibility = GONE
        } else {
            bird_exchangereason.text = "Exchanged Reason: " + birdExchangeReason
        }
        if (birdExchangeWith.isNullOrEmpty() || birdExchangeWith == "null") {
            bird_exchangewith.visibility = GONE
        } else {
            bird_exchangewith.text = "Exchanged With: " + birdExchangeWith
        }

        if (birdBreeder.isNullOrEmpty() || birdBreeder == "null") {
            bird_breeder.visibility = GONE
        } else {
            bird_breeder.text = "Breeder: " + birdBreeder
        }


        if (birdLostDate.isNullOrEmpty() || birdLostDate == "null") {
            bird_lostdate.visibility = GONE
        } else {
            bird_lostdate.text = "Lost Date: " + birdLostDate
        }
        if (birdLostDetails.isNullOrEmpty() || birdLostDetails == "null") {
            bird_lostdetails.visibility = GONE
        } else {
            bird_lostdetails.text = "Lost Details: " + birdLostDetails
        }

        if (birdComment.isNullOrEmpty() || birdComment == "null") {
            bird_comment.visibility = GONE
        } else {
            bird_comment.text = "Comment: " + birdComment
        }

        val otOther = arguments?.getString("BirdOtherOrigin")

        if (otOther.isNullOrEmpty() ||
            otOther == "null"
        ) {
            otherComment.visibility = GONE

        } else {
            otherComment.text = "Other Comment: " + arguments?.getString("BirdOtherOrigin")

        }

        return view

    }

    private fun calculateAge(birthDate: Date, currentDate: Date): String {
        val calendarBirth = Calendar.getInstance()
        calendarBirth.time = birthDate

        val calendarCurrent = Calendar.getInstance()
        calendarCurrent.time = currentDate

        val years = calendarCurrent.get(Calendar.YEAR) - calendarBirth.get(Calendar.YEAR)
        val months = calendarCurrent.get(Calendar.MONTH) - calendarBirth.get(Calendar.MONTH)
        val days =
            calendarCurrent.get(Calendar.DAY_OF_MONTH) - calendarBirth.get(Calendar.DAY_OF_MONTH)

        val ageStringBuilder = StringBuilder()

        if (years > 0) {
            ageStringBuilder.append("$years year${if (years > 1) "s" else ""}")
        }

        if (months > 0) {
            if (ageStringBuilder.isNotEmpty()) {
                ageStringBuilder.append(", ")
            }
            ageStringBuilder.append("$months month${if (months > 1) "s" else ""}")
        }

        if (days > 0) {
            if (ageStringBuilder.isNotEmpty()) {
                if (years > 0 || months > 0) {
                    ageStringBuilder.append(" and ")
                } else {
                    ageStringBuilder.append(", ")
                }
            }
            ageStringBuilder.append("$days day${if (days > 1) "s" else ""}")
        }

        if (ageStringBuilder.isEmpty()) {
            return "TODAY"
        } else {
            return ageStringBuilder.toString() + " old"
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BirdBasicFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BirdBasicFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}