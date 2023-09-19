package com.example.qraviaryapp.adapter

import EggData
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.qraviaryapp.R

class EggClutchesListAdapter(
    private val context: Context,
    private val dataList: MutableList<EggData>
) : RecyclerView.Adapter<EggClutchesHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EggClutchesHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_eggclutch, parent, false)

        return EggClutchesHolder(view, dataList)
    }

    override fun onBindViewHolder(holder: EggClutchesHolder, position: Int) {
        val eggs = dataList[position]

        holder.tvStatus.text = eggs.eggStatus
        holder.tvDate.text = eggs.eggDate

    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}

class EggClutchesHolder(itemvView: View, private val dataList: MutableList<EggData>) :
    RecyclerView.ViewHolder(itemvView) {
    val tvStatus: TextView = itemvView.findViewById(R.id.tvStatus)
    val tvDate: TextView = itemvView.findViewById(R.id.tvDate)

}