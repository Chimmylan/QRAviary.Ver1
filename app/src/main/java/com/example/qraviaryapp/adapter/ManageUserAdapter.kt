package com.example.qraviaryapp.adapter

import AccountData
import android.content.Context
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.qraviaryapp.R

class ManageUserAdapter(
    private val context: Context,
    private val dataList: MutableList<AccountData>
) : RecyclerView.Adapter<AccountViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.item_manageuser, parent, false)


        return AccountViewHolder(view, dataList)
    }



    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        val account = dataList[position]

        holder.tvEmail.text = account.username

        holder.optionmenu.setOnClickListener { view ->
            showPopupMenu(view)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size


    }

}
    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.menuInflater.inflate(R.menu.useroption, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_edit -> {
                    // Handle the edit action here
                    return@setOnMenuItemClickListener true
                }
                R.id.menu_delete -> {
                    // Handle the delete action here
                    return@setOnMenuItemClickListener true
                }
                else -> false
            }
        }

        popupMenu.show()
    }

class AccountViewHolder(itemView: View, private val dataList: MutableList<AccountData>) :
    RecyclerView.ViewHolder(itemView) {


    var tvEmail: TextView = itemView.findViewById(R.id.tvEmail)
    var optionmenu: ImageView = itemView.findViewById(R.id.optionsButton)



    }


