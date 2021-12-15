package com.example.medic.Controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.medic.R
import com.example.medic.Services.AuthenticationService

class ForgotPasswordActivity : AppCompatActivity() {
    private var TAG: String = "Forgot Password Activity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        supportActionBar?.hide()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        val mailField: EditText = findViewById(R.id.mailField)

        val sendButton: Button = findViewById(R.id.sendButton)

        sendButton.setOnClickListener() {
            AuthenticationService.instance.sendResetLink(mailField.text.toString(), completion = { success, error ->
                if (success) {
                    this.finish()
                } else {
                    Log.e(TAG, error?.localizedMessage.toString())
                }
            })
        }
    }
}