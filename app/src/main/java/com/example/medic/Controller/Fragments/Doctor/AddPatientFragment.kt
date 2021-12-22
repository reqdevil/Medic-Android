package com.example.medic.Controller.Fragments.Doctor

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medic.Interface.OnDataPass
import com.example.medic.Model.Patient
import com.example.medic.R
import com.example.medic.Services.DatabaseService
import com.example.medic.View.AddPatientAdapter

class AddPatientFragment : Fragment() {
    private val TAG = "Add Patient Fragment"

    private lateinit var patientView: RecyclerView

    private lateinit var searchPatient: Button
    private lateinit var usernameField: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_patient, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        patientView = view.findViewById(R.id.patientView)
        patientView.layoutManager = LinearLayoutManager(activity)

        usernameField = view.findViewById(R.id.usernameField)

        searchPatient = view.findViewById(R.id.searchPatientButton)
        searchPatient.setOnClickListener() { searchPatient(usernameField.text.toString()) }
    }

    private fun searchPatient(uid: String) {
        DatabaseService.instance.findPatient(uid, completion = { patientList ->
            patientView.adapter = AddPatientAdapter(patientList)
        })
    }
}