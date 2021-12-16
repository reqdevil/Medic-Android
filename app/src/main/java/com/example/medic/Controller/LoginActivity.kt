package com.example.medic.Controller

import android.content.Intent
import android.graphics.Color
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

    private lateinit var usernameField: EditText
    private lateinit var nameField: EditText
    private lateinit var passwordField: EditText
    private lateinit var surnameField: EditText
    private lateinit var phoneField: EditText
    private lateinit var mailField: EditText
    private lateinit var signInButton: Button
    private lateinit var signUpButton: Button
    private lateinit var signUpView: LinearLayout
    private lateinit var forgotPassword: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        usernameField = findViewById(R.id.usernameField)
        passwordField = findViewById(R.id.passwordField)
        nameField = findViewById(R.id.nameField)
        surnameField = findViewById(R.id.surnameField)
        phoneField = findViewById(R.id.phoneField)
        mailField = findViewById(R.id.mailField)

        signInButton = findViewById(R.id.signin)
        signUpButton = findViewById(R.id.signup)

        signUpView = findViewById(R.id.signUpView)
        signUpView.visibility = View.GONE

        forgotPassword = findViewById(R.id.forgotPassword)

        signInButton.setOnClickListener() {
            if (signUpView.visibility == View.VISIBLE) {
                signUpView.visibility = View.GONE
            } else {
                signIn(usernameField.text.toString(), passwordField.text.toString())
            }
        }

        signUpButton.setOnClickListener() {
            if (signUpView.visibility == View.GONE) {
                signUpView.visibility = View.VISIBLE
            } else {
                val userInfo: HashMap<String, Any> = hashMapOf("email" to mailField.text.toString(), "username" to usernameField.text.toString(), "password" to passwordField.text.toString(), "name" to nameField.text.toString(), "surname" to surnameField.text.toString(), "phoneNumber" to phoneField.text.toString())

                signUp(mailField.text.toString(), passwordField.text.toString(), userInfo)
            }
        }

        forgotPassword.setOnClickListener() {
            val forgotPassword = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(forgotPassword)
        }
    }

    private fun signIn(username: String, password: String) {
        Log.i(TAG, "User entrance started")
        signInButton.setBackgroundColor(Color.YELLOW)
        AuthenticationService.instance.signInUser(username, password, completion = { success, error ->
            if (success) {
                val mainActivity = Intent(this, MainActivity::class.java)
                startActivity(mainActivity)
            } else {
                Log.e(TAG, error?.localizedMessage.toString())
            }
        })
    }

    private fun signUp(username: String, password: String, userInfo: HashMap<String, Any>) {
        Log.i(TAG, "User creation started")
        AuthenticationService.instance.signUpUser(username, password, userInfo, completion = { success, error ->
            if (success) {
                val mainActivity = Intent(this, MainActivity::class.java)
                startActivity(mainActivity)
            } else {
                Toast.makeText(this, error?.localizedMessage, Toast.LENGTH_SHORT).show()
                Log.e(TAG, error?.localizedMessage.toString())
            }
        })
    }
}