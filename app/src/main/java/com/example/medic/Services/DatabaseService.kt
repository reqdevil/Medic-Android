package com.example.medic.Services

import android.util.Log
import com.example.medic.Model.Patient
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DatabaseService {
    private val TAG: String = "Database Service"

    private val database: DatabaseReference = Firebase.database.reference

    private val baseReference = database
    private val userReference = baseReference.child("users")
    private val medicationReference = userReference.child(AuthenticationService.instance.getUID()).child("medication")

    companion object {
        val instance = DatabaseService()
    }

    fun createUser(uid: String, userInfo: HashMap<String, Any>) {
        userReference.child(uid).setValue(userInfo)
    }

    fun updateUser(uid: String, userInfo: HashMap<String, Any>) {
        userReference.child(uid).updateChildren(userInfo)
    }

    fun getLoginInformation(username: String, password: String, completion: (Boolean, String?, Exception?) -> Unit) {
        userReference.get().addOnSuccessListener { usersSnapshot ->
            for (user in usersSnapshot.children) {
                val id = user.child("username").value.toString()
                val pwd = user.child("password").value.toString()

                val mail = user.child("email").value.toString()

                if (id == username && pwd == password) {

                    completion(true, mail, null)
                    return@addOnSuccessListener
                }
            }

            completion(false, null, Exception("User Not Found"))
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

                    patientList.add(Patient(uid, id, "$name $surname"))
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

    fun updateMedication(uid: String, medicationInfo: HashMap<String, Any>) {
        userReference.child(uid).child("medication").setValue(medicationInfo)
    }

    fun getMedication() {
        medicationReference.get().addOnSuccessListener { medicationSnapshot ->
            for (medication in medicationSnapshot.children) {
                Log.e(TAG, medication.child("name").value.toString() + "|" + medication.child("period").value.toString())
            }
        }
    }

    fun addPatient(uid: String, patient: Patient) {
        val userInfo = hashMapOf("name" to patient.fullName.split(" ")[0], "surname" to patient.fullName.split(" ")[1], "uid" to patient.uid)
        userReference.child(uid).child("patients").child(patient.username).setValue(userInfo)

        getUserInformation(uid, completion = { username, name, surname, _, _, _ ->
            val userInfo = hashMapOf("name" to name, "surname" to surname, "uid" to uid)
            userReference.child(patient.uid).child("doctors").child(username).setValue(userInfo)
        })
    }
}