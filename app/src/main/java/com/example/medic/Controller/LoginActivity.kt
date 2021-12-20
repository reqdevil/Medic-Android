package com.example.medic.Controller

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
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
    private lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        pref = getPreferences(Context.MODE_PRIVATE)
        val username = pref.getString("username", "")
        val password = pref.getString("password", "")

        usernameField = findViewById(R.id.usernameField)
        passwordField = findViewById(R.id.passwordField)
        if (username != null && password != null) {
            usernameField.setText(username)
            passwordField.setText(password)
        }

        nameField = findViewById(R.id.nameField)
        surnameField = findViewById(R.id.surnameField)
        phoneField = findViewById(R.id.phoneField)
        mailField = findViewById(R.id.mailField)

        signInButton = findViewById(R.id.updateButton)
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
                usernameField.setText("")
                passwordField.setText("")
            } else {
                val userInfo: HashMap<String, Any> = hashMapOf("isDoctor" to false)
                userInfo["email"] = mailField.text.toString()
                userInfo["username"] = usernameField.text.toString()
                userInfo["password"] = passwordField.text.toString()
                userInfo["name"] = nameField.text.toString().replaceFirstChar { it.uppercase() }
                userInfo["surname"] = surnameField.text.toString().replaceFirstChar { it.uppercase() }
                userInfo["phoneNumber"] = phoneField.text.toString()

                signUp(usernameField.text.toString(), mailField.text.toString(), passwordField.text.toString(), userInfo)
            }
        }

        forgotPassword.setOnClickListener() {
            val forgotPassword = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(forgotPassword)
        }

        phoneField.setOnClickListener() {
            phoneField.setText("")
        }
    }

    private fun signIn(username: String, password: String) {
        signInButton.setBackgroundColor(Color.rgb(57, 88, 119))
        signInButton.text = "Giriş Yapılıyor"

        AuthenticationService.instance.signInUser(username, password, completion = { success, error ->
            if (success) {
                signInButton.setBackgroundColor(Color.rgb(87, 138, 167))
                signInButton.text = "Giriş Yapıldı"

                enterApplication(username, password)
            } else {
                signInButton.setBackgroundColor(Color.rgb(206, 22, 20))
                signInButton.text = "Giriş Yap"

                Log.e(TAG, error?.localizedMessage.toString())
            }
        })
    }

    private fun signUp(username: String, email: String, password: String, userInfo: HashMap<String, Any>) {
        signUpButton.setBackgroundColor(Color.rgb(57, 88, 119))
        signUpButton.text = "Kayıt Yapılıyor"
        Log.e(TAG, email)

        AuthenticationService.instance.signUpUser(email, password, userInfo, completion = { success, error ->
            if (success) {
                signUpButton.setBackgroundColor(Color.rgb(87, 138, 167))
                signUpButton.text = "Kayıt Yapıldı"

                enterApplication(username, password)
            } else {
                signUpButton.setBackgroundColor(Color.rgb(206, 22, 20))
                signUpButton.text = "Kayıt Ol"

                Toast.makeText(this, error?.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun enterApplication(username: String, password: String) {
        val editor = pref.edit()
        editor.putString("username", username)
        editor.putString("password", password)
        editor.apply()

        val mainActivity = Intent(this, MainActivity::class.java)
        startActivity(mainActivity)
    }
}