package com.example.todoapp.storage

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.todoapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class UploadImageActivity : AppCompatActivity() {
    private lateinit var image: ImageView
    private lateinit var btnBrowse: Button
    private lateinit var btnUpload: Button

    private var storageRef = Firebase.storage

    private lateinit var uri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_image)

        storageRef = FirebaseStorage.getInstance()

        image = findViewById(R.id.imageView2)
        btnBrowse = findViewById(R.id.button)
        btnUpload = findViewById(R.id.button2)

        val galleryImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
            image.setImageURI(it)
            if (it != null) {
                uri = it
            }
        }

        btnBrowse.setOnClickListener {
            galleryImage.launch("image/*")
        }
        btnUpload.setOnClickListener {
            storageRef.getReference("images").child(System.currentTimeMillis().toString())
                .putFile(uri).addOnSuccessListener { task ->
                    task.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                            val userId = FirebaseAuth.getInstance().currentUser!!.uid

                            val mapImage = mapOf(
                                "url" to it.toString()
                            )

                            val databaseReference =
                                FirebaseDatabase.getInstance().getReference("userImages")
                            databaseReference.child(userId).setValue(mapImage)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Successful", Toast.LENGTH_SHORT).show()
                                }.addOnFailureListener { error ->
                                    Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()
                                }

                        }

                }
        }

    }
}