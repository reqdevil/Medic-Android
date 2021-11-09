package com.example.medic.Services

import com.google.firebase.auth.FirebaseAuth

class AuthenticationService {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var mail: String = "@medic.saglik.gov.tr"

    companion object {
        val instance = AuthenticationService()
    }

    fun userEntry(username: String, password: String, completion: (Boolean, Exception?) -> Unit) {
        auth.signInWithEmailAndPassword(username + mail, password).addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                completion(true, null)
            } else {
                // Maybe there is no user with this email
                auth.createUserWithEmailAndPassword(username + mail, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userInfo: HashMap<String, Any> = hashMapOf("username" to username, "password" to password, "confirmed" to false)
                        DatabaseService.instance.createUser(auth.currentUser!!.uid, userInfo)

                        completion(true, null)
                    } else {
                        completion(false, task.exception)
                    }
                }
            }
        }
    }
}