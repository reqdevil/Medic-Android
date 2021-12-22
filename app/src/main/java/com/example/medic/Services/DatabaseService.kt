package com.example.medic.Services

import android.util.Log
import com.example.medic.Model.Doctor
import com.example.medic.Model.Medication
import com.example.medic.Model.Patient
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class DatabaseService {
    private val TAG: String = "Database Service"

    private val database: DatabaseReference = Firebase.database.reference

    private val baseReference = database
    private val userReference = baseReference.child("users")

    companion object {
        val instance = DatabaseService()
    }

    fun createUser(uid: String, userInfo: HashMap<String, Any>) {
        userReference.child(uid).setValue(userInfo)
    }

    fun updateUser(uid: String, userInfo: HashMap<String, Any>) {
        userReference.child(uid).updateChildren(userInfo)
    }

    fun checkUsername(uid: String, completion: (Boolean) -> Unit) {
        userReference.get().addOnSuccessListener { usersSnapshot ->
            for (user in usersSnapshot.children) {
                val id = user.child("username").value.toString()

                if (uid == id) {
                    completion(true)
                    return@addOnSuccessListener
                }
            }

            completion(false)
        }
    }

    fun getLoginInformation(username: String, password: String, completion: (Boolean, String?, Exception?) -> Unit) {
        userReference.get().addOnSuccessListener { usersSnapshot ->
            for (user in usersSnapshot.children) {
                val id = user.child("username").value.toString()
                val pwd = user.child("password").value.toString()

                val mail = user.child("email").value.toString()

                if (id == username) {
                    if (pwd == password) {
                        completion(true, mail, null)
                        return@addOnSuccessListener
                    }

                    completion(false, null, Exception("Şifre yanlış"))
                    return@addOnSuccessListener
                }
            }

            completion(false, null, Exception("Bu TC Kimlik ile kayıt bulunamadı"))
        }
    }

    fun getUserInformation(uid: String, completion: (String, String, String, String, String, Boolean) -> Unit) {
        userReference.get().addOnSuccessListener { usersSnapshot ->
            for (user in usersSnapshot.children) {
                val userID = user.key.toString()

                if (userID == uid) {
                    val username = user.child("username").value.toString()
                    val name = user.child("name").value.toString()
                    val surname = user.child("surname").value.toString()
                    val email = user.child("email").value.toString()
                    val phoneNumber = "0" + user.child("phoneNumber").value.toString()
                    val isDoctor = user.child("isDoctor").value

                    completion(username, name, surname, email, phoneNumber, isDoctor as Boolean)
                }
            }
        }
    }

    fun findPatient(username: String, completion: (ArrayList<Patient>) -> Unit) {
        val patientList: ArrayList<Patient> = ArrayList()

        userReference.get().addOnSuccessListener { usersSnapshot ->
            for (user in usersSnapshot.children) {
                val id = user.child("username").value.toString()
                val isDoctor = user.child("isDoctor").value

                if (id.contains(username) && isDoctor == false) {
                    val name = user.child("name").value.toString()
                    val surname = user.child("surname").value.toString()
                    val uid = user.key.toString()

                    patientList.add(Patient(uid, id, name, surname, false))
//                    StorageService.instance.getProfilePicture(AuthenticationService.instance.getUID(), completion = { success, byteArray, error ->
//                        if (success) {
//                            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)
//
//                        } else {
//                            patientList.add(Patient(null, id, "$name $surname"))
//                        }
//                    })
                }
            }

            completion(patientList)
        }
    }

    fun addMedication(uid: String, medicationInfo: HashMap<String, Any>) {
        val uuid = UUID.randomUUID()
        userReference.child(uid).child("medications").child(uuid.toString()).setValue(medicationInfo)
    }

    fun getMedication(completion: (ArrayList<Medication>) -> Unit) {
        val medicationList: ArrayList<Medication> = ArrayList()

        userReference.child(AuthenticationService.instance.getUID()).child("medications").get().addOnSuccessListener { medicationSnapshot ->
            for (medication in medicationSnapshot.children) {
                val name = medication.child("medicationName").value.toString()
                val tpd = medication.child("timePerDay").value as Long

                medicationList.add(Medication(name, tpd.toInt()))
            }

            completion(medicationList)
        }
    }

    fun addPatient(uid: String, patient: Patient) {
        val userInfo = hashMapOf("name" to patient.name, "surname" to patient.surname, "uid" to patient.uid)
        userReference.child(uid).child("patients").child(patient.username).setValue(userInfo)

        getUserInformation(uid, completion = { username, name, surname, _, phoneNumber, _ ->
            val userInfo = hashMapOf("name" to name, "surname" to surname, "uid" to uid, "phoneNumber" to phoneNumber)
            userReference.child(patient.uid).child("doctors").child(username).setValue(userInfo)
        })
    }

    fun removePatient(patientList: ArrayList<Patient>, completion: (Boolean) -> Unit) {
        Log.e(TAG, patientList.toString())

        for (patient in patientList) {
            Log.e(TAG, patient.username)
            userReference.child(AuthenticationService.instance.getUID()).child("patients").child(patient.username).removeValue()
            getUserInformation(AuthenticationService.instance.getUID(), completion = { username, _, _, _, _, _ ->
                userReference.child(patient.uid).child("doctors").child(username).removeValue()
            })
        }

        completion(true)
    }

    fun getPatientList(completion: (ArrayList<Patient>) -> Unit) {
        var patientList: ArrayList<Patient> = ArrayList()

        userReference.child(AuthenticationService.instance.getUID()).child("patients").get().addOnSuccessListener { patientsSnapshot ->
            for (patient in patientsSnapshot.children) {
                val uid = patient.child("uid").value.toString()
                val name = patient.child("name").value.toString()
                val surname = patient.child("surname").value.toString()
                val username = patient.key.toString()

                patientList.add(Patient(uid, username, name, surname, false))
            }

            completion(patientList)
        }
    }

    fun getDoctorList(completion: (ArrayList<Doctor>) -> Unit) {
        var doctorList: ArrayList<Doctor> = ArrayList()

        userReference.child(AuthenticationService.instance.getUID()).child("doctors").get().addOnSuccessListener { doctorSnapshot ->
            for (doctor in doctorSnapshot.children) {
                val uid = doctor.child("uid").value.toString()
                val name = doctor.child("name").value.toString()
                val surname = doctor.child("surname").value.toString()
                val phoneNumber = doctor.child("phoneNumber").value.toString()
                val username = doctor.key.toString()

                doctorList.add(Doctor(uid, username, name, surname, phoneNumber))
            }

            completion(doctorList)
        }
    }
}