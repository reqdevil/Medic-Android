package com.example.medic.Controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.medic.R
import com.example.medic.Services.AuthenticationService

class LoginActivity : AppCompatActivity() {
    private val TAG = "Login Page"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        val usernameField: EditText = findViewById(R.id.usernameField)
        val passwordField: EditText = findViewById(R.id.passwordField)

        val entryButton: Button = findViewById(R.id.entryButton)
        entryButton.setOnClickListener() { enterApplication(usernameField, passwordField) }
    }

    private fun enterApplication(usernameField: EditText, passwordField: EditText) {
        Log.i(TAG, "Auth Started")
        AuthenticationService.instance.userEntry(usernameField.text.toString(), passwordField.text.toString(), completion = { status, error ->
            if (status) {
                Log.i(TAG, "Status is true")
            } else {
                Log.e(TAG, error?.localizedMessage.toString())
            }
        })
    }
}