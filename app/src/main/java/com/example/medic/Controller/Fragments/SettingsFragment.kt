package com.example.medic.Controller.Fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.medic.Interface.OnDataPass
import com.example.medic.R
import com.example.medic.Services.AuthenticationService
import com.example.medic.Services.DatabaseService
import com.example.medic.Services.StorageService
import com.example.medic.Utilities.DATA_UPDATED


class SettingsFragment : Fragment() {
    private lateinit var nameText: TextView
    private lateinit var surnameText: TextView
    private lateinit var emailText: TextView
    private lateinit var phoneNumberText: TextView
    private lateinit var editButton: TextView

    private lateinit var nameField: EditText
    private lateinit var surnameField: EditText
    private lateinit var mailField: EditText
    private lateinit var phoneField: EditText
    private lateinit var updateButton: Button

    private lateinit var editProfile: LinearLayout
    private lateinit var informationView: ConstraintLayout
    private lateinit var profilePicture: ImageView

    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null

    lateinit var dataPass: OnDataPass

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataPass = context as OnDataPass
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nameText = view.findViewById(R.id.nameText)
        surnameText = view.findViewById(R.id.surnameText)
        emailText = view.findViewById(R.id.emailText)
        phoneNumberText = view.findViewById(R.id.phoneNumberText)
        editButton = view.findViewById(R.id.editButton)

        nameField = view.findViewById(R.id.nameField)
        surnameField = view.findViewById(R.id.surnameField)
        mailField = view.findViewById(R.id.mailField)
        phoneField = view.findViewById(R.id.phoneField)
        updateButton =view.findViewById(R.id.updateButton)

        editProfile = view.findViewById(R.id.editProfile)
        informationView = view.findViewById(R.id.informationView)
        editProfile.visibility = View.GONE

        profilePicture = view.findViewById(R.id.profilePicture)

        DatabaseService.instance.getUserInformation(AuthenticationService.instance.getUID(), completion = { _, name, surname, email, phoneNumber, _ ->
            nameText.text = name
            surnameText.text = surname
            emailText.text = email
            phoneNumberText.text = phoneNumber

            nameField.setText(name)
            surnameField.setText(surname)
            mailField.setText(email)
            phoneField.setText(phoneNumber)
        })

        StorageService.instance.getProfilePicture(AuthenticationService.instance.getUID(), completion = { success, byteArray, _ ->
            if (success) {
                val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray?.size!!)
                profilePicture.setImageBitmap(bitmap)
            }
        })

        editButton.setOnClickListener() {
            informationView.visibility = View.GONE
            editProfile.visibility = View.VISIBLE
        }

        updateButton.setOnClickListener() {
            val name = nameField.text.toString()
            val surname = surnameField.text.toString()
            val mail = mailField.text.toString()
            val phoneNumber = phoneField.text.toString().drop(1)

            val userInfo: HashMap<String, Any> = hashMapOf("name" to name, "surname" to surname, "email" to mail, "phoneNumber" to phoneNumber)
            DatabaseService.instance.updateUser(AuthenticationService.instance.getUID(), userInfo)

            if (filePath != null) {
                StorageService.instance.uploadProfilePicture(
                    AuthenticationService.instance.getUID(),
                    filePath!!
                )
            }

            passData(DATA_UPDATED)
        }

        profilePicture.setOnClickListener() { launchGallery() }
    }

    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Fotoğraf Seçin"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data == null || data.data == null) {
                return
            }

            filePath = data.data
            profilePicture.setImageURI(filePath)
        }
    }

    fun passData(data: String) {
        dataPass.onDataPass(data)
    }
}