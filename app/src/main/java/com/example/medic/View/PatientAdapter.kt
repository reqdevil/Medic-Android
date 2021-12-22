package com.example.medic.View

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.medic.Model.Patient
import com.example.medic.R


class PatientAdapter(private val patientsList: ArrayList<Patient>, val activity: FragmentActivity, val callback: (Int, Boolean) -> Unit, val dialogCallback: (Patient) -> Unit) : RecyclerView.Adapter<PatientAdapter.MyViewHolder>() {
    private val TAG = "My Adapter"

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //        val profilePicture: ImageView = itemView.findViewById(R.id.profilePicture)
        val usernameText: TextView = itemView.findViewById(R.id.usernameText)
        val fullNameText: TextView = itemView.findViewById(R.id.fullNameText)
        val selectPatient: CheckBox = itemView.findViewById(R.id.selectPatient)
        val addMedication: Button = itemView.findViewById(R.id.addMedicationButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_patient, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = patientsList[position]

//        if (currentItem.profileImage != null) {
//            holder.profilePicture.setImageBitmap(currentItem.profileImage)
//        }
        holder.usernameText.text = currentItem.username
        holder.fullNameText.text = currentItem.name + " " + currentItem.surname
        holder.selectPatient.setOnClickListener() { callback(position, holder.selectPatient.isChecked) }
        holder.addMedication.setOnClickListener() { dialogCallback(currentItem) }
    }

    override fun getItemCount(): Int {
        return patientsList.size
    }


}