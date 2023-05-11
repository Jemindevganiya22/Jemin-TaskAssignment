package com.example.testDemo.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import com.example.testDemo.R
import com.example.testDemo.databinding.ActivityHomeBinding
import com.example.testDemo.databinding.DialogLogoutBinding
import com.example.testDemo.utils.SavedSharedPreference

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init(){
        binding.logoutBtn.setOnClickListener {
            logoutDialog()
        }
    }

    fun logoutDialog() {
        val dialog =
            Dialog(this, R.style.MyRounded_MaterialComponents_MaterialAlertDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogConfirmBinding =
            DialogLogoutBinding.inflate(LayoutInflater.from(this))
        dialog.setContentView(dialogConfirmBinding.root)

        dialogConfirmBinding.tvConfirm.setOnClickListener {
            SavedSharedPreference().clearAllPreference(this)
            startActivity(Intent(this,LoginActivity::class.java))
            finishAffinity()
            dialog.dismiss()
        }

        dialogConfirmBinding.tvCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        );
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        dialog.show()
    }
}