package com.example.medic.Services

import android.util.Log
import com.google.firebase.auth.FirebaseAuth

class AuthenticationService {
    private var TAG: String = "Authentication Service"

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    companion object {
        val instance = AuthenticationService()
    }

    fun getUID(): String {
        return auth.currentUser!!.uid
    }

    fun signUpUser(email: String, password: String, userInfo: HashMap<String, Any>, completion: (Boolean, Exception?) -> Unit) {

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                DatabaseService.instance.createUser(auth.currentUser!!.uid, userInfo)

                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        completion(true, null)
                    } else {
                        completion(false, task.exception)
                    }
                }
            } else {
                Log.e(TAG, task.exception.toString())
                completion(false, task.exception)
            }
        }
    }

    fun signInUser(username: String, password: String, completion: (Boolean, Exception?) -> Unit) {
        DatabaseService.instance.getUserInformation(username, password, completion = { success, mail, error ->
            if (success) {
                auth.signInWithEmailAndPassword(mail!!, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        completion(true, null)
                    } else {
                        completion(false, task.exception)
                    }
                }
            } else {
                completion(false, error)
                Log.e(TAG, error?.localizedMessage.toString())
            }
        })
    }

    fun sendPasswordResetLink(email: String, completion: (Boolean, Exception?) -> Unit) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                completion(true, null)
            } else {
                completion(false, task.exception)
            }
        }
    }

    fun signOut() {
        auth.signOut()
    }
}