package com.example.qraviaryapp.adapter

import BirdData
import ClickListener
import MutationData
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.dashboards.MutationsActivity

class GenesAdapter(
    private val context: android.content.Context,
    private val dataList: MutableList<MutationData>,
    private val clickListener: ClickListener
)
    : RecyclerView.Adapter<GeneViewHolder>() {


    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_GENE = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GeneViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_gene,parent,false)

        return GeneViewHolder(view, dataList, context as MutationsActivity)
    }

    override fun onBindViewHolder(holder: GeneViewHolder, position: Int) {
        val mutation = dataList[position]

        holder.geneTextView.text = mutation.mutations

    }
    override fun getItemCount(): Int {
        return dataList.size
    }


  /*  class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val headerTextView: TextView = itemView.findViewById(R.id.headerTextView)

        fun bind(header: String) {
            headerTextView.text = header
        }
    }*/
}
class GeneViewHolder(itemView: View, private val dataList: MutableList<MutationData>, private val activity: MutationsActivity) : RecyclerView.ViewHolder(itemView) {
    val geneTextView: TextView = itemView.findViewById(R.id.geneTextView)

    init {
        itemView.setOnClickListener {
            val mutationName = dataList[adapterPosition].mutations
            val mutationIncubatingDays = dataList[adapterPosition].mutationsIncubateDays
            val mutationMaturingDays = dataList[adapterPosition].mutationsMaturingDays

            val intent = Intent()
            intent.putExtra("selectedMutationId", mutationName)
            intent.putExtra("selectedMutationMaturingDays", mutationMaturingDays)
            intent.putExtra("selectedMutationIncubatingDays", mutationIncubatingDays)

            activity.setResult(Activity.RESULT_OK, intent)
            activity.finish()

        }
    }

}
