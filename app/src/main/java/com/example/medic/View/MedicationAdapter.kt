package com.example.medic.View

import android.content.Context
import android.content.IntentSender
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.medic.Interface.OnDataPass
import com.example.medic.Model.Medication
import com.example.medic.R

class MedicationAdapter(private val medicationList: ArrayList<Medication>, private val context: Context, private val TAG_FRAGMENT: String) : RecyclerView.Adapter<MedicationAdapter.MyViewHolder>() {
    private val TAG = "Medication Adapter"
    lateinit var dataPass: OnDataPass

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
//            startCounter(holder)
            sendNotification(holder, 0)
        }
    }

    override fun getItemCount(): Int {
        return medicationList.size
    }

    private fun startCounter(holder: MyViewHolder) {
        val countTime = holder.tpdText.text.toString().toLong()
        var id = 0

        object : CountDownTimer(/*86400000*/ 10000  / countTime, 1000) {
            override fun onTick(p0: Long) {
                if (p0 == 1000 as Long) {
                    id += 1
                }
            }

            override fun onFinish() {
                sendNotification(holder, id)
            }
        }
    }

    private fun sendNotification(holder: MyViewHolder, id: Int) {
        val builder = NotificationCompat.Builder(context, TAG_FRAGMENT)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(holder.nameText.text.toString())
            .setContentText("İlacınızı içme vaktiniz gelmiştir.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

       with(NotificationManagerCompat.from(context)) {
           notify(id, builder.build())
       }
    }
}