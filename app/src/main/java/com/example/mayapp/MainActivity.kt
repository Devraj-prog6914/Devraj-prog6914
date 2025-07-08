package com.example.mayapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.mayapp.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.shashank.sony.fancytoastlib.FancyToast

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val but1 = binding.textView11
        val but2 = binding.SignIn
        val gogbutt = binding.googi

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestIdToken(getString(R.string.default_web_client_id)).build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        but1.setOnClickListener {
            startActivity(Intent(this, MainActivity7::class.java))
            finish()
        }

        but2.setOnClickListener {
            val mail = binding.editTextTextEmailAddress3.text.toString().trim()
            val pass = binding.editTextTextPassword2.text.toString().trim()

            if (mail.isEmpty() || pass.isEmpty()) {
                FancyToast.makeText(this, "Please fill all details!", FancyToast.LENGTH_LONG, FancyToast.WARNING, false).show()
            } else {
                auth.signInWithEmailAndPassword(mail, pass)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            FancyToast.makeText(this, "Logged In", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show()
                            startActivity(Intent(this, MainActivity2::class.java))
                            finish()
                        } else {
                            val error = "Unknown error"
                            FancyToast.makeText(this, "Signing Failed!!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show()
                        }
                    }
            }
        }

        gogbutt.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser: FirebaseUser? = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this, MainActivity2::class.java))
            finish()
        }
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account: GoogleSignInAccount? = task.result
                if (account != null) {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    auth.signInWithCredential(credential).addOnCompleteListener {
                        if (it.isSuccessful) {

                            FancyToast.makeText(this, "Logged In", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show()
                            startActivity(Intent(this, MainActivity2::class.java))
                            finish()
                        } else {

                            FancyToast.makeText(this, "Login Failed", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
                        }
                    }
                } else {
                    FancyToast.makeText(this, "Account is null", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
                }
            } catch (e: Exception) {

                FancyToast.makeText(this, "Login Failed", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
            }
        } else {
            FancyToast.makeText(this, "Login Cancelled", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show()
        }
    }
}
