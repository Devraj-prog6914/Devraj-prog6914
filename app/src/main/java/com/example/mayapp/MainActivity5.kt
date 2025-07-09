package com.example.mayapp

import EcuEntry
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mayapp.databinding.ActivityMain5Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.shashank.sony.fancytoastlib.FancyToast

class MainActivity5 : AppCompatActivity() {
    private lateinit var binding: ActivityMain5Binding  // Declare the binding object
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMain5Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get UI elements
        val name = binding.namee
        val regi = binding.regn
        val branch = binding.branchs

        databaseReference = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        val butt = binding.button4

        // Hide text fields initially
        name.visibility = View.GONE
        regi.visibility = View.GONE
        branch.visibility = View.GONE

        // Get intent data
        val stdname = intent.getStringExtra("name") ?: ""
        val stdreg = intent.getStringExtra("Regno") ?: ""
        val stdbrch = intent.getStringExtra("branch") ?: ""

        // Check if all fields are filled
        if (stdname.isNotEmpty() && stdreg.isNotEmpty() && stdbrch.isNotEmpty()) {
            // Set text and show the fields
            name.text = stdname
            regi.text = stdreg
            branch.text = stdbrch

            name.visibility = View.VISIBLE
            regi.visibility = View.VISIBLE
            branch.visibility = View.VISIBLE
        } else {
            // Show error message
            FancyToast.makeText(
                this, "Please fill all details!",
                FancyToast.LENGTH_LONG,
                FancyToast.ERROR, false
            ).show()

            // Redirect back to the previous activity
            finish()  // Close this activity and return to the previous one
            return
        }

        // Button to go to MainActivity2
        butt.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
            finish()  // Close the current activity
        }

        // SO here we are going to save our realtime data in the text views which we have
        // this is called as data retrieval
        // but it will show different data to different users by authentication

        val currentUser = auth.currentUser
        currentUser?.let { user ->
            // Reference to the ECU data for the current user
            val userEcuRef = FirebaseDatabase.getInstance()
                .getReference("ecu_data")
                .child("A1B2C3D4UserUID")

            // Fetch data from Firebase
            userEcuRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Get the first entry (entry2) for simplicity
                    val entry = snapshot.child("entry2")

                    val vehicleSpeed =
                        entry.child("vehicle_speed_kmh").getValue(Int::class.java)?.toString()
                            ?: "N/A"
                    val fuelPressure =
                        entry.child("fuel_pressure_bar").getValue(Double::class.java)?.toString()
                            ?: "N/A"
                    val airIntakeTemp =
                        entry.child("air_intake_temperature_c").getValue(Int::class.java)
                            ?.toString() ?: "N/A"
                    val fuelTrim =
                        entry.child("fuel_trim_percent").getValue(Int::class.java)?.toString()
                            ?: "N/A"
                    val oilPressure =
                        entry.child("oil_pressure_bar").getValue(Double::class.java)?.toString()
                            ?: "N/A"
                    val transmissionGear =
                        entry.child("transmission_gear").getValue(Int::class.java)?.toString()
                            ?: "N/A"
                    val batteryVoltage =
                        entry.child("battery_voltage_v").getValue(Double::class.java)?.toString()
                            ?: "N/A"

                    // Set values to your TextViews

                    binding.val3.text = fuelPressure
                    binding.val4.text = airIntakeTemp
                    binding.val5.text = fuelTrim
                    binding.val6.text = oilPressure
                    binding.val7.text = transmissionGear
                    binding.val8.text = batteryVoltage
                    binding.val2.text = vehicleSpeed
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error if data retrieval fails
                    FancyToast.makeText(
                        this@MainActivity5, "Failed to load data!",
                        FancyToast.LENGTH_LONG,
                        FancyToast.ERROR, false
                    ).show()

                }
            })
        }
    }
}
