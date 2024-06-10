package com.example.todoapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.todoapp.R
import com.example.todoapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {
    private lateinit var tvEmail: TextView
    private lateinit var tvPassword: TextView
    private lateinit var tvAddress: TextView
    private lateinit var tvName: TextView
    private lateinit var btnRegister: Button

    private lateinit var auth: FirebaseAuth

    private lateinit var sEmail: String
    private lateinit var sPassword: String
    private lateinit var sName: String
    private lateinit var sAddress: String
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Initialize Firebase Auth
        auth = Firebase.auth
        database = Firebase.database.reference

        tvEmail = findViewById(R.id.tv_email)
        tvPassword = findViewById(R.id.tv_password)
        tvName = findViewById(R.id.tv_name)
        tvAddress = findViewById(R.id.tv_address)
        btnRegister = findViewById(R.id.btn_register)
        btnRegister.setOnClickListener {
            sEmail = tvEmail.text.toString().trim()
            sPassword = tvPassword.text.toString().trim()

            auth.createUserWithEmailAndPassword(sEmail, sPassword)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information

                        auth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
                                Toast.makeText(
                                    this, "Please Verify your Email!", Toast.LENGTH_SHORT
                                ).show()
                                saveData()
                            }?.addOnFailureListener {
                                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                            }
//                        updateUI()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
//                        updateUI(null)
                    }
                }

        }
    }

    private fun saveData() {
        sEmail = tvEmail.text.toString().trim()
        sAddress = tvAddress.text.toString().trim()
        sName = tvName.text.toString().trim()
        val user = User(sName, sEmail, sAddress)
        val userID = FirebaseAuth.getInstance().currentUser!!.uid

        database.child("User").child(userID).setValue(user)

    }

    private fun updateUI() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }
}