package com.example.qraviaryapp.adapter

import AccountData
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.mainactivities.NavHomeActivity
import com.google.firebase.auth.FirebaseAuth

class SaveLoginAdapter(
    private val context: Context,
    private val dataList: MutableList<AccountData>
) : RecyclerView.Adapter<SaveLoginHolder>() {

    private lateinit var mAuth: FirebaseAuth
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaveLoginHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.item_savelogin, parent, false)

        mAuth = FirebaseAuth.getInstance()

        return SaveLoginHolder(view, dataList)
    }

    override fun onBindViewHolder(holder: SaveLoginHolder, position: Int) {
        val account = dataList[position]

        holder.tvEmail.text = account.username


        holder.itemView.setOnClickListener {
            val email = account.username
            val password = account.password

            mAuth.signInWithEmailAndPassword(email.toString(),password.toString()).addOnCompleteListener(){
                if (it.isSuccessful){
                    holder.itemView.context.startActivity(Intent(context, NavHomeActivity::class.java))
                }

            }


        }

    }

    override fun getItemCount(): Int {
        return dataList.size


    }

}




class SaveLoginHolder(itemView: View, private val dataList: MutableList<AccountData>) :
    RecyclerView.ViewHolder(itemView) {


    var tvEmail: TextView = itemView.findViewById(R.id.tvEmail)


}