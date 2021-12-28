package com.example.medic.Services

import android.net.Uri
import android.util.Log
import androidx.core.net.toFile
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.lang.Exception

class StorageService {
    private val TAG = "Storage Service"

    private val reference = FirebaseStorage.getInstance().reference
    private val profileReference = reference.child("profilePictures")

    companion object {
        val instance = StorageService()
    }

    fun uploadProfilePicture(uid: String, uri: Uri) {
        profileReference.child(uid).putFile(uri)
    }

    fun getProfilePicture(uid: String, completion: (Boolean, ByteArray?, Exception?) -> Unit) {
        profileReference.child(uid).getBytes(1024 * 1024).addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                completion(true, task.result, null)
            } else {
                completion(false, null, task.exception)
            }
        }
//        profileReference.listAll().addOnSuccessListener { listResult ->
//            for (list in listResult.items) {
//                Log.e(TAG, list.toString())
//            }
//        }
    }
}