package com.example.qraviaryapp.adapter

import MutationData
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.qraviaryapp.R

class HomeGenesAdapter(
    private val context: Context,
    private val dataList: MutableList<MutationData>
)
    : RecyclerView.Adapter<HomeGeneViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeGeneViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_gene,parent,false)

        return HomeGeneViewHolder(view, dataList)
    }

    override fun onBindViewHolder(holder: HomeGeneViewHolder, position: Int) {
        val mutation = dataList[position]

        holder.geneTextView.text = mutation.mutations

    }
    override fun getItemCount(): Int {
        return dataList.size
    }



}
class HomeGeneViewHolder(itemView: View, private val dataList: MutableList<MutationData>) : RecyclerView.ViewHolder(itemView) {
    val geneTextView: TextView = itemView.findViewById(R.id.geneTextView)


}
