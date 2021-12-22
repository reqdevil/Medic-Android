package com.example.medic.Controller.Fragments.Patient

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        medicationView.adapter = MedicationAdapter(medicationList)

        DatabaseService.instance.getMedication(completion = { medicationList ->
            this.medicationList = medicationList

            medicationView.adapter = MedicationAdapter(medicationList)
        })
    }
}