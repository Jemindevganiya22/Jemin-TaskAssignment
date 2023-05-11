package com.example.testDemo.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.testDemo.model.User
import com.example.testDemo.databinding.ActivityRejisterBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRejisterBinding
    lateinit var database: DatabaseReference
    var token = ""

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRejisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

    }

    private fun init() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener<String> { task ->
                try {
                    if (!task.isSuccessful) {
                        return@OnCompleteListener
                    }
                    token = task.result
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            })
        database = Firebase.database.reference
        binding.btnsubmit.setOnClickListener {
            val userName = binding.etname.text.toString()
            val email = binding.etgmail.text.toString()
            val password = binding.etpwd.text.toString()
            val mobileNumber = binding.etmno.text.toString()
            var gender = ""
            gender = if (binding.rbmale.isChecked) {
                "male"
            } else {
                "female"
            }
            if (userName.isNullOrEmpty()) {
                Toast.makeText(this, "Please enter your username.", Toast.LENGTH_SHORT).show()
            } else if (mobileNumber.length < 8) {
                Toast.makeText(this, "Please enter your valid mobile number.", Toast.LENGTH_SHORT)
                    .show()
            } else if (!isValidEmail(email)) {
                Toast.makeText(this, "Please enter your valid email address.", Toast.LENGTH_SHORT)
                    .show()
            } else if (password.length < 6) {
                Toast.makeText(this, "Your password must be 6 characters ", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val user = User(userName, email, password, mobileNumber, gender, token)
                database = FirebaseDatabase.getInstance().getReference("user")
                database.child(mobileNumber).setValue(user).addOnSuccessListener {
                    binding.etname.text.clear()
                    binding.etpwd.text.clear()
                    binding.etgmail.text.clear()
                    binding.etmno.text.clear()

                    Toast.makeText(this, "Sign up Successfully", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
        binding.tvlogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}