package com.example.testDemo.activity

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.example.testDemo.databinding.ActivitySplashBinding
import com.example.testDemo.utils.SavedSharedPreference
import java.util.*

class SplashActivity:AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    var loginStates = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginStates = SavedSharedPreference().getPreferences(this).getBoolean("Login",false)

        object: CountDownTimer(500,500){
            override fun onTick(p0: Long) {}

            override fun onFinish() {
                if (loginStates) {
                    startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                    finishAffinity()
                } else {
                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                    finishAffinity()
                }

            }
        }.start()
    }

}