package com.example.todoapp.firestore

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.todoapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SaveDataActivity : AppCompatActivity() {
    private lateinit var etName: EditText
    private lateinit var etAddress: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSave: Button

    private lateinit var progressBar: ProgressBar

    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_data)

        etName = findViewById(R.id.utv_name)
        etAddress = findViewById(R.id.utv_address)
        etEmail = findViewById(R.id.utv_email)
        etPassword = findViewById(R.id.utv_password)
        btnSave = findViewById(R.id.ubtn_save)
        progressBar = findViewById(R.id.progressBar)

        btnSave.setOnClickListener {
            progressBar.visibility = View.VISIBLE

            val sName = etName.text.toString().trim()
            val sAddress = etAddress.text.toString().trim()
            val sEmail = etEmail.text.toString().trim()
            val sPassword = etPassword.text.toString().trim()

            val userMap = hashMapOf(
                "name" to sName,
                "address" to sAddress,
                "email" to sEmail,
                "password" to sPassword
            )

            val userId = FirebaseAuth.getInstance().currentUser!!.uid

            db.collection("user").document().set(userMap)
                .addOnSuccessListener {
                    Toast.makeText(this, "Successfully Added!", Toast.LENGTH_SHORT).show()
                    etName.text.clear()
                    etAddress.text.clear()
                    etEmail.text.clear()
                    etPassword.text.clear()

                    progressBar.visibility = View.GONE
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show()
                }

        }
    }
}