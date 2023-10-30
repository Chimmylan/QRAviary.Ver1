package com.example.qraviaryapp.adapter

import AccountData
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.qraviaryapp.R

class SaveLoginAdapter(
    private val context: Context,
    private val dataList: MutableList<AccountData>
) : RecyclerView.Adapter<SaveLoginHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaveLoginHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.item_savelogin, parent, false)

        return SaveLoginHolder(view, dataList)
    }

    override fun onBindViewHolder(holder: SaveLoginHolder, position: Int) {
        val account = dataList[position]

        holder.tvEmail.text = account.username


    }

    override fun getItemCount(): Int {
        return dataList.size


    }

}

class SaveLoginHolder(itemView: View, private val dataList: MutableList<AccountData>) :
    RecyclerView.ViewHolder(itemView) {


    var tvEmail: TextView = itemView.findViewById(R.id.tvEmail)


}