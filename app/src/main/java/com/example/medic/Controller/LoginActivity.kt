package com.example.medic.Controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.medic.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()
    }
}