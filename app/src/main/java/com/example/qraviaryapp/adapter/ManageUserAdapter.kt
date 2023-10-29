package com.example.qraviaryapp.adapter

import AccountData
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.qraviaryapp.R

class ManageUserAdapter(
    private val context: Context,
    private val dataList: MutableList<AccountData>
) : RecyclerView.Adapter<AccountViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.item_savelogin, parent, false)

        return AccountViewHolder(view, dataList)
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        val account = dataList[position]

        holder.tvEmail.text = account.username


    }

    override fun getItemCount(): Int {
        return dataList.size


    }

}

class AccountViewHolder(itemView: View, private val dataList: MutableList<AccountData>) :
    RecyclerView.ViewHolder(itemView) {

    var tvName: TextView = itemView.findViewById(R.id.tvName)
    var tvEmail: TextView = itemView.findViewById(R.id.tvEmail)


}