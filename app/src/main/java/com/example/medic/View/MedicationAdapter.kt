package com.example.medic.View

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.medic.Model.Medication
import com.example.medic.R

class MedicationAdapter(private val medicationList: ArrayList<Medication>) : RecyclerView.Adapter<MedicationAdapter.MyViewHolder>() {
    private val TAG = "Medication Adapter"

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameText: TextView = itemView.findViewById(R.id.nameText)
        val tpdText: TextView = itemView.findViewById(R.id.tpdText)
        val createAlarmButton: Button = itemView.findViewById(R.id.createAlarmButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_medication, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = medicationList[position]

        holder.nameText.text = currentItem.name
        holder.tpdText.text = currentItem.tpd.toString()

        holder.createAlarmButton.setOnClickListener() {
            Log.e(TAG, "Alarm button created")
        }
    }

    override fun getItemCount(): Int {
        return medicationList.size
    }
}