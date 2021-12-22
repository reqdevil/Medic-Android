package com.example.medic.Controller.Fragments.Patient

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medic.Model.Doctor
import com.example.medic.Model.Medication
import com.example.medic.R
import com.example.medic.Services.DatabaseService
import com.example.medic.View.DoctorAdapter
import com.example.medic.View.MedicationAdapter

class DoctorFragment : Fragment() {

    private lateinit var doctorView: RecyclerView
    private lateinit var doctorList: ArrayList<Doctor>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        doctorList = ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_doctor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        doctorView = view.findViewById(R.id.doctorRecyclerView)
        doctorView.layoutManager = LinearLayoutManager(activity)
        doctorView.adapter = DoctorAdapter(doctorList)

        DatabaseService.instance.getDoctorList(completion =  { doctorList ->
            this.doctorList = doctorList

            doctorView.adapter = DoctorAdapter(doctorList)
        })
    }
}