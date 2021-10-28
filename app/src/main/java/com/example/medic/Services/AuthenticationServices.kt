package com.example.medic.Services

import com.google.firebase.auth.FirebaseAuth

class AuthenticationServices {
    private lateinit var  auth: FirebaseAuth

    companion object {
        val instance = AuthenticationServices()
    }

    constructor() {
        auth = FirebaseAuth.getInstance()
    }

    fun loginUser(email: String, password: String, completion: (Boolean, Exception?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                print(auth.currentUser)
                completion(true, null)
            } else {
                completion(false, task.exception)
            }
        }
    }
}