package com.example.medic.Services

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.lang.Error

class DatabaseService {
    private var TAG: String = "Database Service"

    private var database: DatabaseReference = Firebase.database.reference

    private var baseReference = database
    private var userReference = baseReference.child("users")

    companion object {
        val instance = DatabaseService()
    }

    fun createUser(uid: String, userInfo: HashMap<String, Any>) {
        userReference.child(uid).setValue(userInfo)
    }

    fun updateUser(uid: String, userInfo: HashMap<String, Any>) {
        userReference.child(uid).updateChildren(userInfo)
    }

    fun getUserInformation(username: String, password: String, completion: (Boolean, String?, Exception?) -> Unit) {
        userReference.get().addOnSuccessListener { usersSnapshot ->
            for (user in usersSnapshot.children) {
                val id = user.child("username").value.toString()
                val pwd = user.child("password").value.toString()

                val mail = user.child("email").value.toString()

                if (id == username && pwd == password) {
                    Log.e(TAG, "User Found")
                    completion(true, mail, null)
                    return@addOnSuccessListener
                }
            }

            completion(false, null, Exception("User Not Found"))
        }
    }

    fun checkUsername(username: String, completion: (Boolean) -> Unit) {
        userReference.get().addOnSuccessListener { userSnapshot ->
            for (user in userSnapshot.children) {
                val id = user.child("username").value.toString()

                if (id == username) {

                }
            }
        }
    }
}