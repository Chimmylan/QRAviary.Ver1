package com.example.qraviaryapp.adapter

import PairData
import android.content.ContentValues.TAG
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
import java.text.SimpleDateFormat
import java.util.Locale

class PairListAdapter(
    private val context: Context,
    private val dataList: MutableList<PairData>,
) : RecyclerView.Adapter<PairBirdViewHolder>() {
    private var originalList = dataList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PairBirdViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_pairlist, parent, false)

        return PairBirdViewHolder(view, dataList)
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
        Log.d(TAG, "female $femaleimg")
        Log.d(TAG, "male $maleimg")
        holder.dateId.text = pairs.pairDateBeg
        holder.femaleBird.text = pairs.pairFemale
        holder.femaleMutation.text = pairs.pairFemaleMutation
        holder.maleBird.text = pairs.pairMale
        holder.maleMutation.text = pairs.pairMaleMutation

    }

    fun filterDataRange(
        fromDate: String? = null,
        toDate: String? = null,

    ) {
        val dateFormat = SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH)

        val fromDateObj = fromDate?.let { dateFormat.parse(it) }
        val toDateObj = toDate?.let { dateFormat.parse(it) }

        val filteredList = originalList.filter { bird ->
            val pairDateBeg = bird.pairDateBeg


            val soldDateObj = pairDateBeg?.let { dateFormat.parse(it) }

            val isDateInRange = (fromDateObj == null || toDateObj == null || (soldDateObj != null && soldDateObj.after(fromDateObj) && soldDateObj.before(toDateObj)))

            isDateInRange
        }

        setData(filteredList.toMutableList())
    }



    private fun setData(newData: MutableList<PairData>) {
        dataList.clear()
        dataList.addAll(newData)
        notifyDataSetChanged()
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
            bundle.putString("CageKeyFemale", dataList[adapterPosition].paircagekeyFemale)//
            bundle.putString("CageKeyMale", dataList[adapterPosition].paircagekeyMale)//
            bundle.putString("CageBirdFemale", dataList[adapterPosition].paircagebirdFemale)//
            bundle.putString("CageBirdMale", dataList[adapterPosition].paircagebirdMale)//
            bundle.putString("CageKey", dataList[adapterPosition].pairCageKey)
            bundle.putString("CagePairKey", dataList[adapterPosition].cagePairKey)
            bundle.putString("PairCage", dataList[adapterPosition].pairCage)
            val i = Intent(itemView.context, PairsDetailedActivity::class.java)
            i.putExtras(bundle)
            itemView.context.startActivity(i)
        }
    }
}