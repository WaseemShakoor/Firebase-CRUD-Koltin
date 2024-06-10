package com.example.todoapp.firestore

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.todoapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UpdateActivity : AppCompatActivity() {
    private lateinit var etName: EditText
    private lateinit var etAddress: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnUpdate: Button

    private lateinit var progressBar: ProgressBar

    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        etName = findViewById(R.id.utv_name)
        etAddress = findViewById(R.id.utv_address)
        etEmail = findViewById(R.id.utv_email)
        etPassword = findViewById(R.id.utv_password)
        btnUpdate = findViewById(R.id.btn_update)
        progressBar = findViewById(R.id.progressBar)

        setData()

        btnUpdate.setOnClickListener {
            val sName = etName.text.toString()
            val sAddress = etAddress.text.toString()
            val sEmail = etEmail.text.toString()
            val sPassword = etPassword.text.toString()

            val updateMap = mapOf(
                "name" to sName,
                "address" to sAddress,
                "email" to sEmail,
                "password" to sPassword
            )
            val userId = FirebaseAuth.getInstance().currentUser!!.uid
            db.collection("user").document(userId).update(updateMap)

            Toast.makeText(this, "Successfully Edited!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setData() {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val ref = db.collection("user").document(userId)
        ref.get().addOnSuccessListener {
            if (it != null) {
                val name = it.data?.get("name")?.toString()
                val address = it.data?.get("address")?.toString()
                val email = it.data?.get("email")?.toString()
                val password = it.data?.get("password").toString()

                etName.setText(name)
                etAddress.setText(address)
                etEmail.setText(email)
                etPassword.setText(password)
            }
        }
            .addOnFailureListener {
                Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show()
            }
    }
}