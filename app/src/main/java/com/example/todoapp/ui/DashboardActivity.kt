package com.example.todoapp.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.todoapp.R
import com.example.todoapp.databinding.ActivityDashboardBinding
import com.example.todoapp.firestore.DeleteActivity
import com.example.todoapp.firestore.FetchDataActivity
import com.example.todoapp.firestore.RecyclerViewActivity
import com.example.todoapp.firestore.SaveDataActivity
import com.example.todoapp.firestore.UpdateActivity
import com.example.todoapp.storage.FetchImageActivity
import com.example.todoapp.storage.RecyclerImageActivity
import com.example.todoapp.storage.UploadImageActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        database = Firebase.database.reference

        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        database.child("User").child(userId).get().addOnSuccessListener {
            val name = it.child("name").value.toString()
            val email = it.child("email").value.toString()
            val address = it.child("address").value.toString()

            binding.tvName.text = name
            binding.tvEmail.text = email
            binding.tvAddress.text = address
        }.addOnFailureListener {
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
        }

        binding.ivEdit.setOnClickListener {
            val bottomSheet = BottomSheetDialog(this)
            bottomSheet.requestWindowFeature(Window.FEATURE_NO_TITLE)
            bottomSheet.setContentView(R.layout.bottom_sheet)
            bottomSheet.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            bottomSheet.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val btnCancel: Button? = bottomSheet.findViewById(R.id.btn_cancel)
            val btnEdit: Button? = bottomSheet.findViewById(R.id.btn_edited)

            val etName: EditText? = bottomSheet.findViewById(R.id.editName)
            val etEmail: EditText? = bottomSheet.findViewById(R.id.editEmail)
            val etAddress: EditText? = bottomSheet.findViewById(R.id.editAddress)

            val sName = binding.tvName.text.toString()
            val sEmail = binding.tvEmail.text.toString()
            val sAddress = binding.tvAddress.text.toString()

            Log.d("testing", "onCreate: $sName$sEmail$sAddress")

            etName!!.setText(sName)
            etEmail!!.setText(sEmail)
            etAddress!!.setText(sAddress)

            btnCancel!!.setOnClickListener {
                bottomSheet.dismiss()
            }

            btnEdit!!.setOnClickListener {
                val name = etName.text.toString()
                val email = etEmail.text.toString()
                val address = etAddress.text.toString()

                val editMap = mapOf(
                    "name" to name, "email" to email, "address" to address
                )

                database.child("User").child(userId).updateChildren(editMap)

                Toast.makeText(this, "Edited Successfully!", Toast.LENGTH_SHORT).show()
                bottomSheet.dismiss()
            }
            bottomSheet.show()
        }

        binding.ivDelete.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Delete Data")
            builder.setMessage("Are You Sure!")
            builder.setIcon(R.drawable.ic_baseline_delete_24)
            builder.setCancelable(false)

            builder.setPositiveButton("Yes") { _, _ ->
                database.child("User").child(userId).removeValue()
                Toast.makeText(this, "Deleted!", Toast.LENGTH_SHORT).show()
            }
            builder.setNegativeButton("No") { _, _ ->

            }

            val alertDialog = builder.create()
            alertDialog.show()
        }

        binding.tvSaveData.setOnClickListener {
            navigateToActivity(SaveDataActivity::class.java)
        }

        binding.tvUpdateData.setOnClickListener {
            navigateToActivity(UpdateActivity::class.java)
        }

        binding.tvDeleteData.setOnClickListener {
            navigateToActivity(DeleteActivity::class.java)
        }

        binding.tvFetchData.setOnClickListener {
            navigateToActivity(FetchDataActivity::class.java)
        }

        binding.tvFetchDataInRecyclerView.setOnClickListener {
            navigateToActivity(RecyclerViewActivity::class.java)
        }

        binding.tvFireStoreRecyclerView.setOnClickListener {
            navigateToActivity(RecyclerViewActivity::class.java)
        }

        binding.tvUploadImage.setOnClickListener {
            navigateToActivity(UploadImageActivity::class.java)
        }

        binding.tvFetchImage.setOnClickListener {
            navigateToActivity(FetchImageActivity::class.java)
        }

        binding.tvFetchMultipleImages.setOnClickListener {
            navigateToActivity(RecyclerImageActivity::class.java)
        }

        binding.btnSignOut.setOnClickListener {
            Firebase.auth.signOut()

            navigateToActivity(LoginActivity::class.java, finishCurrent = true)
        }
    }

    private fun <T> navigateToActivity(
        targetActivity: Class<T>,
        extras: Bundle? = null,
        finishCurrent: Boolean = false,
    ) {
        val intent = Intent(this, targetActivity)
        extras?.let {
            intent.putExtras(it)
        }
        startActivity(intent)
        if (finishCurrent) {
            this.finish()
        }
    }

}