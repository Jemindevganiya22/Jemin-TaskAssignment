package com.example.testDemo.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.testDemo.databinding.ActivityLoginBinding
import com.example.testDemo.model.User
import com.example.testDemo.utils.SavedSharedPreference
import com.google.android.datatransport.Priority
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.messaging.FirebaseMessaging
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    lateinit var database: DatabaseReference
    lateinit var firebaseDatabase: FirebaseDatabase
    var userList: ArrayList<User> = ArrayList()
    var flag: Boolean = false
    var token = ""
    var userName = ""
    val key =
        "AAAAvYwCy6I:APA91bHtVTn7Rdqzai6_YUrtHnBETXMJv28bqxOnLhIZefzDYf-N-GzFSREahGIFjN9uLlSWeft3D2JjPoZdVP3FutqJdLD0frlRMRNdkwrx5SyjjmGCcSjJmhl1QmCCeBWQh0QwnqIm"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
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
        firebaseDatabase = FirebaseDatabase.getInstance()
        database = firebaseDatabase.reference

        binding.btnsubmit1.setOnClickListener {
            val mail = binding.etemail1.text.toString()
            val pwd = binding.etpwd1.text.toString()
            if (!isValidEmail(mail)) {
                Toast.makeText(this, "Please enter your valid email address.", Toast.LENGTH_SHORT)
                    .show()
            } else if (pwd.isNullOrEmpty()) {
                Toast.makeText(this, "Please enter your password.", Toast.LENGTH_SHORT).show()
            } else {
                database.child("user").addValueEventListener(object : ValueEventListener {
                    @SuppressLint("SuspiciousIndentation")
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (i in snapshot.children) {
                            try {
                                val alldata = i.getValue<User>()
                                alldata?.let { it1 -> userList.add(it1) }
                            } catch (e: Exception) {
                                Log.e("error", "onDataChange:$e")
                            }
                        }
                        for (user in userList) {
                            val email = user.email
                            val password = user.password
                            if (email.equals(mail) && password.equals(pwd)) {
                                database.child("user").child(user.mno.toString()).child("token").setValue(token).addOnCompleteListener {

                                }
                                userName = user.username.toString()
                                flag = true
                                break
                            }
                        }
                        if (flag) {
                            if (token != "")
                                sendMessage(
                                    "TestDemo",
                                    "Welcome to our Application $userName",
                                    token
                                )
                            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                            SavedSharedPreference().setLogin(this@LoginActivity, true)
                            finishAffinity()
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                "Please check you email and password. We not not find this user in our record.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
//                        Toast.makeText(this@LoginActivity, "Fail to get data.", Toast.LENGTH_SHORT).show()
                    }
                })
            }

        }
        binding.tvsignup0.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun sendMessage(title: String, body: String, token: String) {
        val notification = JSONObject()
        val notifcationBody = JSONObject()
        notifcationBody.put("title", title)
        notifcationBody.put("body", body)
        notification.put("to", token)
        notification.put("notification", notifcationBody)
        AndroidNetworking.post("https://fcm.googleapis.com/fcm/send").addHeaders(
            "Authorization", "key=$key"
        ).addHeaders("Content-Type", "application/json")
            .addJSONObjectBody(notification) //                .setOkHttpClient(client)
            .setTag("https://fcm.googleapis.com/fcm/send")
            .setPriority(com.androidnetworking.common.Priority.HIGH)
            .build().getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Log.e("sendMessage-response", response.toString());
                }

                override fun onError(anError: ANError?) {
                    Log.e("sendMessage-error", anError?.message.toString())
                }
            })
    }

    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}