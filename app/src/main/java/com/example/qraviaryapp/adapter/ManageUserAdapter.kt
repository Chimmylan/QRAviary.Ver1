package com.example.qraviaryapp.adapter

import AccountData
import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
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

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var originalList: MutableList<AccountData>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.item_manageuser, parent, false)

        originalList = dataList


        sharedPreferences = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)

        return AccountViewHolder(view, dataList)
    }




    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        val account = dataList[position]

        holder.tvEmail.text = account.username

        holder.optionmenu.setOnClickListener { view ->
            showPopupMenu(view,dataList[position], this)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size


    }

    fun deleteAccount(username: String, password: String, adapter: ManageUserAdapter){
        val maxAccount = 4

        for(i in 1..maxAccount){
            val userKey = "user$i"
            val passKey = "userpass$i"
            val userName = sharedPreferences.getString(userKey, "")
            val userPassword = sharedPreferences.getString(passKey, "")
            if (userName == username && userPassword == password ){
                sharedPreferences.edit().remove(userKey).apply()
                sharedPreferences.edit().remove(passKey).apply()
                // Remove the item from the dataList
                val removedItem = dataList.find { it.username == username && it.password == password }
                removedItem?.let { dataList.remove(it) }
                // Notify the adapter about the data change
                adapter.notifyDataSetChanged()
                return  // Exit the loop after finding and removing the item
            }
        }
    }

    private fun showPopupMenu(view: View, dataList: AccountData, adapter: ManageUserAdapter) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.menuInflater.inflate(R.menu.useroption, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_edit -> {
                    // Handle the edit action here
                    return@setOnMenuItemClickListener true
                }
                R.id.menu_delete -> {

                    deleteAccount(dataList.username.toString(), dataList.password.toString(), adapter)


                    return@setOnMenuItemClickListener true
                }
                else -> false
            }
        }

        popupMenu.show()
    }

}




class AccountViewHolder(itemView: View, private val dataList: MutableList<AccountData>) :
    RecyclerView.ViewHolder(itemView) {


    var tvEmail: TextView = itemView.findViewById(R.id.tvEmail)
    var optionmenu: ImageView = itemView.findViewById(R.id.optionsButton)



    }


