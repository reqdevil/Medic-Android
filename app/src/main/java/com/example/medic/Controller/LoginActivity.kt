package com.example.medic.Controller

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
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
        val nameField: EditText = findViewById(R.id.nameField)
        val surnameField: EditText = findViewById(R.id.surnameField)
        val phoneField: EditText = findViewById(R.id.phoneField)
        val mailField: EditText = findViewById(R.id.mailField)

        val signinButton: Button = findViewById(R.id.signin)
        val signupButton: Button = findViewById(R.id.signup)

        val signUpView: LinearLayout = findViewById(R.id.signUpView)
        signUpView.visibility = View.GONE

        val forgotPasswordText: TextView = findViewById(R.id.forgotPassword)

        signinButton.setOnClickListener() {
            if (signUpView.visibility == View.VISIBLE) {
                signUpView.visibility = View.GONE
            } else {
                signIn(usernameField.text.toString(), passwordField.text.toString())
            }
        }

        signupButton.setOnClickListener() {
            if (signUpView.visibility == View.GONE) {
                signUpView.visibility = View.VISIBLE
            } else {
                val userInfo: HashMap<String, Any> = hashMapOf("email" to mailField.text.toString(), "username" to usernameField.text.toString(), "password" to passwordField.text.toString(), "name" to nameField.text.toString(), "surname" to surnameField.text.toString(), "phoneNumber" to phoneField.text.toString())

                signUp(mailField.text.toString(), passwordField.text.toString(), userInfo)
            }
        }

        forgotPasswordText.setOnClickListener() {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signIn(username: String, password: String) {
        Log.i(TAG, "User entrance started")
        AuthenticationService.instance.signinUser(username, password, completion = { success, error ->
            if (success) {
                Log.i(TAG, "Status is true")
            } else {
                Log.e(TAG, error?.localizedMessage.toString())
            }
        })
    }

    private fun signUp(username: String, password: String, userInfo: HashMap<String, Any>) {
        Log.i(TAG, "User creation started")
        AuthenticationService.instance.signupUser(username, password, userInfo, completion = { success, error ->
            if (success) {
                Log.i(TAG, "Status is true")
            } else {
                Toast.makeText(this, error?.localizedMessage, Toast.LENGTH_SHORT).show()
                Log.e(TAG, error?.localizedMessage.toString())
            }
        })
    }
}