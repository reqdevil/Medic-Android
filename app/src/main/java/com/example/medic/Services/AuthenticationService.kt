package com.example.medic.Services

import android.util.Log
import com.google.firebase.auth.FirebaseAuth

class AuthenticationService {
    private var TAG: String = "Authentication Service"

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var mail: String = "@medic.saglik.gov.tr"

    companion object {
        val instance = AuthenticationService()
    }

    fun signupUser(email: String, password: String, userInfo: HashMap<String, Any>, completion: (Boolean, Exception?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                DatabaseService.instance.createUser(auth.currentUser!!.uid, userInfo)

                completion(true, null)
            } else {
                Log.e(TAG, task.exception.toString())
                completion(false, task.exception)
            }
        }
    }

    fun signinUser(email: String, password: String, completion: (Boolean, Exception?) -> Unit) {
        DatabaseService.instance.getUserInformation(email, password, completion = { success, uid, error ->
            Log.e(TAG, success.toString() + " " + uid + " " + error.toString())
        })
    }

    fun sendResetLink(email: String, completion: (Boolean, Exception?) -> Unit) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                completion(true, null)
            } else {
                completion(false, task.exception)
            }
        }
    }
}