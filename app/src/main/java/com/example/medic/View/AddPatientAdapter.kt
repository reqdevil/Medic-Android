package com.example.medic.View

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.medic.Model.Patient
import com.example.medic.R
import com.example.medic.Services.AuthenticationService
import com.example.medic.Services.DatabaseService

class AddPatientAdapter(private val patientsList: ArrayList<Patient>) : RecyclerView.Adapter<AddPatientAdapter.MyViewHolder>() {
    private val TAG = "My Adapter"

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //        val profilePicture: ImageView = itemView.findViewById(R.id.profilePicture)
        val usernameText: TextView = itemView.findViewById(R.id.usernameText)
        val fullNameText: TextView = itemView.findViewById(R.id.fullNameText)
        val addPerson: Button = itemView.findViewById(R.id.addPersonButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_add_patient, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = patientsList[position]
//
//        if (currentItem.profileImage != null) {
//            holder.profilePicture.setImageBitmap(currentItem.profileImage)
//        }
        holder.usernameText.text = currentItem.username
        holder.fullNameText.text = currentItem.name + " " + currentItem.surname
        holder.addPerson.setOnClickListener() { addPatient(AuthenticationService.instance.getUID(), patientsList, position) }
    }

    override fun getItemCount(): Int {
        return patientsList.size
    }

    private fun addPatient(uid: String, patientsList: ArrayList<Patient>, position: Int) {
        DatabaseService.instance.addPatient(uid, patientsList[position])
    }
}