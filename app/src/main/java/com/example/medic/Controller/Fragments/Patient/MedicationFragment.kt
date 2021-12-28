package com.example.medic.Controller.Fragments.Patient

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medic.Model.Medication
import com.example.medic.R
import com.example.medic.Services.DatabaseService
import com.example.medic.View.MedicationAdapter
import com.example.medic.View.PatientAdapter

class MedicationFragment : Fragment() {
    private val TAG = "Medication Fragment"

    private lateinit var medicationView: RecyclerView
    private lateinit var medicationList: ArrayList<Medication>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        medicationList = ArrayList()

        createNotificationChannel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_medication, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        medicationView = view.findViewById(R.id.medicationRecyclerView)
        medicationView.layoutManager = LinearLayoutManager(activity)
        medicationView.adapter = MedicationAdapter(medicationList, requireContext(), TAG)

        DatabaseService.instance.getMedication(completion = { medicationList ->
            this.medicationList = medicationList

            medicationView.adapter = MedicationAdapter(medicationList, requireContext(), TAG)
        })
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Title"
            val description = "Notification Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(TAG, name, importance).apply {
                this.description = description
            }
            val notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }
}