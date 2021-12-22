package com.example.medic.View

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.medic.Model.Doctor
import com.example.medic.Model.Medication
import com.example.medic.R

class DoctorAdapter(private val doctorList: ArrayList<Doctor>) : RecyclerView.Adapter<DoctorAdapter.MyViewHolder>() {
    private val TAG = "Medication Adapter"

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val profilePicture: ImageView = itemView.findViewById(R.id.profilePicture)
        val nameText: TextView = itemView.findViewById(R.id.nameText)
        val phoneText: TextView = itemView.findViewById(R.id.phoneText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_doctor, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = doctorList[position]

//        if (currentItem.profilePicture != null) {
//            holder.profilePicture.setImageBitmap(currentItem.profilePicture)
//        }
        holder.nameText.text = currentItem.name + " " + currentItem.surname
        holder.phoneText.text = currentItem.phoneNumber.toString()
    }

    override fun getItemCount(): Int {
        return doctorList.size
    }
}