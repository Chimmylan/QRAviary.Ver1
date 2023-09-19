package com.example.qraviaryapp.adapter.DetailedAdapter


import BirdData
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.qraviaryapp.R

class BirdGalleryAdapter(private val context: Context,
                         private var imageList: MutableList<BirdData>
): RecyclerView.Adapter<BirdGalleryHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BirdGalleryHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_gallery,parent, false)

        return BirdGalleryHolder(view,imageList)

    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: BirdGalleryHolder, position: Int) {
        val imageLists = imageList[position]

        Glide.with(context)
            .load(imageLists.imgUrl)
            .into(holder.imgItem)
    }

}

class BirdGalleryHolder(itemView: View, private val imageList: MutableList<BirdData>)
    : RecyclerView.ViewHolder(itemView){

    var imgItem : ImageView = itemView.findViewById(R.id.imgItem)

}