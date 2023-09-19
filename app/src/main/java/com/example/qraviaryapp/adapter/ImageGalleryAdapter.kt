package com.example.qraviaryapp.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.qraviaryapp.R

class ImageGalleryAdapter(private val context: android.content.Context,
                          private var imageList: MutableList<Uri>): RecyclerView.Adapter<MyGalleryHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyGalleryHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_gallerylist, parent, false)

        return MyGalleryHolder(view,imageList)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: MyGalleryHolder, position: Int) {
        val imageLists = imageList[position]

        holder.imgItem.setImageURI(imageLists)

        holder.delete.setOnClickListener{
            val alertDialog = AlertDialog.Builder(holder.itemView.context)
                .setTitle("Delete Image")
                .setMessage("Are you sure you want to delete this image?")
                .setPositiveButton("Delete"){_,_->
                    imageList.removeAt(position)
                    notifyDataSetChanged()
                }
                .setNegativeButton("Cancel", null)
                .create()
            alertDialog.show()
            true
        }
    }
}

class MyGalleryHolder(itemView: View, private val imageList: MutableList<Uri>)
    : RecyclerView.ViewHolder(itemView){

        var imgItem : ImageView = itemView.findViewById(R.id.imgItem)
        var delete : ImageView = itemView.findViewById(R.id.deleteImgView)

}
