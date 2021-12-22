package com.example.medic.Controller.Fragments.Doctor

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medic.Model.Patient
import com.example.medic.R
import com.example.medic.Services.DatabaseService
import com.example.medic.View.PatientAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog

class PatientFragment : Fragment() {
    private val TAG = "Patient Fragment"

    private lateinit var patientView: RecyclerView
    private lateinit var patientList: ArrayList<Patient>

    private lateinit var removePatient: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        patientList = ArrayList<Patient>()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_patient, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        patientView = view.findViewById(R.id.patientRecyclerView)
        patientView.layoutManager = LinearLayoutManager(activity)
        patientView.adapter = PatientAdapter(patientList, requireActivity(), callback = {_, _ ->}, dialogCallback = {})

        removePatient = view.findViewById(R.id.removePatient)
        removePatient.setOnClickListener() { removePatient() }

        getData()
    }

    private fun getData() {
        DatabaseService.instance.getPatientList { patientList ->
            this.patientList = patientList

            patientView.adapter = PatientAdapter(patientList, requireActivity(), callback = { position, isSelected ->
                patientList[position].isSelected = isSelected
            }, dialogCallback = { patient ->
                addMedicationDialog(patient)
            })
        }
    }

    private fun removePatient() {
        val removePatientList: ArrayList<Patient> = ArrayList()

        if (!removePatientList.isEmpty()) {
            for (patient in patientList) {
                if (patient.isSelected) {
                    removePatientList.add(patient)
                }
            }

            DatabaseService.instance.removePatient(removePatientList, completion = { success ->
                if (success) {
                    getData()
                }
            })
        } else {
            Toast.makeText(activity, "Lütfen hasta seçiniz", Toast.LENGTH_LONG).show()
        }
    }

    private fun addMedicationDialog(patient: Patient) {
        Log.e(TAG, "Add Medication Started")

        val dialog = BottomSheetDialog(requireActivity())
        val view = layoutInflater.inflate(R.layout.dialog_add_medication, null)
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.setCancelable(true)
        dialog.setContentView(view)

        val nameField: EditText = dialog.findViewById(R.id.nameField)!!
        val tpdField: EditText = dialog.findViewById(R.id.tpdField)!!

        val addButton: Button = dialog.findViewById(R.id.addButton)!!
        val cancelButton: Button = dialog.findViewById(R.id.cancelButton)!!

        cancelButton.setOnClickListener() { dialog.dismiss()}

        addButton.setOnClickListener() {
            val medicationInfo: HashMap<String, Any> = hashMapOf("medicationName" to nameField.text.toString(), "timePerDay" to tpdField.text.toString().toInt())

            DatabaseService.instance.addMedication(patient.uid, medicationInfo)

            dialog.dismiss()
        }

        dialog.show()
    }
}