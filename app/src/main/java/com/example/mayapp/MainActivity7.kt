package com.example.mayapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mayapp.databinding.ActivityMain4Binding
import com.example.mayapp.databinding.ActivityMain7Binding
import com.google.firebase.auth.FirebaseAuth
import com.shashank.sony.fancytoastlib.FancyToast

class MainActivity7 : AppCompatActivity() {
    private lateinit var binding: ActivityMain7Binding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMain7Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val backsignin = binding.registor
        val backsignin2 = binding.backtosignin
        backsignin2.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


        auth = FirebaseAuth.getInstance()
        backsignin.setOnClickListener {
            val usrname = binding.username.text.toString()
            val mail = binding.emails.text.toString()
            val pass = binding.passs.text.toString()
            val repass = binding.vrfypass.text.toString()


            if (mail.isEmpty() || pass.isEmpty() || usrname.isEmpty() || repass.isEmpty()) {
                FancyToast.makeText(
                    this, "Please fill all details!",
                    FancyToast.LENGTH_LONG,
                    FancyToast.WARNING, false
                ).show()
            } else if (pass != repass) {
                FancyToast.makeText(
                    this, "Both passwords should be same!",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.ERROR, false
                ).show()
            } else {
                auth.createUserWithEmailAndPassword(mail, pass)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            FancyToast.makeText(
                                this, "Registration Successful!",
                                FancyToast.LENGTH_LONG,
                                FancyToast.SUCCESS, false
                            ).show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            FancyToast.makeText(
                                this, "Registration Failed!! : ${task.exception?.message}",
                                FancyToast.LENGTH_LONG,
                                FancyToast.ERROR, false
                            ).show()
                        }
                    }


            }
        }
    }
}