package com.example.medic.Services

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DatabaseService {
    private var database: DatabaseReference = Firebase.database.reference

    private var baseReference = database
    private var userReference = baseReference.child("users")

    companion object {
        val instance = DatabaseService()
    }

    fun createUser(uid: String, userInfo: HashMap<String, Any>) {
        userReference.child(uid).setValue(userInfo)
    }
}