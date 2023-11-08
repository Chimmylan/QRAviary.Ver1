package com.example.qraviaryapp.adapter

import CageData
import ClickListener
import PairData
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.AddActivities.GiveFosterActivity
import com.example.qraviaryapp.activities.dashboards.BreedingCagesListActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FosterAdapter(
    private val context: android.content.Context,
    private var dataList: MutableList<PairData>,
    private val clickListener: ClickListener
) : RecyclerView.Adapter<CageFosterViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CageFosterViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_currentpair, parent, false)


        return CageFosterViewHolder(view, dataList, context as GiveFosterActivity)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: CageFosterViewHolder, position: Int) {
        val pairs = dataList[position]
        val femaleimg = pairs.pairfemaleimg
        val maleimg = pairs.pairmaleimg

        holder.pairId.text = "${pairs.pairId}"


        Log.d(ContentValues.TAG, "female $femaleimg")
        Log.d(ContentValues.TAG, "male $maleimg")
        holder.dateId.text = pairs.pairDateBeg
        holder.femaleBird.text = pairs.pairFemale
        holder.femaleMutation.text = pairs.pairFemaleMutation
        holder.maleBird.text = pairs.pairMale
        holder.maleMutation.text = pairs.pairMaleMutation

    }


}

class CageFosterViewHolder(
    itemView: View,
    private val dataList: MutableList<PairData>,
    private val activity: GiveFosterActivity
) :
    RecyclerView.ViewHolder(itemView) {

    private var mAuth = FirebaseAuth.getInstance()
    private var dbase = FirebaseDatabase.getInstance().reference
    var currentUserId = mAuth.currentUser?.uid

    var pairId: TextView = itemView.findViewById(R.id.tvidpairs)

    var maleBird: TextView = itemView.findViewById(R.id.tvmaleid)
    var femaleBird: TextView = itemView.findViewById(R.id.tvfemaleid)
    var maleMutation: TextView = itemView.findViewById(R.id.tvMaleMutation)
    var femaleMutation: TextView = itemView.findViewById(R.id.tvFemaleMutation)
    var dateId: TextView = itemView.findViewById(R.id.tvDate)


    init {
        itemView.setOnClickListener {
            val pairRef = dbase.child("Users").child("ID: $currentUserId").child("Pairs")
                .child(dataList[adapterPosition].parentPairKey.toString()).child("Clutches")
                .child(dataList[adapterPosition].pairClutchKey.toString())
            val destinationPairRef = dbase.child("Users").child("ID: $currentUserId").child("Pairs")
                .child(dataList[adapterPosition].pairKey.toString()).child("Clutches")


            val parentData = dbase.child("Users").child("ID: $currentUserId").child("Pairs")
                .child(dataList[adapterPosition].parentPairKey.toString()).ref

            var parentKey: String? = null


            parentData.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    parentKey = snapshot.key
                    val parentDataBody = snapshot.value

                    pairRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {

                            val key = snapshot.key
                            val data = snapshot.value


//                    val clutchData: HashMap<String, Any?> = hashMapOf(
//                        key.toString() to data
//                    )

                            destinationPairRef.child(key.toString()).setValue(data)
                            destinationPairRef.child(key.toString()).child("Parent").child(parentKey.toString()).setValue(parentDataBody)
                            destinationPairRef.child(key.toString()).child("Parent").child("ParentKey").setValue(parentKey.toString())

                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })


            activity.finish()
        }

    }


}
