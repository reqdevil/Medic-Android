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
import com.example.medic.View.MyAdapter

class AddPatientFragment : Fragment(), OnDataPass {
    private val TAG = "Add Patient Fragment"

    private lateinit var patientView: RecyclerView
    private lateinit var patientList: ArrayList<Patient>

    private lateinit var searchPatient: Button
    private lateinit var usernameField: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        patientList = ArrayList<Patient>()
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
        val adapter = MyAdapter(patientList)
        patientView.adapter = adapter

        usernameField = view.findViewById(R.id.usernameField)

        searchPatient = view.findViewById(R.id.searchPatientButton)
        searchPatient.setOnClickListener() { searchPatient(usernameField.text.toString()) }
    }

    private fun searchPatient(uid: String) {
        Log.e(TAG, "search patient started")

        DatabaseService.instance.findPatient(uid, completion = { patientList ->
            patientView.adapter = MyAdapter(patientList)
        })

        Log.e(TAG, patientList.toString())
    }

    override fun onDataPass(data: String) {
        Log.e(TAG, data)
    }
}