package com.example.mayapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.mayapp.databinding.ActivityMain2Binding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

@Suppress("DEPRECATION")
class MainActivity2 : AppCompatActivity() {
    private lateinit var binding1: ActivityMain2Binding
    private lateinit var myAdapter: MyAdaptor
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding1 = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding1.root)

        databaseReference = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        val logoutButton = binding1.logout
        val cloudstore = binding1.butts2



        cloudstore.setOnClickListener {
            val intent = Intent(this, MainActivity8::class.java)
            startActivity(intent)
        }

        logoutButton.setOnClickListener {

            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .build()


            googleSignInClient = GoogleSignIn.getClient(this, gso)

            SweetAlertDialog(this@MainActivity2, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Do you want to logout?")
                .setConfirmText("Yes !")
                .setCancelText("No!")
                .setConfirmClickListener {
                    // Signing out from Firebase and Google
                    FirebaseAuth.getInstance().signOut()
                    googleSignInClient.signOut()

                    // Navigate to MainActivity and clear the activity stack
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                .setCancelClickListener {
                    it.dismiss()
                }
                .show()
        }
            myAdapter = MyAdaptor(dataobj.getdata(), this)
            binding1.rv.layoutManager = LinearLayoutManager(this)
            binding1.rv.adapter = myAdapter
        }
    }
