package com.example.medic.Controller

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.medic.Controller.Fragments.Doctor.AddPatientFragment
import com.example.medic.Controller.Fragments.Doctor.PatientFragment
import com.example.medic.Controller.Fragments.Patient.DoctorFragment
import com.example.medic.Controller.Fragments.Patient.MedicationFragment
import com.example.medic.Controller.Fragments.SettingsFragment
import com.example.medic.Interface.OnDataPass
import com.example.medic.R
import com.example.medic.Services.AuthenticationService
import com.example.medic.Services.DatabaseService
import com.example.medic.Services.StorageService
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), OnDataPass {
    private val TAG = "Main Activity"

    private lateinit var leftFragment: Fragment
    private lateinit var middleFragment: Fragment
    private lateinit var settingsFragment: Fragment

    private lateinit var profilePicture: ImageView

    private lateinit var verifiedLogo: ImageView
    private lateinit var usernameText: TextView
    private lateinit var nameText: TextView

    private lateinit var doctorPage: ConstraintLayout
    private lateinit var patientPage: ConstraintLayout
    private lateinit var bottomNavigation: BottomNavigationView

    private var fragmentView: Int = 0
    private var isDoctor: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        verifiedLogo = findViewById(R.id.verifiedLogo)
        usernameText = findViewById(R.id.usernameText)
        nameText = findViewById(R.id.nameText)
        profilePicture = findViewById(R.id.profilePhoto)

        doctorPage = findViewById(R.id.doctorPage)
        patientPage = findViewById(R.id.patientPage)

        DatabaseService.instance.getUserInformation(AuthenticationService.instance.getUID(), completion = { username, name, surname, _, _, isDoctor ->
            usernameText.text = username
            nameText.text = "$name $surname"
            this.isDoctor = isDoctor
            settingsFragment = SettingsFragment()

            if (isDoctor) {
                leftFragment = PatientFragment()
                middleFragment = AddPatientFragment()
                bottomNavigation = findViewById(R.id.doctorBottomNavigation)
                fragmentView = R.id.doctorFragmentView

                verifiedLogo.visibility = View.VISIBLE
                doctorPage.visibility = View.VISIBLE
                patientPage.visibility = View.GONE

                replaceFragment(leftFragment)
            } else {
                leftFragment = DoctorFragment()
                middleFragment = MedicationFragment()
                bottomNavigation = findViewById(R.id.patientBottomNavigation)
                fragmentView = R.id.patientFragmentView

                verifiedLogo.visibility = View.GONE
                doctorPage.visibility = View.GONE
                patientPage.visibility = View.VISIBLE

                replaceFragment(middleFragment)
            }

            bottomNavigation.setOnNavigationItemSelectedListener {
                when(it.itemId) {
                    R.id.first -> replaceFragment(leftFragment)
                    R.id.second -> replaceFragment(middleFragment)
                    R.id.settings -> replaceFragment(settingsFragment)
                    R.id.logout -> logout()
                }
                true
            }
        })

        StorageService.instance.getProfilePicture(AuthenticationService.instance.getUID(), completion = { success, byteArray, error ->
            if (success) {
                val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray?.size!!)
                profilePicture.setImageBitmap(bitmap)
            } else {
                Log.e(TAG, error.toString())
                Toast.makeText(this, "Profil Fotoğrafı yüklenirken bir hata oluştu.", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(fragmentView, fragment)
        transaction.commit()
    }

    private fun logout() {
        AuthenticationService.instance.signOut()
        finish()
    }

    override fun onDataPass(data: String) {
        Log.e(TAG, data)

        if (data == "DATA_UPDATED") {
            finish()
            startActivity(intent)
        }
    }
}