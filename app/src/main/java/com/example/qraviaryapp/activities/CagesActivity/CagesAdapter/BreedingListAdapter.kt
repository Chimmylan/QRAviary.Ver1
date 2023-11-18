package com.example.qraviaryapp.activities.CagesActivity.CagesAdapter

import PairData
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
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
import com.example.qraviaryapp.activities.detailedactivities.PairsDetailedActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class BreedingListAdapter(
    private val context: Context,
    private val dataList: MutableList<PairData>,
) : RecyclerView.Adapter<PairBirdViewHolder>() {
    private lateinit var delete: ImageView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PairBirdViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_pairlist, parent, false)


        return PairBirdViewHolder(view, dataList)
    }
    private fun showDeleteDialog(bird: PairData, position: Int) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("Delete Bird")
        alertDialogBuilder.setMessage("Are you sure you want to remove this pair from the cage?")

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

    private fun deleteBird(bird: PairData, position: Int) {

        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

        val breedingPath = FirebaseDatabase.getInstance().reference.child("Users").child("ID: $currentUserId")
            .child("Cages").child("Breeding Cages").child(bird.cageKey.toString()).child("Pair Birds")
            .child(bird.pairCageKey.toString()).removeValue()

        dataList.removeAt(position)
        notifyItemRemoved(position)
    }
    override fun onBindViewHolder(holder: PairBirdViewHolder, position: Int) {
        val pairs = dataList[position]
        val femaleimg = pairs.pairfemaleimg
        val maleimg = pairs.pairmaleimg

        holder.pairId.text = "${pairs.pairId}"

        if (femaleimg.isNullOrEmpty()) {
            Glide.with(context)
                .load(R.drawable.noimage)
                .placeholder(R.drawable.noimage)
                .error(R.drawable.noimage)
                .into(holder.femaleimageview)

        } else {
            Glide.with(context)
                .load(femaleimg)
                .placeholder(R.drawable.noimage)
                .error(R.drawable.noimage)
                .into(holder.femaleimageview)
        }
        if (maleimg.isNullOrEmpty()) {
            Glide.with(context)
                .load(R.drawable.noimage)
                .placeholder(R.drawable.noimage)
                .error(R.drawable.noimage)
                .into(holder.maleimageview)

        } else {
            Glide.with(context)
                .load(maleimg)
                .placeholder(R.drawable.noimage)
                .error(R.drawable.noimage)
                .into(holder.maleimageview)
        }
        Log.d(ContentValues.TAG, "female $femaleimg")
        Log.d(ContentValues.TAG, "male $maleimg")
        holder.dateId.text = pairs.pairDateBeg
        holder.femaleBird.text = pairs.pairFemale
        holder.femaleMutation.text = pairs.pairFemaleMutation
        holder.maleBird.text = pairs.pairMale
        holder.maleMutation.text = pairs.pairMaleMutation

        holder.delete.visibility = View.VISIBLE
        holder.delete.setOnClickListener {
            showDeleteDialog(pairs, position)
        }

    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}

class PairBirdViewHolder(itemView: View, private val dataList: MutableList<PairData>) :
    RecyclerView.ViewHolder(itemView) {
    var pairId: TextView = itemView.findViewById(R.id.tvidpairs)
    var maleimageview: ImageView = itemView.findViewById(R.id.maleImageView)
    var femaleimageview: ImageView = itemView.findViewById(R.id.femaleImageView)
    var maleBird: TextView = itemView.findViewById(R.id.tvmaleid)
    var femaleBird: TextView = itemView.findViewById(R.id.tvfemaleid)
    var maleMutation: TextView = itemView.findViewById(R.id.tvMaleMutation)
    var femaleMutation: TextView = itemView.findViewById(R.id.tvFemaleMutation)
    var dateId: TextView = itemView.findViewById(R.id.tvDate)
    var delete: ImageView = itemView.findViewById(R.id.options)
    init {
        itemView.setOnClickListener {
            val bundle = Bundle()

            bundle.putString("PairFemaleImg", dataList[adapterPosition].pairfemaleimg)//
            bundle.putString("PairMaleImg", dataList[adapterPosition].pairmaleimg)//
            bundle.putString("PairId", dataList[adapterPosition].pairId)//
            bundle.putString("PairMaleKey", dataList[adapterPosition].pairMaleKey)//
            bundle.putString("PairFemaleKey", dataList[adapterPosition].pairFemaleKey)//
            bundle.putString("PairFlightMaleKey", dataList[adapterPosition].pairFlightMaleKey)//
            bundle.putString("PairFlightFemaleKey", dataList[adapterPosition].pairFlightFemaleKey)//
            bundle.putString("PairKey", dataList[adapterPosition].pairKey)//
            bundle.putString("MaleID", dataList[adapterPosition].pairMale)//
            bundle.putString("FemaleID", dataList[adapterPosition].pairFemale)//
            bundle.putString("BeginningDate", dataList[adapterPosition].pairDateBeg)//
            bundle.putString("SeparateDate", dataList[adapterPosition].pairDateSep)// /toodo
            bundle.putString("MaleGender", dataList[adapterPosition].pairMaleMutation)//
            bundle.putString("FemaleGender", dataList[adapterPosition].pairFemaleMutation)//
            bundle.putString("CagePairKey", dataList[adapterPosition].cagePairKey)
            bundle.putString("CageKeyFemale", dataList[adapterPosition].paircagekeyFemale)//
            bundle.putString("CageKeyMale", dataList[adapterPosition].paircagekeyMale)//
            bundle.putString("CageBirdFemale", dataList[adapterPosition].paircagebirdFemale)//
            bundle.putString("CageBirdMale", dataList[adapterPosition].paircagebirdMale)//
            val i = Intent(itemView.context, PairsDetailedActivity::class.java)
            i.putExtras(bundle)
            itemView.context.startActivity(i)
        }
    }
}
