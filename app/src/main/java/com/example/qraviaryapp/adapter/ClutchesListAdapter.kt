package com.example.qraviaryapp.adapter

import EggData
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.detailedactivities.ClutchesDetailedActivity
import com.example.qraviaryapp.activities.detailedactivities.PairsDetailedActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.math.log

class ClutchesListAdapter(
    private val context: android.content.Context,
    private var dataList: MutableList<EggData>
) : RecyclerView.Adapter<ClutchesViewHolder>() {

    private var dbase = FirebaseDatabase.getInstance().reference
    private var mAuth = FirebaseAuth.getInstance()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClutchesViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_clutcheslist, parent, false)

        return ClutchesViewHolder(view, dataList)
    }

    override fun onBindViewHolder(holder: ClutchesViewHolder, position: Int) {
        val clutch = dataList[position]

        val eggCount = clutch.eggCount
        val incubatingCount = clutch.eggIncubating
        val laidCount = clutch.eggLaid
        val hatchedCount = clutch.eggHatched
        val notFertilizedCount = clutch.eggNotFertilized
        val brokenCount = clutch.eggBroken
        val abandonCount = clutch.eggAbandon
        val deadInShellCount = clutch.eggDeadInShell
        val deadBeforeMovingToNurseryCount = clutch.eggDeadBeforeMovingToNursery
        val incubateDate = clutch.eggIncubationStartDate
        val laidDate = clutch.eggLaidStartDate
        val moveCount = clutch.eggMoved


        holder.tvTotal.text = eggCount

        if (clutch.fosterPair == true){
            holder.btnFoster.visibility = View.VISIBLE
        }
        if (clutch.parentPair == true){
            holder.btnParent.visibility = View.VISIBLE
        }

        holder.btnFoster.setOnClickListener {

        }

        holder.btnParent.setOnClickListener {
            val currentUserId = mAuth.currentUser?.uid
            val db = dbase.child("Users")
                .child("ID: ${currentUserId}").child("Pairs")
                .child(clutch.pairKey.toString()).child("Clutches").child(clutch.eggKey.toString()).child("Parent")

            db.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val parentKey = snapshot.child("ParentKey").value.toString()
                    if (parentKey.isNotEmpty()){
                        val i = Intent(context, PairsDetailedActivity::class.java)

                        val femalegallery = snapshot.child(parentKey).child("Female Gallery").value.toString()
                        val malegallery = snapshot.child(parentKey).child("Male Gallery").value.toString()
                        val key = parentKey
                        val pairsId = snapshot.child(parentKey).child("Pair ID").value.toString()
                        val cageName = snapshot.child(parentKey).child("Cage").value.toString()
                        val cageKeyFemale = snapshot.child(parentKey).child("CageKeyFemale").value.toString()
                        val cageKeyMale = snapshot.child(parentKey).child("CageKeyMale").value.toString()
                        val cageBirdFemale = snapshot.child(parentKey).child("CageKeyFlightFemaleValue").value.toString()
                        val cageBirdMale = snapshot.child(parentKey).child("CageKeyFlightMaleValue").value.toString()
                        val male = snapshot.child(parentKey).child("Male").value.toString()
                        val female = snapshot.child(parentKey).child("Female").value.toString()
                        val maleMutation = snapshot.child(parentKey).child("Male Mutation").value.toString()
                        val femaleMutation = snapshot.child(parentKey).child("Female Mutation").value.toString()
                        val beginningDate = snapshot.child(parentKey).child("Beginning").value.toString()
                        val pairMaleKey = snapshot.child(parentKey).child("Male Bird Key").value.toString()
                        val pairFemaleKey = snapshot.child(parentKey).child("Female Bird Key").value.toString()
                        val separateDate = snapshot.child(parentKey).child("Separate Date").value.toString()
                        val pairMaleFlightKey = snapshot.child(parentKey).child("Male Flight Key").value.toString()
                        val pairFemaleFlightKey = snapshot.child(parentKey).child("Female Flight Key").value.toString()
                        val pairCageKey = snapshot.child(parentKey).child("Cage Key").value.toString()
                        val cagePairKey = snapshot.child(parentKey).child("Pair Cage Key").value.toString()


                        val bundle = Bundle()
                        bundle.putString("PairFemaleImg", femalegallery)//
                        bundle.putString("PairMaleImg", malegallery)//
                        bundle.putString("PairId", pairsId)//
                        bundle.putString("PairMaleKey", pairMaleKey)//
                        bundle.putString("PairFemaleKey", pairFemaleKey)//
                        bundle.putString("PairFlightMaleKey", pairMaleFlightKey)//
                        bundle.putString("PairFlightFemaleKey", pairFemaleFlightKey)//
                        bundle.putString("PairKey", key)//
                        bundle.putString("MaleID", male)//
                        bundle.putString("FemaleID", female)//
                        bundle.putString("BeginningDate", beginningDate)//
                        bundle.putString("SeparateDate", separateDate)// /toodo
                        bundle.putString("MaleGender", maleMutation)//
                        bundle.putString("FemaleGender", femaleMutation)//
                        bundle.putString("CageKeyFemale", cageKeyFemale)//
                        bundle.putString("CageKeyMale", cageKeyMale)//
                        bundle.putString("CageBirdFemale", cageBirdFemale)//
                        bundle.putString("CageBirdMale", cageBirdMale)//
                        bundle.putString("CageKey", pairCageKey)
                        bundle.putString("CagePairKey", cagePairKey)
                        bundle.putString("PairCage", cageName)

                        Log.d(TAG, bundle.toString())
                        i.putExtras(bundle)


                        context.startActivity(i)
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }


        // Set visibility and text for Incubating
        if (incubatingCount != null && incubatingCount.toInt() > 0) {
            holder.tvIncubating.visibility = View.VISIBLE
            holder.tvIncubating.text = "$incubatingCount Incubating"
            holder.tvDate.text = incubateDate
        } else {
            holder.tvIncubating.visibility = View.GONE
        }

        // Set visibility and text for Laid
        if (laidCount != null && laidCount.toInt() > 0) {
            holder.tvLaid.visibility = View.VISIBLE
            holder.tvLaid.text = "$laidCount Laid"
            holder.tvDate.text = laidDate
        } else {
            holder.tvLaid.visibility = View.GONE
        }

        // Set visibility and text for Hatched
        if (hatchedCount != null && hatchedCount.toInt() > 0) {
            holder.tvHatched.visibility = View.VISIBLE
            holder.tvHatched.text = "$hatchedCount Hatched"
            // You might want to set the date for hatched eggs here if applicable.
        } else {
            holder.tvHatched.visibility = View.GONE
        }
        if (notFertilizedCount != null && notFertilizedCount.toInt() > 0) {
            holder.tvNotFertilized.visibility = View.VISIBLE
            holder.tvNotFertilized.text = "$notFertilizedCount Not Fertilized"
            // You might want to set the date for hatched eggs here if applicable.
        } else {
            holder.tvNotFertilized.visibility = View.GONE
        }
        if (brokenCount != null && brokenCount.toInt() > 0) {
            holder.tvBroken.visibility = View.VISIBLE
            holder.tvBroken.text = "$brokenCount Broken"
            // You might want to set the date for hatched eggs here if applicable.
        } else {
            holder.tvBroken.visibility = View.GONE
        }
        if (abandonCount != null && abandonCount.toInt() > 0) {
            holder.tvAbandon.visibility = View.VISIBLE
            holder.tvAbandon.text = "$abandonCount Abandon"
            // You might want to set the date for hatched eggs here if applicable.
        } else {
            holder.tvAbandon.visibility = View.GONE
        }
        if (deadInShellCount != null && deadInShellCount.toInt() > 0) {
            holder.tvDeadInShell.visibility = View.VISIBLE
            holder.tvDeadInShell.text = "$deadInShellCount Dead In Shell"
            // You might want to set the date for hatched eggs here if applicable.
        } else {
            holder.tvDeadInShell.visibility = View.GONE
        }
        if (deadBeforeMovingToNurseryCount != null && deadBeforeMovingToNurseryCount.toInt() > 0) {
            holder.tvDeadBeforeMoving.visibility = View.VISIBLE
            holder.tvDeadBeforeMoving.text = "$deadBeforeMovingToNurseryCount Dead Before Moving to Nursery"
            // You might want to set the date for hatched eggs here if applicable.
        } else {
            holder.tvDeadBeforeMoving.visibility = View.GONE
        }
        Log.d(ContentValues.TAG, moveCount.toString())
        if ( moveCount != null && moveCount.toInt() > 0) {
            holder.tvMoved.visibility = View.VISIBLE
            holder.tvMoved.text = "$moveCount Moved Egg"
            // You might want to set the date for hatched eggs here if applicable.
        } else {
            holder.tvMoved.visibility = View.GONE
        }
    }


    override fun getItemCount(): Int {
        return dataList.size
    }

}

class ClutchesViewHolder(itemView: View, private val dataList: MutableList<EggData>) :
    RecyclerView.ViewHolder(itemView) {

    val tvTotal: TextView = itemView.findViewById(R.id.tvTotal)
    val tvIncubating: TextView = itemView.findViewById(R.id.tvIncubating)
    val tvLaid: TextView = itemView.findViewById(R.id.tvLaid)
    val tvHatched: TextView = itemView.findViewById(R.id.tvHatched)
    val tvNotFertilized: TextView = itemView.findViewById(R.id.tvNotFertilized)
    val tvAbandon: TextView = itemView.findViewById(R.id.tvAbandon)
    val tvBroken: TextView = itemView.findViewById(R.id.tvBroke)
    val tvDeadInShell: TextView = itemView.findViewById(R.id.tvDeadInShell)
    val tvDeadBeforeMoving: TextView = itemView.findViewById(R.id.tvDeadBeforeRinging)
    val tvMoved: TextView = itemView.findViewById(R.id.tvMove)
    val tvDate: TextView = itemView.findViewById(R.id.tvDate)
    val btnParent: MaterialButton = itemView.findViewById(R.id.parents)
    val btnFoster: MaterialButton = itemView.findViewById(R.id.fosterpair)

    init {
        itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("CageKey", dataList[adapterPosition].paircagekey)
            bundle.putString("PairKey", dataList[adapterPosition].pairKey)
            bundle.putString("EggKey", dataList[adapterPosition].eggKey)
            bundle.putString("PairFlightMaleKey", dataList[adapterPosition].pairFlightMaleKey)
            bundle.putString("PairFlightFemaleKey", dataList[adapterPosition].pairFlightFemaleKey)
            bundle.putString("PairMaleKey", dataList[adapterPosition].pairBirdMaleKey)
            bundle.putString("PairFemaleKey", dataList[adapterPosition].pairBirdFemaleKey)
            bundle.putString("PairMaleID", dataList[adapterPosition].pairMaleId)
            bundle.putString("PairFemaleID", dataList[adapterPosition].pairFemaleId)
            bundle.putString("CageKeyFemale", dataList[adapterPosition].eggcagekeyFemale)
            bundle.putString("CageKeyMale", dataList[adapterPosition].eggcagekeyMale)
            bundle.putString("CageBirdFemale", dataList[adapterPosition].eggcagebirdFemale)
            bundle.putString("CageBirdMale", dataList[adapterPosition].eggcagebirdMale)
            val i = Intent(itemView.context, ClutchesDetailedActivity::class.java)
            i.putExtras(bundle)
            itemView.context.startActivity(i)
        }
    }

}