package com.example.qraviaryapp.adapter.DetailedAdapter

import EggData
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

class EggAdapter(
    private val context: android.content.Context,
    private var dataList: MutableList<EggData>
) : RecyclerView.Adapter<ClutchesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClutchesViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_clutcheslist, parent, false)

        return ClutchesViewHolder(view, dataList)
    }

    override fun onBindViewHolder(holder: ClutchesViewHolder, position: Int) {
        val clutch = dataList[position]

        val eggCount = clutch.eggCount
        val incubatingCount = clutch.eggIncubating
        val laidCount = clutch.eggLaid
        val incubateDate = clutch.eggIncubationStartDate
        val laidDate = clutch.eggLaidStartDate

        holder.tvTotal.text = eggCount
        if (incubatingCount != null) {
            if (incubatingCount > 0.toString()){
                holder.tvIncubating.visibility = View.VISIBLE
                holder.tvIncubating.text = "$incubatingCount Incubating"
                holder.tvDate.text = incubateDate
            }
        }
        if (laidCount != null) {
            if (laidCount > 0.toString()){
                holder.tvLaid.visibility = View.VISIBLE
                holder.tvLaid.text = "$laidCount Laid"
                holder.tvDate.text = laidDate

            }
        }

        Log.d(TAG,clutch.eggKey.toString())

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
    val tvDate: TextView = itemView.findViewById(R.id.tvDate)

    init {
        itemView.setOnClickListener {
            val bundle = Bundle()

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