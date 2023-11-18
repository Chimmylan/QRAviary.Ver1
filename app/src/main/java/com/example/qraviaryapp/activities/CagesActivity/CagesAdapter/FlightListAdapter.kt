package com.example.qraviaryapp.activities.CagesActivity.CagesAdapter


import BirdData
import android.app.AlertDialog
import android.content.ContentValues
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class FlightListAdapter(
    val context: android.content.Context,
    private var dataList: MutableList<BirdData>
) :
    RecyclerView.Adapter<MyViewHolder>() {

    companion object {
        const val MAX_MUTATION_LENGTH = 10
    }

    fun getHeaderForPosition(position: Int): String {
        if (position < 0 || position >= dataList.size) {
            return ""
        }
        // Assuming dataList is sorted by mutation name
        return dataList[position].month?.substring(0, 4) ?: ""
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_flightlist, parent, false)

        return MyViewHolder(view, dataList)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    private fun showDeleteDialog(bird: BirdData, position: Int) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("Delete Bird")
        alertDialogBuilder.setMessage("Are you sure you want to remove this bird from the cage?")

        alertDialogBuilder.setPositiveButton("Delete") { _, _ ->
            // Handle the deletion here
            deleteBird(bird, position)
        }

        alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun deleteBird(bird: BirdData, position: Int) {
        val birdRef = bird.adultingKey
        val flight = bird.flightKey
        val cageBirdRef = bird.birdKey
        val cageKeyRef = bird.cageKey

        Log.d(ContentValues.TAG, birdRef + " " + flight + " " + cageBirdRef + " " + cageKeyRef)

        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        val birdRefPath =
            FirebaseDatabase.getInstance().reference.child("Users").child("ID: $currentUserId")
                .child("Birds")
                .child(birdRef.toString())
        val birdCage = birdRefPath.child("Cage").setValue("None")
        val birdCageKey = birdRefPath.child("CageKey").removeValue()

        val nurseryRefPath =
            FirebaseDatabase.getInstance().reference.child("Users").child("ID: $currentUserId")
                .child("Flight Birds")
                .child(flight.toString())
        val nurseryCage = nurseryRefPath.child("Cage").setValue("None")
        val nirseryCageKey = nurseryRefPath.child("CageKey").removeValue()

        val cagePath =
            FirebaseDatabase.getInstance().reference.child("Users").child("ID: $currentUserId")
                .child("Cages").child("Flight Cages").child(cageKeyRef.toString())
                .child("Birds").child(cageBirdRef.toString()).removeValue()

        dataList.removeAt(position)
        notifyDataSetChanged()
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

        holder.delete.visibility = View.VISIBLE
        holder.delete.setOnClickListener {
            showDeleteDialog(bird, position)
        }
    }
}

class MyViewHolder(itemView: View, private val dataList: MutableList<BirdData>) :
    RecyclerView.ViewHolder(itemView) {
    var imageView: ImageView = itemView.findViewById(R.id.birdImageView)
    var tvIdentifier: TextView = itemView.findViewById(R.id.tvIdentifier)
    var tvLegband: TextView = itemView.findViewById(R.id.tvLegband)
    var tvMutation: TextView = itemView.findViewById(R.id.tvMutation)
    var tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
    var tvGender: TextView = itemView.findViewById(R.id.tvGender)
    var imageGender: ImageView = itemView.findViewById(R.id.GenderImageView)
    var delete: ImageView = itemView.findViewById(R.id.options)

    init {

        itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("BirdKey", dataList[adapterPosition].birdKey)
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
            bundle.putString("BirdMutation1", dataList[adapterPosition].mutation1)
            bundle.putString("BirdMutation2", dataList[adapterPosition].mutation2)
            bundle.putString("BirdMutation3", dataList[adapterPosition].mutation3)
            bundle.putString("BirdMutation4", dataList[adapterPosition].mutation4)
            bundle.putString("BirdMutation5", dataList[adapterPosition].mutation5)
            bundle.putString("BirdMutation6", dataList[adapterPosition].mutation6)
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
            bundle.putBoolean("fromFlightListAdapter", true)
            val i = Intent(itemView.context, BirdsDetailedActivity::class.java)
            i.putExtras(bundle)
            itemView.context.startActivity(i)
        }
    }
}

