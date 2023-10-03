package com.example.qraviaryapp.adapter

import BirdData
import ClickListener
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.dashboards.MaleBirdListActivity
import com.example.qraviaryapp.activities.dashboards.PairMaleBirdListActivity

class PairMaleBirdListAdapter
    (
    private val context: Context,
    private val dataList: MutableList<BirdData>,
    private val clickListener: ClickListener
) : RecyclerView.Adapter<PairMaleBirdViewHolder>() {

    companion object {
        private const val MAX_MUTATION_LENGTH = 10
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PairMaleBirdViewHolder {


        val view = LayoutInflater.from(context).inflate(R.layout.item_birdlist,parent,false)

        return PairMaleBirdViewHolder(view, dataList, context as PairMaleBirdListActivity)
    }



    override fun onBindViewHolder(holder: PairMaleBirdViewHolder, position: Int) {
        val bird = dataList[position]

        val maxIdentifierLength = 5 // Define your desired maximum length here
        val identifierText = if (bird.identifier?.length!! > maxIdentifierLength) {
            bird.identifier?.substring(0, maxIdentifierLength) + "..." // Add ellipsis
        } else {
            bird.identifier
        }

        holder.tvIdentifier.text = "ID: $identifierText"


        val mutationList = mutableListOf(
            bird.mutation1,
            bird.mutation2,
            bird.mutation3,
            bird.mutation4,
            bird.mutation5,
            bird.mutation6
        )


        val nonNullMutations = mutationList.filter { !it.isNullOrBlank() }


        val combinedMutations = nonNullMutations.joinToString("\nMutation: ")


        if (combinedMutations.length > BirdListAdapter.MAX_MUTATION_LENGTH) {
            val trimmedMutations = combinedMutations.substring(0,
                BirdListAdapter.MAX_MUTATION_LENGTH
            ) + "..." // Add ellipsis
            holder.tvMutation.text = "Mutations: $trimmedMutations"
        } else {
            holder.tvMutation.text = "Mutations: $combinedMutations"
        }

        holder.tvStatus.text = bird.status
        val genderIcon = if(bird.gender == "Male"){
            R.drawable.baseline_male_24
        } else if (bird.gender == "Female") {
            R.drawable.female_logo
        }else{
            R.drawable.bird
        }

        holder.imageGender.setImageResource(genderIcon)
        holder.tvGender.text = bird.gender

    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}

class PairMaleBirdViewHolder(
    itemView: View,
    private val dataList: MutableList<BirdData>,
    private val activity: PairMaleBirdListActivity
) : RecyclerView.ViewHolder(itemView) {

    var tvIdentifier: TextView = itemView.findViewById(R.id.tvIdentifier)
    var tvMutation: TextView = itemView.findViewById(R.id.tvMutation)
    var tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
    var tvGender: TextView = itemView.findViewById(R.id.tvGender)
    var imageGender: ImageView = itemView.findViewById(R.id.GenderImageView)


    init {
        itemView.setOnClickListener {

            val maleBirdKey = dataList[adapterPosition].birdKey
            val maleFlightKey = dataList[adapterPosition].flightKey
            val maleBirdMutation = dataList[adapterPosition].mutation1
            val maleBird =
                dataList[adapterPosition].identifier // Retrieve the cage name from the data list
            val cagekey = dataList[adapterPosition].cagekeymalevalue
            val cagebirdkey = dataList[adapterPosition].cagebirdmalekey
            val intent = Intent()
            intent.putExtra("MaleBirdId", maleBird)
            intent.putExtra("MaleBirdKey", maleBirdKey)
            intent.putExtra("MaleFlightKey", maleFlightKey)
            intent.putExtra("MaleMutation", maleBirdMutation)
            intent.putExtra("CageKeyMale",cagekey)
            intent.putExtra("CageBirdKeyMale",cagebirdkey)
            activity.setResult(Activity.RESULT_OK, intent)

            activity.finish()
        }

    }

}