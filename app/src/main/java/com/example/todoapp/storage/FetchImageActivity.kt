package com.example.todoapp.storage

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.todoapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FetchImageActivity : AppCompatActivity() {
    private lateinit var ivFirebase: ImageView
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetch_image)

        ivFirebase = findViewById(R.id.fetchImage)

        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        databaseReference = FirebaseDatabase.getInstance().getReference("userImages")
            .child(userId)
        databaseReference.get().addOnSuccessListener {
            val url = it.child("userImage").value.toString()

            Glide.with(this).load(url).into(ivFirebase)
        }

    }
}