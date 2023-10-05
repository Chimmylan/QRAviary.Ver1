package com.example.qraviaryapp.adapter

import BirdData
import PairData
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.detailedactivities.PairsDetailedActivity
import org.w3c.dom.Text

class PairListAdapter(
    private val context: Context,
    private val dataList: MutableList<PairData>,
) : RecyclerView.Adapter<PairBirdViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PairBirdViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_pairlist, parent, false)

        return PairBirdViewHolder(view, dataList)
    }

    override fun onBindViewHolder(holder: PairBirdViewHolder, position: Int) {
        val pairs = dataList[position]
        holder.dateId.text = pairs.pairDateBeg
        holder.femaleBird.text = pairs.pairFemale
        holder.femaleMutation.text = pairs.pairFemaleMutation
        holder.maleBird.text = pairs.pairMale
        holder.maleMutation.text = pairs.pairMaleMutation

    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}

class PairBirdViewHolder(itemView: View, private val dataList: MutableList<PairData>) :
    RecyclerView.ViewHolder(itemView) {
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
            bundle.putString("PairFlightMaleKey", dataList[adapterPosition].pairFlightMaleKey)
            bundle.putString("PairFlightFemaleKey", dataList[adapterPosition].pairFlightFemaleKey)
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
            val i = Intent(itemView.context, PairsDetailedActivity::class.java)
            i.putExtras(bundle)
            itemView.context.startActivity(i)
        }
    }
}