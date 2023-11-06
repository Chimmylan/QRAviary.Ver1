package com.example.qraviaryapp.adapter

import PairData
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.detailedactivities.PreviousPairActivity

class PreviousPairAdapter(
    private val context: Context,
    private val dataList: MutableList<PairData>,
) : RecyclerView.Adapter<PreviousViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviousViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_previous_pairlist, parent, false)

        return PreviousViewHolder(view, dataList)
    }
    fun getHeaderForPosition(position: Int): String {
        if (position < 0 || position >= dataList.size) {
            return ""
        }
        // Assuming dataList is sorted by mutation name
        return dataList[position].pairyearbeg?.substring(0, 4) ?: ""
    }
    override fun onBindViewHolder(holder: PreviousViewHolder, position: Int) {
        val pairs = dataList[position]
        val femaleimg = pairs.pairfemaleimg
        val maleimg = pairs.pairmaleimg


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
        holder.dateId.text = pairs.pairDateBeg + "-" + pairs.pairDateSep
        holder.femaleBird.text = pairs.pairFemale
        holder.femaleMutation.text = pairs.pairFemaleMutation
        holder.maleBird.text = pairs.pairMale
        holder.maleMutation.text = pairs.pairMaleMutation

    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}

class PreviousViewHolder(itemView: View, private val dataList: MutableList<PairData>) :
    RecyclerView.ViewHolder(itemView) {
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

            bundle.putString("PairMaleKey", dataList[adapterPosition].pairMaleKey)
            bundle.putString("PairFemaleKey", dataList[adapterPosition].pairFemaleKey)
            bundle.putString("PairKey", dataList[adapterPosition].pairKey)
            bundle.putString("MaleID", dataList[adapterPosition].pairMale)
            bundle.putString("FemaleID", dataList[adapterPosition].pairFemale)
            bundle.putString("BeginningDate", dataList[adapterPosition].pairDateBeg)
            bundle.putString("SeparateDate", dataList[adapterPosition].pairDateSep)
            bundle.putString("MaleGender", dataList[adapterPosition].pairMaleMutation)
            bundle.putString("FemaleGender", dataList[adapterPosition].pairFemaleMutation)
            bundle.putString("CageKeyFemale", dataList[adapterPosition].paircagekeyFemale)
            bundle.putString("CageKeyMale", dataList[adapterPosition].paircagekeyMale)
            bundle.putString("CageBirdFemale", dataList[adapterPosition].paircagebirdFemale)
            bundle.putString("CageBirdMale", dataList[adapterPosition].paircagebirdMale)
            bundle.putString("CageKey", dataList[adapterPosition].pairCageKey)
            bundle.putString("CagePairKey", dataList[adapterPosition].cagePairKey)
            val i = Intent(itemView.context, PreviousPairActivity::class.java)
            i.putExtras(bundle)
            itemView.context.startActivity(i)
        }
    }
}