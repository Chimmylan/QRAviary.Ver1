package com.example.qraviaryapp.adapter

import BirdData
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.detailedactivities.BirdsDetailedActivity

class BirdListAdapter(
    val context: android.content.Context,
    private var dataList: MutableList<BirdData>
) :
    RecyclerView.Adapter<MyViewHolder>() {
    private var originalList = dataList
    companion object {
        const val MAX_MUTATION_LENGTH = 10
    }
    fun getHeaderForPosition(position: Int): String {
        if (position < 0 || position >= dataList.size) {
            return ""
        }
        // Assuming dataList is sorted by mutation name
        return dataList[position].year?.substring(0, 4) ?: ""
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_birdlist, parent, false)


        return MyViewHolder(view, dataList)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }






    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val bird = dataList[position]

        if (bird.img.isNullOrEmpty()) {
            Glide.with(context)
                .load(R.drawable.noimage)
                .placeholder(R.drawable.noimage)
                .error(R.drawable.noimage)
                .into(holder.imageView)

        } else {
            Glide.with(context)
                .load(bird.img)
                .placeholder(R.drawable.noimage)
                .error(R.drawable.noimage)
                .into(holder.imageView)
        }
//        if (bird.bitmap != null) {
//            holder.imageView.setImageBitmap(bird.bitmap)
//        } else {
//            // Set a default image or placeholder if the Bitmap is null
//            holder.imageView.setImageResource(R.drawable.noimage)
//        }

//        if (bird.bitmap != null) {
//            holder.imageView.setImageBitmap(bird.bitmap)
//        } else {
//            // Handle the case where bird.bitmap is null or not available.
//        }

        val maxIdentifierLength = 5 // Define your desired maximum length here
        val identifierText = if (bird.identifier?.length!! > maxIdentifierLength) {
            bird.identifier?.substring(0, maxIdentifierLength) + "..." // Add ellipsis
        } else {
            bird.identifier
        }

        holder.tvIdentifier.text = "ID: $identifierText"


        val mutationList = listOf(
            bird.mutation1,
            bird.mutation2,
            bird.mutation3,
            bird.mutation4,
            bird.mutation5,
            bird.mutation6
        )

        val nonNullMutations = mutationList.filter { !it.isNullOrBlank() }

        val combinedMutations = if (nonNullMutations.isNotEmpty()) {
            "Mutation: " + nonNullMutations.joinToString(" / ")
        } else {
            "Mutation: None"
        }

        holder.tvMutation.text = combinedMutations


        holder.tvStatus.text = bird.status
        val status = bird.status
        holder.tvStatus.text = status
        when (status) {
            "Available" -> holder.tvStatus.setTextColor(Color.parseColor("#006400"))
            "For Sale" -> holder.tvStatus.setTextColor(Color.parseColor("#000080")) // Dark blue
            "Sold" -> holder.tvStatus.setTextColor(Color.parseColor("#8B0000")) // Dark red
            "Deceased" -> holder.tvStatus.setTextColor(Color.BLACK)
            "Exchanged" -> holder.tvStatus.setTextColor(Color.CYAN) // You can change this color
            "Lost" -> holder.tvStatus.setTextColor(Color.MAGENTA)
            "Donated" -> holder.tvStatus.setTextColor(Color.YELLOW)
            "Paired" -> holder.tvStatus.setTextColor(Color.parseColor("#FF69B4"))
            else -> holder.tvStatus.setTextColor(Color.GRAY)
        }

        if (bird.status == "Available" || bird.status == "For Sale") {
            val cageInfo = when {
                bird.status == "Available" -> bird.availCage
                bird.status == "For Sale" -> bird.forSaleCage
                else -> ""
            }

            if (cageInfo.isNullOrBlank()) {
                holder.tvCage.visibility = View.GONE
            } else {
                holder.tvCage.visibility = View.VISIBLE
                holder.tvCage.text = "Cage: $cageInfo"
            }
        } else {
            holder.tvCage.visibility = View.GONE
        }

        val genderIcon = when (bird.gender) {
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

        holder.imageGender.setImageResource(genderIcon)
        holder.tvGender.text = bird.gender

        if (bird.legband.isNullOrEmpty()) {
            holder.tvLegband.visibility = View.GONE
        } else {
            holder.tvLegband.text = bird.legband
        }
    }

    fun filterData(selectedAttributes: Map<String, Set<String>>, age: String) {

        val filteredList = originalList.filter { item ->
            selectedAttributes.all { (attribute, selectedValues) ->
                when (attribute) {
                    "status" -> item.status in selectedValues
                    "gender" -> item.gender in selectedValues

                    // Add more cases for other attributes
                    else -> true // Default to true if the attribute is not recognized
                }
            }
        }
        val finalFilteredList = when (age) {
            "Youngest" -> filteredList.sortedByDescending { it.year?.substring(0, 4)?.toIntOrNull() ?: 0 }
            else -> filteredList.sortedBy { it.month }

        }

        setData(finalFilteredList.toMutableList())
    }

//    fun getFilteredItemCount(): Int {
//        return dataList.size
//    }
    private fun setData(newData: MutableList<BirdData>){
        dataList.clear()

        dataList.addAll(newData)
        notifyDataSetChanged()
    }

//    fun filterAge(age: String){
//        val string = "id"
//        if (age == "Youngest"){
//            dataList.sortByDescending { it.year?.substring(0, 4)?.toIntOrNull() ?: 0 }
//        Log.d(TAG, "Youngest")
//        }else if (age == "Oldest"){
//            dataList.sortBy { it.dateOfBirth }
//            Log.d(TAG, "Oldest")
//
//        }
//        else{
//            dataList.sortBy { it.identifier }
//            Log.d(TAG, "Id")
//
//        }
//
//    }

}

class MyViewHolder(itemView: View, private val dataList: MutableList<BirdData>) :
    RecyclerView.ViewHolder(itemView) {
    var imageView : ImageView = itemView.findViewById(R.id.birdImageView)
    var tvIdentifier: TextView = itemView.findViewById(R.id.tvIdentifier)
    var tvLegband: TextView = itemView.findViewById(R.id.tvLegband)
    var tvMutation: TextView = itemView.findViewById(R.id.tvMutation)
    var tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
    var tvGender: TextView = itemView.findViewById(R.id.tvGender)
    var tvCage: TextView = itemView.findViewById(R.id.tvCage)
    var imageGender: ImageView = itemView.findViewById(R.id.GenderImageView)

    init {

        itemView.setOnClickListener {
            val bundle = Bundle()
            dataList[adapterPosition].clutch?.let { it1 -> bundle.putBoolean("Clutch", it1) }
            bundle.putString("BirdKey", dataList[adapterPosition].birdKey)//
            bundle.putString("FlightKey", dataList[adapterPosition].flightKey)
            bundle.putString("BirdLegband", dataList[adapterPosition].legband)
            bundle.putString("BirdId", dataList[adapterPosition].identifier)
            bundle.putString("BirdImage", dataList[adapterPosition].img)
            bundle.putString("BirdGender", dataList[adapterPosition].gender)
            bundle.putString("BirdStatus", dataList[adapterPosition].status)
            bundle.putString("BirdDateBirth", dataList[adapterPosition].dateOfBirth)
            bundle.putString("BirdSalePrice", dataList[adapterPosition].soldPrice)
            bundle.putString("BirdBuyer", dataList[adapterPosition].saleContact)
            bundle.putString("BirdDeathReason", dataList[adapterPosition].deathReason)
            bundle.putString("BirdExchangeReason", dataList[adapterPosition].exReason)
            bundle.putString("BirdExchangeWith", dataList[adapterPosition].exContact)
            bundle.putString("BirdLostDetails", dataList[adapterPosition].lostDetails)
            bundle.putString("BirdAvailCage", dataList[adapterPosition].availCage)
            bundle.putString("BirdForsaleCage", dataList[adapterPosition].forSaleCage)
            bundle.putString("BirdRequestedPrice", dataList[adapterPosition].reqPrice)
            bundle.putString("BirdBuyPrice", dataList[adapterPosition].buyPrice)
            bundle.putString("BirdBoughtOn", dataList[adapterPosition].boughtDate)
            bundle.putString("BirdBoughtBreeder", dataList[adapterPosition].breederContact)
            bundle.putString("BirdComment",dataList[adapterPosition].otherComments)
            bundle.putString("BirdMutation1", dataList[adapterPosition].mutation1)
            bundle.putString("BirdMutation2", dataList[adapterPosition].mutation2)
            bundle.putString("BirdMutation3", dataList[adapterPosition].mutation3)
            bundle.putString("BirdMutation4", dataList[adapterPosition].mutation4)
            bundle.putString("BirdMutation5", dataList[adapterPosition].mutation5)
            bundle.putString("BirdMutation6", dataList[adapterPosition].mutation6)
            bundle.putString("BirdMaturingDays1", dataList[adapterPosition].maturingdays1)
            bundle.putString("BirdMaturingDays2", dataList[adapterPosition].maturingdays2)
            bundle.putString("BirdMaturingDays3", dataList[adapterPosition].maturingdays3)
            bundle.putString("BirdMaturingDays4", dataList[adapterPosition].maturingdays4)
            bundle.putString("BirdMaturingDays5", dataList[adapterPosition].maturingdays5)
            bundle.putString("BirdMaturingDays6", dataList[adapterPosition].maturingdays6)
            bundle.putString("BirdIncubatingDays1", dataList[adapterPosition].incubatingdays1)
            bundle.putString("BirdIncubatingDays2", dataList[adapterPosition].incubatingdays2)
            bundle.putString("BirdIncubatingDays3", dataList[adapterPosition].incubatingdays3)
            bundle.putString("BirdIncubatingDays4", dataList[adapterPosition].incubatingdays4)
            bundle.putString("BirdIncubatingDays5", dataList[adapterPosition].incubatingdays5)
            bundle.putString("BirdIncubatingDays6", dataList[adapterPosition].incubatingdays6)
            bundle.putString("BirdSoldDate", dataList[adapterPosition].soldDate)
            bundle.putString("BirdDeceaseDate", dataList[adapterPosition].deathDate)
            bundle.putString("BirdExchangeDate", dataList[adapterPosition].exDate)
            bundle.putString("BirdLostDate", dataList[adapterPosition].lostDate)
            bundle.putString("BirdDonatedDate", dataList[adapterPosition].donatedDate)
            bundle.putString("BirdDonatedContact", dataList[adapterPosition].donatedContact)
            bundle.putString("BirdFather", dataList[adapterPosition].father)
            bundle.putString("BirdFatherKey", dataList[adapterPosition].fatherKey)
            bundle.putString("BirdMother", dataList[adapterPosition].mother)
            bundle.putString("BirdMotherKey", dataList[adapterPosition].motherKey)
            bundle.putString("CageKeyValue", dataList[adapterPosition].cageKey)
            bundle.putString("FlightType", dataList[adapterPosition].flightType)
            bundle.putString("NurseryType", dataList[adapterPosition].nurseryType)
            bundle.putString("otOther", dataList[adapterPosition].otOtherContact)
            bundle.putString("CageBirdKey", dataList[adapterPosition].cagebirdkey)
            val i = Intent(itemView.context, BirdsDetailedActivity::class.java)
            i.putExtras(bundle)
            itemView.context.startActivity(i)
        }
    }
}

