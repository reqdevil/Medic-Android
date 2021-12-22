package com.example.medic.Services

import android.util.Log
import com.google.firebase.auth.FirebaseAuth

class AuthenticationService {
    private val TAG: String = "Authentication Service"

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    companion object {
        val instance = AuthenticationService()
    }

    fun getUID(): String {
        return auth.currentUser!!.uid
    }

    fun signUpUser(email: String, password: String, userInfo: HashMap<String, Any>, completion: (Boolean, Exception?) -> Unit) {
        DatabaseService.instance.checkUsername(userInfo["username"] as String, completion = { userFound ->
            if (userFound) {
                completion(false, Exception("Bu TC Kimlik Numarası daha önceden sisteme kaydedilmiş"))
            } else {
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
        })
    }

    fun signInUser(username: String, password: String, completion: (Boolean, Exception?) -> Unit) {
        DatabaseService.instance.getLoginInformation(username, password, completion = { success, mail, error ->
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